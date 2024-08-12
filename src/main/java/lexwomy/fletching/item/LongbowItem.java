package lexwomy.fletching.item;

import lexwomy.fletching.Fletching;
import lexwomy.fletching.mixin.ExposeArrowDamageMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static lexwomy.fletching.Fletching.FOCUS;
import static lexwomy.fletching.Fletching.FOCUS_EFFECT;

//TODO Add compatibility by using a custom event to add piercing enchantment to longbow
public class LongbowItem extends RangedWeaponItem {
    public static final int RANGE = 20;
    public static final float DRAW_TIME = 50.0F;
    public static final float BASE_VELOCITY = 3.5F;
    public static final double DAMAGE = 2.0F;
    //private int FOCUS = 0;

    public LongbowItem(Settings settings) {
        super(settings);
    }

    public double getFocusedDamage(LivingEntity user) {
        StatusEffectInstance effect = user.getStatusEffect(FOCUS);
        int focus_stack = effect == null ? 0 : effect.getAmplifier() + 1;
        if (focus_stack > 8) {
            focus_stack = 8;
        }
        return DAMAGE + (0.25 * focus_stack);
    }

    public float getFocusedDivergence(LivingEntity user) {
        StatusEffectInstance effect = user.getStatusEffect(FOCUS);
        int focus_stack = effect == null ? 0 : effect.getAmplifier() + 1;
        if (focus_stack > 8) {
            focus_stack = 8;
        }
        return 1.0F - (0.125F * focus_stack);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        ((ExposeArrowDamageMixin) projectile).setDamage(getFocusedDamage(shooter));
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0F, speed, divergence);
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
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack = playerEntity.getProjectileType(stack);
            if (!itemStack.isEmpty()) {
                int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                float f = getPullProgress(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = load(stack, itemStack, playerEntity);
                    if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
                        this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, f * BASE_VELOCITY, this.getFocusedDivergence(user), f == 1.0F, null);
                    }

                    world.playSound(
                            null,
                            playerEntity.getX(),
                            playerEntity.getY(),
                            playerEntity.getZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT,
                            SoundCategory.PLAYERS,
                            1.0F,
                            0.9F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean bl = !user.getProjectileType(itemStack).isEmpty();
        if (!user.isInCreativeMode() && !bl) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }
}
