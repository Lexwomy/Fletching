package lexwomy.fletching;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lexwomy.fletching.enchantment.FletchingEnchantments;
import lexwomy.fletching.entity.FletchingEntities;
import lexwomy.fletching.entity.damage.FletchingDamageTypes;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.tags.FletchingEnchantmentTags;
import lexwomy.fletching.tags.FletchingItemTags;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class FletchingDataGenerator implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
    pack.addProvider(FletchingDynamicRegistryProvider::new);
    pack.addProvider(FletchingLanguageProvider::new);
    FletchingTagProvider.initialize(pack);
  }

  @Override
  public void buildRegistry(RegistrySetBuilder registryBuilder) {
    DataGeneratorEntrypoint.super.buildRegistry(registryBuilder);
    registryBuilder.add(Registries.ENCHANTMENT, FletchingEnchantments::bootstrap);
    registryBuilder.add(Registries.DAMAGE_TYPE, FletchingDamageTypes::bootstrap);
  }

  private static class FletchingDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    private FletchingDynamicRegistryProvider(
        FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
      super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
      entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
      entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
    }

    @Override
    public String getName() {
      return "Fletching Dynamic Registry";
    }
  }

  private static class FletchingLanguageProvider extends FabricLanguageProvider {
    private static final List<Pair<String, String>> TRANSLATIONS =
        List.of(
            new Pair<>("item.fletching.shortbow", "Shortbow"),
            new Pair<>("item.fletching.longbow", "Longbow"),
            new Pair<>("item.fletching.greatbow", "Greatbow"),
            new Pair<>("effect.fletching.frenzy", "Frenzy"),
            new Pair<>("effect.fletching.focus", "Focus"),
            new Pair<>("effect.fletching.eaglesight", "Eaglesight"),
            new Pair<>("enchantment.fletching.frenzy", "Frenzy"),
            new Pair<>("enchantment.fletching.scattershot", "Scattershot"),
            new Pair<>("enchantment.fletching.focus", "Focus"),
            new Pair<>("tag.item.fletching.bows", "Bows"),
            new Pair<>("tag.item.fletching.pilums", "Pilums"),
            new Pair<>("tag.item.fletching.enchantable.shortbow", "Shortbow Enchantable"),
            new Pair<>("tag.item.fletching.enchantable.longbow", "Longbow Enchantable"));

    private FletchingLanguageProvider(
        FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
      super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(
        HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
      for (Pair<String, String> translation : TRANSLATIONS) {
        translationBuilder.add(translation.getFirst(), translation.getSecond());
      }
    }
  }

  private static class FletchingTagProvider {
    public static void initialize(FabricDataGenerator.Pack pack) {
      pack.addProvider(FletchingEntityTypeTagProvider::new);
      pack.addProvider(FletchingItemTagProvider::new);
      pack.addProvider(FletchingEnchantmentTagProvider::new);
      pack.addProvider(FletchingDamageTypeTagProvider::new);
    }

    private static class FletchingEnchantmentTagProvider extends FabricTagProvider<Enchantment> {
      private static final List<ResourceKey<Enchantment>> NON_TREASURE_ENCHANTMENTS =
          List.of(FletchingEnchantments.SCATTERSHOT_KEY, FletchingEnchantments.FRENZY_KEY);

      private FletchingEnchantmentTagProvider(
          FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENCHANTMENT, registriesFuture);
      }

      @Override
      protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(FletchingEnchantmentTags.MODIFIES_SHRAPNEL_COUNT)
            .add(FletchingEnchantments.SCATTERSHOT_KEY);
        builder(FletchingEnchantmentTags.MODIFIES_DRAW_TIME)
            .add(FletchingEnchantments.SCATTERSHOT_KEY);
        builder(FletchingEnchantmentTags.MODIFIES_ACCURACY).add(FletchingEnchantments.FRENZY_KEY);

        builder(FletchingEnchantmentTags.SCATTERSHOT_EXCLUSIVE).add(Enchantments.INFINITY);
        builder(FletchingEnchantmentTags.FRENZY_EXCLUSIVE).add(Enchantments.INFINITY);
        builder(FletchingEnchantmentTags.FOCUS_EXCLUSIVE).add(Enchantments.PIERCING);

        builder(EnchantmentTags.NON_TREASURE).addAll(NON_TREASURE_ENCHANTMENTS).setReplace(false);
      }
    }

    private static class FletchingEntityTypeTagProvider
        extends FabricTagProvider.EntityTypeTagProvider {

      private FletchingEntityTypeTagProvider(
          FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
      }

      @Override
      protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(EntityTypeTags.ARROWS).add(FletchingEntities.SHRAPNEL);
      }
    }

    private static class FletchingItemTagProvider extends FabricTagProvider.ItemTagProvider {

      private FletchingItemTagProvider(
          FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
      }

      @Override
      protected void addTags(HolderLookup.Provider wrapperLookup) {
        valueLookupBuilder(FletchingItemTags.BOWS)
            .add(FletchingItems.SHORTBOW)
            .add(FletchingItems.LONGBOW)
            .add(FletchingItems.GREATBOW);
        valueLookupBuilder(FletchingItemTags.SHORTBOW_ENCHANTABLE).add(FletchingItems.SHORTBOW);
        valueLookupBuilder(FletchingItemTags.LONGBOW_ENCHANTABLE).add(FletchingItems.LONGBOW);

        valueLookupBuilder(ItemTags.ARROWS)
            .add(FletchingItems.IRON_ARROW)
            .add(FletchingItems.DIAMOND_ARROW)
            .add(FletchingItems.NETHERITE_ARROW)
            .setReplace(false);
        valueLookupBuilder(ItemTags.BOW_ENCHANTABLE)
            .add(FletchingItems.SHORTBOW)
            .add(FletchingItems.LONGBOW)
            .setReplace(false);
        valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE)
            .add(FletchingItems.SHORTBOW)
            .add(FletchingItems.LONGBOW)
            .setReplace(false);
      }
    }

    private static class FletchingDamageTypeTagProvider extends FabricTagProvider<DamageType> {
      private FletchingDamageTypeTagProvider(
          FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
      }

      @Override
      protected void addTags(HolderLookup.Provider wrapperLookup) {
        builder(DamageTypeTags.BYPASSES_COOLDOWN)
            .add(FletchingDamageTypes.SHRAPNEL)
            .setReplace(false);
        builder(DamageTypeTags.IS_PROJECTILE).add(FletchingDamageTypes.SHRAPNEL).setReplace(false);
      }
    }
  }
}
