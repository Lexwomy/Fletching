package lexwomy.fletching.enchantment;

import lexwomy.fletching.Fletching;
import lexwomy.fletching.effect.FletchingEffects;
import lexwomy.fletching.enchantment.effects.RemoveMobEffect;
import lexwomy.fletching.entity.FletchingEntities;
import lexwomy.fletching.item.FletchingItems;
import lexwomy.fletching.tags.FletchingEnchantmentTags;
import lexwomy.fletching.tags.FletchingItemTags;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.ApplyMobEffect;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class FletchingEnchantments {
  public static final ResourceKey<Enchantment> SCATTERSHOT_KEY =
      Fletching.createResourceKey(Registries.ENCHANTMENT, "scattershot");
  public static final ResourceKey<Enchantment> FRENZY_KEY =
      Fletching.createResourceKey(Registries.ENCHANTMENT, "frenzy");
  public static final ResourceKey<Enchantment> FOCUS_KEY =
      Fletching.createResourceKey(Registries.ENCHANTMENT, "focus");

  public static void bootstrap(BootstrapContext<Enchantment> bootstrapContext) {
    HolderGetter<Item> itemGetter = bootstrapContext.lookup(Registries.ITEM);
    HolderGetter<Enchantment> enchantmentGetter = bootstrapContext.lookup(Registries.ENCHANTMENT);
    HolderGetter<EntityType<?>> entityTypeGetter = bootstrapContext.lookup(Registries.ENTITY_TYPE);

    register(
        bootstrapContext,
        SCATTERSHOT_KEY,
        Enchantment.enchantment(
                Enchantment.definition(
                    itemGetter.getOrThrow(FletchingItemTags.SHORTBOW_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.constantCost(20),
                    Enchantment.constantCost(50),
                    4,
                    EquipmentSlotGroup.MAINHAND))
            .exclusiveWith(
                enchantmentGetter.getOrThrow(FletchingEnchantmentTags.SCATTERSHOT_EXCLUSIVE))
            .withSpecialEffect(
                FletchingEnchantmentEffectComponentTypes.DRAW_TIME,
                new AddValue(LevelBasedValue.constant(10.0F)))
            .withSpecialEffect(
                FletchingEnchantmentEffectComponentTypes.SHRAPNEL_COUNT,
                new AddValue(LevelBasedValue.constant(5.0F)))
            .withSpecialEffect(
                FletchingEnchantmentEffectComponentTypes.INACCURACY,
                new AddValue(LevelBasedValue.constant(5.0F))));

    register(
        bootstrapContext,
        FRENZY_KEY,
        Enchantment.enchantment(
                Enchantment.definition(
                    itemGetter.getOrThrow(FletchingItemTags.SHORTBOW_ENCHANTABLE),
                    3,
                    4,
                    Enchantment.dynamicCost(5, 8),
                    Enchantment.dynamicCost(25, 8),
                    2,
                    EquipmentSlotGroup.MAINHAND))
            .exclusiveWith(enchantmentGetter.getOrThrow(FletchingEnchantmentTags.FRENZY_EXCLUSIVE))
            .withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.ATTACKER,
                new ApplyMobEffect(
                    HolderSet.direct(FletchingEffects.FRENZY),
                    LevelBasedValue.perLevel(6, -1),
                    LevelBasedValue.perLevel(6, -1),
                    LevelBasedValue.perLevel(0, 1),
                    LevelBasedValue.perLevel(0, 1)),
                AnyOfCondition.anyOf(
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.DIRECT_ATTACKER,
                        EntityPredicate.Builder.entity()
                            .entityType(
                                EntityTypePredicate.of(entityTypeGetter, EntityTypeTags.ARROWS))),
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.DIRECT_ATTACKER,
                        EntityPredicate.Builder.entity()
                            .entityType(
                                EntityTypePredicate.of(
                                    entityTypeGetter, FletchingEntities.SHRAPNEL))))));

    register(
        bootstrapContext,
        FOCUS_KEY,
        Enchantment.enchantment(
                Enchantment.definition(
                    itemGetter.getOrThrow(FletchingItemTags.LONGBOW_ENCHANTABLE),
                    2,
                    1,
                    Enchantment.constantCost(20),
                    Enchantment.constantCost(30),
                    2,
                    EquipmentSlotGroup.MAINHAND))
            .exclusiveWith(enchantmentGetter.getOrThrow(FletchingEnchantmentTags.FOCUS_EXCLUSIVE))
            .withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.ATTACKER,
                new ApplyMobEffect(
                    HolderSet.direct(FletchingEffects.FOCUS),
                    LevelBasedValue.constant(30),
                    LevelBasedValue.constant(30),
                    LevelBasedValue.constant(0),
                    LevelBasedValue.constant(0)),
                LootItemEntityPropertyCondition.hasProperties(
                    LootContext.EntityTarget.DIRECT_ATTACKER,
                    EntityPredicate.Builder.entity()
                        .entityType(
                            EntityTypePredicate.of(entityTypeGetter, EntityTypeTags.ARROWS))))
            .withEffect(
                EnchantmentEffectComponents.HIT_BLOCK,
                new RemoveMobEffect(HolderSet.direct(FletchingEffects.FOCUS))));
  }

  private static void register(
      BootstrapContext<Enchantment> bootstrapContext,
      ResourceKey<Enchantment> resourceKey,
      Enchantment.Builder builder) {
    bootstrapContext.register(resourceKey, builder.build(resourceKey.identifier()));
  }

  public static void initialize() {
    EnchantmentEvents.ALLOW_ENCHANTING.register(
        (enchantment, target, enchantingContext) -> {
          if (target.is(FletchingItems.LONGBOW) && enchantment.is(Enchantments.PIERCING)) {
            return TriState.TRUE;
          }
          return TriState.DEFAULT;
        });
    Fletching.LOGGER.info("Fletching enchantment keys initialized!");
  }
}
