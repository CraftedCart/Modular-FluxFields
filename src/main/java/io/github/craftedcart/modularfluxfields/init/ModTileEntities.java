package io.github.craftedcart.modularfluxfields.init;

import io.github.craftedcart.modularfluxfields.reference.Names;
import io.github.craftedcart.modularfluxfields.tileentity.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */
public class ModTileEntities {

    public static void init() {

        GameRegistry.registerTileEntity(TEForcefield.class, Names.TEForcefield);
        GameRegistry.registerTileEntity(TEFFProjector.class, Names.TEFFProjector);
        GameRegistry.registerTileEntity(TEPowerCube.class, Names.TEPowerCube);
        GameRegistry.registerTileEntity(TECrystalRefinery.class, Names.TECrystalRefinery);
        GameRegistry.registerTileEntity(TECrystalConstructor.class, Names.TECrystalConstructor);
        GameRegistry.registerTileEntity(TECreativePowerGenerator.class, Names.TECreativePowerGenerator);
        GameRegistry.registerTileEntity(TESolarPowerGenerator.class, Names.TESolarPowerGenerator);
        GameRegistry.registerTileEntity(TEPowerRelay.class, Names.TEPowerRelay);

    }

}
