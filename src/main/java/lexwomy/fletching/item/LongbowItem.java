package lexwomy.fletching.item;

import lexwomy.fletching.effect.FletchingEffects;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

//TODO Add compatibility by using a custom event to add piercing enchantment to longbow
public class LongbowItem extends ProjectileWeaponItem {
    public static final int RANGE = 20;
    public static final float DRAW_TIME = 50.0F;
    public static final float BASE_VELOCITY = 3.5F;
    public static final double DAMAGE = 2.0F;
    //private int FOCUS = 0;

    public LongbowItem(net.minecraft.world.item.Item.Properties settings) {
        super(settings);
    }

    public double getFocusedDamage(LivingEntity user) {
        MobEffectInstance effect = user.getEffect(FletchingEffects.FOCUS);
        int focus_stack = effect == null ? 0 : effect.getAmplifier() + 1;
        if (focus_stack > 8) {
            focus_stack = 8;
        }
        return DAMAGE + (0.5 * focus_stack);
    }

    public float getFocusedDivergence(LivingEntity user) {
        MobEffectInstance effect = user.getEffect(FletchingEffects.FOCUS);
        int focus_stack = effect == null ? 0 : effect.getAmplifier() + 1;
        if (focus_stack > 8) {
            focus_stack = 8;
        }
        return 1.0F - (0.125F * focus_stack);
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
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        //((ExposeArrowDamageMixin) projectile).setDamage(getFocusedDamage(shooter));
        ((AbstractArrow) projectile).setBaseDamage(getFocusedDamage(shooter));
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + yaw, 0.0F, speed, divergence);
    }

    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / DRAW_TIME;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {
            ItemStack itemStack = playerEntity.getProjectile(stack);
            if (!itemStack.isEmpty()) {
                int i = this.getUseDuration(stack, user) - remainingUseTicks;
                float f = getPullProgress(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = draw(stack, itemStack, playerEntity);
                    if (world instanceof ServerLevel serverWorld && !list.isEmpty()) {
                        this.shoot(serverWorld, playerEntity, playerEntity.getUsedItemHand(), stack, list, f * BASE_VELOCITY, this.getFocusedDivergence(user), f == 1.0F, null);
                    }

                    world.playSound(
                            null,
                            playerEntity.getX(),
                            playerEntity.getY(),
                            playerEntity.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            SoundSource.PLAYERS,
                            1.0F,
                            0.9F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    playerEntity.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BOW;
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
}
