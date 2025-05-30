package lexwomy.fletching.item;


import lexwomy.fletching.Fletching;
import lexwomy.fletching.component.FletchingComponents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class FletchingItems {
    public static Item register(String id, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the registry key/identifier for the item.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Fletching.MOD_ID, id));

        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        return Registry.register(Registries.ITEM, itemKey, item);
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
            "longbow",
            LongbowItem::new,
            new Item.Settings().maxDamage(384)
    );

    public static final Item SHORTBOW = register(
            "shortbow",
            ShortbowItem::new,
            new Item.Settings().maxDamage(512)
    );

    public static final Item GREATBOW = register(
            "greatbow",
            GreatbowItem::new,
            new Item.Settings().maxDamage(512)
    );

    public static final Item IRON_ARROW = register(
            "iron_arrow",
            ArrowItem::new,
            new Item.Settings().component(FletchingComponents.HARDNESS, 1)
    );

    public static final Item DIAMOND_ARROW = register(
            "diamond_arrow",
            ArrowItem::new,
            new Item.Settings().component(FletchingComponents.HARDNESS, 3)
    );

    public static final Item NETHERITE_ARROW = register(
            "netherite_arrow",
            ArrowItem::new,
            new Item.Settings().component(FletchingComponents.HARDNESS, 5)
    );

    public static final Item FLINT_PILUM = register(
            "flint_pilum",
            PilumItem::new,
            new Item.Settings().component(FletchingComponents.HARDNESS, 1)
                    .component(FletchingComponents.PIERCING, 1)
    );

    public static final Item IRON_PILUM = register(
            "iron_pilum",
            PilumItem::new,
            new Item.Settings().component(FletchingComponents.HARDNESS, 3)
                    .component(FletchingComponents.PIERCING, 2)
    );

    public static final Item DIAMOND_PILUM = register(
            "diamond_pilum",
            PilumItem::new,
            new Item.Settings().component(FletchingComponents.HARDNESS, 5)
                    .component(FletchingComponents.PIERCING, 3)
    );

    public static final Item NETHERITE_PILUM = register(
            "netherite_pilum",
            PilumItem::new,
            new Item.Settings().component(FletchingComponents.HARDNESS, 7)
                    .component(FletchingComponents.PIERCING, 4)
    );
}
