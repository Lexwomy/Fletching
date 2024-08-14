package lexwomy.fletching.tags;

import lexwomy.fletching.Fletching;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class FletchingItemTags {
    public static final TagKey<Item> BOWS = TagKey.of(RegistryKeys.ITEM, Identifier.of(Fletching.MOD_ID, "bows"));
    public static void initialize() {
        Fletching.LOGGER.info("Fletching item tags registered!");
    }
}
