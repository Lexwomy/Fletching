package lexwomy.fletchingtable;

import lexwomy.fletchingtable.item.FletchingItems;
import lexwomy.fletchingtable.screen.FletchingScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FletchingTableInitializer implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("fletching-table");
	public static final String MOD_ID = "fletching-table";

	public static final ScreenHandlerType<FletchingScreenHandler> FLETCHING = new ScreenHandlerType<>(FletchingScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

	@Override
	public void onInitialize() {
		//Registers the fletching screen handler to the minecraft registries
		Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "fletching"), FLETCHING);

		FletchingItems.initialize();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}