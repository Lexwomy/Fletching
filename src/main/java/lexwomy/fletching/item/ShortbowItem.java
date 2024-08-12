package lexwomy.fletching.item;

import lexwomy.fletching.Fletching;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ShortbowItem extends RangedWeaponItem {
    //Used as the base draw time of the bow, which can be affected by frenzy
    public static final float DRAW_TIME = 15.0F;
    public static final float BASE_VELOCITY = 1.75F;
    public static final int RANGE = 10;
    private static final Random RANDOM = Random.create();

    public ShortbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    public float getFrenzyDrawTime(LivingEntity user) {
        StatusEffectInstance effect = user.getStatusEffect(Fletching.FRENZY);
        int frenzy_stack = effect == null ? 0 : effect.getAmplifier() + 1;
        if (frenzy_stack > 40) {
            frenzy_stack = 40;
        }
        return Math.round(DRAW_TIME - (0.25F * frenzy_stack));
    }

    public float getFrenzyInaccuracy(LivingEntity user) {
        StatusEffectInstance effect = user.getStatusEffect(Fletching.FRENZY);
        int frenzy_stack = effect == null ? 0 : effect.getAmplifier() + 1;
        int range = Math.round(0.125F * frenzy_stack);
        return range != 0 ? RANDOM.nextBetweenExclusive(-range, range) : 0;
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    //Check for frenzy and add a random value to yaw to simulate inaccurate "frenzied" shooting
    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch() + + getFrenzyInaccuracy(shooter),
                shooter.getYaw() + yaw + getFrenzyInaccuracy(shooter), 0.0F, speed, divergence);
    }

    public float getPullProgress(int useTicks, LivingEntity user) {


        float base = this.getFrenzyDrawTime(user);
        //Fletching.LOGGER.info("Draw time is now: {}", base);
        float f = (float)useTicks / base;
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
                float f = this.getPullProgress(i, user);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = load(stack, itemStack, playerEntity);
                    if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
                         this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list,
                                f * BASE_VELOCITY, 1.0F, f == 1.0F, null);
                    }

                    world.playSound(
                            null,
                            playerEntity.getX(),
                            playerEntity.getY(),
                            playerEntity.getZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT,
                            SoundCategory.PLAYERS,
                            1.0F,
                            1.3F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
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
