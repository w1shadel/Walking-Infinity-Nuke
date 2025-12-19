package com.Maxwell.walking_ifnuke;

import com.Maxwell.walking_ifnuke.entity.WalkingInfNuke;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntity {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WalkingMod.MODID);

    public static final RegistryObject<EntityType<WalkingInfNuke>> WALKING_INF_NUKE = ENTITIES.register("walking_infnuke",
            () -> EntityType.Builder.<WalkingInfNuke>of(WalkingInfNuke::new, MobCategory.MONSTER) 
                    .sized(0.6F, 1.95F)
                    .build(new ResourceLocation(WalkingMod.MODID, "walking_infnuke").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}