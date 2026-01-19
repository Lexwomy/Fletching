package lexwomy.fletching.effect;

import lexwomy.fletching.mixin.MobEffectInstanceAccessor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class FrenzyEffect extends MobEffect implements StackingEffect {
  public static final int MAX_FRENZY_STACK = 40;
  public static final int MAX_FRENZY_AMPLIFIER = MAX_FRENZY_STACK - 1;

  public FrenzyEffect() {
    super(MobEffectCategory.BENEFICIAL, 0x9b870c);
  }

  public static int getFrenzyStack(@Nullable MobEffectInstance instance) {
    return instance == null ? 0 : Math.min(instance.getAmplifier() + 1, MAX_FRENZY_STACK);
  }

  @Override
  public boolean applyStack(LivingEntity livingEntity, MobEffectInstance incomingStack) {
    MobEffectInstance currentStack = livingEntity.getEffect(incomingStack.getEffect());
    if (currentStack == null || !currentStack.is(FletchingEffects.FRENZY)) {
      return false;
    } else {
      int new_amplifier = getStackedFrenzyAmplifier(currentStack, incomingStack);
      ((MobEffectInstanceAccessor) currentStack).fletching$setAmplifier(new_amplifier);
      ((MobEffectInstanceAccessor) currentStack).fletching$setDuration(incomingStack.getDuration());
      return true;
    }
  }

  // TODO - Make custom particle effect?

  public static int getStackedFrenzyAmplifier(
      MobEffectInstance currentInstance, MobEffectInstance incomingInstance) {
    return Math.min(
        currentInstance.getAmplifier() + incomingInstance.getAmplifier() + 1, MAX_FRENZY_AMPLIFIER);
  }

  @Override
  public StackingEffectType getStackingEffectType() {
    return StackingEffectType.STACKING;
  }
}
