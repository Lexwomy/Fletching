package lexwomy.fletching.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EaglesightEffect extends MobEffect implements StackingEffect {
  public EaglesightEffect() {
    super(MobEffectCategory.BENEFICIAL, 0xADD8E6);
  }

  @Override
  public StackingEffectType getStackingEffectType() {
    return StackingEffectType.BURST_STACKING;
  }
}
