package lexwomy.fletching.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import lexwomy.fletching.tags.FletchingEnchantmentTags;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.apache.commons.lang3.mutable.MutableFloat;

public class FletchingEnchantmentHelper {

    public static float modifyDrawTime(LivingEntity user, ItemStack stack, float drawTime) {
        MutableFloat mutableFloat = new MutableFloat(drawTime);
        //This check might not be necessary
        if (EnchantmentHelper.hasAnyEnchantmentsIn(stack, FletchingEnchantmentTags.MODIFIES_DRAW_TIME)) {
            ItemEnchantmentsComponent itemEnchantmentsComponent = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
            //Grab all enchantments on the item and iterate through them
            //If an enchantment has a value effect that is listed under the DRAW_TIME component, modify the draw time float
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                Enchantment enchantment = entry.getKey().value();
                EnchantmentValueEffect enchantmentValueEffect = enchantment.effects().get(FletchingEnchantmentEffectComponentTypes.DRAW_TIME);
                if (enchantmentValueEffect != null) {
                    mutableFloat.setValue(enchantmentValueEffect.apply(entry.getIntValue(), user.getRandom(), mutableFloat.floatValue()));
                }
            }
        }
        return mutableFloat.floatValue();
    }

    public static float increaseGreatbowVelocity(ItemStack stack, float velocity) {
        if (EnchantmentHelper.hasAnyEnchantmentsIn(stack, FletchingEnchantmentTags.PHOTONIC_CHARGE)) {
            return velocity * 1.8F;
        } else {
            return velocity;
        }
    }
}
