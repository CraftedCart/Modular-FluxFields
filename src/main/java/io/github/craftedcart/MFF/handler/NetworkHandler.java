package io.github.craftedcart.MFF.handler;

import io.github.craftedcart.MFF.network.MessageFFProjectorGuiSaveSecurity;
import io.github.craftedcart.MFF.network.MessageFFProjectorGuiSaveSizing;
import io.github.craftedcart.MFF.network.MessageRequestOpenGui;
import io.github.craftedcart.MFF.reference.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by CraftedCart on 23/11/2015 (DD/MM/YYYY)
 */

public class NetworkHandler {

    public static SimpleNetworkWrapper network;

    public static void init() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        network.registerMessage(MessageFFProjectorGuiSaveSizing.Handler.class, MessageFFProjectorGuiSaveSizing.class, 0, Side.SERVER);
        network.registerMessage(MessageRequestOpenGui.Handler.class, MessageRequestOpenGui.class, 1, Side.SERVER);
        network.registerMessage(MessageFFProjectorGuiSaveSecurity.Handler.class, MessageFFProjectorGuiSaveSecurity.class, 2, Side.SERVER);
    }

}
