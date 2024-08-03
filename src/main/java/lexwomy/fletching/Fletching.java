package lexwomy.fletching;

import lexwomy.fletching.component.FletchingComponents;
import lexwomy.fletching.effect.FrenzyEffect;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.screen.FletchingScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.effect.StatusEffect;
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
	public static final StatusEffect FRENZY_EFFECT = Registry.register(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "frenzy"), new FrenzyEffect());
	public static final RegistryEntry<StatusEffect> FRENZY = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MOD_ID, "frenzy"), new FrenzyEffect());

	@Override
	public void onInitialize() {
		//Registers the fletching screen handler to the minecraft registries
		Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "fletching"), FLETCHING);

		FletchingComponents.initialize();
		FletchingItems.initialize();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}