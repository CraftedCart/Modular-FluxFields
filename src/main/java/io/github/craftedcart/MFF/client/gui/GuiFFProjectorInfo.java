package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.client.gui.guiutils.*;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by CraftedCart on 30/11/2015 (DD/MM/YYYY)
 */

public class GuiFFProjectorInfo extends GuiFFProjectorBase {

    public static int konamiCodeProgress = 0;
    public static boolean upKeyDown = false;
    public static boolean downKeyDown = false;

    public GuiFFProjectorInfo(EntityPlayer player, TEFFProjector te) {
        this.te = te;
        this.player = player;
    }

    @Override
    public void onInit() {
        super.onInit();

        konamiCodeProgress = 0;

        setTitle(StatCollector.translateToLocal("gui.mff:info"));

        final UILabel powerTitleLabel = new UILabel(getWorkspace(),
                "powerTitleLabel",
                new PosXY(24, 24),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        powerTitleLabel.setText(StatCollector.translateToLocal("gui.mff:power"));

        final UILabel powerLabel = new UILabel(getWorkspace(),
                "powerLabel",
                new PosXY(24, 24),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        powerLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerLabel.setText(String.format("%012.2f / %012.2f %s (%06.2f%%)", te.power, te.maxPower, StatCollector.translateToLocal("gui.mff:fe"), te.power / te.maxPower * 100));
            }
        });

