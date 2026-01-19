package lexwomy.fletching.effect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface SupportsStackingEffect {
  boolean fletching$stackEffect(
      MobEffectInstance stackableMobEffectInstance, @Nullable Entity entity);

  boolean fletching$stackBurstEffect(
      MobEffectInstance regularMobEffectInstance,
      MobEffectInstance burstMobEffectInstance,
      int burstTrigger,
      @Nullable Entity entity);
}
