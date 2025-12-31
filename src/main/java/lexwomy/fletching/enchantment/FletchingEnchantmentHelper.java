package lexwomy.fletching.enchantment;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import lexwomy.fletching.tags.FletchingEnchantmentTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

public class FletchingEnchantmentHelper {

    public static float modifyDrawTime(LivingEntity user, ItemStack stack, float drawTime) {
        MutableFloat mutableFloat = new MutableFloat(drawTime);
        //This check might not be necessary
        if (EnchantmentHelper.hasTag(stack, FletchingEnchantmentTags.MODIFIES_DRAW_TIME)) {
            ItemEnchantments itemEnchantmentsComponent = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            //Grab all enchantments on the item and iterate through them
            //If an enchantment has a value effect that is listed under the DRAW_TIME component, modify the draw time float
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantmentsComponent.entrySet()) {
                Enchantment enchantment = entry.getKey().value();
                EnchantmentValueEffect enchantmentValueEffect = enchantment.effects().get(FletchingEnchantmentEffectComponentTypes.DRAW_TIME);
                if (enchantmentValueEffect != null) {
                    mutableFloat.setValue(enchantmentValueEffect.process(entry.getIntValue(), user.getRandom(), mutableFloat.floatValue()));
                }
            }
        }
        return mutableFloat.floatValue();
    }

    public static float modifyInaccuracy(LivingEntity user, ItemStack stack, float radius) {
        MutableFloat mutableFloat = new MutableFloat(radius);
        if (EnchantmentHelper.hasTag(stack, FletchingEnchantmentTags.MODIFIES_ACCURACY)) {
            ItemEnchantments itemEnchantmentsComponent = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantmentsComponent.entrySet()) {
                Enchantment enchantment = entry.getKey().value();
                EnchantmentValueEffect enchantmentValueEffect = enchantment.effects().get(FletchingEnchantmentEffectComponentTypes.INACCURACY);
                if (enchantmentValueEffect != null) {
                    mutableFloat.setValue(enchantmentValueEffect.process(entry.getIntValue(), user.getRandom(), mutableFloat.floatValue()));
                }
            }
        }
        return mutableFloat.floatValue();
    }

    public static int getShrapnelCount(LivingEntity user, ItemStack stack) {
        MutableInt mutableInt = new MutableInt(0);
        if (EnchantmentHelper.hasTag(stack, FletchingEnchantmentTags.MODIFIES_SHRAPNEL_COUNT)) {
            ItemEnchantments itemEnchantmentsComponent = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantmentsComponent.entrySet()) {
                Enchantment enchantment = entry.getKey().value();
                EnchantmentValueEffect enchantmentValueEffect = enchantment.effects().get(FletchingEnchantmentEffectComponentTypes.SHRAPNEL_COUNT);
                if (enchantmentValueEffect != null) {
                    mutableInt.setValue(enchantmentValueEffect.process(entry.getIntValue(), user.getRandom(), mutableInt.intValue()));
                }
            }
        }
        return mutableInt.intValue();
    }

    public static boolean hasEnchantment(ItemStack stack, Component name) {
        ItemEnchantments itemEnchantmentsComponent = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : itemEnchantmentsComponent.entrySet()) {
            Enchantment enchantment = entry.getKey().value();
            if (enchantment.description().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static float increaseGreatbowVelocity(ItemStack stack, float velocity) {
        if (EnchantmentHelper.hasTag(stack, FletchingEnchantmentTags.PHOTONIC_CHARGE)) {
            return velocity * 1.8F;
        } else {
            return velocity;
        }
    }
}
