package lexwomy.fletching.item;

import lexwomy.fletching.enchantment.FletchingEnchantmentHelper;
import lexwomy.fletching.tags.FletchingItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class GreatbowItem extends ProjectileWeaponItem {
    public static final Predicate<ItemStack> GREATBOW_PROJECTILES = stack -> stack.is(FletchingItemTags.PILUMS);
    public static final float DRAW_TIME = 90.0F;
    public static final float BASE_VELOCITY = 5.0F;
    public static final float BASE_DAMAGE = 3.0F;
    public static final int RANGE = 25;

    public GreatbowItem(net.minecraft.world.item.Item.Properties settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return GREATBOW_PROJECTILES;
    }

    public float getVelocity(ItemStack itemStack) {
        return FletchingEnchantmentHelper.increaseGreatbowVelocity(itemStack, BASE_VELOCITY);
    }

    public float getDrawTime(ItemStack itemStack, LivingEntity user) {
        return FletchingEnchantmentHelper.modifyDrawTime(user, itemStack, DRAW_TIME);
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

    //TODO - Use fabric asm to extend the enum for custom greatbow rendering
    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getDefaultProjectileRange() {
        return RANGE;
    }

    @Override
    protected void shoot(
            ServerLevel world,
            LivingEntity shooter,
            InteractionHand hand,
            ItemStack stack,
            List<ItemStack> projectiles,
            float speed,
            float divergence,
            boolean critical,
            @Nullable LivingEntity target
    ) {

        for (int j = 0; j < projectiles.size(); j++) {
            ItemStack itemStack = projectiles.get(j);
            if (!itemStack.isEmpty()) {
                Projectile projectileEntity = this.createPilumEntity(world, shooter, stack, itemStack, critical);
                this.shootProjectile(shooter, projectileEntity, j, speed, 0, divergence, target);
                world.addFreshEntity(projectileEntity);
                stack.hurtAndBreak(this.getDurabilityUse(itemStack), shooter, hand.asEquipmentSlot());
                if (stack.isEmpty()) {
                    break;
                }
            }
        }
    }

    protected Projectile createPilumEntity(Level world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        PilumItem pilumItem2 = projectileStack.getItem() instanceof PilumItem pilumItem ? pilumItem : (PilumItem) FletchingItems.FLINT_PILUM;
        AbstractArrow persistentProjectileEntity = pilumItem2.createPilum(world, projectileStack, shooter, weaponStack);
        if (critical) {
            persistentProjectileEntity.setCritArrow(true);
        }

        return persistentProjectileEntity;
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float speed, float yaw, float divergence, @Nullable LivingEntity target) {
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, speed, divergence);
    }

    public float getPullProgress(int useTicks, LivingEntity user, ItemStack itemStack) {
        float f = (float)useTicks / this.getDrawTime(itemStack, user);
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {
            ItemStack itemStack = playerEntity.getProjectile(stack);
            if (!itemStack.isEmpty()) {
                int i = this.getUseDuration(stack, user) - remainingUseTicks;
                float f = this.getPullProgress(i, user, stack);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemStack, playerEntity);
                    if (world instanceof ServerLevel serverWorld && !list.isEmpty()) {
                        this.shoot(serverWorld, playerEntity, playerEntity.getUsedItemHand(), stack, list, f * this.getVelocity(stack), 1.0F, f == 1.0F, null);
                    }

                    world.playSound(
                            null,
                            playerEntity.getX(),
                            playerEntity.getY(),
                            playerEntity.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            SoundSource.PLAYERS,
                            1.0F,
                            0.7F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    playerEntity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
        return false;
    }
}
