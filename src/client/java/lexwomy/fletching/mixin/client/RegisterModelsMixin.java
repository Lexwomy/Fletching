package lexwomy.fletching.mixin.client;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.sugar.Local;
import lexwomy.fletching.FletchingClient;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LayerDefinitions.class)
public abstract class RegisterModelsMixin {
    @Inject(method = "createRoots", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"))
    private static void registerModels(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir, @Local ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder) {
        // Skip putting new models in ALL_MODELS, the check is unnecessary for now..
        for (Map.Entry<ModelLayerLocation, LayerDefinition> entry : FletchingClient.MODELS.entrySet()) {
            builder.put(entry.getKey(), entry.getValue());
        }
    }
}
