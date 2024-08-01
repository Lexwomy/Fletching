package lexwomy.fletchingtable.mixin;

import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArrowItem.class)
public abstract class ArrowItemToFlintArrowItemMixin extends Item {
    public ArrowItemToFlintArrowItemMixin(Settings settings) {
        super(settings);
    }
}
