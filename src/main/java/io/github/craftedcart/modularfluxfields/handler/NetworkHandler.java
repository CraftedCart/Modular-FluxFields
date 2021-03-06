package io.github.craftedcart.modularfluxfields.handler;

import io.github.craftedcart.modularfluxfields.network.*;
import io.github.craftedcart.modularfluxfields.reference.Reference;
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
        network.registerMessage(MessageFFProjectorSendPowerStatsToClient.Handler.class, MessageFFProjectorSendPowerStatsToClient.class, 3, Side.CLIENT);
        network.registerMessage(MessageRequestPowerStats.Handler.class, MessageRequestPowerStats.class, 4, Side.SERVER);
        network.registerMessage(MessageSetInputSide.Handler.class, MessageSetInputSide.class, 5, Side.SERVER);
        network.registerMessage(MessageSetOutputSide.Handler.class, MessageSetOutputSide.class, 6, Side.SERVER);
    }

}
