package lexwomy.fletching.mixin;

import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(BowItem.class)
public abstract class ConvertBowToShortbowMixin extends RangedWeaponItem {
    public ConvertBowToShortbowMixin(Settings settings) {
        super(settings);
    }


}
