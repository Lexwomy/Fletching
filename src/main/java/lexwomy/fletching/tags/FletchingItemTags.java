package lexwomy.fletching.tags;

import lexwomy.fletching.Fletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class FletchingItemTags {
  public static final TagKey<Item> BOWS = FletchingTags.createTagKey(Registries.ITEM, "bows");
  public static final TagKey<Item> PILUMS = FletchingTags.createTagKey(Registries.ITEM, "pilums");

  public static final TagKey<Item> SHORTBOW_ENCHANTABLE =
      FletchingTags.createTagKey(Registries.ITEM, "enchantable/shortbow");
  public static final TagKey<Item> LONGBOW_ENCHANTABLE =
      FletchingTags.createTagKey(Registries.ITEM, "enchantable/longbow");

  public static void initialize() {
    Fletching.LOGGER.info("Fletching item tags registered!");
  }
}
