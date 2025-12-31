//package lexwomy.fletching.mixin;
//
//import lexwomy.fletching.screen.FletchingScreenHandler;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.chat.Component;
//import net.minecraft.stats.Stats;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.MenuProvider;
//import net.minecraft.world.SimpleMenuProvider;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.ContainerLevelAccess;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.CraftingTableBlock;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.BlockHitResult;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(FletchingTableBlock.class)
//public abstract class FletchingTableBlockMixin extends CraftingTableBlock {
//	@Unique
//	private static final Component TITLE = Component.translatable("container.fletching_table");
//
//	public FletchingTableBlockMixin(BlockBehaviour.Properties settings) {
//        super(settings);
//    }
//
//	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
//	protected void onUseOverwrite(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
//		if (world.isClientSide) {
//			cir.setReturnValue(InteractionResult.SUCCESS);
//		} else {
//			player.openMenu(state.getMenuProvider(world, pos));
//			player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE); //TO-DO Add interact with fletching table stat
//			cir.setReturnValue(InteractionResult.CONSUME);
//		}
//	}
//
//	protected MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
//		return new SimpleMenuProvider(
//				(syncId, inventory, player) -> new FletchingScreenHandler(syncId, inventory, ContainerLevelAccess.create(world, pos)), TITLE);
//	}
//}