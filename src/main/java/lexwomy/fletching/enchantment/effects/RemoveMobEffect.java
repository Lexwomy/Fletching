package lexwomy.fletching.enchantment.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Iterator;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record RemoveMobEffect(HolderSet<MobEffect> toRemove) implements EnchantmentEntityEffect {
  public static final MapCodec<RemoveMobEffect> CODEC =
      RecordCodecBuilder.mapCodec(
          instance ->
              instance
                  .group(
                      RegistryCodecs.homogeneousList(Registries.MOB_EFFECT)
                          .fieldOf("to_remove")
                          .forGetter(RemoveMobEffect::toRemove))
                  .apply(instance, RemoveMobEffect::new));

  @Override
  public void apply(
      ServerLevel serverLevel,
      int i,
      EnchantedItemInUse enchantedItemInUse,
      Entity entity,
      Vec3 vec3) {
    if (enchantedItemInUse.owner() != null) {
      LivingEntity owner = enchantedItemInUse.owner();
      for (Iterator<Holder<MobEffect>> it = this.toRemove().iterator(); it.hasNext(); ) {
        Holder<MobEffect> mobEffect = it.next();
        owner.removeEffect(mobEffect);
      }
    }
  }

  @Override
  public MapCodec<? extends EnchantmentEntityEffect> codec() {
    return CODEC;
  }
}
