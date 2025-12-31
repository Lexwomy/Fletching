package lexwomy.fletching.renderer;

import lexwomy.fletching.entity.ShrapnelEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class ShrapnelEntityRenderer extends EntityRenderer<ShrapnelEntity, ShrapnelEntityRenderState> {
    public ShrapnelEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ShrapnelEntityRenderState createRenderState() {
        return new ShrapnelEntityRenderState();
    }

    //TODO - create custom pilum model later
    protected Identifier getTextureLocation(ShrapnelEntityRenderState state) {
        return Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");
    }
}
