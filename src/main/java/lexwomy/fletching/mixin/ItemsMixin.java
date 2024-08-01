package lexwomy.fletching.mixin;

import lexwomy.fletching.item.ShortbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.minecraft.item.Items.register;

@Mixin(Items.class)
public abstract class ItemsMixin {
    //Didn't work

    @ModifyArgs(method = "<clinit>",
                slice = @Slice(
                        from = @At(value = "CONSTANT", args = "stringValue=bow")
                ),
                at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;",
                        ordinal = 0))
    private static void changeBowToShortbow(Args args) {
        args.set(1, new ShortbowItem(new Item.Settings().maxDamage(512)));
    }
}
