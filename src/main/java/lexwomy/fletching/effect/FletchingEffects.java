package lexwomy.fletching.effect;

import lexwomy.fletching.Fletching;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class FletchingEffects {
    public static final StatusEffect FRENZY_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(Fletching.MOD_ID, "frenzy"),
            new FrenzyEffect().addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(Fletching.MOD_ID, "frenzy"),
                    0.02F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final StatusEffect FOCUS_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(Fletching.MOD_ID, "focus"),
            new FocusEffect());
    public static final RegistryEntry<StatusEffect> FRENZY = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Fletching.MOD_ID, "frenzy"), FRENZY_EFFECT);
    public static final RegistryEntry<StatusEffect> FOCUS = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Fletching.MOD_ID, "focus"), FOCUS_EFFECT);
    public static void initialize() {

    }
}
