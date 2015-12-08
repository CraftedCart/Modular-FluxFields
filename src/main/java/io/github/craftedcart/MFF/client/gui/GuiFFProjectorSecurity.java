package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.container.ContainerFFProjectorInfo;
import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageRequestOpenGui;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import io.github.craftedcart.MFF.utility.PlayerUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CraftedCart on 06/12/2015 (DD/MM/YYYY)
 */

public class GuiFFProjectorSecurity extends GuiContainer {

    private IInventory playerInv;
    private TEFFProjector te;
    private EntityPlayer player;
    public List<List<String>> permittedPlayers = new ArrayList<List<String>>();
    private Integer selectedPlayer;
    private byte guiMode = 0;
    /*
     * Gui Modes
     * 0: Add and manage players
     * 1: Add and manage groups
     */

    //Gui Elements
    private GuiTextField addPlayerTextField;
    private GuiTextField addGroupTextField;
    private GuiButton removeButton;
    //Gui(ish) Elements
    private String statusMessage;

    public GuiFFProjectorSecurity(EntityPlayer player, IInventory playerInv, TEFFProjector te) {
        super(new ContainerFFProjectorInfo(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;
        this.player = player;

        this.xSize = 256;
        this.ySize = 178;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.addPlayerTextField = new GuiTextField(0, this.fontRendererObj, 15, 29, 154, 9);
        addPlayerTextField.setMaxStringLength(16);
        this.addPlayerTextField.setFocused(true);
        this.buttonList.add(this.removeButton = new GuiButton(0, this.guiLeft + 7, this.guiTop + 62, 64, 20, StatCollector.translateToLocal("gui.mff:remove")));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        this.drawDefaultBackground();

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.te.hasSecurityUpgrade) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurity.png"));
        } else {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurityNoUpgrade.png"));
        }
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        //Draw power value
        double power = this.te.power;

        this.drawRect(this.guiLeft - 24, this.guiTop - 8, this.guiLeft + xSize + 24, this.guiTop - 6, 0xFF212121);
        this.drawRect(this.guiLeft - 24, this.guiTop - 8, (int) (this.guiLeft - 24 + (double) (xSize + 48) * power / PowerConf.ffProjectorMaxPower), this.guiTop - 6, 0xFF2196F3);

        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:power") + ": " + String.format("%012.2f", power) + " / " + String.format("%09.2f", PowerConf.ffProjectorMaxPower) + " FE",
                this.guiLeft - 24, this.guiTop - 18, 0xFAFAFA, false);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = this.te.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, 75, 4, 0x404040); //Draw block name
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 84, 0x404040); //Draw inventory name
        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:security"), 15, 18, 0x404040); //Draw tab name

        if (!this.te.hasSecurityUpgrade) {
            this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securityNoUpgrade"), 15, 29, 0xFAFAFA); //Draw no upgrade warning
        } else {

            if (guiMode == 0) { //If it's on add/manage players mode

                this.addPlayerTextField.drawTextBox(); //Draw add player text box
                this.fontRendererObj.drawString(StatCollector.translateToLocal(statusMessage), 15, 40, 0x404040); //Draw status message

                GL11.glScalef(0.5f, 0.5f, 0.5f);
                Iterator<List<String>> iter = permittedPlayers.iterator();
                int index = 0;
                while (iter.hasNext()) {
                    int col;
                    if (selectedPlayer != null && selectedPlayer == index) {
                        col = 0x2196F3;
                    } else {
                        col = 0x404040;
                    }
                    this.fontRendererObj.drawString(iter.next().get(1), 344, 58 + 9 * index, col);
                    index++;
                }

                if (selectedPlayer == null) {
                    removeButton.enabled = false;
                } else {
                    removeButton.enabled = true;
                }

            } else if (guiMode == 1) {

                //TODO

            }

        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

    }

    @Override
    protected void mouseClicked(int x, int y, int btn) {

        try {
            super.mouseClicked(x, y, btn);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Minus offsets
        x -= this.guiLeft;
        y -= this.guiTop;

        this.addPlayerTextField.mouseClicked(x, y, btn);

        int x2 = x * 2;
        int y2 = y * 2;
        //Select player
        if (x2 >= 344 && x2 <= 504 && y2 >= 58) {
            for (int i = 0; i < permittedPlayers.size(); i++) {
                if ((y2 - 58) / 9 <= permittedPlayers.size()) {
                    selectedPlayer = (y2 - 58) / 9;
                }
            }
        }

        if (x >= 1 && x <= 15 && y >= 1 && y <= 13) {
            //Info button clicked
            NetworkHandler.network.sendToServer(new MessageRequestOpenGui(this.te.getPos(), player, GuiHandler.FFProjector_Info_TILE_ENTITY_GUI));
        } else if (x >= 16 && x <= 29 && y >= 1 && y <= 13) {
            //Sizing button clicked
            NetworkHandler.network.sendToServer(new MessageRequestOpenGui(this.te.getPos(), player, GuiHandler.FFProjector_Sizing_TILE_ENTITY_GUI));
        } else if (x >= 30 && x <= 43 && y >= 1 && y <= 13) {
            //Security button clicked
            //NO-OP, We're already on the security tab
        } else if (x >= 44 && x <= 57 && y >= 1 && y <= 13) {
            //Upgrades button clicked
        } else if (x >= 58 && x <= 71 && y >= 1 && y <= 13) {
            //Power Usage button clicked
        }

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        if (button == removeButton) {
            //Player clicked on remove
            if (selectedPlayer != null) {
                permittedPlayers.remove((int) selectedPlayer);
                if (selectedPlayer - 1 >= 0 && permittedPlayers.size() > 0) {
                    selectedPlayer -= 1;
                } else if (!(permittedPlayers.size() > 0)) {
                    selectedPlayer = null;
                }
            }
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.addPlayerTextField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_RETURN && this.addPlayerTextField.isFocused()) {
            //Add player to permitted players list

            List<Object> player = PlayerUtils.getUUIDFromPlayerName(addPlayerTextField.getText());

            if (player != null) {
                //That player's online! Add the player to the list it the player's not on there already
                if (!doesUUIDExistInPermittedPlayers(player.get(0).toString())) {
                    List playerInfo = new ArrayList<String>();
                    playerInfo.add(player.get(0).toString());
                    playerInfo.add(player.get(1));
                    permittedPlayers.add(playerInfo);
                    statusMessage = "gui.mff:addedPermittedPlayer";
                } else {
                    statusMessage = "gui.mff:permittedPlayerAlreadyExists";
                }
            } else {
                //That players not online. Show a warning
                statusMessage = "gui.mff:playerNotOnline";
            }
            //Clear text field
            this.addPlayerTextField.setText("");
        } else if (!(keyCode == Keyboard.KEY_E  &&  this.addPlayerTextField.isFocused()) ){
            try {
                super.keyTyped(typedChar, keyCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();

        //TODO
        //NetworkHandler.network.sendToServer(new SomethingOrOther());

    }

    private boolean doesUUIDExistInPermittedPlayers(String UUID) {

        Iterator<List<String>> iter = permittedPlayers.iterator();
        while (iter.hasNext()) {
            if (UUID.equals(iter.next().get(0))) {
                return true;
            }
        }
        return false;

    }

}
