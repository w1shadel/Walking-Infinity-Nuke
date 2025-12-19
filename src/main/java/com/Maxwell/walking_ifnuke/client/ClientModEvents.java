package com.Maxwell.walking_ifnuke.client;

import com.Maxwell.walking_ifnuke.ModEntity;
import com.Maxwell.walking_ifnuke.WalkingMod;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WalkingMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(ModEntity.WALKING_INF_NUKE.get(), WalkingInfNukeRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(WalkingInfNukeRenderer.NUKE_LAYER, WalkingInfNukeModel::createBodyLayer);

        event.registerLayerDefinition(WalkingInfNukeRenderer.NUKE_ARMED_LAYER,
                () -> WalkingInfNukeModelArmed.createBodyLayer(CubeDeformation.NONE));

        event.registerLayerDefinition(WalkingInfNukeRenderer.NUKE_ARMED_BIG_LAYER,
                () -> WalkingInfNukeModelArmed.createBodyLayer(new CubeDeformation(0.2F)));
    }
}