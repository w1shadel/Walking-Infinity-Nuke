package com.Maxwell.walking_ifnuke.client;

import com.Maxwell.walking_ifnuke.entity.WalkingInfNuke;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class WalkingInfNukeModelArmed extends EntityModel<WalkingInfNuke> {
    private final ModelPart top;
    private final ModelPart bottom;
    private final ModelPart core;
    private final ModelPart upper_leg_left;
    private final ModelPart lower_leg_left;
    private final ModelPart upper_leg_right;
    private final ModelPart lower_leg_right;
    private final ModelPart bone;

    public WalkingInfNukeModelArmed(ModelPart model) {
        this.top = model.getChild("top");
        this.bottom = model.getChild("bottom");
        this.core = model.getChild("core");
        this.upper_leg_left = model.getChild("upper_leg_left");
        this.lower_leg_left = this.upper_leg_left.getChild("lower_leg_left");
        this.upper_leg_right = model.getChild("upper_leg_right");
        this.lower_leg_right = this.upper_leg_right.getChild("lower_leg_right");
        this.bone = model.getChild("bone");
    }

    public static LayerDefinition createBodyLayer(CubeDeformation size) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        // --- 既存パーツ ---
        partDefinition.addOrReplaceChild("top", CubeListBuilder.create()
                        .texOffs(60, 53).addBox(10.0f, -10.4f, -11.0f, -4.0f, 3.0f, 4.0f, size)
                        .texOffs(48, 24).addBox(6.0f, -12.4f, -11.0f, 4.0f, 1.0f, 4.0f, size)
                        .texOffs(32, 52).addBox(4.0f, -11.4f, -13.0f, 8.0f, 4.0f, 8.0f, size),
                PartPose.offsetAndRotation(-8.0f, 17.4f, 8.0f, 0.0f, 0.0f, 0.0f));
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create()
                        .texOffs(60, 53).addBox(10.0f, -7.4f, -11.0f, -4.0f, 3.0f, 4.0f, size).mirror()
                        .texOffs(0, 52).addBox(4.0f, -7.4f, -13.0f, 8.0f, 4.0f, 8.0f, size),
                PartPose.offsetAndRotation(-8.0f, 17.4f, 8.0f, 0.0f, 0.0f, 0.0f));
        partDefinition.addOrReplaceChild("core", CubeListBuilder.create()
                        .texOffs(50, 35).addBox(5.0f, -7.9f, -9.5f, 6.0f, 1.0f, 1.0f, size)
                        .texOffs(52, 29).addBox(6.5f, -8.9f, -10.5f, 3.0f, 3.0f, 3.0f, size),
                PartPose.offsetAndRotation(-8.0f, 17.4f, 8.0f, 0.0f, 0.0f, 0.0f));
        // --- Left Leg ---
        PartDefinition upperLegLeft = partDefinition.addOrReplaceChild("upper_leg_left", CubeListBuilder.create()
                        .texOffs(49, 5).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, size),
                PartPose.offset(4.0F, 2.0F, -1.0F));
        upperLegLeft.addOrReplaceChild("lower_leg_left", CubeListBuilder.create()
                        .texOffs(49, 13).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, size)
                        .texOffs(56, 37).addBox(-1.0F, 10.0F, -4.0F, 2.0F, 4.0F, 2.0f, size).mirror()
                        .texOffs(56, 43).addBox(-1.0F, 10.0F, 2.0F, 2.0F, 4.0F, 2.0f, size)
                        .texOffs(0, 42).addBox(-1.0F, 8.0F, -4.0F, 2.0F, 2.0F, 8.0f, size),
                PartPose.offset(0.0F, 8.0F, 0.0F));
        // --- Right Leg ---
        PartDefinition upperLegRight = partDefinition.addOrReplaceChild("upper_leg_right", CubeListBuilder.create()
                        .texOffs(54, 5).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, size),
                PartPose.offset(-4.0F, 2.0F, -1.0F));
        upperLegRight.addOrReplaceChild("lower_leg_right", CubeListBuilder.create()
                        .texOffs(54, 13).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F, size)
                        .texOffs(56, 37).addBox(-1.0F, 10.0F, -4.0F, 2.0F, 4.0F, 2.0f, size)
                        .texOffs(0, 42).addBox(-1.0F, 8.0F, -4.0F, 2.0F, 2.0F, 8.0f, size)
                        .texOffs(56, 43).addBox(-1.0F, 10.0F, 2.0F, 2.0F, 4.0F, 2.0f, size),
                PartPose.offset(0.0F, 8.0F, 0.0F));
        // --- Bone ---
        partDefinition.addOrReplaceChild("bone", CubeListBuilder.create()
                        .texOffs(42, 0).addBox(-5.0f, -26.0f, -1.5f, 10.0f, 4.0f, 1.0f, size),
                PartPose.offsetAndRotation(0.0f, 24.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(WalkingInfNuke entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // 歩行
        this.upper_leg_left.xRot = Mth.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
        this.upper_leg_right.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.0F * limbSwingAmount;
        // 膝
        this.lower_leg_left.xRot = -Math.abs(Mth.sin(limbSwing * 0.6662F) * 0.8F * limbSwingAmount);
        this.lower_leg_right.xRot = -Math.abs(Mth.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount);
        float wobble = Mth.cos(limbSwing * 0.6662F) * 0.1F * limbSwingAmount;
        this.bottom.zRot = wobble;
        this.core.zRot = wobble;
        if (entity.isExploding()) {
            float shakeSpeed = 0.8F;
            float shakeAmount = 0.05F;
            this.top.xRot += Mth.cos(ageInTicks * shakeSpeed) * shakeAmount;
            this.bottom.xRot -= Mth.cos(ageInTicks * shakeSpeed) * shakeAmount;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        top.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        bottom.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        core.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        upper_leg_left.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        upper_leg_right.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        bone.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}