package lexwomy.fletching.item;

import lexwomy.fletching.tags.FletchingItemTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
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

public class GreatbowItem extends RangedWeaponItem {
    public static final Predicate<ItemStack> GREATBOW_PROJECTILES = stack -> stack.isIn(FletchingItemTags.PILUMS);
    public static final float DRAW_TIME = 90.0F;
    public static final float BASE_VELOCITY = 5.0F;
    public static final float BASE_DAMAGE = 3.0F;
    public static final int RANGE = 25;

    public GreatbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return GREATBOW_PROJECTILES;
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

    //TODO - Use fabric asm to extend the enum for custom greatbow rendering
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getRange() {
        return RANGE;
    }

    @Override
    protected void shootAll(
            ServerWorld world,
            LivingEntity shooter,
            Hand hand,
            ItemStack stack,
            List<ItemStack> projectiles,
            float speed,
            float divergence,
            boolean critical,
            @Nullable LivingEntity target
    ) {
        float f = EnchantmentHelper.getProjectileSpread(world, stack, shooter, 0.0F);
        float g = projectiles.size() == 1 ? 0.0F : 2.0F * f / (float)(projectiles.size() - 1);
        float h = (float)((projectiles.size() - 1) % 2) * g / 2.0F;
        float i = 1.0F;

        for (int j = 0; j < projectiles.size(); j++) {
            ItemStack itemStack = (ItemStack)projectiles.get(j);
            if (!itemStack.isEmpty()) {
                float k = h + i * (float)((j + 1) / 2) * g;
                i = -i;
                ProjectileEntity projectileEntity = this.createPilumEntity(world, shooter, stack, itemStack, critical);
                this.shoot(shooter, projectileEntity, j, speed, divergence, k, target);
                world.spawnEntity(projectileEntity);
                stack.damage(this.getWeaponStackDamage(itemStack), shooter, LivingEntity.getSlotForHand(hand));
                if (stack.isEmpty()) {
                    break;
                }
            }
        }
    }

    protected ProjectileEntity createPilumEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        PilumItem pilumItem2 = projectileStack.getItem() instanceof PilumItem pilumItem ? pilumItem : (PilumItem) FletchingItems.FLINT_PILUM;
        PersistentProjectileEntity persistentProjectileEntity = pilumItem2.createPilum(world, projectileStack, shooter, weaponStack);
        if (critical) {
            persistentProjectileEntity.setCritical(true);
        }

        return persistentProjectileEntity;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float yaw, float divergence, @Nullable LivingEntity target) {
        projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, speed, divergence);
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
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
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
                }
            }
        }
    }
}
