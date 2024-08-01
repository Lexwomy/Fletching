package lexwomy.fletchingtable.item;


import lexwomy.fletchingtable.FletchingTableInitializer;
import lexwomy.fletchingtable.component.FletchingComponents;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FletchingItems {
    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(FletchingTableInitializer.MOD_ID, id);

        // Register the item.
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static void initialize() {

    }

    public static final Item LONGBOW = register(
            new LongbowItem(new Item.Settings().maxDamage(384)),
            "longbow"
    );

    public static final Item IRON_ARROW = register(
            new IronArrowItem(new Item.Settings().component(FletchingComponents.HARDNESS, 4)),
            "iron_arrow"
    );
}
