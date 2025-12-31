package lexwomy.fletching.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lexwomy.fletching.Fletching;
import lexwomy.fletching.item.GreatbowItem;
import lexwomy.fletching.item.LongbowItem;
import lexwomy.fletching.item.ShortbowItem;
import lexwomy.fletching.tags.FletchingItemTags;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemInHandRenderer.class)
public abstract class AddNewBowsToHeldItemRendererMixin {
    //These mixins allow the new bows to be rendered the same way the vanilla bow is rendered from the first person client perspective
    @ModifyVariable(method = "evaluateWhichHandsToRender", at = @At("STORE"), ordinal = 0)
    private static boolean modifyHoldingBowCriteria(boolean result, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2) {
        return result || itemStack.is(FletchingItemTags.BOWS) || itemStack2.is(FletchingItemTags.BOWS);
    }

    @WrapOperation(method = "selectionUsingItemWhileHoldingBowLike",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 0))
    private static boolean includeNewBowsToRenderCheck(ItemStack instance, Item item, Operation<Boolean> original) {
        return original.call(instance, item) || instance.is(FletchingItemTags.BOWS);
    }

    //This modifies the mxx variable to match the draw time of the other bows
    //This method controls the movement/transformation of the 3D item model when pulling the bow back
    @ModifyVariable(method = "renderArmWithItem",
                    slice = @Slice(
                            from = @At(
                                    value = "INVOKE",
                                    target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I",
                                    ordinal = 1),
                            to = @At(
                                    value = "INVOKE",
                                    target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I",
                                    ordinal = 2)),
                    at = @At(value = "STORE", ordinal = 0),
                    index = 17)
    private float adjustDrawTimeForPullBack(float value, @Local(argsOnly = true) AbstractClientPlayer abstractClientPlayer,
                                            @Local(argsOnly = true) ItemStack itemStack, @Local(index=16) float lxxx) {
        Item bow = itemStack.getItem();
        Fletching.devLogger("adjustDrawTimeForPullBack check - original value {}, captured lxxx {}", value, lxxx);
        switch (bow) {
            case ShortbowItem shortbow -> {
                float draw_time = shortbow.getFrenzyDrawTime(abstractClientPlayer, itemStack);
                //Fletching.LOGGER.info("fxx original: {}, mx: {}, new fxx: {}, draw_time: {}", original, mx, mx / draw_time, draw_time);
                return lxxx / draw_time;
            }
            case LongbowItem longbowItem -> {
                float draw_time = LongbowItem.DRAW_TIME;
                //Fletching.LOGGER.info("fxx original: {}, mx: {}, new fxx: {}", original, mx, mx / draw_time);
                return lxxx / draw_time;
            }
            case GreatbowItem greatbow -> {
                float draw_time = greatbow.getDrawTime(itemStack, abstractClientPlayer);
                return lxxx / draw_time;
            }
            default -> {
                return value;
            }
        }
        //TODO - Add greatbow logic
    }
}


