package lexwomy.fletching.tags;

import lexwomy.fletching.Fletching;

public class FletchingTags {
    public static void initialize() {
        FletchingItemTags.initialize();
        FletchingEnchantmentTags.initialize();
        Fletching.LOGGER.info("Fletching tags all registered!");
    }
}