        final UIProgressBar powerBar = new UIProgressBar(getWorkspace(),
                "powerBar",
                new PosXY(24, 48),
                new PosXY(-24, 52),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        powerBar.setPanelBackgroundColor(UIColor.matGrey900());
        powerBar.setPanelForegroundColor(UIColor.matBlue());
        powerBar.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerBar.setProgress(te.power / te.maxPower);
            }
        });

        final UILabel powerUsageTitleLabel = new UILabel(getWorkspace(),
                "powerUsageTitleLabel",
                new PosXY(24, 52),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        powerUsageTitleLabel.setText(StatCollector.translateToLocal("gui.mff:powerUsage"));

        final UILabel powerUsageLabel = new UILabel(getWorkspace(),
                "powerUsageLabel",
                new PosXY(24, 52),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        powerUsageLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerUsageLabel.setText(String.format("%.2f %s / %s", te.powerUsage, StatCollector.translateToLocal("gui.mff:fe"), StatCollector.translateToLocal("gui.mff:t")));
            }
        });

        final UILabel sizingTitleLabel = new UILabel(getWorkspace(),
                "sizingTitleLabel",
                new PosXY(24, 76),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        sizingTitleLabel.setText(StatCollector.translateToLocal("gui.mff:sizing"));

        final UILabel sizingLabel = new UILabel(getWorkspace(),
                "sizingLabel",
                new PosXY(24, 76),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        sizingLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                sizingLabel.setText(String.format("XYZ: %d, %d, %d", te.maxX - te.minX + 1, te.maxY - te.minY + 1, te.maxZ - te.minZ + 1));
            }
        });

        final UILabel ownerTitleLabel = new UILabel(getWorkspace(),
                "ownerTitleLabel",
                new PosXY(24, 100),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        ownerTitleLabel.setText(StatCollector.translateToLocal("gui.mff:owner"));

        final UILabel ownerLabel = new UILabel(getWorkspace(),
                "ownerLabel",
                new PosXY(24, 100),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        ownerLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                ownerLabel.setText(te.ownerName);
            }
        });

        final UILabel uptimeTitleLabel = new UILabel(getWorkspace(),
                "ownerTitleLabel",
                new PosXY(24, 124),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        uptimeTitleLabel.setText(StatCollector.translateToLocal("gui.mff:uptime"));

        final UILabel uptimeLabel = new UILabel(getWorkspace(),
                "uptimeLabel",
                new PosXY(24, 124),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        uptimeLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                final int seconds = te.uptime / 20;

                final int hr = seconds / 3600;
                final int rem = seconds % 3600;
                final int mn = rem / 60;
                final int sec = rem % 60;
                final String hrStr = (hr < 10 ? "0" : "") + hr;
                final String mnStr = (mn < 10 ? "0" : "") + mn;
                final String secStr = (sec < 10 ? "0" : "") + sec;

                uptimeLabel.setText(String.format("%s : %s : %s", hrStr, mnStr, secStr));
            }
        });

        //<editor-fold desc="Initialization radial progress bars and labels">
        final UIComponent initGroup = new UIComponent(getWorkspace(),
                "initGroup",
                new PosXY(24, 148),
                new PosXY(-24, -24),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 1));
        initGroup.setPanelBackgroundColor(UIColor.transparent());

        final UIRadialProgressBar initWallsRadialProgBar = new UIRadialProgressBar(initGroup,
                "initWallsRadialProgBar",
                new PosXY(0, 0),
                new PosXY(0, 0),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 1));
        initWallsRadialProgBar.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                initWallsRadialProgBar.setProgress((double) te.blockPlaceProgress / te.wallBlockList.size());
            }
        });

        final UIRadialProgressBar initInnerRadialProgBar = new UIRadialProgressBar(initGroup,
                "initInnerRadialProgBar",
                new PosXY(6, 6),
                new PosXY(-6, -6),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 1));
        initInnerRadialProgBar.setRadialForegroundColor(UIColor.matOrange());
        initInnerRadialProgBar.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                initInnerRadialProgBar.setProgress((double) (te.blockPlaceProgress - te.wallBlockList.size()) / te.innerBlockList.size());
            }
        });

        final UILabel initializingTitleLabel = new UILabel(initGroup,
                "initializingTitleLabel",
                new PosXY(0, -35),
                new AnchorPoint(0.5, 0.5),
                GuiUtils.font);
        initializingTitleLabel.setHorizontalAlign(0);
        initializingTitleLabel.setText(StatCollector.translateToLocal("gui.mff:initializing"));

        final UILabel remainingLabel = new UILabel(initGroup,
                "initRemainingLabel",
                new PosXY(0, -12),
                new AnchorPoint(0.5, 0.5),
                GuiUtils.font);
        remainingLabel.setHorizontalAlign(0);
        remainingLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                //Calculate setup time remaining
                double calcSecsDiff = (double) ((te.wallBlockList.size() + te.innerBlockList.size()) - te.blockPlaceProgress) / 20;
                int chr = (int) (calcSecsDiff / 3600);
                int crem = (int) (calcSecsDiff % 3600);
                int cmn = crem / 60;
                int csec = crem % 60;
                String chrStr = (chr < 10 ? "0" : "") + chr;
                String cmnStr = (cmn < 10 ? "0" : "") + cmn;
                String csecStr = (csec < 10 ? "0" : "") + csec;

                remainingLabel.setText(String.format("%s : %s : %s", chrStr, cmnStr, csecStr));
            }
        });

        final UILabel initPercentLabel = new UILabel(initGroup,
                "initPercentLabel",
                new PosXY(0, 12),
                new AnchorPoint(0.5, 0.5),
                GuiUtils.font);
        initPercentLabel.setHorizontalAlign(0);
        initPercentLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                initPercentLabel.setText(String.format("%06.2f%%", (double) te.blockPlaceProgress / (te.wallBlockList.size() + te.innerBlockList.size()) * 100));
            }
        });

        initGroup.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                if (te.hasCalcInnerBlocksUpgrade) {
                    initInnerRadialProgBar.setVisible(true);
                } else {
                    initInnerRadialProgBar.setVisible(false);
                }
            }
        });
        //</editor-fold>

        //<editor-fold desc="Open Your Eyes Konami Code Easter Egg (UILabel)">
        final UILabel openYourEyesEasterEggLabel = new UILabel(getWorkspace(),
                "openYourEyesKonamiCodeEasterEggLabel",
                new PosXY(8, -24),
                new AnchorPoint(0, 1),
                GuiUtils.font);
        openYourEyesEasterEggLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                //Up Up
                if (konamiCodeProgress <= 1) {
                    if (!upKeyDown) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                            upKeyDown = true;
                            konamiCodeProgress += 1;
                        }
                    } else {
                        if (!Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                            upKeyDown = false;
                        }
                    }
                }

                //Down Down
                if (konamiCodeProgress == 2 || konamiCodeProgress == 3) {
                    if (!downKeyDown) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                            downKeyDown = true;
                            konamiCodeProgress += 1;
                        }
                    } else {
                        if (!Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                            downKeyDown = false;
                        }
                    }
                }

                //Left - Left
                if ((konamiCodeProgress == 4 || konamiCodeProgress == 6) && Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    konamiCodeProgress += 1;
                }

                //Right - Right
                if ((konamiCodeProgress == 5 || konamiCodeProgress == 7) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    konamiCodeProgress += 1;
                }

                //B
                if ((konamiCodeProgress == 8) && Keyboard.isKeyDown(Keyboard.KEY_B)) {
                    konamiCodeProgress += 1;
                }

                //A
                if ((konamiCodeProgress == 9) && Keyboard.isKeyDown(Keyboard.KEY_A)) {
                    //Play Open Your Eyes (MIDI) by Aviators
                    konamiCodeProgress += 1;

                    try {
                        if (GuiUtils.sequencer != null) {

                            GuiUtils.drawString(GuiUtils.font, sidebarWidth + 8, Display.getHeight() - 24, StatCollector.translateToLocal("loadingEasterEgg"), UIColor.matGrey900());

                            GuiUtils.sequencer.open();
                            InputStream midiFile = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("mff:eastereggs/AviatorsOpenYourEyesMIDI.mid")).getInputStream();
                            Sequence sequence = MidiSystem.getSequence(midiFile);
                            GuiUtils.sequencer.setSequence(sequence);
                            GuiUtils.sequencer.setTempoInBPM(125);
                            GuiUtils.sequencer.start();

                            openYourEyesEasterEggLabel.setText(StatCollector.translateToLocal("gui.mff:openYourEyesEasterEgg"));

                        }
                    } catch (MidiUnavailableException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidMidiDataException e) {
                        e.printStackTrace();
                    }
                }

                if (konamiCodeProgress == 10) {
                    openYourEyesEasterEggLabel.setText(StatCollector.translateToLocal("gui.mff:openYourEyesEasterEgg") +
                            String.format(" [%06.2f / %06.2f]", GuiUtils.sequencer.getMicrosecondPosition() / 1000000f, GuiUtils.sequencer.getSequence().getMicrosecondLength() / 1000000f));
                }
            }
        });
        //</editor-fold>

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (GuiUtils.sequencer.isRunning()) {
            GuiUtils.sequencer.stop();
            GuiUtils.sequencer.close();
        }
    }
}
