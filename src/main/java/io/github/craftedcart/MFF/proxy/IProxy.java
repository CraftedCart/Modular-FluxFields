package io.github.craftedcart.MFF.proxy;

import java.awt.*;
import java.io.IOException;

/**
 * Created by CraftedCart on 17/11/2015 (DD/MM/YYYY)
 */

public interface IProxy {

    void registerRenders();
    void init() throws IOException, FontFormatException;

}
