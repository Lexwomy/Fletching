package lexwomy.fletching.mixin;

import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ArrowItem.class)
public abstract class ArrowItemToFlintArrowItemMixin extends Item {
    public ArrowItemToFlintArrowItemMixin(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {

    }
}
