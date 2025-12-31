package lexwomy.fletching.mixin;

import lexwomy.fletching.Fletching;
import lexwomy.fletching.component.FletchingComponents;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CombatRules.class)
public abstract class AddHardnessToCalcMixin {
    @ModifyVariable(method = "getDamageAfterAbsorb", at = @At(value = "STORE"), ordinal = 5)
    private static float addHardnessCalculation(float result, LivingEntity armorWearer, float damageAmount, DamageSource damageSource, float armor, float armorToughness) {
        //Result should just be i
        if (damageSource.getDirectEntity() instanceof Arrow) {
            ItemStack stack = ((Arrow) (damageSource.getDirectEntity())).getPickupItemStackOrigin();

            if (stack.has(FletchingComponents.HARDNESS)) {
                int hardness = stack.getOrDefault(FletchingComponents.HARDNESS, 0);
                float penetration = hardness * 0.1F;
                //1 - (i - 0.1h)
                Fletching.LOGGER.info("Previous result: {}, Penetration: {}, new result: {}", result, penetration, result - penetration);
                return Math.max(result - penetration, 0);
            }
        }
        return result;
    }

}
