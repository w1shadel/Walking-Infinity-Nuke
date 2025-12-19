package com.Maxwell.walking_ifnuke.client;

import com.Maxwell.walking_ifnuke.entity.WalkingInfNuke;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class WalkingInfNukeModel extends EntityModel<WalkingInfNuke> {
    private final ModelPart top;
    private final ModelPart bottom;
    private final ModelPart core;
    private final ModelPart upper_leg_left;
    private final ModelPart lower_leg_left;
    private final ModelPart upper_leg_right;
    private final ModelPart lower_leg_right;
    private final ModelPart bone;

    public WalkingInfNukeModel(ModelPart model) {
        this.top = model.getChild("top");
        this.bottom = model.getChild("bottom");
        this.core = model.getChild("core");
        this.upper_leg_left = model.getChild("upper_leg_left");
        this.lower_leg_left = this.upper_leg_left.getChild("lower_leg_left");
        this.upper_leg_right = model.getChild("upper_leg_right");
        this.lower_leg_right = this.upper_leg_right.getChild("lower_leg_right");
        this.bone = model.getChild("bone");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        // --- 本体パーツ（変更なし） ---
        partDefinition.addOrReplaceChild("top", CubeListBuilder.create()
                        .texOffs(60, 53).addBox(10.0f, -13.4f, -11.0f, -4.0f, 3.0f, 4.0f)
                        .texOffs(48, 24).addBox(6.0f, -15.4f, -11.0f, 4.0f, 1.0f, 4.0f)
                        .texOffs(32, 52).addBox(4.0f, -14.4f, -13.0f, 8.0f, 4.0f, 8.0f),
                PartPose.offsetAndRotation(-8.0f, 17.4f, 8.0f, 0.0f, 0.0f, 0.0f));
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create()
                        .texOffs(60, 53).addBox(10.0f, -4.4f, -11.0f, -4.0f, 3.0f, 4.0f).mirror()
                        .texOffs(0, 52).addBox(4.0f, -4.4f, -13.0f, 8.0f, 4.0f, 8.0f),
                PartPose.offsetAndRotation(-8.0f, 17.4f, 8.0f, 0.0f, 0.0f, 0.0f));
        partDefinition.addOrReplaceChild("core", CubeListBuilder.create()
                        .texOffs(50, 35).addBox(5.0f, -7.9f, -9.5f, 6.0f, 1.0f, 1.0f)
                        .texOffs(52, 29).addBox(6.5f, -8.9f, -10.5f, 3.0f, 3.0f, 3.0f),
                PartPose.offsetAndRotation(-8.0f, 17.4f, 8.0f, 0.0f, 0.0f, 0.0f));
        // --- 左足 (Left Leg) ---
        // ヒップ（回転軸）の位置を Y=2.0F に設定。ここを中心に回転します。
        PartDefinition upperLegLeft = partDefinition.addOrReplaceChild("upper_leg_left", CubeListBuilder.create()
                        // 太もも部分: Pivot(4,2,-1) からの相対座標
                        .texOffs(49, 5).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F),
                PartPose.offset(4.0F, 2.0F, -1.0F));
        // 膝（回転軸）の位置を Y=8.0F (上足の下端) に設定
        upperLegLeft.addOrReplaceChild("lower_leg_left", CubeListBuilder.create()
                        // スネ部分
                        .texOffs(49, 13).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F)
                        // 足先の装飾 (Coordinates recalculated relative to Knee)
                        .texOffs(56, 37).addBox(-1.0F, 10.0F, -4.0F, 2.0F, 4.0F, 2.0f).mirror()
                        .texOffs(56, 43).addBox(-1.0F, 10.0F, 2.0F, 2.0F, 4.0F, 2.0f)
                        .texOffs(0, 42).addBox(-1.0F, 8.0F, -4.0F, 2.0F, 2.0F, 8.0f),
                PartPose.offset(0.0F, 8.0F, 0.0F));
        // --- 右足 (Right Leg) ---
        // ヒップ（回転軸）の位置
        PartDefinition upperLegRight = partDefinition.addOrReplaceChild("upper_leg_right", CubeListBuilder.create()
                        // 太もも部分
                        .texOffs(54, 5).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F),
                PartPose.offset(-4.0F, 2.0F, -1.0F));
        // 膝（回転軸）の位置
        upperLegRight.addOrReplaceChild("lower_leg_right", CubeListBuilder.create()
                        // スネ部分
                        .texOffs(54, 13).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 8.0F, 3.0F)
                        // 足先の装飾
                        .texOffs(56, 37).addBox(-1.0F, 10.0F, -4.0F, 2.0F, 4.0F, 2.0f)
                        .texOffs(0, 42).addBox(-1.0F, 8.0F, -4.0F, 2.0F, 2.0F, 8.0f)
                        .texOffs(56, 43).addBox(-1.0F, 10.0F, 2.0F, 2.0F, 4.0F, 2.0f),
                PartPose.offset(0.0F, 8.0F, 0.0F));
        // --- 骨 (変更なし) ---
        partDefinition.addOrReplaceChild("bone", CubeListBuilder.create()
                        .texOffs(42, 0).addBox(-5.0f, -26.0f, -1.5f, 10.0f, 4.0f, 1.0f),
                PartPose.offsetAndRotation(0.0f, 24.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(WalkingInfNuke entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // 歩行 (上足)
        this.upper_leg_left.xRot = Mth.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
        this.upper_leg_right.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.0F * limbSwingAmount;
        // 膝 (下足) - 逆位相で動かして膝を表現
        this.lower_leg_left.xRot = -Math.abs(Mth.sin(limbSwing * 0.6662F) * 0.8F * limbSwingAmount);
        this.lower_leg_right.xRot = -Math.abs(Mth.sin(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount);
        // 体の揺れ
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