package lexwomy.fletching.enchantment;

import lexwomy.fletching.Fletching;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import org.jetbrains.annotations.NotNull;

public class FletchingEnchantmentEffectComponentTypes {
    public static DataComponentType<EnchantmentValueEffect> DRAW_TIME = Registry.register(
            BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "draw_time"),
            DataComponentType.<EnchantmentValueEffect>builder().persistent(EnchantmentValueEffect.CODEC).build());
    public static DataComponentType<EnchantmentValueEffect> INACCURACY = Registry.register(
            BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "inaccuracy"),
            DataComponentType.<EnchantmentValueEffect>builder().persistent(EnchantmentValueEffect.CODEC).build());
    public static DataComponentType<EnchantmentValueEffect> SHRAPNEL_COUNT = Registry.register(
            BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "shrapnel_count"),
            DataComponentType.<EnchantmentValueEffect>builder().persistent(EnchantmentValueEffect.CODEC).build());

    public static void initialize() {
        Fletching.LOGGER.info("Fletching enchantment effect component types initialized!");
    }
}
