package lexwomy.fletching.tags;

import lexwomy.fletching.Fletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class FletchingEnchantmentTags {
    public static final TagKey<Enchantment> MODIFIES_DRAW_TIME = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "modifies_draw_time"));
    public static final TagKey<Enchantment> MODIFIES_ACCURACY = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "modifies_accuracy"));
    public static final TagKey<Enchantment> MODIFIES_SHRAPNEL_COUNT = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "modifies_shrapnel_count"));
    public static final TagKey<Enchantment> GREATBOW_ULTIMATE = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "greatbow_ultimate"));
    public static final TagKey<Enchantment> PHOTONIC_CHARGE = TagKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "photonic_charge"));
    public static void initialize() {
        Fletching.LOGGER.info("Fletching enchantment tags registered!");
    }
}
