package lexwomy.fletching.entity;

import lexwomy.fletching.Fletching;
import lexwomy.fletching.enchantment.FletchingEnchantmentHelper;
import lexwomy.fletching.entity.damage.FletchingDamageTypes;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class ShrapnelEntity extends AbstractArrow {
    // The shrapnel entity inherits properties from the original projectile stack
    private ItemStack parentArrow = null;
    private int shrapnelCount = 5;
    private float shrapnelDamage = 0.5F;

    public ShrapnelEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        this.pickup = Pickup.DISALLOWED;
    }

    public ShrapnelEntity(Level world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(FletchingEntities.SHRAPNEL, x, y, z, world, stack, shotFrom);
        this.parentArrow = stack.copy();
        this.pickup = Pickup.DISALLOWED;
    }

    public ShrapnelEntity(Level world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(FletchingEntities.SHRAPNEL, owner, world, stack, shotFrom);
        this.parentArrow = stack.copy();
        this.pickup = Pickup.DISALLOWED;
    }

    public void setOwner(@Nullable Entity entity) {
        super.setOwner(entity);
        this.pickup = Pickup.DISALLOWED;
    }

    public void setShrapnelCount(int shrapnelCount) {
        this.shrapnelCount = shrapnelCount;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();
        float f = (float)this.getDeltaMovement().length();

        double d = this.shrapnelDamage;
        Entity owner = this.getOwner();
        DamageSource damageSource = this.damageSources().source(FletchingDamageTypes.SHRAPNEL, this, owner != null ? owner : this);
        if (this.getWeaponItem() != null && this.level() instanceof ServerLevel serverLevel) {
            // Enchantments are applied at 1/(n - 1)th effectiveness
            d = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), target, damageSource, (float)d) / (this.shrapnelCount - 1 == 0 ? 1 : this.shrapnelCount - 1);
        }
        Fletching.devLogger("Base damage after enchantments: {}", d);

        int i = Mth.ceil(Mth.clamp(f * d, 0.0, 2.147483647E9));

        if (owner instanceof LivingEntity livingEntity) {
            livingEntity.setLastHurtMob(target);
        }

        boolean bl = target.getType() == EntityType.ENDERMAN;
        int j = target.getRemainingFireTicks();
        if (this.isOnFire() && !bl) {
            target.igniteForSeconds(5.0F);
        }

        if (target.hurtOrSimulate(damageSource, i)) {
            if (bl) {
                return;
            }

            if (target instanceof LivingEntity targetAsLivingEntity) {
                this.doKnockback(targetAsLivingEntity, damageSource);
                if (this.level() instanceof ServerLevel serverWorld2) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverWorld2, targetAsLivingEntity, damageSource, this.getWeaponItem());
                }

                this.doPostHurtEffects(targetAsLivingEntity);
                if (targetAsLivingEntity instanceof Player
                        && owner instanceof ServerPlayer serverPlayerEntity
                        && !this.isSilent()
                        && targetAsLivingEntity != serverPlayerEntity) {
                    serverPlayerEntity.connection
                            .send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND, ClientboundGameEventPacket.DEMO_PARAM_INTRO));
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

            this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            this.discard();
        } else {
            target.setRemainingFireTicks(j);
            this.deflect(ProjectileDeflection.REVERSE, target, this.owner, false);
            this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
            if (this.level() instanceof ServerLevel serverWorld3 && this.getDeltaMovement().lengthSqr() < 1.0E-7) {
                if (this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(serverWorld3, this.getPickupItem(), 0.1F);
                }

                this.discard();
            }
        }
    }
}
