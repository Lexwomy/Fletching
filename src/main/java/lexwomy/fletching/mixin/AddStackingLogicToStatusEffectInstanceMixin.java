package lexwomy.fletching.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lexwomy.fletching.Fletching;
import lexwomy.fletching.effect.FletchingEffects;
import net.minecraft.world.effect.MobEffectInstance;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//TODO - fix frenzy stacking logic, it appears to be inconsistent right now
@Mixin(MobEffectInstance.class)
public abstract class AddStackingLogicToStatusEffectInstanceMixin {
    //Allows stacking effects to bypass normal time checks when adding hidden effects
    //This allows for deterioration and also allows the previous effect to be accessed to be compounded
    //This requires a recursive check upon every new status effect which can quickly compound
    //Decided not worth to pursue true stack like behavior
//    @ModifyExpressionValue(method = "upgrade",
//            at = @At(value = "INVOKE",
//                    target = "Lnet/minecraft/entity/effect/StatusEffectInstance;lastsShorterThan(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
//                    ordinal = 0))
//    private boolean allowStackingToBypassTimeCheck(boolean original, StatusEffectInstance that) {
//        boolean stacking = that.equals(Fletching.FRENZY); //TODO - Add longbow stacking behavior later
//        return original || stacking;
//    }

    //Allows stacking effects to bypass normal amplifier check restriction
    //As adding a smaller frenzy e.g. should compound on the current one
    @Definition(id = "amplifier", field = "Lnet/minecraft/world/effect/MobEffectInstance;amplifier:I")
    @Definition(id = "that", local = @Local(argsOnly = true, type = MobEffectInstance.class))
    @Expression("? > this.amplifier")
    @ModifyExpressionValue(method = "update", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean allowStackingToBypassAmplifierCheck(boolean original, MobEffectInstance that) {
        boolean stacking = that.is(FletchingEffects.FRENZY);
        return original || stacking;
    }

    //Sets the current amplifier for the new effect to be the sum of the previous and the incoming stacking effect
    @WrapOperation(method = "update",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/effect/MobEffectInstance;amplifier:I", opcode = Opcodes.PUTFIELD))
    private void applyPreviousAmplifierToCurrent(MobEffectInstance instance, int value, Operation<Void> original, MobEffectInstance that) {
        if (that.is(FletchingEffects.FRENZY)) {
            int new_amplifier = value + instance.getAmplifier() + 1;
            if (new_amplifier > 39) {
                new_amplifier = 39;
            }
            original.call(instance, new_amplifier);
        } else {
            original.call(instance, value);
        }
    }
}
