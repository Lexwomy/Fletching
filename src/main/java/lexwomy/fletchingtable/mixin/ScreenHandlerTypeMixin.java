package lexwomy.fletchingtable.mixin;

import lexwomy.fletchingtable.FletchingScreenHandler;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ScreenHandlerType.class)
public abstract class ScreenHandlerTypeMixin<T extends ScreenHandler> implements ToggleableFeature {
    @Shadow protected abstract <T extends ScreenHandler> ScreenHandlerType<T> register(String id, ScreenHandlerType.Factory<T> factory);

    public static final ScreenHandlerType<FletchingScreenHandler> FLETCHING = register()
}
