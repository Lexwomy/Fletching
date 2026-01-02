package lexwomy.fletching.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lexwomy.fletching.FletchingClient;
import lexwomy.fletching.entity.ShrapnelEntity;
import lexwomy.fletching.models.ShrapnelModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class ShrapnelEntityRenderer extends EntityRenderer<ShrapnelEntity, ShrapnelEntityRenderState> {
    private final ShrapnelModel model;

    public ShrapnelEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ShrapnelModel(context.bakeLayer(ShrapnelModel.LAYER_LOCATION));
    }

    @Override
    public void submit(ShrapnelEntityRenderState shrapnelEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(shrapnelEntityRenderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(shrapnelEntityRenderState.xRot));
        submitNodeCollector.submitModel(
                this.model,
                shrapnelEntityRenderState,
                poseStack,
                this.model.renderType(ShrapnelModel.TEXTURE),
                shrapnelEntityRenderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                shrapnelEntityRenderState.outlineColor,
                null);
        poseStack.popPose();
        super.submit(shrapnelEntityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    @Override
    public ShrapnelEntityRenderState createRenderState() {
        return new ShrapnelEntityRenderState();
    }

    @Override
    public void extractRenderState(ShrapnelEntity shrapnelEntity, ShrapnelEntityRenderState shrapnelEntityRenderState, float f) {
        super.extractRenderState(shrapnelEntity, shrapnelEntityRenderState, f);
        shrapnelEntityRenderState.xRot = shrapnelEntity.getXRot(f);
        shrapnelEntityRenderState.yRot = shrapnelEntity.getYRot(f);
    }
}
