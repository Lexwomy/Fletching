package lexwomy.fletching.render.item.property.numeric;

import com.mojang.serialization.MapCodec;
import lexwomy.fletching.item.GreatbowItem;
import lexwomy.fletching.item.LongbowItem;
import lexwomy.fletching.item.ShortbowItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

// Similar to numeric property UseDuration, but returns a % of the use time with respect to the draw time
// This will return 0 for non-bow items
// This is useful for rendering the shortbow while under frenzy or other draw time affecting enchantments
public record UsePercentProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<UsePercentProperty> CODEC = MapCodec.unit(new UsePercentProperty());

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable ItemOwner itemOwner, int i) {
        if (itemOwner != null) {
            LivingEntity holder = itemOwner.asLivingEntity();
            if (holder != null && holder.getActiveItem() == itemStack) {
                Item item = itemStack.getItem();
                if (item instanceof ShortbowItem shortbow) {
                    return (itemStack.getUseDuration(holder) - holder.getUseItemRemainingTicks()) / shortbow.getFrenzyDrawTime(holder, itemStack);
                } else if (item instanceof LongbowItem) {
                    return (itemStack.getUseDuration(holder) - holder.getUseItemRemainingTicks()) / LongbowItem.DRAW_TIME;
                } else if (item instanceof GreatbowItem) {
                    return (itemStack.getUseDuration(holder) - holder.getUseItemRemainingTicks()) / GreatbowItem.DRAW_TIME;
                } else if (item instanceof BowItem) {
                    return (itemStack.getUseDuration(holder) - holder.getUseItemRemainingTicks()) / 20.0F;
                }
            }
        }
        return 0.0F;
    }

    @Override
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return CODEC;
    }
}
