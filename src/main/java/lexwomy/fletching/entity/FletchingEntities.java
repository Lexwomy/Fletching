package lexwomy.fletching.entity;

import lexwomy.fletching.Fletching;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class FletchingEntities {
    public static final ResourceKey<EntityType<?>> PILUM_KEY = Fletching.createResourceKey(Registries.ENTITY_TYPE,"pilum");
    public static final ResourceKey<EntityType<?>> SHRAPNEL_KEY = Fletching.createResourceKey(Registries.ENTITY_TYPE, "shrapnel");
    public static final EntityType<PilumEntity> PILUM = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            PILUM_KEY,
            EntityType.Builder.<PilumEntity>of(PilumEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.0F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(PILUM_KEY)
    );
    public static final EntityType<ShrapnelEntity> SHRAPNEL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            SHRAPNEL_KEY,
            EntityType.Builder.<ShrapnelEntity>of(ShrapnelEntity::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(0.1F, 0.1F)
                    .eyeHeight(0.05F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(SHRAPNEL_KEY)
    );

    public static void initialize() {
        Fletching.LOGGER.info("Fletching entities registered!");
    }
}
