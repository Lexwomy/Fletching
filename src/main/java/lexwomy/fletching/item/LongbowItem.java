package lexwomy.fletching.item;

import java.util.List;
import java.util.function.Predicate;
import lexwomy.fletching.effect.FletchingEffects;
import lexwomy.fletching.effect.FocusEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LongbowItem extends ProjectileWeaponItem {
  public static final int RANGE = 20;
  public static final float DRAW_TIME = 40.0F;
  public static final float BASE_VELOCITY = 3.5F;
  public static final double DAMAGE = 2.0F;

  public LongbowItem(net.minecraft.world.item.Item.Properties settings) {
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
  protected void shootProjectile(
      LivingEntity shooter,
      Projectile projectile,
      int index,
      float speed,
      float divergence,
      float yaw,
      @Nullable LivingEntity target) {
    ((AbstractArrow) projectile).setBaseDamage(getFocusedDamage(shooter));
    projectile.shootFromRotation(
        shooter, shooter.getXRot(), shooter.getYRot() + yaw, 0.0F, speed, divergence);
  }

  public double getFocusedDamage(LivingEntity user) {
    MobEffectInstance effect = user.getEffect(FletchingEffects.FOCUS);
    int focus_stack = effect == null ? 0 : FocusEffect.getFocusStack(effect);
    if (focus_stack == 2) {
      return DAMAGE + 3.0;
    }
    return DAMAGE + (0.5 * focus_stack);
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
        float f = getPullProgress(i);
        if (!((double) f < 0.5)) {
          List<ItemStack> list = draw(weapon, projectileItem, playerEntity);
          if (world instanceof ServerLevel serverWorld && !list.isEmpty()) {
            this.shoot(
                serverWorld,
                playerEntity,
                playerEntity.getUsedItemHand(),
                weapon,
                list,
                f * BASE_VELOCITY,
                this.getFocusedDivergence(user),
                f == 1.0F,
                null);
          }

          if (FocusEffect.getFocusedState(playerEntity) == FocusEffect.FocusState.EAGLESIGHT) {
            // Do extra stuff here TODO
            playerEntity.removeEffect(FletchingEffects.FOCUS);
          }

          world.playSound(
              null,
              playerEntity.getX(),
              playerEntity.getY(),
              playerEntity.getZ(),
              SoundEvents.ARROW_SHOOT,
              SoundSource.PLAYERS,
              1.0F,
              0.9F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
          playerEntity.awardStat(Stats.ITEM_USED.get(this));
        }
      }
    }
    return false;
  }

  public float getFocusedDivergence(LivingEntity user) {
    MobEffectInstance effect = user.getEffect(FletchingEffects.FOCUS);
    int focus_stack = FocusEffect.getFocusStack(effect);
    return 1.0F - (0.33F * focus_stack);
  }

  public static float getPullProgress(int useTicks) {
    float f = (float) useTicks / DRAW_TIME;
    f = Math.min((f * f + f * 2.0F) / 3.0F, 1.0F);

    return f;
  }
}
