package lexwomy.fletching.render.item.property.numeric;

import com.mojang.serialization.MapCodec;
import lexwomy.fletching.item.GreatbowItem;
import lexwomy.fletching.item.LongbowItem;
import lexwomy.fletching.item.ShortbowItem;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

// Similar to numeric property UseDuration, but returns a % of the use time with respect to the draw time
// This will return 0 for non-bow items
// This is useful for rendering the shortbow while under frenzy or other draw time affecting enchantments
public record UsePercentProperty() implements NumericProperty {
    public static final MapCodec<UsePercentProperty> CODEC = MapCodec.unit(new UsePercentProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        if (holder != null && holder.getActiveItem() == stack) {
            Item item = stack.getItem();
            if (item instanceof ShortbowItem shortbow) {
                return (stack.getMaxUseTime(holder) - holder.getItemUseTimeLeft()) / shortbow.getFrenzyDrawTime(holder, stack);
            } else if (item instanceof LongbowItem) {
                return (stack.getMaxUseTime(holder) - holder.getItemUseTimeLeft()) / LongbowItem.DRAW_TIME;
            } else if (item instanceof GreatbowItem) {
                return (stack.getMaxUseTime(holder) - holder.getItemUseTimeLeft()) / GreatbowItem.DRAW_TIME;
            } else if (item instanceof BowItem) {
                return (stack.getMaxUseTime(holder) - holder.getItemUseTimeLeft()) / 20.0F;
            }
        }
        return 0.0F;
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}
