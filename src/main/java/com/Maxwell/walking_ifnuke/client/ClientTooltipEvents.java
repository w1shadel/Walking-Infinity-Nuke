package com.Maxwell.walking_ifnuke.client;

import com.Maxwell.walking_ifnuke.WalkingMod;
import com.buuz135.industrial.module.ModuleTool;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WalkingMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientTooltipEvents {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() == ModuleTool.INFINITY_NUKE.get()) {
            event.getToolTip().add(Component.translatable("walking_ifnuke.normal").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            if (Screen.hasShiftDown()) {
                event.getToolTip().add(Component.literal("----------------").withStyle(ChatFormatting.GRAY));
                event.getToolTip().add(Component.translatable("walking_ifnuke.shift_1").withStyle(ChatFormatting.RED));
                event.getToolTip().add(Component.translatable("walking_ifnuke.shift_2").withStyle(ChatFormatting.RED));
                event.getToolTip().add(Component.translatable("walking_ifnuke.shift_3").withStyle(ChatFormatting.RED));
                event.getToolTip().add(Component.literal(" ").withStyle(ChatFormatting.RESET));
                event.getToolTip().add(Component.translatable("walking_ifnuke.shift_4").withStyle(ChatFormatting.LIGHT_PURPLE));
                event.getToolTip().add(Component.translatable("walking_ifnuke.shift_5").withStyle(ChatFormatting.LIGHT_PURPLE));
            } else {
                event.getToolTip().add(Component.translatable("walking_ifnuke.press_shift").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            }
        }
    }
}