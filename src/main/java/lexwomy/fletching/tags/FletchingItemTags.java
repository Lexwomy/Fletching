package lexwomy.fletching.tags;

import lexwomy.fletching.Fletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FletchingItemTags {
    public static final TagKey<Item> BOWS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "bows"));
    public static final TagKey<Item> PILUMS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "pilums"));
    public static void initialize() {
        Fletching.LOGGER.info("Fletching item tags registered!");
    }
}
