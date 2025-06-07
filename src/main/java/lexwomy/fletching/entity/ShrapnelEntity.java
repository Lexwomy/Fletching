package lexwomy.fletching.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import lexwomy.fletching.entity.damage.FletchingDamageTypes;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShrapnelEntity extends PersistentProjectileEntity {
    // The shrapnel entity inherits properties from the original projectile stack
    public ShrapnelEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ShrapnelEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(FletchingEntities.SHRAPNEL, x, y, z, world, stack, shotFrom);
    }

    public ShrapnelEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(FletchingEntities.SHRAPNEL, owner, world, stack, shotFrom);
    }

    // Assume the shrapnel inherits the traits of a normal arrow
    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();
        float f = (float)this.getVelocity().length();
        double d = 1.5F;
        Entity owner = this.getOwner();
        DamageSource damageSource = new DamageSource(
                this.getWorld().getRegistryManager()
                        .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                        .getEntry(FletchingDamageTypes.SHRAPNEL.getValue())
                        .get(),
                this, owner != null ? owner : this);
        if (this.getWeaponStack() != null && this.getWorld() instanceof ServerWorld serverWorld) {
            d = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), target, damageSource, (float)d);
        }

        int i = MathHelper.ceil(MathHelper.clamp(f * d, 0.0, 2.147483647E9));

        if (this.isCritical()) {
            long l = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(l + i, 2147483647L);
        }

        if (owner instanceof LivingEntity livingEntity) {
            livingEntity.onAttacking(target);
        }

        boolean bl = target.getType() == EntityType.ENDERMAN;
        int j = target.getFireTicks();
        if (this.isOnFire() && !bl) {
            target.setOnFireFor(5.0F);
        }

        if (target.sidedDamage(damageSource, i)) {
            if (bl) {
                return;
            }

            if (target instanceof LivingEntity targetAsLivingEntity) {
                this.knockback(targetAsLivingEntity, damageSource);
                if (this.getWorld() instanceof ServerWorld serverWorld2) {
                    EnchantmentHelper.onTargetDamaged(serverWorld2, targetAsLivingEntity, damageSource, this.getWeaponStack());
                }

                this.onHit(targetAsLivingEntity);
                if (targetAsLivingEntity instanceof PlayerEntity
                        && owner instanceof ServerPlayerEntity serverPlayerEntity
                        && !this.isSilent()
                        && targetAsLivingEntity != serverPlayerEntity) {
                    serverPlayerEntity.networkHandler
                            .sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }

                // TODO - replace with killed by shrapnel criteria
//                if (!this.getWorld().isClient && entity2 instanceof ServerPlayerEntity serverPlayerEntity) {
//                    if (this.piercingKilledEntities != null) {
//                        Criteria.KILLED_BY_ARROW.trigger(serverPlayerEntity, this.piercingKilledEntities, this.weapon);
//                    } else if (!entity.isAlive()) {
//                        Criteria.KILLED_BY_ARROW.trigger(serverPlayerEntity, List.of(entity), this.weapon);
//                    }
//                }
            }

            this.playSound(this.getSound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
        } else {
            target.setFireTicks(j);
            this.deflect(ProjectileDeflection.SIMPLE, target, this.getOwner(), false);
            this.setVelocity(this.getVelocity().multiply(0.2));
            if (this.getWorld() instanceof ServerWorld serverWorld3 && this.getVelocity().lengthSquared() < 1.0E-7) {
                if (this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(serverWorld3, this.asItemStack(), 0.1F);
                }

                this.discard();
            }
        }
    }
}
