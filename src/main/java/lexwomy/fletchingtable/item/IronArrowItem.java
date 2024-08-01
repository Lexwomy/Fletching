package lexwomy.fletchingtable.item;

import lexwomy.fletchingtable.component.FletchingComponents;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class IronArrowItem extends ArrowItem {

    public IronArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        int hardness = stack.getOrDefault(FletchingComponents.HARDNESS, 0);
        tooltip.add(Text.translatable("item.fletching-table.hardness.info1").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("item.fletching-table.hardness.info2", hardness).formatted(Formatting.BLUE));
    }
}
