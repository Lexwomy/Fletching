package lexwomy.fletching.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lexwomy.fletching.item.GreatbowItem;
import lexwomy.fletching.item.LongbowItem;
import lexwomy.fletching.item.ShortbowItem;
import lexwomy.fletching.tags.FletchingItemTags;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(HeldItemRenderer.class)
public abstract class AddNewBowsToHeldItemRendererMixin {
    //These mixins allow the new bows to be rendered the same way the vanilla bow is rendered from the first person client perspective
    @ModifyVariable(method = "getHandRenderType", at = @At(value = "STORE", opcode = Opcodes.ISTORE), ordinal = 0)
    private static boolean modifyHoldingBowCriteria(boolean result, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        return result || itemStack.isIn(FletchingItemTags.BOWS) || itemStack2.isIn(FletchingItemTags.BOWS);
    }

    @WrapOperation(method = "getUsingItemHandRenderType",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 0))
    private static boolean includeNewBowsToRenderCheck(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || instance.isIn(FletchingItemTags.BOWS);
    }

    //This modifies the fxx variable to match the draw time of the other bows
    //This method controls the movement/transformation of the 3D item model when pulling the bow back
    @ModifyVariable(method = "renderFirstPersonItem",
                    slice = @Slice(
                            from = @At(
                                    value = "INVOKE",
                                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                                    ordinal = 5),
                            to = @At(
                                    value = "INVOKE",
                                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                                    ordinal = 6)),
                    at = @At(value = "STORE", opcode = Opcodes.FSTORE, ordinal = 0),
                    ordinal = 5)
    private float adjustDrawTimeForPullBack(float original, AbstractClientPlayerEntity player, float tickDelta,
                       float pitch, Hand hand, float swingProgress, ItemStack item,
                       float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light) {
        Item bow = item.getItem();
        float mx = (float)item.getMaxUseTime(player) - ((float)player.getItemUseTimeLeft() - tickDelta + 1.0F);
        if (bow instanceof ShortbowItem shortbow) {
            float draw_time = shortbow.getFrenzyDrawTime(player, item);
            //Fletching.LOGGER.info("fxx original: {}, mx: {}, new fxx: {}", original, mx, mx / draw_time);
            return mx / draw_time;
        } else if (bow instanceof LongbowItem) {
            float draw_time = LongbowItem.DRAW_TIME;
            //Fletching.LOGGER.info("fxx original: {}, mx: {}, new fxx: {}", original, mx, mx / draw_time);
            return mx / draw_time;
        } else if (bow instanceof GreatbowItem) {
            float draw_time = GreatbowItem.DRAW_TIME;
            return mx / draw_time;
        }
        //TODO - Add greatbow logic
        return original;
    }
}


