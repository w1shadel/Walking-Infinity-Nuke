package com.Maxwell.walking_ifnuke;

import com.Maxwell.walking_ifnuke.entity.WalkingInfNuke;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid = WalkingMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvent {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntity.WALKING_INF_NUKE.get(), WalkingInfNuke.createAttributes().build());
    }
}
