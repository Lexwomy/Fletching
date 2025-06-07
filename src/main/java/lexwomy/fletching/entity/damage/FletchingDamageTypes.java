package lexwomy.fletching.entity.damage;

import lexwomy.fletching.Fletching;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class FletchingDamageTypes {
    public static RegistryKey<DamageType> SHRAPNEL = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Fletching.MOD_ID, "shrapnel"));

    public static void initialize() {
        Fletching.LOGGER.info("Fletching damage types initialized!");
    }

}
