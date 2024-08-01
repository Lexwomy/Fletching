package lexwomy.fletching.mixin;

import lexwomy.fletching.FletchingInitializer;
import lexwomy.fletching.component.FletchingComponents;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DamageUtil.class)
public abstract class AddHardnessToCalcMixin {
    @ModifyVariable(method = "getDamageLeft", at = @At(value = "STORE"), ordinal = 5)
    private static float addHardnessCalculation(float result, LivingEntity armorWearer, float damageAmount, DamageSource damageSource, float armor, float armorToughness) {
        //Result should just be i
        if (damageSource.getSource() instanceof ArrowEntity) {
            ItemStack stack = ((ArrowEntity) (damageSource.getSource())).getItemStack();

            if (stack.contains(FletchingComponents.HARDNESS)) {
                int hardness = stack.getOrDefault(FletchingComponents.HARDNESS, 0);
                float penetration = hardness * 0.1F;
                //1 - (i - 0.1h)
                FletchingInitializer.LOGGER.info("Previous result: {}, Penetration: {}, new result: {}", result, penetration, result - penetration);
                return Math.max(result - penetration, 0);
            }
        }
        return result;
    }

}
