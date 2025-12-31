package lexwomy.fletching.effect;

import lexwomy.fletching.Fletching;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class FletchingEffects {
//    public static final StatusEffect FRENZY_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(Fletching.MOD_ID, "frenzy"),
//            new FrenzyEffect().addAttributeModifier(EntityAttributes.MOVEMENT_SPEED, Identifier.of(Fletching.MOD_ID, "frenzy"),
//                    0.02F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
//    public static final StatusEffect FOCUS_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(Fletching.MOD_ID, "focus"),
//            new FocusEffect());
    public static final Holder<MobEffect> FRENZY = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "frenzy"), new FrenzyEffect().addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "frenzy"),
            0.02F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> FOCUS = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "focus"), new FocusEffect());
    public static void initialize() {

    }
}
