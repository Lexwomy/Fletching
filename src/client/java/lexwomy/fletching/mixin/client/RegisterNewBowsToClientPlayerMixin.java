package lexwomy.fletching.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mojang.authlib.GameProfile;
import lexwomy.fletching.Fletching;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.item.GreatbowItem;
import lexwomy.fletching.item.LongbowItem;
import lexwomy.fletching.item.ShortbowItem;
import lexwomy.fletching.tags.FletchingItemTags;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractClientPlayer.class)
public abstract class RegisterNewBowsToClientPlayerMixin extends Player {

    public RegisterNewBowsToClientPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    //These mixins add separate logic to change the draw times used to render the pulling animation times for the client
	@WrapOperation(method = "getFieldOfViewModifier",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
	private boolean replaceCheckWithBowTag(ItemStack instance, Item item, Operation<Boolean> original) {
		return original.call(instance, item) || instance.is(FletchingItemTags.BOWS);
	}

	@ModifyExpressionValue(method = "getFieldOfViewModifier",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;getTicksUsingItem()I"))
	private int passOnTickValue(int original, @Share("i") LocalIntRef tick_ref) {
		tick_ref.set(original);
		//Fletching.LOGGER.info("Tick: {}", tick_ref.get());
		return original;
	}

	@ModifyExpressionValue(method = "getFieldOfViewModifier", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"))
	private float passOnHValue(float original, @Share("h") LocalFloatRef h_ref) {
		h_ref.set(original);
		//Fletching.LOGGER.info("Grabbed h: {}", h_ref.get());
		return original;
	}

	@ModifyArg(method = "getFieldOfViewModifier", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"), index = 0)
	private float replaceDrawTime(float original,
								  @Share("i") LocalIntRef tick_ref, @Share("fov_factor") LocalFloatRef fov_ref) {
		ItemStack itemStack = ((AbstractClientPlayer)(Object)this).getActiveItem();
		float draw_time = 20.0F;
		if (itemStack.is(FletchingItems.SHORTBOW)) {
			ShortbowItem bow = (ShortbowItem) itemStack.getItem();
			draw_time = bow.getFrenzyDrawTime((LivingEntity) (Object)this, itemStack);
			fov_ref.set(0.1F);
		} else if (itemStack.is(FletchingItems.LONGBOW)) {
			draw_time = LongbowItem.DRAW_TIME;
			fov_ref.set(0.25F);
		} else if (itemStack.is(FletchingItems.GREATBOW)) {
			GreatbowItem bow = (GreatbowItem) itemStack.getItem();
			draw_time = bow.getDrawTime(itemStack, (LivingEntity) (Object)this);
			fov_ref.set(0.5F);
		} else {
			//Regular bow
			fov_ref.set(0.15F);
			return original;
		}

		//Fletching.LOGGER.info("Tick: {}, draw time: {}", tick_ref.get(), draw_time);

		//return (float)(((AbstractClientPlayerEntity) (Object)this).getItemUseTime()) / draw_time;
		return (float)tick_ref.get() / draw_time;
	}

	// TODO - may be brittle, consider looking at other modifiers within MixinExtras when there is time/internet because this does not return original
	@ModifyVariable(method = "getFieldOfViewModifier",
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;square(F)F"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isScoping()Z")
			),
			at = @At("STORE"),
			ordinal = 1, index = 3)
	private float replaceFovFactor(float original, @Share("h") LocalFloatRef h_ref, @Share("fov_factor") LocalFloatRef fov_ref) {
		//Fletching.LOGGER.info("Fov ref: {}, result: {}", fov_ref.get(), 1.0F - MathHelper.square(h_ref.get()) * fov_ref.get());
		return 1.0F - Mth.square(h_ref.get()) * fov_ref.get();
	}
}