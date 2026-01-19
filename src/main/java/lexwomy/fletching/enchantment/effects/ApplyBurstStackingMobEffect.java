package lexwomy.fletching.enchantment.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lexwomy.fletching.effect.SupportsStackingEffect;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record ApplyBurstStackingMobEffect(
    HolderSet<@NotNull MobEffect> regularEffect,
    HolderSet<@NotNull MobEffect> burstEffect,
    LevelBasedValue minDuration,
    LevelBasedValue maxDuration,
    LevelBasedValue minAmplifier,
    LevelBasedValue maxAmplifier,
    LevelBasedValue burstAmplifierTrigger)
    implements EnchantmentEntityEffect {
  public static final MapCodec<ApplyBurstStackingMobEffect> CODEC =
      RecordCodecBuilder.mapCodec(
          instance ->
              instance
                  .group(
                      RegistryCodecs.homogeneousList(Registries.MOB_EFFECT)
                          .fieldOf("regular_effect")
                          .forGetter(ApplyBurstStackingMobEffect::regularEffect),
                      RegistryCodecs.homogeneousList(Registries.MOB_EFFECT)
                          .fieldOf("burst_effect")
                          .forGetter(ApplyBurstStackingMobEffect::burstEffect),
                      LevelBasedValue.CODEC
                          .fieldOf("min_duration")
                          .forGetter(ApplyBurstStackingMobEffect::minDuration),
                      LevelBasedValue.CODEC
                          .fieldOf("max_duration")
                          .forGetter(ApplyBurstStackingMobEffect::maxDuration),
                      LevelBasedValue.CODEC
                          .fieldOf("min_amplifier")
                          .forGetter(ApplyBurstStackingMobEffect::minAmplifier),
                      LevelBasedValue.CODEC
                          .fieldOf("max_amplifier")
                          .forGetter(ApplyBurstStackingMobEffect::maxAmplifier),
                      LevelBasedValue.CODEC
                          .fieldOf("burst_amplifier_trigger")
                          .forGetter(ApplyBurstStackingMobEffect::burstAmplifierTrigger))
                  .apply(instance, ApplyBurstStackingMobEffect::new));

  @Override
  public void apply(
      ServerLevel serverLevel,
      int i,
      EnchantedItemInUse enchantedItemInUse,
      Entity entity,
      Vec3 vec3) {
    if (entity instanceof LivingEntity livingEntity) {
      RandomSource randomSource = livingEntity.getRandom();
      int duration =
          Math.round(
              Mth.randomBetween(
                      randomSource, this.minDuration.calculate(i), this.maxDuration.calculate(i))
                  * 20.0F);
      int amplifier =
          Math.max(
              0,
              Math.round(
                  Mth.randomBetween(
                      randomSource,
                      this.minAmplifier.calculate(i),
                      this.maxAmplifier.calculate(i))));
      ((SupportsStackingEffect) livingEntity)
          .fletching$stackBurstEffect(
              new MobEffectInstance(this.regularEffect.get(0), duration, amplifier),
              new MobEffectInstance(this.burstEffect.get(0), duration, amplifier),
              Math.round(this.burstAmplifierTrigger.calculate(amplifier)),
              null);
    }
  }

  @Override
  public @NotNull MapCodec<? extends EnchantmentEntityEffect> codec() {
    return CODEC;
  }
}
