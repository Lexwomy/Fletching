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
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class RegisterNewBowsToClientPlayerMixin extends PlayerEntity {
	//Do not use
	public RegisterNewBowsToClientPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}
	//These mixins add separate logic to change the draw times used to render the pulling animation times for the client
	@WrapOperation(method = "getFovMultiplier",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
	private boolean replaceCheckWithBowTag(ItemStack instance, Item item, Operation<Boolean> original) {
		return original.call(instance, item) || instance.isIn(FletchingItemTags.BOWS);
	}

	@ModifyExpressionValue(method = "getFovMultiplier",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTime()I"))
	private int passOnTickValue(int original, @Share("i") LocalIntRef tick_ref) {
		tick_ref.set(original);
		Fletching.LOGGER.info("Tick: {}", tick_ref.get());
		return original;
	}

	@ModifyExpressionValue(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"))
	private float passOnHValue(float original, @Share("h") LocalFloatRef h_ref) {
		h_ref.set(original);
		Fletching.LOGGER.info("Grabbed h: {}", h_ref.get());
		return original;
	}

	@ModifyArg(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"), index = 0)
	private float replaceDrawTime(float original,
								  @Share("i") LocalIntRef tick_ref, @Share("fov_factor") LocalFloatRef fov_ref) {
		ItemStack itemStack = ((AbstractClientPlayerEntity)(Object)this).getActiveItem();
		float draw_time = 20.0F;
		if (itemStack.isOf(FletchingItems.SHORTBOW)) {
			ShortbowItem bow = (ShortbowItem) itemStack.getItem();
			draw_time = bow.getFrenzyDrawTime((LivingEntity) (Object)this, itemStack);
			fov_ref.set(0.1F);
		} else if (itemStack.isOf(FletchingItems.LONGBOW)) {
			draw_time = LongbowItem.DRAW_TIME;
			fov_ref.set(0.25F);
		} else if (itemStack.isOf(FletchingItems.GREATBOW)) {
			GreatbowItem bow = (GreatbowItem) itemStack.getItem();
			draw_time = bow.getDrawTime(itemStack, (LivingEntity) (Object)this);
			fov_ref.set(0.5F);
		} else {
			//Regular bow
			fov_ref.set(0.15F);
			return original;
		}

		Fletching.LOGGER.info("Tick: {}, draw time: {}", tick_ref.get(), draw_time);

		//return (float)(((AbstractClientPlayerEntity) (Object)this).getItemUseTime()) / draw_time;
		return (float)tick_ref.get() / draw_time;
	}

//	@ModifyVariable(method = "getFovMultiplier", at = @At(value = "STORE", opcode = Opcodes.FSTORE), ordinal = 1)
//	private float replaceDrawTime(float original,
//								  @Share("i") LocalIntRef tick_ref, @Share("g") LocalFloatRef g_ref, @Share("fov_factor") LocalFloatRef fov_ref) {
//		ItemStack itemStack = ((AbstractClientPlayerEntity)(Object)this).getActiveItem();
//		float draw_time = 20.0F;
//		if (itemStack.isOf(FletchingItems.SHORTBOW)) {
//			ShortbowItem bow = (ShortbowItem) itemStack.getItem();
//			draw_time = bow.getFrenzyDrawTime((LivingEntity) (Object)this, itemStack);
//			fov_ref.set(0.1F);
//		} else if (itemStack.isOf(FletchingItems.LONGBOW)) {
//			draw_time = LongbowItem.DRAW_TIME;
//			fov_ref.set(0.25F);
//		} else if (itemStack.isOf(FletchingItems.GREATBOW)) {
//			GreatbowItem bow = (GreatbowItem) itemStack.getItem();
//			draw_time = bow.getDrawTime(itemStack, (LivingEntity) (Object)this);
//			fov_ref.set(0.5F);
//		} else {
//			fov_ref.set(0.15F);
//		}
//
//		float new_g = Math.min(1.0F, tick_ref.get() / draw_time);
//		g_ref.set(new_g * new_g);
//		//Fletching.LOGGER.info("Tick: {}", tick_ref.get());
//		return new_g;
//	}

	// TODO - may be brittle, consider looking at other modifiers within MixinExtras when there is time/internet because this does not return original
	@ModifyVariable(method = "getFovMultiplier",
			slice = @Slice(
					from = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;square(F)F"),
					to = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingSpyglass()Z")
			),
			at = @At(value = "STORE", opcode = Opcodes.FSTORE),
			ordinal = 1, index = 3)
	private float replaceFovFactor(float original, @Share("h") LocalFloatRef h_ref, @Share("fov_factor") LocalFloatRef fov_ref) {
		Fletching.LOGGER.info("Fov ref: {}, result: {}", fov_ref.get(), 1.0F - MathHelper.square(h_ref.get()) * fov_ref.get());
		return 1.0F - MathHelper.square(h_ref.get()) * fov_ref.get();
//		float reverse = original / (1.0F - h_ref.get() * 0.15F);
//		//Fletching.LOGGER.info("Original: {}, reverse: {}, new: {}", original, reverse, reverse * (1.0F - g_ref.get() * fov_ref.get()));
//		return reverse * (1.0F - g_ref.get() * fov_ref.get());
	}
}