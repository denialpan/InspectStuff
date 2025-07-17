package com.danpan1232.inspectstuff.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class InspectArmLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public InspectArmLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        System.out.println("isInspecting = " + InspectAnimationState.isInspecting);

        if (!InspectAnimationState.isInspecting) return;

        PlayerModel<AbstractClientPlayer> model = this.getParentModel();

        poseStack.pushPose();

        // Start from default right arm transform
        model.rightArm.translateAndRotate(poseStack);

        // Apply **additional offset** to move off-screen at rest
        poseStack.translate(0.0F, -2.0F, -2.0F); // hidden when idle
        poseStack.translate(0.0F, InspectAnimationState.getInterpolatedOffset(partialTick), 0.0F);

        // Optionally rotate
        // poseStack.mulPose(Axis.YP.rotationDegrees(180));

        ResourceLocation skin = (Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player))
                .getTextureLocation(player);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(skin));

        // Render the right arm again — with offset!
        model.rightArm.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }
}

