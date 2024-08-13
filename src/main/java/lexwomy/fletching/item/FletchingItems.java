package lexwomy.fletching.item;


import lexwomy.fletching.Fletching;
import lexwomy.fletching.component.FletchingComponents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FletchingItems {
    public static Item register(Item item, String id) {
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(Fletching.MOD_ID, id);

        // Register the item.
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register((itemGroup) -> {
            itemGroup.add(FletchingItems.LONGBOW);
            itemGroup.add(FletchingItems.IRON_ARROW);
            itemGroup.add(FletchingItems.DIAMOND_ARROW);
            itemGroup.add(FletchingItems.NETHERITE_ARROW);
        });
        Fletching.LOGGER.info("Fletching items registered!");
    }

    public static final Item LONGBOW = register(
            new LongbowItem(new Item.Settings().maxDamage(384)),
            "longbow"
    );

    public static final Item SHORTBOW = register(
            new ShortbowItem(new Item.Settings().maxDamage(512)),
            "shortbow"
    );

    public static final Item IRON_ARROW = register(
            new ArrowItem(new Item.Settings().component(FletchingComponents.HARDNESS, 1)),
            "iron_arrow"
    );

    public static final Item DIAMOND_ARROW = register(
            new ArrowItem(new Item.Settings().component(FletchingComponents.HARDNESS, 3)),
            "diamond_arrow"
    );

    public static final Item NETHERITE_ARROW = register(
            new ArrowItem(new Item.Settings().component(FletchingComponents.HARDNESS, 5)),
            "netherite_arrow"
    );

    public static final Item FLINT_PILUM = register(
            new PilumItem(new Item.Settings()),
            "flint_pilum"
    );
}
