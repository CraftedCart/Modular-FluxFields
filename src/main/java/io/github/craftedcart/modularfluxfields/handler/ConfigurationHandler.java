package io.github.craftedcart.modularfluxfields.handler;

import io.github.craftedcart.modularfluxfields.reference.MFFSettings;
import io.github.craftedcart.modularfluxfields.utility.LogHelper;
import io.github.craftedcart.modularfluxfields.utility.SelectGraphicsPreset;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by CraftedCart on 25/03/2016 (DD/MM/YYYY)
 */
public class ConfigurationHandler {

    public static void initClient(File configFile) {

        Configuration configuration = new Configuration(configFile);

        try {

            configuration.load();

            if (configuration.hasKey("graphics", "useHighPolyModels") &&
                    configuration.hasKey("graphics", "useGLSLShaders")) {

                MFFSettings.useHighPolyModels = configuration.get("graphics", "useHighPolyModels", true).getBoolean();
                MFFSettings.useGLSLShaders = configuration.get("graphics", "useGLSLShaders", true).getBoolean();

            } else {
                SelectGraphicsPreset.askUser(configFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.error(e.getMessage());
        } finally {
            configuration.save();
        }

    }

    public static void saveGraphicsConfig(File configFile) {

        Configuration configuration = new Configuration(configFile);

        try {

            configuration.load();

            configuration.get("graphics", "useHighPolyModels", MFFSettings.useHighPolyModels);
            configuration.get("graphics", "useGLSLShaders", MFFSettings.useGLSLShaders);

        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.error(e.getMessage());
        } finally {
            configuration.save();
        }

    }

}
