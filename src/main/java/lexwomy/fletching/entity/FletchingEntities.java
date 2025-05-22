package lexwomy.fletching.entity;

import lexwomy.fletching.Fletching;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FletchingEntities {
    public static final EntityType<PilumEntity> PILUM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(Fletching.MOD_ID, "pilum"),
            EntityType.Builder.<PilumEntity>create(PilumEntity::new, SpawnGroup.MISC)
                    .dimensions(1.0F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
                    .build()
    );

    public static void initialize() {
        Fletching.LOGGER.info("Fletching entities registered!");
    }
}
