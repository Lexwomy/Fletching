package lexwomy.fletching.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lexwomy.fletching.Fletching;
import lexwomy.fletching.renderer.ShrapnelEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class ShrapnelModel extends EntityModel<ShrapnelEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "shrapnel"), "main");
	public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Fletching.MOD_ID, "textures/entity/shrapnel.png");
    private final ModelPart body;

	public ShrapnelModel(ModelPart root) {
        super(root, RenderTypes::entitySolid);
        this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

//		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
//		.texOffs(12, 0).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(12, 0).addBox(0.0F, -4.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(-1.0F, 0.0F, 1.0F, 0.0F, 0.0F, (float) (Math.PI / 2)));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -4.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 2)));

		return LayerDefinition.create(meshdefinition.transformed(partPose -> partPose.scaled(0.6F)), 16, 16);
	}

//	@Override
//	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
//	}
}