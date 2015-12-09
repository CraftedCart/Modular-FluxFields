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
    public List<List<String>> permittedPlayers; //List containing lists containing a player's UUID, and the player's username (in that order)
    public List<List<Object>> permissionGroups; //List containing lists of groups containing the group ID and its permissions (in that order)
    private Integer selectedItem; //The selected player on the permitted player list
    private byte guiMode = 0;
    /*
     * Gui Modes
     * 0: Add and manage players
     * 1: Add and manage groups
     */

    //Gui Elements
    private GuiTextField addItemTextField; //The text box to add a player to the permitted players list or to create a new group
    private GuiButton removeButton; //The remove button
    //Gui(ish) Elements
    private String statusMessage; //The status message text

    public GuiFFProjectorSecurity(EntityPlayer player, IInventory playerInv, TEFFProjector te) {
        super(new ContainerFFProjectorInfo(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;
        this.player = player;

        this.xSize = 256;
        this.ySize = 178;

        this.permittedPlayers = this.te.permittedPlayers;
        this.permissionGroups = this.te.permissionGroups;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.addItemTextField = new GuiTextField(0, this.fontRendererObj, 15, 29, 154, 9); //Create text field to add a player to the security system
        addItemTextField.setMaxStringLength(16); //Set the text field's max length to 16 (the max length of a Minecraft username)
        this.addItemTextField.setFocused(true); //Automatically focus the text box by default
        this.buttonList.add(this.removeButton = new GuiButton(0, this.guiLeft + 7, this.guiTop + 62, 64, 20, StatCollector.translateToLocal("gui.mff:remove"))); //Create remove button
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        this.drawDefaultBackground(); //Draw semi-transparent background

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f); //Make sure the GUI isn't tinted
        if (this.te.hasSecurityUpgrade) { //If a security upgrade is installed in the FF Projector
            if (guiMode == 0) { //If it's on add/manage players mode
                this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurityPlayers.png")); //Set the bg image to the players one
            } else if (guiMode == 1) {
                this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurityGroups.png")); //Set the bg image to the groups one
            }
        } else { //It it's on add/manage groups mode
            this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurityNoUpgrade.png")); //Set the bg image to the upgrade warning one
        }
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize); //Draw the bg image

        //Draw power value
        double power = this.te.power; //Get the FF Projector power value

        this.drawRect(this.guiLeft - 24, this.guiTop - 8, this.guiLeft + xSize + 24, this.guiTop - 6, 0xFF212121); //Draw power bar bg
        this.drawRect(this.guiLeft - 24, this.guiTop - 8, (int) (this.guiLeft - 24 + (double) (xSize + 48) * power / PowerConf.ffProjectorMaxPower), this.guiTop - 6, 0xFF2196F3); //Draw power bar fg

        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:power") + ": " + String.format("%012.2f", power) + " / " + String.format("%09.2f", PowerConf.ffProjectorMaxPower) + " FE",
                this.guiLeft - 24, this.guiTop - 18, 0xFAFAFA, false); //Draw power text

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = this.te.getDisplayName().getUnformattedText(); //Get the display name of the FF Projector
        this.fontRendererObj.drawString(s, 75, 4, 0x404040); //Draw block name
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 84, 0x404040); //Draw inventory name

        if (!this.te.hasSecurityUpgrade) { //If a security upgrade isn't installed
            this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securityNoUpgrade"), 15, 29, 0xFAFAFA); //Draw no upgrade warning
        } else {

            if (guiMode == 0) { //If it's on add/manage players mode

                this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securityPlayers"), 15, 18, 0x404040); //Draw tab name

                this.addItemTextField.drawTextBox(); //Draw add item text box
                this.fontRendererObj.drawString(StatCollector.translateToLocal(statusMessage), 15, 40, 0x404040); //Draw status message

                // Draw the list of players //////////////////////////////////////////////////////////////////////////////////
                GL11.glScalef(0.5f, 0.5f, 0.5f); //Used to half the text size                                               //
                Iterator<List<String>> iter = permittedPlayers.iterator();                                                  //
                int index = 0;                                                                                              //
                while (iter.hasNext()) { //Loop through the list of permitted players                                       //
                    int col;                                                                                                //
                    if (selectedItem != null && selectedItem == index) { //If the player it's about to draw is selected     //
                        col = 0x2196F3; //Set the colour to blue                                                            //
                    } else {                                                                                                //
                        col = 0x404040; //Set the colour to grey                                                            //
                    }                                                                                                       //
                    this.fontRendererObj.drawString(iter.next().get(1), 344, 58 + 9 * index, col); //Draw the player's name //
                    index++;                                                                                                //
                }                                                                                                           //
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////

                if (selectedItem == null) { //If there's no selected player on the permitted player's list
                    removeButton.enabled = false; //Disable the remove button
                } else {
                    removeButton.enabled = true; //Enable the remove button
                }

            } else if (guiMode == 1) { //It it's on add/manage groups mode

                this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securityGroups"), 15, 18, 0x404040); //Draw tab name

                this.addItemTextField.drawTextBox(); //Draw add item text box
                this.fontRendererObj.drawString(StatCollector.translateToLocal(statusMessage), 15, 40, 0x404040); //Draw status message

                // Draw the list of groups ///////////////////////////////////////////////////////////////////////////////////////////
                GL11.glScalef(0.5f, 0.5f, 0.5f); //Used to half the text size                                                       //
                Iterator<List<Object>> iter = permissionGroups.iterator();                                                          //
                int index = 0;                                                                                                      //
                while (iter.hasNext()) { //Loop through the list of groups                                                          //
                    int col;                                                                                                        //
                    if (selectedItem != null && selectedItem == index) { //If the group it's about to draw is selected              //
                        col = 0x2196F3; //Set the colour to blue                                                                    //
                    } else {                                                                                                        //
                        col = 0x404040; //Set the colour to grey                                                                    //
                    }                                                                                                               //
                    this.fontRendererObj.drawString((String) iter.next().get(0), 344, 58 + 9 * index, col); //Draw the group's name //
                    index++;                                                                                                        //
                }                                                                                                                   //
                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            }

        }

        GL11.glScalef(2f, 2f, 2f); //Reset scaling (for moving inventory items)
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

        this.addItemTextField.mouseClicked(x, y, btn);

        int x2 = x * 2;
        int y2 = y * 2;
        //Select player in the permitted players list
        if (x2 >= 344 && x2 <= 504 && y2 >= 58) {
            for (int i = 0; i < permittedPlayers.size(); i++) {
                if ((y2 - 58) / 9 <= permittedPlayers.size()) {
                    selectedItem = (y2 - 58) / 9;
                }
            }
        }

        if (x >= 228 && x <= 242 && y >= 148 && y <= 178) {
            //Security groups management button clicked
            selectedItem = null; //Deselect stuff
            guiMode = 1; //Set the Gui mode to groups management mode
        } else if (x >= 243 && x <= 257 && y >= 148 && y <= 178) {
            //Security players management button clicked
            selectedItem = null; //Deselect stuff
            guiMode = 0; //Set the Gui mode to players management mode
        } else if (x >= 1 && x <= 15 && y >= 1 && y <= 13) {
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
            if (selectedItem != null) { //If a player has been selected from the permitted players list
                permittedPlayers.remove((int) selectedItem); //Remove the player from the permitted plaers list
                if (selectedItem - 1 >= 0 && permittedPlayers.size() > 0) { //If there is a player before the one removed
                    selectedItem -= 1; //Select the previous player
                } else if (!(permittedPlayers.size() > 0)) { //If they're no players left on the list
                    selectedItem = null; //Deselect
                }
            }
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.addItemTextField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_RETURN && this.addItemTextField.isFocused()) { //If enter was pressed while typing in the add player text box
            if (guiMode == 0) { //If it's on add/manage players mode

                //Add player to permitted players list

                List<Object> player = PlayerUtils.getUUIDFromPlayerName(addItemTextField.getText()); //Get the player's UUID

                if (player != null) {
                    //That player's online! Add the player to the list it the player's not on there already
                    if (!doesItemNameExistInList(player.get(0).toString(), permittedPlayers)) { //If the player's not already on the permitted players list
                        List playerInfo = new ArrayList<String>();
                        playerInfo.add(player.get(0).toString()); //Add the UUID
                        playerInfo.add(player.get(1)); //Add the username
                        permittedPlayers.add(playerInfo); //Add the UUID and player name to the permitted players list
                        statusMessage = "gui.mff:addedPermittedPlayer"; //Set the status message to the success message
                    } else {
                        statusMessage = "gui.mff:permittedPlayerAlreadyExists"; //Set the status message to the player already exists on the list message
                    }
                } else {
                    //That players not online. Show a warning
                    statusMessage = "gui.mff:playerNotOnline";
                }

            } else if (guiMode == 1) { //It it's on add/manage groups mode

                if (!doesItemNameExistInList(addItemTextField.getText(), permissionGroups)) { //If the player's not already on the permitted players list
                    List groupInfo = new ArrayList<String>();
                    groupInfo.add(addItemTextField.getText()); //Add the Group ID
                    permittedPlayers.add(groupInfo); //Add the Group info the player list
                    statusMessage = "gui.mff:addedNewGroup"; //Set the status message to the success message
                } else {
                    statusMessage = "gui.mff:groupAlreadyExists"; //Set the status message to the group already exists on the list message
                }

            }

            //Clear text field
            this.addItemTextField.setText("");

        } else if (!(keyCode == Keyboard.KEY_E  &&  this.addItemTextField.isFocused()) ){ //Prevent typing E from closing the gui if a text box is focused
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

    private boolean doesItemNameExistInList(String item, List list) { //Check if an item exists in a 2D list (In index 0)

        Iterator<List<String>> iter = list.iterator();
        while (iter.hasNext()) {
            if (item.toLowerCase().equals(iter.next().get(0).toLowerCase())) {
                return true;
            }
        }
        return false;

    }

}
