package lexwomy.fletching.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(LivingEntity.class)
public abstract class AddStackingStatusEffectLogicMixin {
//    @ModifyArg(method = "onStatusEffectUpgraded",
//            slice = @Slice(from = @At(value = "INVOKE",
//                    target = "Lnet/minecraft/entity/effect/StatusEffect;onRemoved(Lnet/minecraft/entity/attribute/AttributeContainer;)V")),
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;onApplied(Lnet/minecraft/entity/attribute/AttributeContainer;I)V"),
//            index = 1)
    @ModifyExpressionValue(method = "onStatusEffectUpgraded",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;getAmplifier()I"))
    private int updateAmplifierIfFrenzy(int amplifier, StatusEffectInstance effect) {
        StatusEffectInstance previous = ((ExposeHiddenEffectMixin) effect).getHiddenEffect();
        if (previous != null) {
            amplifier += previous.getAmplifier();
        }
        return amplifier;
    }
}
