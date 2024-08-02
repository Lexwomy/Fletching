package lexwomy.fletching.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lexwomy.fletching.FletchingInitializer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HeldItemRenderer.class)
public abstract class AddNewBowsToHeldItemRendererMixin {

    @ModifyVariable(method = "getHandRenderType", at = @At(value = "STORE", opcode = Opcodes.ISTORE), ordinal = 0)
    private static boolean modifyHoldingBowCriteria(boolean result, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        return itemStack.isIn(FletchingInitializer.BOWS) || itemStack2.isIn(FletchingInitializer.BOWS);
    }

    @WrapOperation(method = "getUsingItemHandRenderType",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private static boolean includeNewBowsToRenderCheck(ItemStack instance, Item item, Operation<Boolean> original) {
        return !instance.isIn(FletchingInitializer.BOWS);
    }
}
