package io.github.craftedcart.modularfluxfields.init;

import io.github.craftedcart.modularfluxfields.ModModularFluxFields;
import io.github.craftedcart.modularfluxfields.handler.GuiHandler;
import io.github.craftedcart.modularfluxfields.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by CraftedCart on 27/02/2016 (DD/MM/YYYY)
 */
public class ModKeyBindings {

    public static KeyBinding settingsKeybind;

    public static void init() {
        settingsKeybind = new KeyBinding("modularfluxfields:key.settings", Keyboard.KEY_F8, "key.categories.modularfluxfields");

        ClientRegistry.registerKeyBinding(settingsKeybind);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        if (settingsKeybind.isPressed()) {
            LogHelper.info("Opening modularfluxfields Settings GUI");
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            player.openGui(ModModularFluxFields.instance, GuiHandler.modularfluxfieldsSettings_GUI, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        }

    }

}
