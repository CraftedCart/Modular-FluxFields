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

    private static Configuration configuration;

    public static void initClient() {

        try {

            configuration.load();

            if (configuration.hasKey("graphics", "useHighPolyModels") &&
                    configuration.hasKey("graphics", "useGLSLShaders")) {

                MFFSettings.useHighPolyModels = configuration.getBoolean("useHighPolyModels", "graphics", MFFSettings.useHighPolyModels,
                        "Should MFF use high polygon count models? This drastically impacts performance");
                MFFSettings.useGLSLShaders = configuration.getBoolean("useHighPolyModels", "graphics", MFFSettings.useGLSLShaders,
                        "Should MFF use GLSL shaders? This improves performance, and looks fancy");

            } else {
                SelectGraphicsPreset.askUser();
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.error(e.getMessage());
        } finally {
            if (configuration.hasChanged()) {
                configuration.save();
            }
        }

    }

    public static void initCommon(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
        }
    }

    public static void saveGraphicsConfig() {

        try {

            configuration.load();

            configuration.get("graphics", "useHighPolyModels", MFFSettings.useHighPolyModels).set(MFFSettings.useHighPolyModels);
            configuration.get("graphics", "useGLSLShaders", MFFSettings.useGLSLShaders).set(MFFSettings.useGLSLShaders);

        } catch (Exception e) {
            e.printStackTrace();
            LogHelper.error(e.getMessage());
        } finally {
            if (configuration.hasChanged()) {
                configuration.save();
            }
        }

    }

    public static void saveMFFSettings() {
        saveGraphicsConfig();
    }

}
