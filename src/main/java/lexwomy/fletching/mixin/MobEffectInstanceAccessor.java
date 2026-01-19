package lexwomy.fletching.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEffectInstance.class)
public interface MobEffectInstanceAccessor {
  @Accessor("amplifier")
  void fletching$setAmplifier(int amplifier);

  @Accessor("duration")
  void fletching$setDuration(int duration);
}
