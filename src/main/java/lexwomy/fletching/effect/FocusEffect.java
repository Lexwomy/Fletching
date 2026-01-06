package lexwomy.fletching.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class FocusEffect extends MobEffect {
  public FocusEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xADD8E6);
  }

  public static FocusState getFocusedState(LivingEntity user) {
    MobEffectInstance effect = user.getEffect(FletchingEffects.FOCUS);
    int focus_stack = effect == null ? 0 : FocusEffect.getFocusStack(effect);
    if (focus_stack == 2) {
      return FocusState.EAGLESIGHT;
    } else if (focus_stack == 0) {
      return FocusState.UNFOCUSED;
    } else {
      return FocusState.FOCUSED;
    }
  }

  public static int getFocusStack(@Nullable MobEffectInstance instance) {
    return instance == null ? 0 : Math.min(instance.getAmplifier() + 1, 3);
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
