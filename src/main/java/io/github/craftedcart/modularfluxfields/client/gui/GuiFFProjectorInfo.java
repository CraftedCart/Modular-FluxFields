package io.github.craftedcart.modularfluxfields.client.gui;

import io.github.craftedcart.modularfluxfields.client.gui.guiutils.*;
import io.github.craftedcart.modularfluxfields.tileentity.TEFFProjector;
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

    private MetaEventListener midiMetaEventListener;

    public GuiFFProjectorInfo(EntityPlayer player, TEFFProjector te) {
        this.te = te;
        this.player = player;
    }

    @Override
    public void onInit() {
        super.onInit();

        konamiCodeProgress = 0;

        setTitle(StatCollector.translateToLocal("gui.modularfluxfields:info"));

        final UILabel powerTitleLabel = new UILabel(getWorkspace(),
                "powerTitleLabel",
                new PosXY(24, 24),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        powerTitleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:power"));

        final UILabel powerLabel = new UILabel(getWorkspace(),
                "powerLabel",
                new PosXY(24, 24),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        powerLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerLabel.setText(String.format("%012.2f / %012.2f %s (%06.2f%%)", te.power, te.maxPower, StatCollector.translateToLocal("gui.modularfluxfields:fe"), te.power / te.maxPower * 100));
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
        powerUsageTitleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:powerUsage"));

        final UILabel powerUsageLabel = new UILabel(getWorkspace(),
                "powerUsageLabel",
                new PosXY(24, 52),
                new AnchorPoint(0.3, 0),
                GuiUtils.font);
        powerUsageLabel.setOnUpdateAction(new UIAction() {
            @Override
            public void execute() {
                powerUsageLabel.setText(String.format("%.2f %s / %s", te.powerUsage, StatCollector.translateToLocal("gui.modularfluxfields:fe"), StatCollector.translateToLocal("gui.modularfluxfields:t")));
            }
        });

        final UILabel sizingTitleLabel = new UILabel(getWorkspace(),
                "sizingTitleLabel",
                new PosXY(24, 76),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        sizingTitleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:sizing"));

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
        ownerTitleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:owner"));

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
        uptimeTitleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:uptime"));

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
        initializingTitleLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:initializing"));

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
                            InputStream midiFile = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("modularfluxfields:eastereggs/AviatorsOpenYourEyesMIDI.mid")).getInputStream();
                            Sequence originalSequence = MidiSystem.getSequence(midiFile);
                            Track[] tracks = originalSequence.getTracks();
                            Sequence sequence = new Sequence(originalSequence.getDivisionType(), originalSequence.getResolution());

                            for (Track track : tracks) {
                                Track newTrack = sequence.createTrack();
                                addNotesToTrack(track, newTrack);
                            }

                            GuiUtils.sequencer.addMetaEventListener(midiMetaEventListener);

                            GuiUtils.sequencer.setSequence(sequence);
                            GuiUtils.sequencer.setTempoInBPM(125);
                            GuiUtils.sequencer.start();

                            openYourEyesEasterEggLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:openYourEyesEasterEgg"));

                        }
                    } catch (MidiUnavailableException | IOException | InvalidMidiDataException e) {
                        e.printStackTrace();
                    }
                }

                if (konamiCodeProgress == 10) {
                    if (GuiUtils.sequencer.getSequence() != null) { //Prevent crash
                        openYourEyesEasterEggLabel.setText(StatCollector.translateToLocal("gui.modularfluxfields:openYourEyesEasterEgg") +
                                String.format(" [%06.2f / %06.2f]", GuiUtils.sequencer.getMicrosecondPosition() / 1000000f, GuiUtils.sequencer.getSequence().getMicrosecondLength() / 1000000f));
                    }
                }
            }
        });

        midiMetaEventListener = new MetaEventListener() {
            @Override
            public void meta(MetaMessage meta) {
                final UIComponent vizComponent = new UIComponent(getWorkspace(),
                        "vizComponent (" + String.valueOf(meta.getMessage()[4]) + ")",
                        new PosXY(meta.getMessage()[4] * 10, -48),
                        new PosXY(meta.getMessage()[4] * 10 + 10, -24),
                        new AnchorPoint(0, 1),
                        new AnchorPoint(0, 1));
                vizComponent.setPanelBackgroundColor(UIColor.matBlue());

                final VizData vizData = new VizData();

                vizComponent.setOnUpdateAction(new UIAction() {
                    @Override
                    public void execute() {
                        vizData.lifetime -= GuiUtils.getDelta() / 10;
                        vizComponent.setPanelBackgroundColor(UIColor.matBlue(vizData.lifetime));
                        vizComponent.setTopLeftAnchor(new AnchorPoint(0, vizData.lifetime ));
                        vizComponent.setBottomRightAnchor(new AnchorPoint(0, vizData.lifetime));
                        if (vizData.lifetime <= 0) {
                            vizComponent.parentComponent.childUiComponents.set(vizComponent.componentID, null); //Destroy self
                        }
                    }
                });
            }
        };
        //</editor-fold>

    }

    /** Iterates the MIDI events of the first track and if they are a
     * NOTE_ON or NOTE_OFF message, adds them to the second track as a
     * Meta event. */
    private static void addNotesToTrack(Track track, Track trk) throws InvalidMidiDataException {
        for (int ii = 0; ii < track.size(); ii++) {
            MidiEvent me = track.get(ii);
            MidiMessage mm = me.getMessage();
            if (mm instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) mm;
                int command = sm.getCommand();
                int com = -1;
                if (command == ShortMessage.NOTE_ON) {
                    com = 1;
                } else if (command == ShortMessage.NOTE_OFF) {
                    com = 2;
                }
                if (com > 0) {
                    byte[] b = sm.getMessage();
                    int l = (b == null ? 0 : b.length);
                    MetaMessage metaMessage = new MetaMessage(com, b, l);
                    MidiEvent me2 = new MidiEvent(metaMessage, me.getTick());
                    trk.add(me2);
                }
            }
            trk.add(me);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (GuiUtils.sequencer.isRunning()) {
            GuiUtils.sequencer.stop();
            GuiUtils.sequencer.removeMetaEventListener(midiMetaEventListener);
            GuiUtils.sequencer.close();
        }
    }

    private class VizData {
        double lifetime = 1;
    }

}
