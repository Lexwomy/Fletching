package lexwomy.fletching.component;

import com.mojang.serialization.Codec;
import lexwomy.fletching.FletchingTableInitializer;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FletchingComponents {
    protected static void initialize() {
        FletchingTableInitializer.LOGGER.info("Component class initialized!");
    }

    public static final ComponentType<Integer> HARDNESS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FletchingTableInitializer.MOD_ID, "hardness"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );
}
