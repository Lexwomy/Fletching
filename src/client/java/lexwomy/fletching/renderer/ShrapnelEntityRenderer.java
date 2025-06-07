package lexwomy.fletching.renderer;

import lexwomy.fletching.entity.ShrapnelEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class ShrapnelEntityRenderer extends ProjectileEntityRenderer<ShrapnelEntity, ShrapnelEntityRenderState> {

    public ShrapnelEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public ShrapnelEntityRenderState createRenderState() {
        return new ShrapnelEntityRenderState();
    }

    //TODO - create custom pilum model later
    @Override
    protected Identifier getTexture(ShrapnelEntityRenderState state) {
        return Identifier.ofVanilla("textures/entity/projectiles/arrow.png");
    }
}
