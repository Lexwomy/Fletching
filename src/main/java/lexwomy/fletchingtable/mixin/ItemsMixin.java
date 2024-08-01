package lexwomy.fletchingtable.mixin;

import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static net.minecraft.item.Items.register;

@Mixin(Items.class)
public abstract class ItemsMixin {
    //Didn't work
    //@Shadow
    //public static final Item ARROW = register("flint_arrow", new ArrowItem(new Item.Settings()));

    @ModifyArgs(method = "<clinit>",
                slice = @Slice(
                        from = @At(value = "CONSTANT", args = "stringValue=arrow")
                ),
                at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;",
                        ordinal = 0))
    private static void modifyArrowToFlintArrow(Args args) {
        //This will load an entirely new item and delete all normal arrows
        //It is necessary that minecraft:arrow stay the same, though the language can be renamed to flint arrow
        //args.set(0, "flint_arrow");
        //Set new ArrowItem() to custom class later TODO
    }
}
