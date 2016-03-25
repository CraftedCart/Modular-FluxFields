package io.github.craftedcart.modularfluxfields.damagesource;

import net.minecraft.util.DamageSource;

/**
 * Created by CraftedCart on 25/12/2015 (DD/MM/YYYY)
 * Merry Christmas!
 */

public class DamageSourceFFSecurityKill extends DamageSource {

    public DamageSourceFFSecurityKill(String damageTypeIn) {
        super(damageTypeIn);
    }

    public static DamageSource causeElectricDamage() {
        return (new DamageSource("forcefieldSecurityKill"));
    }

}
