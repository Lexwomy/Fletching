package lexwomy.fletching.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.authlib.GameProfile;
import lexwomy.fletching.FletchingInitializer;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.item.LongbowItem;
import lexwomy.fletching.item.ShortbowItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class RegisterNewBowsToClientPlayerMixin extends PlayerEntity {

	public RegisterNewBowsToClientPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@WrapOperation(method = "getFovMultiplier",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
	private boolean replaceCheckWithBowTag(ItemStack instance, Item item, Operation<Boolean> original) {
		return instance.isIn(FletchingInitializer.BOWS);
	}

	@Inject(method = "getFovMultiplier",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getItemUseTime()I",
					shift = At.Shift.BY, by = 2),
			cancellable = true)
	private void injectNewBowLogic(CallbackInfoReturnable<Float> cir, @Local ItemStack itemStack, @Local(ordinal = 0) float f, @Local int i) {
		float draw_time = 20.0F; //ShortbowItem.DRAW_TIME
		float fov_factor = 0.15F; //0.1F for shortbow

		if (itemStack.isOf(FletchingItems.LONGBOW)) {
			draw_time = LongbowItem.DRAW_TIME;
			fov_factor = 0.25F;
		}

		float g = (float)i / draw_time;
		if (g > 1.0F) {
			g = 1.0F;
		} else {
			g *= g;
		}

		f *= 1.0F - g * fov_factor;

		cir.setReturnValue(MathHelper.lerp(MinecraftClient.getInstance().options.getFovEffectScale().getValue().floatValue(), 1.0F, f));
	}
}