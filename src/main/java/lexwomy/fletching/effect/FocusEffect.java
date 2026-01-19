package lexwomy.fletching.effect;

import lexwomy.fletching.mixin.MobEffectInstanceAccessor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class FocusEffect extends MobEffect implements StackingEffect {
  public static final int MAX_FOCUS_STACK = 2;
  public static final int MAX_FOCUS_AMPLIFIER = MAX_FOCUS_STACK - 1;

  public FocusEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xADD8E6);
  }

  public static FocusState getFocusedState(LivingEntity user) {
    MobEffectInstance effect = user.getEffect(FletchingEffects.EAGLESIGHT);
    if (effect != null) {
      return FocusState.EAGLESIGHT;
    }
    effect = user.getEffect(FletchingEffects.FOCUS);
    int focus_stack = effect == null ? 0 : FocusEffect.getFocusStack(effect);
    if (focus_stack == 0) {
      return FocusState.UNFOCUSED;
    } else {
      return FocusState.FOCUSED;
    }
  }

  public static int getFocusStack(@Nullable MobEffectInstance instance) {
    return instance == null ? 0 : Math.min(instance.getAmplifier() + 1, MAX_FOCUS_STACK);
  }

  public static int getStackedFocusAmplifier(
      MobEffectInstance currentInstance, MobEffectInstance incomingInstance) {
    return Math.min(
        currentInstance.getAmplifier() + incomingInstance.getAmplifier() + 1, MAX_FOCUS_AMPLIFIER);
  }

  public @Nullable MobEffectInstance applyBurstStack(
      LivingEntity livingEntity,
      MobEffectInstance incomingStack,
      MobEffectInstance burstStack,
      int burstTrigger) {
    MobEffectInstance currentRegularEffectInstance =
        livingEntity.getEffect(incomingStack.getEffect());
    MobEffectInstance currentBurstEffectInstance = livingEntity.getEffect(burstStack.getEffect());
    boolean hasRegularEffect = currentRegularEffectInstance != null;
    boolean hasBurstEffect = currentBurstEffectInstance != null;

    if (hasRegularEffect == hasBurstEffect) {
      return null;
    } else if (hasRegularEffect) {
      int new_amplifier =
          currentRegularEffectInstance.getAmplifier() + incomingStack.getAmplifier() + 1;
      if (new_amplifier >= burstTrigger) {
        livingEntity.removeEffect(incomingStack.getEffect());
        livingEntity.addEffect(burstStack);
        return burstStack;
      } else {
        ((MobEffectInstanceAccessor) currentRegularEffectInstance)
            .fletching$setAmplifier(new_amplifier);
        ((MobEffectInstanceAccessor) currentRegularEffectInstance)
            .fletching$setDuration(incomingStack.getDuration());
        return currentRegularEffectInstance;
      }
    } else {
      livingEntity.removeEffect(currentBurstEffectInstance.getEffect());
      livingEntity.addEffect(incomingStack);
      return incomingStack;
    }
  }

  @Override
  public StackingEffectType getStackingEffectType() {
    return StackingEffectType.BURST_STACKING;
  }

  public enum FocusState {
    UNFOCUSED(false),
    FOCUSED(true),
    EAGLESIGHT(true);

    private final boolean focused;

    FocusState(boolean focused) {
      this.focused = focused;
    }

    public boolean isFocused() {
      return focused;
    }
  }
  // TODO Make custom particle effect
}
