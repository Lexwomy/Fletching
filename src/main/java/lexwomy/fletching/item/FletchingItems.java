package lexwomy.fletching.item;


import java.util.function.Function;
import lexwomy.fletching.Fletching;
import lexwomy.fletching.component.FletchingComponents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class FletchingItems {
    public static final Item LONGBOW = register(
            "longbow",
            LongbowItem::new,
            new Item.Properties().durability(384).enchantable(1)
    );
    public static final Item SHORTBOW = register(
            "shortbow",
            ShortbowItem::new,
            new Item.Properties().durability(512).enchantable(1)
    );
    public static final Item GREATBOW = register(
            "greatbow",
            GreatbowItem::new,
            new Item.Properties().durability(512)
    );
    public static final Item IRON_ARROW = register(
            "iron_arrow",
            ArrowItem::new,
            new Item.Properties().component(FletchingComponents.HARDNESS, 1)
    );
    public static final Item DIAMOND_ARROW = register(
            "diamond_arrow",
            ArrowItem::new,
            new Item.Properties().component(FletchingComponents.HARDNESS, 3)
    );
    public static final Item NETHERITE_ARROW = register(
            "netherite_arrow",
            ArrowItem::new,
            new Item.Properties().component(FletchingComponents.HARDNESS, 5)
    );
    public static final Item FLINT_PILUM = register(
            "flint_pilum",
            PilumItem::new,
            new Item.Properties().component(FletchingComponents.HARDNESS, 1)
                    .component(FletchingComponents.PIERCING, 1)
    );
    public static final Item IRON_PILUM = register(
            "iron_pilum",
            PilumItem::new,
            new Item.Properties().component(FletchingComponents.HARDNESS, 3)
                    .component(FletchingComponents.PIERCING, 2)
    );
    public static final Item DIAMOND_PILUM = register(
            "diamond_pilum",
            PilumItem::new,
            new Item.Properties().component(FletchingComponents.HARDNESS, 5)
                    .component(FletchingComponents.PIERCING, 3)
    );
    public static final Item NETHERITE_PILUM = register(
            "netherite_pilum",
            PilumItem::new,
            new Item.Properties().component(FletchingComponents.HARDNESS, 7)
                    .component(FletchingComponents.PIERCING, 4)
    );

    public static Item register(String id, Function<Item.Properties, Item> itemFactory, Item.Properties settings) {
        // Create the registry key/identifier for the item.
        ResourceKey<Item> itemKey = Fletching.createResourceKey(Registries.ITEM, id);

        Item item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        return Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register((itemGroup) -> {
            itemGroup.accept(FletchingItems.LONGBOW);
            itemGroup.accept(FletchingItems.IRON_ARROW);
            itemGroup.accept(FletchingItems.DIAMOND_ARROW);
            itemGroup.accept(FletchingItems.NETHERITE_ARROW);
        });
        Fletching.LOGGER.info("Fletching items registered!");
    }
}
