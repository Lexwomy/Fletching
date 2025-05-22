package lexwomy.fletching.component;

import com.mojang.serialization.Codec;
import lexwomy.fletching.Fletching;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FletchingComponents {
    public static void initialize() {
        Fletching.LOGGER.info("Component class initialized!");
    }

    public static final ComponentType<Integer> HARDNESS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Fletching.MOD_ID, "hardness"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> PIERCING = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Fletching.MOD_ID, "piercing"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );
}
