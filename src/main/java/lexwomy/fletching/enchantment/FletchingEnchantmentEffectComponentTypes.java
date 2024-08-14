package lexwomy.fletching.enchantment;

import lexwomy.fletching.Fletching;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FletchingEnchantmentEffectComponentTypes {
    public static ComponentType<EnchantmentValueEffect> DRAW_TIME = Registry.register(
            Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
            Identifier.of(Fletching.MOD_ID, "draw_time"),
            ComponentType.<EnchantmentValueEffect>builder().codec(EnchantmentValueEffect.CODEC).build());
    public static void initialize() {
        Fletching.LOGGER.info("Fletching enchantment effect component types initialized!");
    }
}
