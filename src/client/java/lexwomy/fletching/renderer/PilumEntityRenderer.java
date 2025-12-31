package lexwomy.fletching.renderer;

import lexwomy.fletching.entity.PilumEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class PilumEntityRenderer extends EntityRenderer<PilumEntity, PilumEntityRenderState> {


    public PilumEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public PilumEntityRenderState createRenderState() {
        return new PilumEntityRenderState();
    }

    //TODO - create custom pilum model later
    protected Identifier getTextureLocation(PilumEntityRenderState state) {
        return Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");
    }
}
