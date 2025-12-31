package lexwomy.fletching.effect;

import lexwomy.fletching.Fletching;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FrenzyEffect extends MobEffect {
    public FrenzyEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x9b870c);
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
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);


//        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(Fletching.MOD_ID, "frenzy"),
//                0.02F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
