package lexwomy.fletching;

import lexwomy.fletching.component.FletchingComponents;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.item.LongbowItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class FletchingTableClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
			if (stack.isIn(ItemTags.ARROWS)) {
				int hardness = stack.getOrDefault(FletchingComponents.HARDNESS, 0);
				lines.add(Text.translatable("item.fletching.hardness.info1").formatted(Formatting.DARK_PURPLE));
				lines.add(Text.translatable("item.fletching.hardness.info2", hardness).formatted(Formatting.BLUE));
			}
		});

		registerModelPredicateProviders();
	}

	public static void registerModelPredicateProviders() {
		ModelPredicateProviderRegistry.register(FletchingItems.LONGBOW, Identifier.ofVanilla("pull"), (itemStack, clientWorld, livingEntity, seed) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return livingEntity.getActiveItem() != itemStack ? 0.0F :
					(itemStack.getMaxUseTime(livingEntity) - livingEntity.getItemUseTimeLeft()) / LongbowItem.DRAW_TIME;
		});

		ModelPredicateProviderRegistry.register(FletchingItems.LONGBOW, Identifier.ofVanilla("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
		});
	}
}