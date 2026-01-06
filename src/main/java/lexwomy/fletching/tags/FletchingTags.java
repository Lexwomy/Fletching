package lexwomy.fletching.tags;

import lexwomy.fletching.Fletching;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public class FletchingTags {
  public static <T> TagKey<@NotNull T> createTagKey(
      ResourceKey<@NotNull Registry<@NotNull T>> registry, String path) {
    return TagKey.create(registry, Fletching.identifier(path));
  }

  public static void initialize() {
    FletchingItemTags.initialize();
    FletchingEnchantmentTags.initialize();
    Fletching.LOGGER.info("Fletching tags all registered!");
  }
}
