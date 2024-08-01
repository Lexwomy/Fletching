package lexwomy.fletchingtable;

import lexwomy.fletchingtable.component.FletchingComponents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FletchingTableClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
			if (stack.getItem() == Items.ARROW) {
				int hardness = stack.getOrDefault(FletchingComponents.HARDNESS, 0);
				lines.add(Text.translatable("item.fletching-table.hardness.info1").formatted(Formatting.DARK_PURPLE));
				lines.add(Text.translatable("item.fletching-table.hardness.info2", hardness).formatted(Formatting.BLUE));
			}
		});
	}
}