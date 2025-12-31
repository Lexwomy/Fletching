package lexwomy.fletching.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lexwomy.fletching.item.FletchingItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class AddDefaultPilumMixin {

    //Add in check for greatbow default pilum
    @ModifyReturnValue(method = "getProjectile",
            at = @At("TAIL"))
    private ItemStack test(ItemStack original, ItemStack stack) {
        if (stack.is(FletchingItems.GREATBOW)) {
            return ((Player)(Object)this).getAbilities().instabuild ? new ItemStack(FletchingItems.FLINT_PILUM) : ItemStack.EMPTY;
        }
        return original;
    }
}
