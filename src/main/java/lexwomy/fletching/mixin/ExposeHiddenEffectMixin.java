package lexwomy.fletching.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StatusEffectInstance.class)
public interface ExposeHiddenEffectMixin {
    @Accessor
    StatusEffectInstance getHiddenEffect();

    @Accessor("duration")
    void setDuration(int duration);
}
