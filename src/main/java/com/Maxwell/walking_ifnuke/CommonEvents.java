package com.Maxwell.walking_ifnuke;

import com.Maxwell.walking_ifnuke.entity.WalkingInfNuke;
import com.buuz135.industrial.entity.InfinityNukeEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WalkingMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEvents {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;
        if (event.getEntity() instanceof InfinityNukeEntity originalNuke) {
            if (originalNuke.isRemoved()) return;
            event.setCanceled(true);
            WalkingInfNuke myNuke = new WalkingInfNuke(ModEntity.WALKING_INF_NUKE.get(), event.getLevel());
            myNuke.moveTo(originalNuke.getX(), originalNuke.getY(), originalNuke.getZ(), originalNuke.getYRot(), originalNuke.getXRot());
            myNuke.setRadius(originalNuke.getRadius());
            if (originalNuke.isArmed()) {
                myNuke.setArmed(true);
            }
            if (originalNuke.isExploding()) {
                myNuke.setArmed(true);
                myNuke.setExploding(true);
                myNuke.setTicksExploding(originalNuke.getTicksExploding());
            }
            event.getLevel().addFreshEntity(myNuke);
        }
    }
}