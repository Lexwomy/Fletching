package lexwomy.fletching.item;

import java.util.List;
import java.util.function.Predicate;
import lexwomy.fletching.Fletching;
import lexwomy.fletching.effect.FletchingEffects;
import lexwomy.fletching.effect.FrenzyEffect;
import lexwomy.fletching.enchantment.FletchingEnchantmentHelper;
import lexwomy.fletching.entity.ShrapnelEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShortbowItem extends ProjectileWeaponItem {
  // Used as the base draw time of the bow, which can be affected by frenzy
  public static final float DRAW_TIME = 15.0F;
  public static final float BASE_VELOCITY = 1.75F;
  public static final float SHRAPNEL_VELOCITY_INCREASE = 0.25F;
  public static final int RANGE = 10;

  public ShortbowItem(net.minecraft.world.item.Item.Properties settings) {
    super(settings);
  }

  @Override
  public Predicate<ItemStack> getAllSupportedProjectiles() {
    return ARROW_ONLY;
  }

  @Override
  public int getDefaultProjectileRange() {
    return RANGE;
  }

  @Override
  protected void shoot(
      ServerLevel serverLevel,
      LivingEntity shooter,
      InteractionHand hand,
      ItemStack weapon,
      List<ItemStack> projectiles,
      float speed,
      float divergence,
      boolean critical,
      @Nullable LivingEntity target) {

    float radius =
        FletchingEnchantmentHelper.modifyInaccuracy(shooter, weapon, 0)
            + getFrenzyInaccuracy(shooter);
    int shrapnelCount = FletchingEnchantmentHelper.getShrapnelCount(shooter, weapon);

    // Scattershot can get multiplied by any enchantment that provides multiple projectiles
    for (int j = 0; j < projectiles.size(); j++) {
      ItemStack projectileItem = projectiles.get(j);
      int durability = this.getDurabilityUse(projectileItem);
      if (!projectileItem.isEmpty()) {
        if (shrapnelCount > 0) {
          for (int i = 0; i < shrapnelCount; i++) {
            int count = i;
            Projectile.spawnProjectile(
                createShrapnelEntity(serverLevel, shooter, weapon, projectileItem, shrapnelCount),
                serverLevel,
                projectileItem,
                projectile ->
                    this.shootProjectile(
                        shooter, projectile, count, speed, divergence, radius, target));
          }
          // Scattershot will consume twice the durability per shot
          durability *= 2;
        } else {
          // For lambda closure
          int count = j;
          Projectile.spawnProjectile(
              this.createProjectile(serverLevel, shooter, weapon, projectileItem, critical),
              serverLevel,
              projectileItem,
              projectile ->
                  this.shootProjectile(
                      shooter, projectile, count, speed, divergence, radius, target));
        }
        weapon.hurtAndBreak(durability, shooter, hand.asEquipmentSlot());
        if (projectileItem.isEmpty()) {
          break;
        }
      }
    }
  }

  public float getFrenzyInaccuracy(LivingEntity user) {
    MobEffectInstance effect = user.getEffect(FletchingEffects.FRENZY);
    return 0.25F * FrenzyEffect.getFrenzyStack(effect);
  }

  protected Projectile createShrapnelEntity(
      Level world,
      LivingEntity shooter,
      ItemStack weaponStack,
      ItemStack projectileStack,
      int shrapnelCount) {
    ShrapnelEntity shrapnelEntity =
        new ShrapnelEntity(world, shooter, projectileStack, weaponStack);
    shrapnelEntity.setShrapnelCount(shrapnelCount);
    shrapnelEntity.setCritArrow(false);
    return shrapnelEntity;
  }

  // Check for frenzy and add a random value to yaw to simulate inaccurate "frenzied" shooting
  // Spread should only exist on scattershot, and in the case of frenzy, will increase the spread of
  // scattershot instead
  @Override
  protected void shootProjectile(
      @NotNull LivingEntity shooter,
      Projectile projectile,
      int index,
      float speed,
      float divergence,
      float radius,
      @Nullable LivingEntity target) {
    float[] spread = getSpread(shooter, radius);
    projectile.shootFromRotation(
        shooter,
        shooter.getXRot() + spread[1],
        shooter.getYRot() + spread[0],
        0.0F,
        speed,
        divergence);
  }

  // Returns [yaw, pitch]
  private float[] getSpread(LivingEntity user, float radius) {
    float angle = user.getRandom().nextFloat() * 2 * Mth.PI;
    float spread = user.getRandom().nextFloat() * radius;

    return new float[] {Mth.cos(angle) * spread, Mth.sin(angle) * spread};
  }

  @Override
  public InteractionResult use(Level world, Player user, InteractionHand hand) {
    ItemStack itemStack = user.getItemInHand(hand);
    boolean bl = !user.getProjectile(itemStack).isEmpty();
    if (!user.hasInfiniteMaterials() && !bl) {
      return InteractionResult.FAIL;
    } else {
      user.startUsingItem(hand);
      return InteractionResult.CONSUME;
    }
  }

  @Override
  public ItemUseAnimation getUseAnimation(ItemStack stack) {
    return ItemUseAnimation.BOW;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity user) {
    return 72000;
  }

  @Override
  public boolean releaseUsing(
      @NotNull ItemStack weapon,
      @NotNull Level world,
      @NotNull LivingEntity user,
      int remainingUseTicks) {
    if (user instanceof Player playerEntity) {
      ItemStack projectileItem = playerEntity.getProjectile(weapon);
      if (!projectileItem.isEmpty()) {
        int i = this.getUseDuration(weapon, user) - remainingUseTicks;
        float f = this.getPullProgress(i, user, weapon);
        int shrapnelCount = FletchingEnchantmentHelper.getShrapnelCount(user, weapon);
        boolean canShoot = (shrapnelCount > 0 && f >= 0.9) || (shrapnelCount == 0 && f >= 0.5);
        Fletching.devLogger("Can shoot shortbow: {}", canShoot);

        if (canShoot) {
          List<ItemStack> list = draw(weapon, projectileItem, playerEntity);
          if (world instanceof ServerLevel serverWorld && !list.isEmpty()) {
            // Scattershot cannot crit
            float speed =
                shrapnelCount == 0 ? BASE_VELOCITY : BASE_VELOCITY + SHRAPNEL_VELOCITY_INCREASE;
            this.shoot(
                serverWorld,
                playerEntity,
                playerEntity.getUsedItemHand(),
                weapon,
                list,
                f * speed,
                1.0F,
                f == 1.0F && shrapnelCount == 0,
                null);
          }

          world.playSound(
              null,
              playerEntity.getX(),
              playerEntity.getY(),
              playerEntity.getZ(),
              SoundEvents.ARROW_SHOOT,
              SoundSource.PLAYERS,
              1.0F,
              1.3F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
          playerEntity.awardStat(Stats.ITEM_USED.get(this));
        }
      }
    }
    return false;
  }

  public float getPullProgress(int useTicks, LivingEntity user, ItemStack itemStack) {
    float base = this.getFrenzyDrawTime(user, itemStack);
    float f = (float) useTicks / base;
    f = (f * f + f * 2.0F) / 3.0F;
    if (f > 1.0F) {
      f = 1.0F;
    }

    return f;
  }

  public float getFrenzyDrawTime(LivingEntity user, ItemStack itemStack) {
    MobEffectInstance effect = user.getEffect(FletchingEffects.FRENZY);

    float draw_time = FletchingEnchantmentHelper.modifyDrawTime(user, itemStack, DRAW_TIME);
    draw_time -= 0.25F * FrenzyEffect.getFrenzyStack(effect);
    return Math.round(draw_time);
  }
}
