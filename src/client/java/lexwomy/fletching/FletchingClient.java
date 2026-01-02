package lexwomy.fletching;

import lexwomy.fletching.component.FletchingComponents;
import lexwomy.fletching.entity.FletchingEntities;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.item.GreatbowItem;
import lexwomy.fletching.item.LongbowItem;
import lexwomy.fletching.item.ShortbowItem;
import lexwomy.fletching.models.ShrapnelModel;
import lexwomy.fletching.render.item.property.numeric.UsePercentProperty;
import lexwomy.fletching.renderer.PilumEntityRenderer;
import lexwomy.fletching.renderer.ShrapnelEntityRenderer;
import lexwomy.fletching.tags.FletchingItemTags;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;

import java.util.HashMap;
import java.util.Map;

public class FletchingClient implements ClientModInitializer {
    public static final Map<ModelLayerLocation, LayerDefinition> MODELS = new HashMap<>();
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (stack.is(ItemTags.ARROWS) || stack.is(FletchingItemTags.PILUMS)) {
                int hardness = stack.getOrDefault(FletchingComponents.HARDNESS, 0);
                int piercing = stack.getOrDefault(FletchingComponents.PIERCING, 0);
                lines.add(Component.translatable("item.fletching.hardness.info1").withStyle(ChatFormatting.DARK_PURPLE));
                lines.add(Component.translatable("item.fletching.hardness.info2", hardness).withStyle(ChatFormatting.DARK_BLUE));
                if (piercing > 0) {
                    lines.add(Component.translatable("item.fletching.piercing.info", piercing).withStyle(ChatFormatting.BLUE));
                }
            }
        });

        ClientLifecycleEvents.CLIENT_STARTED.register((minecraftClient) -> {
            RangeSelectItemModelProperties.ID_MAPPER.put(Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "use_percent"), UsePercentProperty.CODEC);
            Fletching.LOGGER.info("Registered UsePercent property!");
        });

        EntityRenderers.register(FletchingEntities.PILUM, PilumEntityRenderer::new);
        EntityRenderers.register(FletchingEntities.SHRAPNEL, ShrapnelEntityRenderer::new);
        MODELS.put(ShrapnelModel.LAYER_LOCATION, ShrapnelModel.createBodyLayer());
        //registerModelPredicateProviders();
    }

//	public static void registerModelPredicateProviders() {
//		ModelPredicateProviderRegistry.register(FletchingItems.LONGBOW, Identifier.ofVanilla("pull"), (itemStack, clientWorld, livingEntity, seed) -> {
//			if (livingEntity == null) {
//				return 0.0F;
//			}
//			return livingEntity.getActiveItem() != itemStack ? 0.0F :
//					(itemStack.getMaxUseTime(livingEntity) - livingEntity.getItemUseTimeLeft()) / LongbowItem.DRAW_TIME;
//		});
//
//		ModelPredicateProviderRegistry.register(FletchingItems.LONGBOW, Identifier.ofVanilla("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
//			if (livingEntity == null) {
//				return 0.0F;
//			}
//			return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
//		});
//
//		ModelPredicateProviderRegistry.register(FletchingItems.GREATBOW, Identifier.ofVanilla("pull"), (itemStack, clientWorld, livingEntity, seed) -> {
//			if (livingEntity == null) {
//				return 0.0F;
//			}
//			GreatbowItem greatbow = (GreatbowItem) itemStack.getItem();
//			return livingEntity.getActiveItem() != itemStack ? 0.0F :
//					(itemStack.getMaxUseTime(livingEntity) - livingEntity.getItemUseTimeLeft()) / greatbow.getDrawTime(itemStack, livingEntity);
//		});
//
//		ModelPredicateProviderRegistry.register(FletchingItems.GREATBOW, Identifier.ofVanilla("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
//			if (livingEntity == null) {
//				return 0.0F;
//			}
//			return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
//		});
//
//		ModelPredicateProviderRegistry.register(FletchingItems.SHORTBOW, Identifier.ofVanilla("pull"), (itemStack, clientWorld, livingEntity, seed) -> {
//			if (livingEntity == null) {
//				return 0.0F;
//			}
//
//			ShortbowItem bow = (ShortbowItem) itemStack.getItem();
//			return livingEntity.getActiveItem() != itemStack ? 0.0F :
//					(itemStack.getMaxUseTime(livingEntity) - livingEntity.getItemUseTimeLeft()) / bow.getFrenzyDrawTime(livingEntity, itemStack);
//		});
//
//		ModelPredicateProviderRegistry.register(FletchingItems.SHORTBOW, Identifier.ofVanilla("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
//			if (livingEntity == null) {
//				return 0.0F;
//			}
//			return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
//		});
//	}
}