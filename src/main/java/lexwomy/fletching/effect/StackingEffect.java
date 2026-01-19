package lexwomy.fletching.effect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface StackingEffect {
  // Sets the amplifier, duration, and mob effect instance details only
  default boolean applyStack(LivingEntity livingEntity, MobEffectInstance incomingStack) {
    return false;
  }

  // Applies the regular stack, and on burst trigger
  default @Nullable MobEffectInstance applyBurstStack(
      LivingEntity livingEntity,
      MobEffectInstance incomingStack,
      MobEffectInstance burstStack,
      int burstTrigger) {
    return null;
  }

  StackingEffectType getStackingEffectType();

  enum StackingEffectType {
    STACKING,
    BURST_STACKING,
  }
}
