package lexwomy.fletching.entity;

import lexwomy.fletching.Fletching;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class FletchingEntities {
    public static final RegistryKey<EntityType<?>> PILUM_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Fletching.MOD_ID, "pilum"));
    public static final RegistryKey<EntityType<?>> SHRAPNEL_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(Fletching.MOD_ID, "shrapnel"));
    public static final EntityType<PilumEntity> PILUM = Registry.register(
            Registries.ENTITY_TYPE,
            PILUM_KEY,
            EntityType.Builder.<PilumEntity>create(PilumEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(1.0F, 0.5F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
                    .build(PILUM_KEY)
    );
    public static final EntityType<ShrapnelEntity> SHRAPNEL = Registry.register(
            Registries.ENTITY_TYPE,
            SHRAPNEL_KEY,
            EntityType.Builder.<ShrapnelEntity>create(ShrapnelEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.15F, 0.15F)
                    .eyeHeight(0.13F)
                    .maxTrackingRange(4)
                    .trackingTickInterval(20)
                    .build(SHRAPNEL_KEY)
    );

    public static void initialize() {
        Fletching.LOGGER.info("Fletching entities registered!");
    }
}
