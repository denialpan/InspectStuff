package com.danpan1232.inspectstuff.client;

import com.danpan1232.inspectstuff.InspectStuffClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.minecraft.client.renderer.ItemInHandRenderer;

import net.minecraft.resources.ResourceLocation;

@EventBusSubscriber(modid = "inspectstuff", value = Dist.CLIENT)
public class ArmRenderer {

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        MultiBufferSource buffer = event.getMultiBufferSource();
        int light = event.getPackedLight();
        float partialTicks = event.getPartialTick();

        if (player == null || mc.level == null) return;


        boolean isEmptyHanded = player.getMainHandItem().isEmpty();
        if (!isEmptyHanded || !InspectAnimationState.isInspecting) return;

        // Suppress vanilla hand
        event.setCanceled(true);

        renderVanillaLikeArm(
                event.getPoseStack(),
                event.getMultiBufferSource(),
                event.getPackedLight(),
                event.getEquipProgress(),
                event.getSwingProgress(),
                player.getMainArm(),
                player,
                partialTicks
        );
    }

    public static void renderVanillaLikeArm(PoseStack poseStack, MultiBufferSource buffer, int packedLight, float equippedProgress, float swingProgress, HumanoidArm side, AbstractClientPlayer player, float partialTicks) {

        float offset = InspectAnimationState.getInterpolatedOffset(partialTicks);


        boolean flag = side != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(swingProgress);
        float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * (float) (Math.PI * 2));
        float f4 = -0.4F * Mth.sin(swingProgress * (float) Math.PI);
        poseStack.translate(f * (f2 + 0.64000005F), f3 - 0.6F + equippedProgress * -0.6F, f4 - 0.71999997F + offset * 5);
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
        float f6 = Mth.sin(f1 * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(f * f6 * 70.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(f * f5 * -20.0F));
        poseStack.translate(f * -1.0F, 3.6F, 3.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
        poseStack.translate(f * 5.6F, 0.0F, 0.0F);

        PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
        if (flag) {
            renderer.renderRightHand(poseStack, buffer, packedLight, player);
        } else {
            renderer.renderLeftHand(poseStack, buffer, packedLight, player);
        }

        System.out.println("this is being rendered: " + side);
    }
}
