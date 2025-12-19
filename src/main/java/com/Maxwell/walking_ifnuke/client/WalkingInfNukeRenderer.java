package com.Maxwell.walking_ifnuke.client;

import com.Maxwell.walking_ifnuke.entity.WalkingInfNuke;
import com.buuz135.industrial.utils.Reference;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WalkingInfNukeRenderer extends MobRenderer<WalkingInfNuke, WalkingInfNukeModel> {

    public static final ResourceLocation NUKE = new ResourceLocation(Reference.MOD_ID, "textures/entity/infinity_nuke_entity.png");

    public static final ModelLayerLocation NUKE_LAYER = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "infinity_nuke_entity"), "main");
    public static final ModelLayerLocation NUKE_ARMED_LAYER = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "infinity_nuke_entity"), "armed");
    public static final ModelLayerLocation NUKE_ARMED_BIG_LAYER = new ModelLayerLocation(new ResourceLocation(Reference.MOD_ID, "infinity_nuke_entity"), "armed_big");

    private final WalkingInfNukeModelArmed nukeModelArmed;
    private final WalkingInfNukeModelArmed nukeModelArmedBig;

    public WalkingInfNukeRenderer(EntityRendererProvider.Context context) {
        super(context, new WalkingInfNukeModel(context.bakeLayer(NUKE_LAYER)), 0.5F);
        this.nukeModelArmed = new WalkingInfNukeModelArmed(context.bakeLayer(NUKE_ARMED_LAYER));
        this.nukeModelArmedBig = new WalkingInfNukeModelArmed(context.bakeLayer(NUKE_ARMED_BIG_LAYER));
    }

    @Override
    public void render(WalkingInfNuke entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float bodyYaw = Mth.rotLerp(partialTicks, entityIn.yBodyRotO, entityIn.yBodyRot);
        float headYaw = Mth.rotLerp(partialTicks, entityIn.yHeadRotO, entityIn.yHeadRot);
        float netHeadYaw = headYaw - bodyYaw;
        if (entityIn.isDataArmed()) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0D, 1.5D, 0.0D);
            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(bodyYaw));
            if (entityIn.isDataExploding()) {
                double time = 7 + entityIn.getCommandSenderWorld().getRandom().nextInt(50);
                float shakeX = (float) ((entityIn.getCommandSenderWorld().getRandom().nextDouble() - 0.5) / time);
                float shakeZ = (float) ((entityIn.getCommandSenderWorld().getRandom().nextDouble() - 0.5) / time);
                matrixStackIn.translate(shakeX, 0, shakeZ);
            }
            VertexConsumer ivertexbuilder = net.minecraft.client.renderer.entity.ItemRenderer.getFoilBufferDirect(bufferIn, RenderType.entityTranslucent(this.getTextureLocation(entityIn)), false, false);
            this.nukeModelArmed.setupAnim(entityIn, entityIn.walkAnimation.position(partialTicks), entityIn.walkAnimation.speed(partialTicks), partialTicks, netHeadYaw, entityIn.getXRot());
            this.nukeModelArmed.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            if (entityIn.isDataExploding() && entityIn.level().getRandom().nextDouble() < 0.96) {
                float f = partialTicks + entityIn.getDataTicksExploding() + 10;
                ivertexbuilder = net.minecraft.client.renderer.entity.ItemRenderer.getFoilBufferDirect(bufferIn, RenderType.energySwirl(new ResourceLocation(Reference.MOD_ID, "textures/block/mycelial_clean.png"), f * (entityIn.getDataTicksExploding() / 50000f), f * (entityIn.getDataTicksExploding() / 50000f)), false, false);
                this.nukeModelArmedBig.setupAnim(entityIn, entityIn.walkAnimation.position(partialTicks), entityIn.walkAnimation.speed(partialTicks), partialTicks, netHeadYaw, entityIn.getXRot());
                this.nukeModelArmedBig.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.1F);
            }
            matrixStackIn.popPose();
        } else {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0D, 1.5D, 0.0D);
            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(bodyYaw));
            VertexConsumer ivertexbuilder = net.minecraft.client.renderer.entity.ItemRenderer.getFoilBufferDirect(bufferIn, RenderType.entityTranslucent(this.getTextureLocation(entityIn)), false, false);
            this.model.setupAnim(entityIn, entityIn.walkAnimation.position(partialTicks), entityIn.walkAnimation.speed(partialTicks), partialTicks, netHeadYaw, entityIn.getXRot());
            this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(WalkingInfNuke entity) {
        return NUKE;
    }
}