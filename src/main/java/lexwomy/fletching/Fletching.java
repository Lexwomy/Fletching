package lexwomy.fletching;

import lexwomy.fletching.component.FletchingComponents;
import lexwomy.fletching.effect.FocusEffect;
import lexwomy.fletching.effect.FrenzyEffect;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.screen.FletchingScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.Item;

public class Fletching implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("fletching");
	public static final String MOD_ID = "fletching";

	public static final ScreenHandlerType<FletchingScreenHandler> FLETCHING = new ScreenHandlerType<>(FletchingScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static final TagKey<Item> BOWS = TagKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "bows"));
	public static final StatusEffect FRENZY_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "frenzy"),
			new FrenzyEffect().addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, Identifier.of(Fletching.MOD_ID, "frenzy"),
					0.02F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
	public static final StatusEffect FOCUS_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "focus"),
			new FocusEffect());
	public static final RegistryEntry<StatusEffect> FRENZY = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "frenzy"), FRENZY_EFFECT);
	public static final RegistryEntry<StatusEffect> FOCUS = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "focus"), FOCUS_EFFECT);
	@Override
	public void onInitialize() {
		//Registers the fletching screen handler to the minecraft registries
		Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "fletching"), FLETCHING);
		//Registry.register(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "frenzy"), )

		FletchingComponents.initialize();
		FletchingItems.initialize();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}