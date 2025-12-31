package lexwomy.fletching.mixin;

import lexwomy.fletching.entity.ShrapnelEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class DisablePiercingFromShrapnelMixin {
    //This is just anti cheese if you forcibly put piercing on a shortbow with scattershot
    @Inject(method = "setPierceLevel", at = @At("HEAD"), cancellable = true)
    private void disablePiercingFromShrapnel(byte level, CallbackInfo ci) {
        if (((AbstractArrow)(Object)this) instanceof ShrapnelEntity) {
            ci.cancel();
        }
    }
}
