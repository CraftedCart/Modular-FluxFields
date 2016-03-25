package io.github.craftedcart.modularfluxfields.utility;

import io.github.craftedcart.modularfluxfields.handler.ConfigurationHandler;
import io.github.craftedcart.modularfluxfields.reference.MFFSettings;
import net.minecraft.util.StatCollector;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by CraftedCart on 27/02/2016 (DD/MM/YYYY)
 */

public class SelectGraphicsPreset {

    public static void askUser(final File configFile) {

        final JFrame frame = new JFrame("selectGraphicsPresetFrame");
        frame.setTitle(StatCollector.translateToLocal("gui.modularfluxfields:chooseGraphicsPreset"));

        JPanel panel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(24, 24, 24, 24);
        panel.setBorder(padding);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.setContentPane(panel);

        JLabel titleLabel = new JLabel(StatCollector.translateToLocal("gui.modularfluxfields:chooseGraphicsPreset"), SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        frame.getContentPane().add(titleLabel);

        frame.getContentPane().add(Box.createRigidArea(new Dimension(0, 32)));

        JButton fancyButton = new JButton(StatCollector.translateToLocal("gui.modularfluxfields:fancyGraphics"));
        frame.getContentPane().add(fancyButton);
        fancyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MFFSettings.useHighPolyModels = true;
                MFFSettings.useGLSLShaders = true;

                ConfigurationHandler.saveGraphicsConfig(configFile);

                frame.dispose();
            }
        });

        JLabel fancyLabel = new JLabel(StatCollector.translateToLocal("gui.modularfluxfields:fancyGraphicsInfo"), SwingConstants.LEFT);
        frame.getContentPane().add(fancyLabel);

        JButton mediocreButton = new JButton(StatCollector.translateToLocal("gui.modularfluxfields:mediocreGraphics"));
        frame.getContentPane().add(mediocreButton);
        mediocreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MFFSettings.useHighPolyModels = false;
                MFFSettings.useGLSLShaders = true;

                ConfigurationHandler.saveGraphicsConfig(configFile);

                frame.dispose();
            }
        });

        JLabel mediocreLabel = new JLabel(StatCollector.translateToLocal("gui.modularfluxfields:mediocreGraphicsInfo"), SwingConstants.LEFT);
        frame.getContentPane().add(mediocreLabel);

        JButton plainButton = new JButton(StatCollector.translateToLocal("gui.modularfluxfields:plainGraphics"));
        frame.getContentPane().add(plainButton);
        plainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Plain graphics settings should be the defaults in MFFSettings.java
                ConfigurationHandler.saveGraphicsConfig(configFile);

                frame.dispose();
            }
        });

        JLabel plainLabel = new JLabel(StatCollector.translateToLocal("gui.modularfluxfields:plainGraphicsInfo"), SwingConstants.LEFT);
        frame.getContentPane().add(plainLabel);

        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
        frame.toFront();

    }

}
