package lexwomy.fletching.tags;

import lexwomy.fletching.Fletching;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class FletchingEnchantmentTags {
    public static final TagKey<Enchantment> MODIFIES_DRAW_TIME = TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(Fletching.MOD_ID, "modifies_draw_time"));
    public static void initialize() {
        Fletching.LOGGER.info("Fletching enchantment tags registered!");
    }
}
