package lexwomy.fletching.renderer;

import lexwomy.fletching.entity.PilumEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class PilumEntityRenderer extends ProjectileEntityRenderer<PilumEntity, PilumEntityRenderState> {

    public PilumEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public PilumEntityRenderState createRenderState() {
        return new PilumEntityRenderState();
    }

    //TODO - create custom pilum model later
    @Override
    protected Identifier getTexture(PilumEntityRenderState state) {
        return Identifier.ofVanilla("textures/entity/projectiles/arrow.png");
    }
}
