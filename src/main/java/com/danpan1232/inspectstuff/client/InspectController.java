package com.danpan1232.inspectstuff.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = "inspectstuff", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class InspectController {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().player == null) return;
        Minecraft mc = Minecraft.getInstance();

        InspectAnimationState.tick();

        // Check if player is triggering attack or use/place action
        if (mc.options.keyAttack.isDown()|| mc.options.keyUse.isDown()) {
            InspectAnimationState.cancel(); // Interrupt inspect animation
        }
    }
}
