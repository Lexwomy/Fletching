package lexwomy.fletching.mixin;

import java.util.Map;
import lexwomy.fletching.effect.StackingEffect;
import lexwomy.fletching.effect.SupportsStackingEffect;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class AddStackingMethodsToLivingEntityMixin extends Entity
    implements SupportsStackingEffect, Attackable, WaypointTransmitter {

  @Shadow @Final private Map<Holder<MobEffect>, MobEffectInstance> activeEffects;

  private AddStackingMethodsToLivingEntityMixin(EntityType<?> entityType, Level level) {
    super(entityType, level);
  }

  //  @Inject(
  //      method =
  //
  // "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
  //      at = @At(value = "HEAD"),
  //      cancellable = true)
  //  private void preventExclusiveEffects(
  //      MobEffectInstance mobEffectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir) {
  //    // Prevents exclusive effects from being applied
  //    // If at least 1 effect from the exclusive set is present and if incoming effect is part of
  // exclusive set, return
  //    MobEffectInstance currentEffectInstance = ((LivingEntity) (Object)this).getEffect()
  //  }

  public boolean fletching$stackEffect(
      MobEffectInstance stackableMobEffectInstance, @Nullable Entity entity) {
    LivingEntity this_entity = ((LivingEntity) (Object) this);
    Holder<MobEffect> incomingEffect = stackableMobEffectInstance.getEffect();
    MobEffectInstance currentEffectInstance = this_entity.getEffect(incomingEffect);

    if (!this_entity.canBeAffected(stackableMobEffectInstance)
        || !(incomingEffect.value() instanceof StackingEffect stackingEffect)
        || (incomingEffect.value() instanceof StackingEffect
            && stackingEffect.getStackingEffectType()
                != StackingEffect.StackingEffectType.STACKING)) {
      return false;
    } else if (currentEffectInstance == null) {
      return this_entity.addEffect(stackableMobEffectInstance);
    } else {
      // Current instance is not null, entity can be affected, incoming effect is regular stackable
      boolean successful = stackingEffect.applyStack(this_entity, stackableMobEffectInstance);
      if (successful) {
        this.onEffectUpdated(currentEffectInstance, true, entity);
        stackableMobEffectInstance.onEffectStarted(this_entity);
      }
      return successful;
    }
  }

  @Shadow
  protected abstract void onEffectUpdated(
      MobEffectInstance mobEffectInstance, boolean bl, @Nullable Entity entity);

  public boolean fletching$stackBurstEffect(
      MobEffectInstance regularMobEffectInstance,
      MobEffectInstance burstMobEffectInstance,
      int burstTrigger,
      @Nullable Entity entity) {
    LivingEntity this_entity = ((LivingEntity) (Object) this);
    Holder<MobEffect> incomingRegularEffect = regularMobEffectInstance.getEffect();
    Holder<MobEffect> incomingBurstEffect = burstMobEffectInstance.getEffect();
    MobEffectInstance currentRegularEffectInstance = this_entity.getEffect(incomingRegularEffect);
    MobEffectInstance currentBurstEffectInstance = this_entity.getEffect(incomingBurstEffect);

    /*
    Cannot stack burst effect if:
    Entity cannot be affected by either effect
    Both effects are not burst stacking effects
     */
    boolean canBeAffected =
        this_entity.canBeAffected(regularMobEffectInstance)
            || this_entity.canBeAffected(burstMobEffectInstance);
    boolean areBurstStacking =
        incomingRegularEffect.value() instanceof StackingEffect regularStackingEffect
            && regularStackingEffect.getStackingEffectType()
                == StackingEffect.StackingEffectType.BURST_STACKING
            && incomingBurstEffect.value() instanceof StackingEffect burstStackingEffect
            && burstStackingEffect.getStackingEffectType()
                == StackingEffect.StackingEffectType.BURST_STACKING;
    boolean hasRegularEffect = currentRegularEffectInstance != null;
    boolean hasBurstEffect = currentBurstEffectInstance != null;

    if (!canBeAffected || !areBurstStacking) {
      return false;
    } else if (hasRegularEffect && hasBurstEffect) {
      // Bugged case where user has both regular and burst effects, which should be exclusive
      // Remove both to reset to base
      this_entity.removeEffect(currentRegularEffectInstance.getEffect());
      this_entity.removeEffect(currentBurstEffectInstance.getEffect());
      return false;
    } else if (!hasRegularEffect && !hasBurstEffect) {
      return this_entity.addEffect(regularMobEffectInstance);
    } else {
      MobEffectInstance appliedStack =
          ((StackingEffect) incomingRegularEffect.value())
              .applyBurstStack(
                  this_entity, regularMobEffectInstance, burstMobEffectInstance, burstTrigger);
      if (appliedStack != null) {
        this.onEffectUpdated(appliedStack, true, entity);
        appliedStack.onEffectStarted(this_entity);
      }
      return appliedStack != null;
    }
  }
}
