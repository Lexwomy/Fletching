package lexwomy.fletching;

import lexwomy.fletching.component.FletchingComponents;
import lexwomy.fletching.effect.FletchingEffects;
import lexwomy.fletching.enchantment.FletchingEnchantmentEffectComponentTypes;
import lexwomy.fletching.enchantment.FletchingEnchantments;
import lexwomy.fletching.enchantment.effects.RemoveMobEffect;
import lexwomy.fletching.entity.FletchingEntities;
import lexwomy.fletching.entity.damage.FletchingDamageTypes;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.screen.FletchingScreenHandler;
import lexwomy.fletching.tags.FletchingTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fletching implements ModInitializer {
  // This logger is used to write text to the console and the log file.
  // It is considered best practice to use your mod id as the logger's name.
  // That way, it's clear which mod wrote info, warnings, and errors.
  public static final Logger LOGGER = LoggerFactory.getLogger("fletching");
  public static final String MOD_ID = "fletching";

  public static void devLogger(String loggerInput, Object... loggerObjects) {
    if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
      LOGGER.debug(loggerInput, loggerObjects);
    }
  }

  public static <T> ResourceKey<@NotNull T> createResourceKey(
      ResourceKey<@NotNull Registry<@NotNull T>> resourceKey, String path) {
    return ResourceKey.create(resourceKey, identifier(path));
  }

  public static Identifier identifier(String path) {
    return Identifier.fromNamespaceAndPath(Fletching.MOD_ID, path);
  }

  @Override
  public void onInitialize() {
    // Registers the fletching screen handler to the minecraft registries
    Registry.register(
        BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(MOD_ID, "fletching"), FLETCHING);
    Registry.register(
        BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE,
        Identifier.fromNamespaceAndPath(MOD_ID, "remove_mob_effect"),
        RemoveMobEffect.CODEC);

    FletchingComponents.initialize();
    FletchingItems.initialize();
    FletchingTags.initialize();
    FletchingEffects.initialize();
    FletchingEnchantmentEffectComponentTypes.initialize();
    FletchingEntities.initialize();
    FletchingDamageTypes.initialize();
    FletchingEnchantments.initialize();
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.

    LOGGER.info("Hello Fabric world!");
  }

  public static final MenuType<FletchingScreenHandler> FLETCHING =
      new MenuType<>(FletchingScreenHandler::new, FeatureFlags.VANILLA_SET);
}
