package lexwomy.fletching.entity.damage;

import lexwomy.fletching.Fletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class FletchingDamageTypes {
  public static ResourceKey<DamageType> SHRAPNEL =
      ResourceKey.create(
          Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "shrapnel"));

  // Only used by data generation to fill the dynamic registry so it can be referenced by datagen
  public static void bootstrap(BootstrapContext<DamageType> bootstrapContext) {
    bootstrapContext.register(
        SHRAPNEL,
        new DamageType("shrapnel", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.05F));
  }

  public static void initialize() {
    Fletching.LOGGER.info("Fletching damage types initialized!");
  }
}
