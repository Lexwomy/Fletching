package lexwomy.fletching.entity.damage;

import lexwomy.fletching.Fletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class FletchingDamageTypes {
    public static ResourceKey<DamageType> SHRAPNEL = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "shrapnel"));

    public static void initialize() {
        Fletching.LOGGER.info("Fletching damage types initialized!");
    }

}
