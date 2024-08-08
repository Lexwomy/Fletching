package lexwomy.fletching.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lexwomy.fletching.Fletching;
import lexwomy.fletching.item.LongbowItem;
import lexwomy.fletching.item.ShortbowItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class AddNewBowsToHeldItemRendererMixin {
    //These mixins allow the new bows to be rendered the same way the vanilla bow is rendered from the first person client perspective
    @ModifyVariable(method = "getHandRenderType", at = @At(value = "STORE", opcode = Opcodes.ISTORE), ordinal = 0)
    private static boolean modifyHoldingBowCriteria(boolean result, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        return result || itemStack.isIn(Fletching.BOWS) || itemStack2.isIn(Fletching.BOWS);
    }

    @WrapOperation(method = "getUsingItemHandRenderType",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private static boolean includeNewBowsToRenderCheck(ItemStack instance, Item item, Operation<Boolean> original) {
        return !instance.isIn(Fletching.BOWS);
    }

//    @Inject(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V",
//            at = @At(value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
//                    ordinal = 0))
//    private void observeEquipProgress(float tickDelta, MatrixStack matrices,
//                                      VertexConsumerProvider.Immediate vertexConsumers,
//                                      ClientPlayerEntity player, int light, CallbackInfo ci,
//                                      @Local(name = "k", ordinal = 0) float k) {
//        Fletching.LOGGER.info("Equip progress: {}", k);
//    }

    @Inject(method = "renderFirstPersonItem",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                    ordinal = 5))
    private void observeFirstPersonProgress(AbstractClientPlayerEntity player, float tickDelta,
                                            float pitch, Hand hand, float swingProgress, ItemStack item,
                                            float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                            int light, CallbackInfo ci) {
        //Too hard to target the constants so just calculate mx and fxx here and log them
        Item bow = item.getItem();
        float mx = (float)item.getMaxUseTime(player) - ((float)player.getItemUseTimeLeft() - tickDelta + 1.0F);
        float fxx = mx / 20.0F;
        float fxx_new = (fxx * fxx + fxx * 2.0F) / 3.0F;
        Fletching.LOGGER.info("mx: {}, vanilla fxx: {}, vanilla computed fxx: {}", mx, fxx, fxx_new);

        if (bow instanceof ShortbowItem) {
            float shortbow_fxx = mx / ((ShortbowItem) bow).getFrenzyDrawTime(player);
            float shortbow_fxx_new = (shortbow_fxx * shortbow_fxx + shortbow_fxx * 2.0F) / 3.0F;
            Fletching.LOGGER.info("mx: {}, shortbow fxx: {}, shortbow computed fxx: {}", mx, shortbow_fxx, shortbow_fxx_new);
        }

        if (bow instanceof LongbowItem) {
            float longbow_fxx = mx / LongbowItem.DRAW_TIME;
            float longbow_fxx_new = (longbow_fxx * longbow_fxx + longbow_fxx * 2.0F) / 3.0F;
            Fletching.LOGGER.info("mx: {}, longbow fxx: {}, longbow computed fxx: {}", mx, longbow_fxx, longbow_fxx_new);
        }
    }

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
                    at = @At(value = "STORE", opcode = Opcodes.FSTORE),
                    ordinal = 5)
    private float test(float original, AbstractClientPlayerEntity player, float tickDelta,
                       float pitch, Hand hand, float swingProgress, ItemStack item,
                       float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light) {
        Item bow = item.getItem();
        float mx = (float)item.getMaxUseTime(player) - ((float)player.getItemUseTimeLeft() - tickDelta + 1.0F);
        if (bow instanceof ShortbowItem) {

            //float mx = original * 20.0F;
            //ShortbowItem bow = (ShortbowItem) item.getItem();
            float draw_time = ((ShortbowItem) bow).getFrenzyDrawTime(player);
            Fletching.LOGGER.info("fxx original: {}, mx: {}, new fxx: {}", original, mx, mx / draw_time);
            return mx / draw_time;
        } else if (bow instanceof LongbowItem) {
            //float mx = original * 20.0F;
            float draw_time = LongbowItem.DRAW_TIME;
            Fletching.LOGGER.info("fxx original: {}, mx: {}, new fxx: {}", original, mx, mx / draw_time);
            return mx / draw_time;
        }
        //TODO - Add greatbow logic
        return original;
    }
}
