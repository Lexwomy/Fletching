package lexwomy.fletching.component;

import com.mojang.serialization.Codec;
import lexwomy.fletching.Fletching;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class FletchingComponents {
    public static void initialize() {
        Fletching.LOGGER.info("Component class initialized!");
    }

    public static final DataComponentType<Integer> HARDNESS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "hardness"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    public static final DataComponentType<Integer> PIERCING = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "piercing"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
}
