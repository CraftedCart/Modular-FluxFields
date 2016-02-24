package io.github.craftedcart.MFF.client.gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.craftedcart.MFF.client.gui.guiutils.*;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import io.github.craftedcart.MFF.utility.DependencyUtils;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * Created by CraftedCart on 07/02/2016 (DD/MM/YYYY)
 */

public class GuiFFProjectorSecurity extends GuiFFProjectorBase {

    private Map<String, String[]> permittedPlayers = new HashMap<String, String[]>(); //Map containing players | Key: UUID, String[]: Player name, Group ID
    private Map<String, Map<String, Boolean>> permissionGroups = new HashMap<String, Map<String, Boolean>>();
    //Map containing permission groups | Key: Group ID, Map<String, Boolean>: Permissions assigned for that group
    /*
     * Perm 1: Should players be killed
     */
    private UINotificationManager notificationManager;
    protected List<String> selectedPlayerUUIDs = new ArrayList<String>();
    protected List<String> selectedGroupIDs = new ArrayList<String>();
    protected PlayerData aggregateSelectedPlayerData;
    protected GroupData aggregateSelectedGroupData;

    private Map<String, BufferedImage> bodyImageCache = new HashMap<String, BufferedImage>();

    protected UIComponent playerOptionsBodyImage;
    protected UILabel playerOptionsNameLabel;
    protected UILabel playerOptionsUUIDLabel;
    protected UILabel playerOptionsGroupLabel;
    protected UILabel groupNameLabel;

    /**
     * <b>Array possibilities:</b><br>
     * UndoAction addPlayer, String playerUUID<br>
     * UndoAction addGroup, String groupName<br>
     * UndoAction playersGroupChanged, List&lt;String&gt; selectedPlayerUUIDs, Map&lt;String, String[]&gt; permittedPlayers<br>
     * UndoAction removePlayers, Map&lt;String, String[]&gt; Map of the permitted player entries that were removed<br>
     * UndoAction removeGroups, Map&lt;Map&lt;String, Map&lt;String, Boolean&gt;&gt;, String[]&gt; (Key: The group data map, Value: The removed players)<br>
     * UndoAction renameGroups, Map&lt;String, String&gt; (Key: The new name of the group, Value: The previous name of the group)
     */
    private List<Object[]> undoHistory = new ArrayList<Object[]>();

    protected Map<String, Boolean> permissionGroupDefaultPermissions = new HashMap<String, Boolean>(){{
        put("gui.mff.killPlayers", false);
        put("gui.mff:allowSecurityModification", false);
    }};

    public GuiFFProjectorSecurity(EntityPlayer player, TEFFProjector te) {
        this.te = te;
        this.player = player;
    }

