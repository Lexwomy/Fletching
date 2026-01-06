package lexwomy.fletching.tags;

import lexwomy.fletching.Fletching;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class FletchingEnchantmentTags {
  public static final TagKey<Enchantment> SCATTERSHOT_EXCLUSIVE =
      FletchingTags.createTagKey(Registries.ENCHANTMENT, "exclusive/scattershot");
  public static final TagKey<Enchantment> FRENZY_EXCLUSIVE =
      FletchingTags.createTagKey(Registries.ENCHANTMENT, "exclusive/frenzy");
  public static final TagKey<Enchantment> FOCUS_EXCLUSIVE =
      FletchingTags.createTagKey(Registries.ENCHANTMENT, "exclusive/focus");

  public static final TagKey<Enchantment> MODIFIES_DRAW_TIME =
      FletchingTags.createTagKey(Registries.ENCHANTMENT, "modifies_draw_time");
  public static final TagKey<Enchantment> MODIFIES_ACCURACY =
      FletchingTags.createTagKey(Registries.ENCHANTMENT, "modifies_accuracy");
  public static final TagKey<Enchantment> MODIFIES_SHRAPNEL_COUNT =
      FletchingTags.createTagKey(Registries.ENCHANTMENT, "modifies_shrapnel_count");

  public static final TagKey<Enchantment> GREATBOW_ULTIMATE =
      FletchingTags.createTagKey(Registries.ENCHANTMENT, "greatbow_ultimate");
  public static final TagKey<Enchantment> PHOTONIC_CHARGE =
      FletchingTags.createTagKey(Registries.ENCHANTMENT, "photonic_charge");

  public static void initialize() {
    Fletching.LOGGER.info("Fletching enchantment tags registered!");
  }
}
