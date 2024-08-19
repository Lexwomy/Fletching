package lexwomy.fletching.renderer;

import lexwomy.fletching.entity.PilumEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class PilumEntityRenderer extends ProjectileEntityRenderer<PilumEntity> {

    public PilumEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    //TODO - create custom pilum model later
    @Override
    public Identifier getTexture(PilumEntity entity) {
        return Identifier.ofVanilla("textures/entity/projectiles/arrow.png");
    }
}
