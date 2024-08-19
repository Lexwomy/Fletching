package lexwomy.fletching.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lexwomy.fletching.item.FletchingItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class AddDefaultPilumMixin {

    //Add in check for greatbow default pilum
    @ModifyReturnValue(method = "getProjectileType",
            at = @At("TAIL"))
    private ItemStack test(ItemStack original, ItemStack stack) {
        if (stack.isOf(FletchingItems.GREATBOW)) {
            return ((PlayerEntity)(Object)this).getAbilities().creativeMode ? new ItemStack(FletchingItems.FLINT_PILUM) : ItemStack.EMPTY;
        }
        return original;
    }
}
