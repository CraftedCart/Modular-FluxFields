package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.container.ContainerFFProjectorInfo;
import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageFFProjectorGuiSaveSecurity;
import io.github.craftedcart.MFF.network.MessageRequestOpenGui;
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
    private List<List<String>> permittedPlayers = new ArrayList<List<String>>(); //List containing lists containing a player's UUID, the player's username, and the player's group ID (in that order)
    private List<List<Object>> permissionGroups = new ArrayList<List<Object>>(); //List containing lists of groups containing the group ID and its permissions (in that order)
    /*
     * Perm 1: Boolean: Should players be killed
     */
    private List<Object> generalPermissions = new ArrayList<Object>(); //List containing the general permissions
    /*
     * Perm 1: Boolean: Should hostile mobs be killed
     * Perm 2: Boolean: Should peaceful mobs be killed
     */
    private Integer selectedItem; //The selected player on the permitted player list
    private byte guiMode = 0;
    /*
     * Gui Modes
     * 0: Add and manage players
     * 1: Add and manage groups
     * 2: Add a player to a group
     * 3: Set group permissions
     * 4: Set general permissions
     */

    //Gui Elements
    private GuiTextField addItemTextField; //The text box to add a player to the permitted players list or to create a new group
    private GuiButton removeButton; //The remove button
    private GuiButton addToGroupButton; //The add a player to a group button
    private GuiButton setGroupPermsButton; //The set group permissions to a group button
    //Gui(ish) Elements
    private String statusMessage; //The status message text

    public GuiFFProjectorSecurity(EntityPlayer player, IInventory playerInv, TEFFProjector te) {
        super(new ContainerFFProjectorInfo(playerInv, te));

        this.playerInv = playerInv;
        this.te = te;
        this.player = player;

        this.xSize = 256;
        this.ySize = 178;

        this.permittedPlayers.addAll(this.te.permittedPlayers);
        this.permissionGroups.addAll(this.te.permissionGroups);
        this.generalPermissions.addAll(this.te.generalPermissions);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.addItemTextField = new GuiTextField(0, this.fontRendererObj, 15, 29, 154, 9); //Create text field to add a player to the security system
        addItemTextField.setMaxStringLength(16); //Set the text field's max length to 16 (the max length of a Minecraft username)
        this.addItemTextField.setFocused(true); //Automatically focus the text box by default
        buttonList.add(this.removeButton = new GuiButton(0, this.guiLeft + 7, this.guiTop + 62, 64, 20, StatCollector.translateToLocal("gui.mff:remove"))); //Create remove button
        buttonList.add(this.addToGroupButton = new GuiButton(1, this.guiLeft + 73, this.guiTop + 62, 72, 20, StatCollector.translateToLocal("gui.mff:addToGroup"))); //Create add player to group button
        buttonList.add(this.setGroupPermsButton = new GuiButton(1, this.guiLeft + 73, this.guiTop + 62, 72, 20, StatCollector.translateToLocal("gui.mff:setPerms"))); //Create add player to group button
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        this.drawDefaultBackground(); //Draw semi-transparent background

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f); //Make sure the GUI isn't tinted

        if (this.te.hasSecurityUpgrade) { //If a security upgrade is installed in the FF Projector
            if (guiMode == 0) { //If it's on add/manage players mode
                this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurityPlayers.png")); //Set the bg image to the players one
            } else if (guiMode == 1) { //If it's on add/manage groups mode
                this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurityGroups.png")); //Set the bg image to the groups one
            } else if (guiMode == 2) { //If it's on add player to group mode
                this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurityAddToGroup.png")); //Set the bg image to the add player to group one
            } else if (guiMode == 3) { //If it's on set group perms mode
                this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecuritySetGroupPerms.png")); //Set the bg image to the add player to group one
            } else if (guiMode == 4) { //If it's on set general perms mode
                this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecuritySetGeneralPerms.png")); //Set the bg image to the add player to group one
            }

        } else { //If no security upgrade is installed
            this.mc.getTextureManager().bindTexture(new ResourceLocation("mff:textures/gui/container/ffProjectorSecurityNoUpgrade.png")); //Set the bg image to the upgrade warning one
        }
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize); //Draw the bg image

        //Draw power value
        double power = this.te.power;
        double maxPower = this.te.maxPower;
        double powerUsage = this.te.powerUsage;

        drawRect(this.guiLeft, this.guiTop - 8, this.guiLeft + xSize, this.guiTop - 6, 0xFF212121);
        drawRect(this.guiLeft, this.guiTop - 8, (int) (this.guiLeft + (double) xSize * power / maxPower), this.guiTop - 6, 0xFF2196F3);

        this.fontRendererObj.drawString(
                String.format("%s: %012.2f / %09.0f %s (%.2f %s / t)",
                        StatCollector.translateToLocal("gui.mff:power"), power, maxPower, StatCollector.translateToLocal("gui.mff:fe"), powerUsage, StatCollector.translateToLocal("gui.mff:fe")),
                guiLeft, guiTop - 18, 0xFAFAFA, false);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = this.te.getDisplayName().getUnformattedText(); //Get the display name of the FF Projector
        this.fontRendererObj.drawString(s, 75, 4, 0x404040); //Draw block name
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 84, 0x404040); //Draw inventory name

        if (!this.te.hasSecurityUpgrade) { //If a security upgrade isn't installed
            this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securityNoUpgrade"), 15, 29, 0xFAFAFA); //Draw no upgrade warning
            this.removeButton.visible = false;
            this.addToGroupButton.visible = false;
            this.setGroupPermsButton.visible = false;
        } else {

            try { //I can't figure out why I get an IndexOutOfBoundsException occasionally, so here's a hacky way of preventing it

                if (guiMode == 0) { //If it's on add/manage players mode

                    //<editor-fold desc="Add/Manage players mode">
                    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securityPlayers"), 15, 18, 0x404040); //Draw tab name

                    this.addItemTextField.drawTextBox(); //Draw add item text box
                    this.fontRendererObj.drawString(StatCollector.translateToLocal(statusMessage), 15, 40, 0x404040); //Draw status message

                    //<editor-fold desc="Draw the list of players">
                    GL11.glScalef(0.5f, 0.5f, 0.5f); //Used to half the text size
                    Iterator<List<String>> iter = permittedPlayers.iterator();
                    int index = 0;
                    while (iter.hasNext()) { //Loop through the list of permitted players
                        int col;
                        if (selectedItem != null && selectedItem == index) { //If the player it's about to draw is selected
                            col = 0x2196F3; //Set the colour to blue
                        } else {
                            col = 0x404040; //Set the colour to grey
                        }
                        this.fontRendererObj.drawString(iter.next().get(1), 344, 58 + 9 * index, col); //Draw the player's name
                        index++;
                    }
                    //</editor-fold>

                    GL11.glScalef(2f, 2f, 2f); //Reset scaling

                    if (selectedItem == null) { //If there's no selected player on the permitted player's list
                        removeButton.enabled = false; //Disable the remove button
                        addToGroupButton.enabled = false; //Disable the add to group button
                    } else {
                        removeButton.enabled = true; //Enable the remove button
                        addToGroupButton.enabled = true; //Enable the add to group button
                        this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:group") + ": " + StatCollector.translateToLocal(permittedPlayers.get(selectedItem).get(2)), 15, 51, 0x404040); //Draw selected player's group
                    }

                    this.removeButton.visible = true;
                    this.addToGroupButton.visible = true;
                    this.setGroupPermsButton.visible = false;
                    //</editor-fold>

                } else if (guiMode == 1) { //If it's on add/manage groups mode

                    //<editor-fold desc="Add/Manage groups mode">
                    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securityGroups"), 15, 18, 0x404040); //Draw tab name

                    this.addItemTextField.drawTextBox(); //Draw add item text box
                    this.fontRendererObj.drawString(StatCollector.translateToLocal(statusMessage), 15, 40, 0x404040); //Draw status message

                    //<editor-fold desc="Draw the list of groups">
                    GL11.glScalef(0.5f, 0.5f, 0.5f); //Used to half the text size
                    Iterator<List<Object>> iter = permissionGroups.iterator();
                    int index = 0;
                    while (iter.hasNext()) { //Loop through the list of groups
                        int col;
                        if (selectedItem != null && selectedItem == index) { //If the group it's about to draw is selected
                            col = 0x2196F3; //Set the colour to blue
                        } else {
                            col = 0x404040; //Set the colour to grey
                        }
                        this.fontRendererObj.drawString(StatCollector.translateToLocal((String) iter.next().get(0)), 344, 58 + 9 * index, col); //Draw the group's name
                        index++;
                    }
                    //</editor-fold>

                    GL11.glScalef(2f, 2f, 2f); //Reset scaling

                    if (selectedItem == null) { //If there's no selected group on the permitted player's list
                        removeButton.enabled = false; //Disable the remove button
                        setGroupPermsButton.enabled = false; //Disable the set group perms button

                    } else {
                        setGroupPermsButton.enabled = true; //Enable the set group perms button
                        if (!permissionGroups.get(selectedItem).get(0).equals("gui.mff:everyone")) { //If the selected group is not Everyone
                            removeButton.enabled = true; //Enable the remove button
                        } else {
                            removeButton.enabled = false; //Disable the remove button
                        }
                    }

                    this.removeButton.visible = true;
                    this.addToGroupButton.visible = false;
                    this.setGroupPermsButton.visible = true;
                    //</editor-fold>

                } else if (guiMode == 2) { //If it's on add player to group mode

                    //<editor-fold desc="Add player to group mode">
                    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securityAddToGroup"), 15, 18, 0x404040); //Draw tab name

                    this.fontRendererObj.drawString(StatCollector.translateToLocal(statusMessage), 15, 27, 0x404040); //Draw status message

                    //<editor-fold desc="Draw the list of groups">
                    GL11.glScalef(0.5f, 0.5f, 0.5f); //Used to half the text size
                    Iterator<List<Object>> iter = permissionGroups.iterator();
                    int index = 0;
                    while (iter.hasNext()) { //Loop through the list of groups
                        this.fontRendererObj.drawString(StatCollector.translateToLocal((String) iter.next().get(0)), 344, 58 + 9 * index, 0x404040); //Draw the group's name
                        index++;
                    }
                    //</editor-fold>

                    GL11.glScalef(2f, 2f, 2f); //Reset scaling

                    this.removeButton.visible = false;
                    this.addToGroupButton.visible = false;
                    this.setGroupPermsButton.visible = false;
                    //</editor-fold>

                } else if (guiMode == 3) { //If it's on set group permissions mode

                    //<editor-fold desc="Set group permissions mode">
                    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securitySetGroupPerms"), 15, 18, 0x404040); //Draw tab name

                    this.fontRendererObj.drawString(StatCollector.translateToLocal(statusMessage), 15, 27, 0x404040); //Draw status message

                    int mX = mouseX - guiLeft;
                    int mY = mouseY - guiTop;

                    //<editor-fold desc="Damage players">
                    if ((Boolean) permissionGroups.get(selectedItem).get(1)) {
                        drawRect(6, 57, 22, 65, 0xFF4CAF50);
                    } else {
                        drawRect(6, 57, 22, 65, 0xFFF44336);
                    }

                    if (mX >= 6 && mX <= 22 && mY >= 37 && mY <= 53) { //Draw kill players mobs tooltip
                        List<String> tooltip = new ArrayList<String>();
                        tooltip.add(StatCollector.translateToLocal("gui.mff:securityDamagePlayers"));
                        this.drawHoveringText(tooltip, mX, mY, this.fontRendererObj);
                    }
                    //</editor-fold>

                    this.removeButton.visible = false;
                    this.addToGroupButton.visible = false;
                    this.setGroupPermsButton.visible = false;
                    //</editor-fold>

                } else if (guiMode == 4) { //If it's on set general perms mode

                    //<editor-fold desc="Set general permissions mode">
                    this.fontRendererObj.drawString(StatCollector.translateToLocal("gui.mff:securitySetGeneralPerms"), 15, 18, 0x404040); //Draw tab name

                    int mX = mouseX - guiLeft;
                    int mY = mouseY - guiTop;

                    //<editor-fold desc="Damage hostile mobs">
                    if ((Boolean) generalPermissions.get(0)) {
                        drawRect(6, 49, 22, 57, 0xFF4CAF50);
                    } else {
                        drawRect(6, 49, 22, 57, 0xFFF44336);
                    }

                    if (mX >= 6 && mX <= 22 && mY >= 29 && mY <= 45) { //Draw kill hostile mobs tooltip
                        List<String> tooltip = new ArrayList<String>();
                        tooltip.add(StatCollector.translateToLocal("gui.mff:securityDamageHostileMobs"));
                        this.drawHoveringText(tooltip, mX, mY, this.fontRendererObj);
                    }
                    //</editor-fold>

                    //<editor-fold desc="Damage peaceful mobs">
                    if ((Boolean) generalPermissions.get(1)) {
                        drawRect(26, 49, 42, 57, 0xFF4CAF50);
                    } else {
                        drawRect(26, 49, 42, 57, 0xFFF44336);
                    }

                    if (mX >= 26 && mX <= 42 && mY >= 29 && mY <= 45) { //Draw kill hostile mobs tooltip
                        List<String> tooltip = new ArrayList<String>();
                        tooltip.add(StatCollector.translateToLocal("gui.mff:securityDamagePeacefulMobs"));
                        this.drawHoveringText(tooltip, mX, mY, this.fontRendererObj);
                    }
                    //</editor-fold>

                    this.removeButton.visible = false;
                    this.addToGroupButton.visible = false;
                    this.setGroupPermsButton.visible = false;

                }

            } catch (Exception e) {
                e.printStackTrace();
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

        this.addItemTextField.mouseClicked(x, y, btn);

        int x2 = x * 2;
        int y2 = y * 2;
        //Select player in the currently displayed list
        if (guiMode == 0) { //If it's on manage players mode
            if (x2 >= 344 && x2 <= 504 && y2 >= 58) {
                for (int i = 0; i < permittedPlayers.size(); i++) {
                    if ((y2 - 58) / 9 <= permittedPlayers.size()) {
                        selectedItem = (y2 - 58) / 9;
                    }
                }
            }
        } else if (guiMode == 1) { //If it's on manage groups mode
            if (x2 >= 344 && x2 <= 504 && y2 >= 58) {
                for (int i = 0; i < permissionGroups.size(); i++) {
                    if ((y2 - 58) / 9 <= permissionGroups.size()) {
                        selectedItem = (y2 - 58) / 9;
                    }
                }
            }
        } else if (guiMode == 2) { //If it's on add player to group mode
            if (x2 >= 344 && x2 <= 504 && y2 >= 58) {
                for (int i = 0; i < permissionGroups.size(); i++) {
                    if ((y2 - 58) / 9 <= permissionGroups.size()) {
                        int groupID = (y2 - 58) / 9;
                        permittedPlayers.get(selectedItem).set(2, (String) permissionGroups.get(groupID).get(0)); //Set the selected player's group
                        guiMode = 0; //Set the gui mode to the add/manage players mode
                    }
                }
            }
        } else if (guiMode == 3) { //If it's on set group perms mode

            if (x >= 6 && x <= 22 && y >= 57 && y <= 65) { //Toggle the player kill mode
                permissionGroups.get(selectedItem).set(1, !((Boolean) permissionGroups.get(selectedItem).get(1)));
            }

        } else if (guiMode == 4) { //If it's on set general perms mode

            if (x >= 6 && x <= 22 && y >= 49 && y <= 57) { //Toggle the hostile mobs kill mode
                generalPermissions.set(0, !((Boolean) generalPermissions.get(0)));
            } else if (x >= 26 && x <= 42 && y >= 49 && y <= 57) { //Toggle the peaceful mobs kill mode
                generalPermissions.set(1, !((Boolean) generalPermissions.get(1)));
            }

        }

        if (x >= 214 && x <= 227 && y >= 148 && y <= 178) {
            //General permissions management button clicked
            selectedItem = null; //Deselect stuff
            guiMode = 4; //Set the Gui mode to general perms management mode
        } else if (x >= 228 && x <= 242 && y >= 148 && y <= 178) {
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

        if (button == removeButton) { //Player clicked on remove

            if (guiMode == 0) { //If it's on player management mode

                if (selectedItem != null) { //If a player has been selected from the permitted players list
                    permittedPlayers.remove((int) selectedItem); //Remove the player from the permitted players list
                    if (selectedItem - 1 >= 0 && permittedPlayers.size() > 0) { //If there is a player before the one removed
                        selectedItem -= 1; //Select the previous player
                    } else if (!(permittedPlayers.size() > 0)) { //If they're no players left on the list
                        selectedItem = null; //Deselect
                    }
                }

            } else if (guiMode == 1) { //If it's on group management mode

                if (selectedItem != null) { //If a group has been selected from the permitted players list

                    //Set all players on that group's group to everyone
                    Iterator<List<String>> iter = permittedPlayers.iterator();
                    int index = 0;
                    while (iter.hasNext()) { //Loop through the list of permitted players
                        if (iter.next().get(2).equals(permissionGroups.get(selectedItem).get(0))) { //If that player's on the group about to be removed
                            permittedPlayers.get(index).set(2, "gui.mff:everyone"); //Set its group to everyone
                        }
                        index++;
                    }

                    permissionGroups.remove((int) selectedItem); //Remove the group from the permission groups list

                    if (selectedItem - 1 >= 0 && permissionGroups.size() > 0) { //If there is a group before the one removed
                        selectedItem -= 1; //Select the previous group
                    } else if (!(permissionGroups.size() > 0)) { //If they're no groups left on the list (This should never happen)
                        selectedItem = null; //Deselect
                    }
                }

            }
        } else if (button == addToGroupButton) { //Player clicked on add player to group

            guiMode = 2; //Set GuiMode to add to group mode
            statusMessage = permittedPlayers.get(selectedItem).get(1);

        } else if (button == setGroupPermsButton) {

            guiMode = 3; //Set GuiMode to edit perms mode
            statusMessage = permissionGroups.get(selectedItem).get(0).toString();

            //Do checks in case an update adds more permissions
            if (permissionGroups.get(selectedItem).size() < 2) {
                permissionGroups.get(selectedItem).add(false); //Perm 1: Should players be killed
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
                        playerInfo.add("gui.mff:everyone"); //Add the group ID
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

                if (addItemTextField.getText().matches(".*\\w.*")) { //If it contains ASCII
                    if (!doesItemNameExistInList(addItemTextField.getText(), permissionGroups)) { //If the player's not already on the permitted players list
                        List groupInfo = new ArrayList<Object>();
                        groupInfo.add(addItemTextField.getText()); //Add the Group ID
                        groupInfo.add(false); //Perm 1: Don't auto kill players
                        permissionGroups.add(groupInfo); //Add the Group info the player list
                        statusMessage = "gui.mff:addedNewGroup"; //Set the status message to the success message
                    } else {
                        statusMessage = "gui.mff:groupAlreadyExists"; //Set the status message to the group already exists on the list message
                    }
                } else {
                    statusMessage = "gui.mff:chooseAName"; //Set the status message to the choose a name message
                }

            }

            //Clear text field
            this.addItemTextField.setText("");

        } else if (!this.addItemTextField.isFocused() || keyCode == 1){ //Prevent typing any keys (apart from ESC) from closing the gui if the text box is focused
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

        if (this.te.hasSecurityUpgrade) {
            NetworkHandler.network.sendToServer(new MessageFFProjectorGuiSaveSecurity(this.te.getPos(), permittedPlayers, permissionGroups, generalPermissions));
        }

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
