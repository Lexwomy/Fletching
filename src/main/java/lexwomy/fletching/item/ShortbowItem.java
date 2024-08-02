package lexwomy.fletching.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ShortbowItem extends RangedWeaponItem {
    public static final float DRAW_TIME = 15.0F;
    public static final float BASE_VELOCITY = 1.75F;
    public static final int RANGE = 10;
    private int FRENZY = 0;

    public ShortbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    public float getFrenzyDrawTime() {
        return DRAW_TIME - (0.5F * FRENZY);
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0.0F, speed, divergence);
    }

    public float getPullProgress(int useTicks) {
        int base = Math.round(this.getFrenzyDrawTime());
//        float f = (float)useTicks / DRAW_TIME;
//        f = (f * f + f * 2.0F) / 3.0F;
//        if (f > 1.0F) {
//            f = 1.0F;
//        }
//
//        return f;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            ItemStack itemStack = playerEntity.getProjectileType(stack);
            if (!itemStack.isEmpty()) {
                int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
                float f = this.getPullProgress(i);
                if (!((double)f < 0.1)) {
                    List<ItemStack> list = load(stack, itemStack, playerEntity);
                    if (world instanceof ServerWorld serverWorld && !list.isEmpty()) {
                        this.shootAll(serverWorld, playerEntity, playerEntity.getActiveHand(), stack, list, f * BASE_VELOCITY, 1.0F, f == 1.0F, null);
                    }

                    world.playSound(
                            null,
                            playerEntity.getX(),
                            playerEntity.getY(),
                            playerEntity.getZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT,
                            SoundCategory.PLAYERS,
                            1.0F,
                            0.7F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                    //TODO - If too hard to track entity hits from here, increment frenzy for every arrow shot, check enchantment
                }
            }
        }
    }
}
