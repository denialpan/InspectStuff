package com.danpan1232.inspectstuff.client;
import com.danpan1232.inspectstuff.InspectStuffClient;
import net.minecraft.client.Minecraft;

public class InspectAnimationState {
    public static boolean isInspecting = false;
    private static boolean previousInspectKeyDown = false;
    public static float animationTick = 0f;
    private static int lastSelectedSlot = -1;


    public static void tick() {
        Minecraft mc = Minecraft.getInstance();
        boolean currentDown = InspectStuffClient.INSPECT_KEY.get().isDown();

        // Trigger animation start only on *first* key press
        if (currentDown && !previousInspectKeyDown) {
            isInspecting = true;
            animationTick = 0f;
        }

        // Advance animation if it's active
        // Advance animation if active
        if (isInspecting) {
            animationTick += 1f;

            // Detect hotbar slot change
            if (lastSelectedSlot != -1 && mc.player != null && mc.player.getInventory().selected != lastSelectedSlot) {
                cancel(); // Cancel inspect on hotbar scroll
            }

        }

        lastSelectedSlot = mc.player.getInventory().selected;
        previousInspectKeyDown = currentDown;
    }

    public static void cancel() {
        isInspecting = false;
        animationTick = 0f;
    }

    public static float getInterpolatedOffset(float partialTick) {
        float interpolated = animationTick + partialTick;
        return (float) Math.sin(interpolated / 10f) * 0.5f;  // smooth forward/backward pulse
    }
}
