package com.Maxwell.walking_ifnuke;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WalkingMod.MODID)
public class WalkingMod
{

    public static final String MODID = "walking_ifnuke";
    public WalkingMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        ModEntity.register(modEventBus);
    }

}
