package lexwomy.fletching.effect;

import lexwomy.fletching.Fletching;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class FrenzyEffect extends StatusEffect {
    public FrenzyEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0x9b870c);
    }

    //TODO - Make custom particle effect?
    //protected FrenzyEffect(ParticleEffect particleEffect) {}

//    @Override
//    public boolean canApplyUpdateEffect(int duration, int amplifier) {
//        return true;
//    }
//
//    //Called when effect is applied
//    @Override
//    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
//        //If entity has
//        return super.applyUpdateEffect(entity, amplifier);
//    }

    //Upon receiving the effect, apply speed based on the amplifier
    //The shortbow will check any active frenzy effects and adjust draw time
    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);


        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(Fletching.MOD_ID, "frenzy"),
                0.02F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