    @Override
    public void onInit() {
        super.onInit();

        aggregateSelectedPlayerData = getAggregatePlayerData(selectedPlayerUUIDs);
        aggregateSelectedGroupData = getAggregateGroupData(selectedGroupIDs);

        setTitle(StatCollector.translateToLocal("gui.mff:security"));

        if (te.hasSecurityUpgrade) {

            final UIComponent undoManager = new UIComponent(getWorkspace(),
                    "undoManager",
                    new PosXY(0, 0),
                    new PosXY(0, 0),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(0, 0));
            //Undo manager onUpdateAction is set later

            //<editor-fold desc="Text Fields & List Boxes">
            final UITextField addPlayerTextField = new UITextField(getWorkspace(),
                    "addPlayerTextField",
                    new PosXY(24, 24),
                    new PosXY(-12, 48),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(0.4, 0));
            addPlayerTextField.setPlaceholderText(StatCollector.translateToLocal("gui.mff:addPlayer"));

            final UITextField addPermGroupTextField = new UITextField(getWorkspace(),
                    "addPermGroupTextField",
                    new PosXY(24, 52),
                    new PosXY(-12, 76),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(0.4, 0));
            addPermGroupTextField.setPlaceholderText(StatCollector.translateToLocal("gui.mff:addPermGroup"));

            final UILabel playersTitleLabel = new UILabel(getWorkspace(),
                    "playersTitleLabel",
                    new PosXY(12, 24),
                    new AnchorPoint(0.4, 0),
                    GuiUtils.font);
            playersTitleLabel.setText(StatCollector.translateToLocal("gui.mff:players"));

            final UIListBox playersListBox = new UIListBox(getWorkspace(),
                    "playersListBox",
                    new PosXY(12, 48),
                    new PosXY(-12, -24),
                    new AnchorPoint(0.4, 0),
                    new AnchorPoint(0.7, 1));

            final UILabel permGroupsTitleLabel = new UILabel(getWorkspace(),
                    "permGroupsTitleLabel",
                    new PosXY(12, 24),
                    new AnchorPoint(0.7, 0),
                    GuiUtils.font);
            permGroupsTitleLabel.setText(StatCollector.translateToLocal("gui.mff:permGroups"));

            final UIListBox permGroupsListBox = new UIListBox(getWorkspace(),
                    "permGroupsListBox",
                    new PosXY(12, 48),
                    new PosXY(-24, -24),
                    new AnchorPoint(0.7, 0),
                    new AnchorPoint(1, 1));

            addPlayerTextField.setOnTabAction(new UIAction() {
                @Override
                public void execute() {
                    getWorkspace().setSelectedComponent(addPermGroupTextField.componentID);
                }
            });
            addPermGroupTextField.setOnTabAction(new UIAction() {
                @Override
                public void execute() {
                    getWorkspace().setSelectedComponent(addPlayerTextField.componentID);
                }
            });

            addPlayerTextField.setOnReturnAction(new UIAction() {
                @Override
                public void execute() {
                    if (!addPlayerTextField.value.isEmpty()) {
                        addPlayer(addPlayerTextField.value, playersListBox);
                        addPlayerTextField.clearValue();
                    }
                }
            });

            addPermGroupTextField.setOnReturnAction(new UIAction() {
                @Override
                public void execute() {
                    if (!addPermGroupTextField.value.isEmpty()) {
                        addGroup(addPermGroupTextField.value, permGroupsListBox);
                        addPermGroupTextField.clearValue();
                    }
                }
            });
            //</editor-fold>

            //<editor-fold desc="Tasks Components & Undo Button">
            final UIComponent tasksComponent = new UIComponent(getWorkspace(),
                    "tasksComponent",
                    new PosXY(24, 80),
                    new PosXY(-12, 104),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(0.4, 0));
            tasksComponent.setPanelBackgroundColor(UIColor.transparent());

            final UIIndeterminateRadialProgressBar tasksProgBar = new UIIndeterminateRadialProgressBar(tasksComponent, //Tasks indeterminate radial progress bar
                    "tasksProgBar",
                    new PosXY(0, 0),
                    new PosXY(24, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(0, 0));

            final UILabel tasksLabel = new UILabel(tasksComponent,
                    "tasksLabel",
                    new PosXY(28, 4),
                    new AnchorPoint(0, 0),
                    GuiUtils.font);

            tasksComponent.setOnUpdateAction(new UIAction() {
                @Override
                public void execute() {
                    if (SecurityTaskManager.runningTasks == 0) {
                        tasksProgBar.setVisible(false);
                        tasksLabel.setVisible(false);
                    } else {
                        tasksProgBar.setVisible(true);
                        tasksLabel.setVisible(true);
                        tasksLabel.setText(String.format("%s: %d", StatCollector.translateToLocal("gui.mff:runningTasks"), SecurityTaskManager.runningTasks));
                    }
                }
            });

            final UITextButton undoButton = new UITextButton(tasksComponent,
                    "undoButton",
                    new PosXY(-GuiUtils.font.getWidth(StatCollector.translateToLocal("gui.mff:undo")) - 24, 0),
                    new PosXY(0, 0),
                    new AnchorPoint(1, 0),
                    new AnchorPoint(1, 1));
            undoButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
            undoButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            undoButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            undoButton.uiLabel.setTextColor(UIColor.matWhite());
            undoButton.uiLabel.setText(StatCollector.translateToLocal("gui.mff:undo"));
            //The undo button action is set later
            //</editor-fold>

            final UIListBox securityOptionsListBox = new UIListBox(getWorkspace(),
                    "securityOptionsListBox",
                    new PosXY(24, 128),
                    new PosXY(-12, -24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(0.4, 1));
            securityOptionsListBox.setPanelBackgroundColor(UIColor.transparent());

            //<editor-fold desc="Player Options Title">
            final UIComponent playerOptionsTitleLabelComponent = new UIComponent(null,
                    "playerOptionsTitleLabelComponent",
                    new PosXY(0, 4),
                    new PosXY(0, 32),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            playerOptionsTitleLabelComponent.setPanelBackgroundColor(UIColor.transparent());

            final UILabel playerOptionsLabel = new UILabel(playerOptionsTitleLabelComponent,
                    "playerOptionsLabel",
                    new PosXY(0, 0),
                    new AnchorPoint(0.5, 0),
                    GuiUtils.font);
            playerOptionsLabel.setHorizontalAlign(0);
            playerOptionsLabel.setText(StatCollector.translateToLocal("gui.mff:playerOptions"));

            securityOptionsListBox.addItem("playerOptionsTitleLabelComponent", playerOptionsTitleLabelComponent);
            //</editor-fold>

            //<editor-fold desc="Player Options Player Info">
            final UIComponent playerOptionsPlayerInfo = new UIComponent(null,
                    "playerOptionsPlayerInfo",
                    new PosXY(0, 0),
                    new PosXY(0, 128),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            playerOptionsPlayerInfo.setPanelBackgroundColor(UIColor.transparent());

            playerOptionsBodyImage = new UIComponent(playerOptionsPlayerInfo,
                    "playerOptionsBodyImage",
                    new PosXY(0, 0),
                    new PosXY(57, 128),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(0, 0));
            playerOptionsBodyImage.setPanelBackgroundColor(UIColor.transparent());

            playerOptionsNameLabel = new UILabel(playerOptionsPlayerInfo,
                    "playerOptionsNameLabel",
                    new PosXY(81, 4),
                    new AnchorPoint(0, 0),
                    GuiUtils.font);

            playerOptionsUUIDLabel = new UILabel(playerOptionsPlayerInfo,
                    "playerOptionsUUIDLabel",
                    new PosXY(81, 28),
                    new AnchorPoint(0, 0),
                    GuiUtils.debugFont);

            playerOptionsGroupLabel = new UILabel(playerOptionsPlayerInfo,
                    "playerOptionsGroupLabel",
                    new PosXY(81, 46),
                    new AnchorPoint(0, 0),
                    GuiUtils.font);

            final UITextButton playerOptionsSetGroupButton = new UITextButton(playerOptionsPlayerInfo,
                    "playerOptionsSetGroupButton",
                    new PosXY(81, 70),
                    new PosXY(0, 94),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            playerOptionsSetGroupButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
            playerOptionsSetGroupButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            playerOptionsSetGroupButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            playerOptionsSetGroupButton.uiLabel.setTextColor(UIColor.matWhite());
            playerOptionsSetGroupButton.uiLabel.setText(StatCollector.translateToLocal("gui.mff:assignToSelectedGroup"));
            playerOptionsSetGroupButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    if (selectedPlayerUUIDs.size() > 0) {
                        if (selectedGroupIDs.size() > 0) {
                            if (selectedGroupIDs.size() < 2) {

                                List<String> undoPlayerUUIDs = new ArrayList<String>(selectedPlayerUUIDs);

                                Map<String, String[]> undoPermittedPlayersMap = new HashMap<String, String[]>(permittedPlayers);
                                for (Map.Entry<String, String[]> entry : undoPermittedPlayersMap.entrySet()) {
                                    String[] newArray = new String[entry.getValue().length];
                                    System.arraycopy(entry.getValue(), 0, newArray, 0, entry.getValue().length);
                                    entry.setValue(newArray);
                                }

                                undoHistory.add(new Object[]{UndoAction.playersGroupChanged, undoPlayerUUIDs, undoPermittedPlayersMap});

                                for (String uuid : selectedPlayerUUIDs) {
                                    String[] currentPlayerData = permittedPlayers.get(uuid);
                                    currentPlayerData[1] = selectedGroupIDs.get(0); //Set player group
                                    permittedPlayers.put(uuid, currentPlayerData);
                                }
                                selectedPlayerUUIDsChanged();
                            } else {
                                notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:tooManyGroupsSelected"), UIColor.matRed());
                            }
                        } else {
                            notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:selectAGroup"), UIColor.matRed());
                        }
                    } else {
                        notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:selectPlayersToAssign"), UIColor.matRed());
                    }
                }
            });

            final UITextButton playerOptionsRemoveButton = new UITextButton(playerOptionsPlayerInfo,
                    "playerOptionsRemoveButton",
                    new PosXY(81, 96),
                    new PosXY(0, 120),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            playerOptionsRemoveButton.setPanelDefaultBackgroundColor(UIColor.matRed());
            playerOptionsRemoveButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            playerOptionsRemoveButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            playerOptionsRemoveButton.uiLabel.setTextColor(UIColor.matWhite());
            playerOptionsRemoveButton.uiLabel.setText(StatCollector.translateToLocal("gui.mff:removePlayers"));
            playerOptionsRemoveButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    if (selectedPlayerUUIDs.size() > 0) {
                        //Add undo action
                        Map<String, String[]> undoMap = new HashMap<String, String[]>();
                        for (String selectedPlayerUUID : selectedPlayerUUIDs) {
                            undoMap.put(selectedPlayerUUID, permittedPlayers.get(selectedPlayerUUID));
                        }
                        undoHistory.add(new Object[]{UndoAction.removePlayers, undoMap});

                        playersListBox.removeItems(selectedPlayerUUIDs.toArray(new String [selectedPlayerUUIDs.size()]));
                        for (String UUID : selectedPlayerUUIDs) {
                            permittedPlayers.remove(UUID);
                        }
                        selectedPlayerUUIDs.clear();
                        selectedPlayerUUIDsChanged();
                    } else {
                        notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:selectPlayersToRemove"), UIColor.matRed());
                    }
                }
            });

            securityOptionsListBox.addItem("playerOptionsPlayerInfo", playerOptionsPlayerInfo);
            //</editor-fold>

            //<editor-fold desc="Group Options Title">
            final UIComponent groupOptionsTitleLabelComponent = new UIComponent(null,
                    "groupOptionsTitleLabelComponent",
                    new PosXY(0, 4),
                    new PosXY(0, 32),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            groupOptionsTitleLabelComponent.setPanelBackgroundColor(UIColor.transparent());

            final UILabel GroupOptionsLabel = new UILabel(groupOptionsTitleLabelComponent,
                    "groupOptionsLabel",
                    new PosXY(0, 0),
                    new AnchorPoint(0.5, 0),
                    GuiUtils.font);
            GroupOptionsLabel.setHorizontalAlign(0);
            GroupOptionsLabel.setText(StatCollector.translateToLocal("gui.mff:groupOptions"));

            securityOptionsListBox.addItem("groupOptionsTitleLabelComponent", groupOptionsTitleLabelComponent);
            //</editor-fold>

            //<editor-fold desc="Group Options Buttons">
            UIComponent groupNameLabelComponent = new UIComponent(null,
                    "groupNameLabelComponent",
                    new PosXY(0, 0),
                    new PosXY(0, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            groupNameLabelComponent.setPanelBackgroundColor(UIColor.transparent());

            groupNameLabel = new UILabel(groupNameLabelComponent,
                    "groupNameLabel",
                    new PosXY(0, 0),
                    new AnchorPoint(0, 0),
                    GuiUtils.font);
            securityOptionsListBox.addItem("groupNameLabelComponent", groupNameLabelComponent);

            securityOptionsListBox.addItem("spacer1", new UIListSpacer(null, "spacer1", 2)); //Add 2px spacer

            final UITextField renameGroupTextField = new UITextField(null,
                    "renameGroupTextField",
                    new PosXY(0, 0),
                    new PosXY(0, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            renameGroupTextField.setPlaceholderText(StatCollector.translateToLocal("gui.mff:renameGroup"));
            renameGroupTextField.setOnReturnAction(new UIAction() {
                @Override
                public void execute() {
                    if (!renameGroupTextField.value.isEmpty()) {
                        ArrayList<String> prevGroupIDs = new ArrayList<String>();
                        prevGroupIDs.addAll(selectedGroupIDs);
                        renameGroups(prevGroupIDs, renameGroupTextField.value, permGroupsListBox);
                        renameGroupTextField.clearValue();
                    }
                }
            });
            securityOptionsListBox.addItem("renameGroupTextField", renameGroupTextField);

            securityOptionsListBox.addItem("spacer2", new UIListSpacer(null, "spacer2", 2)); //Add 2px spacer

            UITextButton groupSelectMembersButton = new UITextButton(null,
                    "groupSelectMembersButton",
                    new PosXY(0, 0),
                    new PosXY(0, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            groupSelectMembersButton.setPanelDefaultBackgroundColor(UIColor.matBlue());
            groupSelectMembersButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            groupSelectMembersButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            groupSelectMembersButton.uiLabel.setTextColor(UIColor.matWhite());
            groupSelectMembersButton.uiLabel.setText(StatCollector.translateToLocal("gui.mff:selectMembersOfGroup"));
            groupSelectMembersButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    if (selectedGroupIDs.size() > 0) {
                        selectedPlayerUUIDs.clear();
                        for (Map.Entry<String, String[]> entry : permittedPlayers.entrySet()) { //Loop through the registered players
                            if (selectedGroupIDs.contains(entry.getValue()[1])) { //If the player is in one of the selected groups
                                selectedPlayerUUIDs.add(entry.getKey());
                            }
                        }
                        selectedPlayerUUIDsChanged();
                    } else {
                        notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:selectGroupsToSelectMembers"), UIColor.matRed());
                    }
                }
            });
            securityOptionsListBox.addItem("groupSelectMembersButton", groupSelectMembersButton);

            securityOptionsListBox.addItem("spacer3", new UIListSpacer(null, "spacer3", 2)); //Add 2px spacer

            UITextButton groupRemoveButton = new UITextButton(null,
                    "groupRemoveButton",
                    new PosXY(0, 0),
                    new PosXY(0, 24),
                    new AnchorPoint(0, 0),
                    new AnchorPoint(1, 0));
            groupRemoveButton.setPanelDefaultBackgroundColor(UIColor.matRed());
            groupRemoveButton.setPanelActiveBackgroundColor(UIColor.matBlueGrey300());
            groupRemoveButton.setPanelHitBackgroundColor(UIColor.matBlueGrey700());
            groupRemoveButton.uiLabel.setTextColor(UIColor.matWhite());
            groupRemoveButton.uiLabel.setText(StatCollector.translateToLocal("gui.mff:removeGroups"));
            groupRemoveButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    if (selectedGroupIDs.size() > 0) {

                        //<editor-fold desc="Store undo data">
                        Map<Map<String, Map<String, Boolean>>, String[]> undoData = new HashMap<Map<String, Map<String, Boolean>>, String[]>();
                        for (final String selectedGroupID : selectedGroupIDs) {

                            List<String> prevPlayers = new ArrayList<String>();

                            for (Map.Entry<String, String[]> entry : permittedPlayers.entrySet()) { //Loop through all players
                                if (entry.getValue()[1].equals(selectedGroupID)) { //If that player is assigned to the current group
                                    prevPlayers.add(entry.getKey()); //Store the player UUID in the prevPlayers list
                                }
                            }

                            undoData.put(new HashMap<String, Map<String, Boolean>>(){{
                                put(selectedGroupID, permissionGroups.get(selectedGroupID));
                            }}, prevPlayers.toArray(new String[prevPlayers.size()]));
                        }

                        undoHistory.add(new Object[]{UndoAction.removeGroups, undoData});
                        //</editor-fold>

                        permGroupsListBox.removeItems(selectedGroupIDs.toArray(new String [selectedGroupIDs.size()]));
                        for (String groupID : selectedGroupIDs) {
                            permissionGroups.remove(groupID);
                            for (Map.Entry<String, String[]> entry : permittedPlayers.entrySet()) { //Loop through all players
                                if (entry.getValue()[1].equals(groupID)) { //If that player is assigned to the current group
                                    entry.setValue(new String[]{entry.getValue()[0], "gui.mff:everyone"}); //Assign that player to the everyone group
                                }
                            }
                        }
                        selectedGroupIDs.clear();
                        selectedGroupIDsChanged();
                        selectedPlayerUUIDsChanged();
                    } else {
                        notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:selectGroupsToRemove"), UIColor.matRed());
                    }
                }
            });
            securityOptionsListBox.addItem("groupRemoveButton", groupRemoveButton);
            //</editor-fold>

            //<editor-fold desc="Loop through and add permissions">
            for (Map.Entry<String, Boolean> entry : permissionGroupDefaultPermissions.entrySet()) {
                final UIComponent permWrapperComponent = new UIComponent(null,
                        entry.getKey() + "Component",
                        new PosXY(0, 0),
                        new PosXY(0, 32),
                        new AnchorPoint(0, 0),
                        new AnchorPoint(1, 0));
                permWrapperComponent.setPanelBackgroundColor(UIColor.transparent());

                final UILabel permLabel = new UILabel(permWrapperComponent,
                        "permLabel",
                        new PosXY(4, 4),
                        new AnchorPoint(0, 0),
                        GuiUtils.font);
                permLabel.setText(StatCollector.translateToLocal(entry.getKey()));

                final UIToggleBox permToggleBox = new UIToggleBox(permWrapperComponent,
                        "permToggleBox",
                        new PosXY(-28, 4),
                        new PosXY(-4, -4),
                        new AnchorPoint(1, 0),
                        new AnchorPoint(1, 1));
                permToggleBox.setValue(entry.getValue());

                securityOptionsListBox.addItem(entry.getKey() + "Component", permWrapperComponent);
            }
            //</editor-fold>

            undoManager.setOnUpdateAction(new UIAction() {
                @Override
                public void execute() {
                    if (UIDisplay.keyBuffer.contains(Keyboard.KEY_Z) &&
                            ((Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMENU))) || //If on Mac and Cmd Z pressed
                            !Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)))) { //If not on mac and Ctrl Z pressed

                        int keyZIndex = UIDisplay.keyBuffer.indexOf(Keyboard.KEY_Z);
                        UIDisplay.keyBuffer.remove(keyZIndex); //Prevent Z from being typed into text fields
                        UIDisplay.keyCharBuffer.remove(keyZIndex);
                        UIDisplay.keyStateBuffer.remove(keyZIndex);

                        undo(playersListBox, permGroupsListBox);

                    }
                }
            });

            undoButton.setOnClickAction(new UIAction() {
                @Override
                public void execute() {
                    undo(playersListBox, permGroupsListBox);
                }
            });

            notificationManager = new UINotificationManager(getWorkspace(), "notificationManager");

            updateSecurityOptions();

        } else {
            final UILabel noSecurityUpgradeLabel = new UILabel(getWorkspace(),
                    "noSecurityUpgradeLabel",
                    new PosXY(24, 24),
                    new AnchorPoint(0, 0),
                    GuiUtils.font);
            noSecurityUpgradeLabel.setText(StatCollector.translateToLocal("gui.mff:securityNoUpgrade"));
        }

    }

    private void addPlayer(final String playerName, final UIListBox playersListBox, final String groupID, final boolean shouldAddUndoAction) {

        SecurityTaskManager.addTask(new UIAction() {
            @Override
            public void execute() {
                try {

                    String response = DependencyUtils.httpGetString("https://api.mojang.com/users/profiles/minecraft/" + playerName);

                    if (!response.isEmpty()) {

                        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

                        if (jsonObject.has("name") && jsonObject.has("id")) {

                            final String playerUUID = jsonObject.get("id").getAsString();
                            final String playerName = jsonObject.get("name").getAsString();

                            if (!permittedPlayers.containsKey(playerUUID)) { //If the player's not already on the permitted players list

                                final String[] playerInfo = new String[]{playerName, groupID};
                                permittedPlayers.put(playerUUID, playerInfo); //Add the player info the permitted players list

                                final UIButton listItem = new UIButton(null,
                                        "item" + playerName,
                                        new PosXY(0, 0),
                                        new PosXY(0, 32),
                                        new AnchorPoint(0, 0),
                                        new AnchorPoint(1, 0));
                                listItem.setPanelActiveBackgroundColor(UIColor.matBlueGrey700());
                                listItem.setPanelHitBackgroundColor(UIColor.matBlueGrey300());
                                listItem.setOnClickAction(new UIAction() { //Click to select the player
                                    @Override
                                    public void execute() {
                                        if (!((Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA))) || //If on Mac and CMD key down (Negated)
                                                (!Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))))) { //If not on Mac and CTRL key down (Negated)
                                            selectedPlayerUUIDs.clear();
                                        }
                                        if (selectedPlayerUUIDs.contains(playerUUID)) {
                                            selectedPlayerUUIDs.remove(playerUUID);
                                        } else {
                                            selectedPlayerUUIDs.add(playerUUID);
                                        }
                                        selectedPlayerUUIDsChanged();
                                    }
                                });
                                listItem.setOnUpdateAction(new UIAction() { //Check if selected and update the bg color
                                    @Override
                                    public void execute() {
                                        if (selectedPlayerUUIDs.contains(playerUUID)) {
                                            listItem.setPanelDefaultBackgroundColor(UIColor.matBlue());
                                            listItem.setPanelActiveBackgroundColor(UIColor.matBlue());
                                        } else {
                                            listItem.setPanelDefaultBackgroundColor(UIColor.matGrey900());
                                            listItem.setPanelActiveBackgroundColor(UIColor.matBlueGrey700());
                                        }
                                    }
                                });
                                final UIComponent listItemPlayerFace = new UIComponent(listItem,
                                        "playerFaceComponent",
                                        new PosXY(0, 0),
                                        new PosXY(32, 32),
                                        new AnchorPoint(0, 0),
                                        new AnchorPoint(0, 0));
                                listItemPlayerFace.setPanelBackgroundColor(UIColor.transparent());
                                final UILabel listItemPlayerNameLabel = new UILabel(listItem,
                                        "playerNameLabel",
                                        new PosXY(36, 4),
                                        new AnchorPoint(0, 0),
                                        GuiUtils.font);
                                listItemPlayerNameLabel.setText(playerName);
                                listItemPlayerNameLabel.setTextColor(UIColor.matWhite());

                                playersListBox.addItem(playerUUID, listItem);
                                playersListBox.setTargetScrollY(Math.min(playersListBox.height - playersListBox.getTotalHeightOfComponents(), 0));

                                selectedPlayerUUIDs.clear();
                                selectedPlayerUUIDs.add(playerUUID);
                                selectedPlayerUUIDsChanged(); //Update the security options panel

                                if (shouldAddUndoAction) {
                                    undoHistory.add(new Object[]{UndoAction.addPlayer, playerUUID});
                                }

                                SecurityTaskManager.addTask(new UIAction() {
                                    @Override
                                    public void execute() {
                                        try {

                                            BufferedImage faceImage = DependencyUtils.httpGetImage("https://crafatar.com/avatars/" + playerUUID + "?overlay=true");
                                            listItemPlayerFace.setQueuedImage(faceImage);

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:cantContactCrafatar"), UIColor.matRed());
                                        }
                                    }
                                });

                                notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:playerRegistered"), UIColor.matGreen());
                            } else {
                                //Show player already registered notification
                                notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:playerAlreadyRegistered"), UIColor.matRed());
                            }

                        } else {
                            notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:invalidResponseMojang"), UIColor.matRed());
                        }

                    } else {
                        notificationManager.addNotification(String.format("%s: %s", StatCollector.translateToLocal("gui.mff:playerDoesNotExist"), playerName), UIColor.matRed());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:cantContactMojang"), UIColor.matRed());
                }
            }
        });

    }

    private void addPlayer(final String playerName, final UIListBox playersListBox) {
        addPlayer(playerName, playersListBox, "gui.mff:everyone", true);
    }

    private void addGroup(final String groupName, final UIListBox permGroupsListBox) {

        if (!permissionGroups.containsKey(groupName)) { //If the group doesn't exist

            permissionGroups.put(groupName, permissionGroupDefaultPermissions); //Add the player info the permitted players list

            addGroupListBoxItem(groupName, permGroupsListBox);

            undoHistory.add(new Object[]{UndoAction.addGroup, groupName});

            notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:groupRegistered"), UIColor.matGreen());
        } else {
            //Show player already registered notification
            notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:groupAlreadyRegistered"), UIColor.matRed());
        }

    }

    private void renameGroups(final List<String> previousGroupIDs, final String newGroupID, UIListBox permGroupsListBox) {
        Map<String, String> undoData = new HashMap<String, String>(); //Key: The new group name, Value: The previous group name

        permGroupsListBox.removeItems(previousGroupIDs.toArray(new String[previousGroupIDs.size()])); //Remove the previous group items from the list box

        int appendOffset = 0; //Used in case we try to rename a group to something that already exists
        for (int i = 0; i < previousGroupIDs.size(); i++) {
            permissionGroups.remove(previousGroupIDs.get(i)); //Remove the old group

            String toAppendToEnd = i == 0 ? "" : String.valueOf(i + appendOffset); // If the appendOffset is 0, then don't append a number to the end...
            //...of the new group name, otherwise do append a number

            while (permissionGroups.containsKey(newGroupID + toAppendToEnd)) { //If the new group name already exists
                appendOffset++; //Keep adding 1 to the number at the end
                toAppendToEnd = String.valueOf(i + appendOffset);
            }

            final String appendToEnd = toAppendToEnd; //I need a variable to be final (maybe?)

            addGroup(newGroupID + appendToEnd, permGroupsListBox);

            undoData.put(newGroupID + appendToEnd, previousGroupIDs.get(i)); //Add the new, old group name to the undo data map

            for (Map.Entry<String, String[]> playerData : permittedPlayers.entrySet()) { //Loop through all players
                if (playerData.getValue()[1].equals(previousGroupIDs.get(i))) { //If the player's group ID matches the one we're renaming
                    playerData.setValue(new String[]{playerData.getValue()[0], newGroupID + appendToEnd}); //Set the players group ID the the renamed one
                }
            }

            previousGroupIDs.set(i, newGroupID + appendToEnd);
        }
        Collections.sort(permGroupsListBox.componentNumberIDs);
        permGroupsListBox.reorganiseItems();

        selectedPlayerUUIDsChanged();
        selectedGroupIDsChanged();

        undoHistory.add(new Object[]{UndoAction.renameGroups, undoData});
    }

    private void addGroupListBoxItem(final String groupName, final UIListBox permGroupsListBox) {
        final UIButton listItem = new UIButton(null,
                "item" + groupName,
                new PosXY(0, 0),
                new PosXY(0, 32),
                new AnchorPoint(0, 0),
                new AnchorPoint(1, 0));
        listItem.setPanelActiveBackgroundColor(UIColor.matBlueGrey700());
        listItem.setPanelHitBackgroundColor(UIColor.matBlueGrey300());
        listItem.setOnClickAction(new UIAction() { //Click to select the player
            @Override
            public void execute() {
                if (!((Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA))) || //If on Mac and CMD key down (Negated)
                        (!Minecraft.isRunningOnMac && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))))) { //If not on Mac and CTRL key down (Negated)
                    selectedGroupIDs.clear();
                }
                if (selectedGroupIDs.contains(groupName)) {
                    selectedGroupIDs.remove(groupName);
                } else {
                    selectedGroupIDs.add(groupName);
                }
                selectedGroupIDsChanged();
            }
        });
        listItem.setOnUpdateAction(new UIAction() { //Check if selected and update the bg color
            @Override
            public void execute() {
                if (selectedGroupIDs.contains(groupName)) {
                    listItem.setPanelDefaultBackgroundColor(UIColor.matBlue());
                    listItem.setPanelActiveBackgroundColor(UIColor.matBlue());
                } else {
                    listItem.setPanelDefaultBackgroundColor(UIColor.matGrey900());
                    listItem.setPanelActiveBackgroundColor(UIColor.matBlueGrey700());
                }
            }
        });
        final UILabel listItemGroupNameLabel = new UILabel(listItem,
                "groupNameLabel",
                new PosXY(4, 4),
                new AnchorPoint(0, 0),
                GuiUtils.font);
        listItemGroupNameLabel.setText(StatCollector.translateToLocal(groupName));
        listItemGroupNameLabel.setTextColor(UIColor.matWhite());

        permGroupsListBox.addItem(groupName, listItem);
        Collections.sort(permGroupsListBox.componentNumberIDs);
        permGroupsListBox.reorganiseItems();
        permGroupsListBox.setTargetScrollY(Math.min(permGroupsListBox.height - permGroupsListBox.componentMap.get(groupName).height, 0));
        selectedGroupIDs.clear();
        selectedGroupIDs.add(groupName);
        selectedGroupIDsChanged(); //Update the security options panel
    }

    private PlayerData getAggregatePlayerData(List<String> selectedPlayerUUIDs) {

        PlayerData playerData = new PlayerData();
        if (selectedPlayerUUIDs.size() == 0) {

            playerData.name = StatCollector.translateToLocal("gui.mff:nothingSelected");
            playerData.uuid = StatCollector.translateToLocal("gui.mff:nothingSelected");
            playerData.group = StatCollector.translateToLocal("gui.mff:nothingSelected");

        } else if (selectedPlayerUUIDs.size() == 1) {

            playerData.name = permittedPlayers.get(selectedPlayerUUIDs.get(0))[0];
            playerData.uuid = selectedPlayerUUIDs.get(0);
            playerData.group = permittedPlayers.get(selectedPlayerUUIDs.get(0))[1];

        } else {

            //<editor-fold desc="Aggregate groups">
            boolean allSame = true;
            for (String selectedPlayerUUID : selectedPlayerUUIDs) {
                if (!permittedPlayers.get(selectedPlayerUUID)[1].equals(permittedPlayers.get(selectedPlayerUUIDs.get(0))[1])) {
                    allSame = false;
                    break;
                }
            }

            if (allSame) {
                playerData.group = permittedPlayers.get(selectedPlayerUUIDs.get(0))[1];
            }
            //</editor-fold>

        }

        return playerData;

    }

    private GroupData getAggregateGroupData(List<String> selectedGroupIDs) {

        GroupData groupData = new GroupData();
        if (selectedGroupIDs.size() == 0) {

            groupData.name = StatCollector.translateToLocal("gui.mff:nothingSelected");

        } else if (selectedGroupIDs.size() == 1) {

            groupData.name = selectedGroupIDs.get(0);

        } else {

            //<editor-fold desc="Aggregate groups">
//            boolean allSame = true;
//            for (String selectedPlayerUUID : selectedPlayerUUIDs) {
//                if (!permittedPlayers.get(selectedPlayerUUID)[1].equals(permittedPlayers.get(selectedPlayerUUIDs.get(0))[1])) {
//                    allSame = false;
//                    break;
//                }
//            }
//
//            if (allSame) {
//                playerData.group = permittedPlayers.get(selectedPlayerUUIDs.get(0))[1];
//            }
            //</editor-fold>

            //TODO

        }

        return groupData;

    }

    protected void selectedPlayerUUIDsChanged() {
        aggregateSelectedPlayerData = getAggregatePlayerData(selectedPlayerUUIDs);
        updateSecurityOptions();
    }

    protected void selectedGroupIDsChanged() {
        aggregateSelectedGroupData = getAggregateGroupData(selectedGroupIDs);
        updateSecurityOptions();
    }

    private void updateSecurityOptions() {

        //<editor-fold desc="Get player body render">
        if (selectedPlayerUUIDs.size() > 0) {
            if (bodyImageCache.containsKey(selectedPlayerUUIDs.get(0))) {
                playerOptionsBodyImage.setQueuedImage(bodyImageCache.get(selectedPlayerUUIDs.get(0)));
            } else {

                SecurityTaskManager.addTask(new UIAction() {
                    @Override
                    public void execute() {
                        try {

                            String uuidToGet = selectedPlayerUUIDs.get(0);

                            BufferedImage bodyImage = DependencyUtils.httpGetImage("https://crafatar.com/renders/body/" + uuidToGet + "?overlay=true");
                            if (selectedPlayerUUIDs.get(0).equals(uuidToGet)) {
                                playerOptionsBodyImage.setQueuedImage(bodyImage);
                            }
                            bodyImageCache.put(uuidToGet, bodyImage);

                        } catch (IOException e) {
                            e.printStackTrace();
                            notificationManager.addNotification(StatCollector.translateToLocal("gui.mff:cantContactCrafatar"), UIColor.matRed());
                        }
                    }
                });

            }
        } else {
            playerOptionsBodyImage.setImage(null);
        }
        //</editor-fold>

        playerOptionsNameLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.mff:name"), aggregateSelectedPlayerData.name));
        playerOptionsUUIDLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.mff:uuid"), aggregateSelectedPlayerData.uuid));
        playerOptionsGroupLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.mff:group"), StatCollector.translateToLocal(aggregateSelectedPlayerData.group)));
        groupNameLabel.setText(String.format("%s: %s", StatCollector.translateToLocal("gui.mff:name"), aggregateSelectedGroupData.name));

    }

    @SuppressWarnings("unchecked")
    private void undo(UIListBox playersListBox, UIListBox permGroupsListBox) {

        if (undoHistory.size() > 0) {
            Object[] undo = undoHistory.get(undoHistory.size() - 1);
            UndoAction undoAction = (UndoAction) undo[0];

            switch (undoAction) {
                case addPlayer: { //Remove the added player
                    String playerUUID = (String) undo[1];

                    if (playerUUID != null && permittedPlayers.containsKey(playerUUID)) {
                        playersListBox.removeItems(playerUUID);
                        permittedPlayers.remove(playerUUID);

                        if (selectedPlayerUUIDs.contains(playerUUID)) {
                            selectedPlayerUUIDs.remove(playerUUID);
                            selectedPlayerUUIDsChanged();
                        }
                    } else {
                        LogHelper.warn("Failed to undo! Action: addPlayer");
                    }

                    break;
                } case addGroup: { //Remove the added group
                    String groupID = (String) undo[1];

                    if (groupID != null) {
                        permGroupsListBox.removeItems(groupID);
                        permissionGroups.remove(groupID);

                        for (Map.Entry<String, String[]> entry : permittedPlayers.entrySet()) { //Loop through all players
                            if (entry.getValue()[1].equals(groupID)) { //If that player is assigned to the current group
                                entry.setValue(new String[]{entry.getValue()[0], "gui.mff:everyone"}); //Assign that player to the everyone group
                            }
                        }

                        if (selectedGroupIDs.contains(groupID)) {
                            selectedGroupIDs.remove(groupID);
                            selectedGroupIDsChanged();
                            selectedPlayerUUIDsChanged();
                        }
                    } else {
                        LogHelper.warn("Failed to undo! Action: addGroup");
                    }

                    break;
                } case playersGroupChanged: { //Change the players' groups back
                    List<String> playerUUIDs = (List<String>) undo[1];
                    Map<String, String[]> oldPermittedPlayers = (Map<String, String[]>) undo[2];

                    if (playerUUIDs != null) {
                        for (String uuid : playerUUIDs) {
                            String[] currentPlayerData = permittedPlayers.get(uuid);
                            currentPlayerData[1] = oldPermittedPlayers.get(uuid)[1]; //Set player group
                            permittedPlayers.put(uuid, currentPlayerData);
                        }

                        selectedPlayerUUIDsChanged();
                    } else {
                        LogHelper.warn("Failed to undo! Action: playersGroupChanged");
                    }

                    break;
                } case removePlayers: { //Re-add the removed players
                    Map<String, String[]> removedPlayers = (Map<String, String[]>) undo[1];

                    if (removedPlayers != null) {
                        for (Map.Entry<String, String[]> entry : removedPlayers.entrySet()) {
                            addPlayer(entry.getValue()[0], playersListBox, entry.getValue()[1], false);
                        }
                    } else {
                        LogHelper.warn("Failed to undo! Action: removePlayers");
                    }

                    break;
                } case removeGroups: {
                    final Map<Map<String, Map<String, Boolean>>, String[]> groupData = (Map<Map<String, Map<String, Boolean>>, String[]>) undo[1];

                    for (final Map.Entry<Map<String, Map<String, Boolean>>, String[]> entry : groupData.entrySet()) {
                        Map<String, Map<String, Boolean>> mapData = new HashMap<String, Map<String, Boolean>>(){{
                            put(entry.getKey().entrySet().iterator().next().getKey(), entry.getKey().entrySet().iterator().next().getValue());
                        }};

                        permissionGroups.putAll(mapData);

                        for (int i = 0; i < entry.getValue().length; i++) {
                            permittedPlayers.put(entry.getValue()[i], new String[]{permittedPlayers.get(entry.getValue()[i])[0],
                                    entry.getKey().entrySet().iterator().next().getKey()});
                        }

                        addGroupListBoxItem(entry.getKey().entrySet().iterator().next().getKey(), permGroupsListBox);

                    }

                    selectedPlayerUUIDsChanged();

                    break;
                } case renameGroups: {
                    final Map<String, String> renameData = (Map<String, String>) undo[1];

                    for (final Map.Entry<String, String> entry : renameData.entrySet()) {
                        renameGroups(new ArrayList<String>(){{
                            add(entry.getKey());
                        }}, entry.getValue(), permGroupsListBox);
                    }

                    break;
                }
            }

            undoHistory.remove(undoHistory.size() - 1);
        }

    }

}

class PlayerData {
    protected String name = StatCollector.translateToLocal("gui.mff:multiple");
    protected String uuid = StatCollector.translateToLocal("gui.mff:multiple");
    protected String group = StatCollector.translateToLocal("gui.mff:multiple");
}

class GroupData {
    protected String name = StatCollector.translateToLocal("gui.mff:multiple");
}

enum UndoAction {
    addPlayer,
    addGroup,
    playersGroupChanged,
    removePlayers,
    removeGroups,
    renameGroups
}
