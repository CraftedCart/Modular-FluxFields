package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.damagesource.DamageSourceFFSecurityKill;
import io.github.craftedcart.MFF.eventhandler.PreventFFBlockBreak;
import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.init.ModEntityTracker;
import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEFFProjector extends TEPoweredBlock implements IUpdatePlayerListBox, IInventory {

    //Config-y stuff
    public int minX = -5;
    public int maxX = 5;
    public int minY = -5;
    public int maxY = 5;
    public int minZ = -5;
    public int maxZ = 5;

    //Upgrades
    public boolean hasCalcInnerBlocksUpgrade = true;
    public boolean hasSecurityUpgrade = true;

    //Not so config-y stuff
    private int updateTime = 1;
    private int downTime = 0; //The ff projector is allowed a short grace period without power before everything is reset
    public ArrayList<BlockPos> wallBlockList = new ArrayList<BlockPos>(); //The list of blockposes of the forcefield walls
    public ArrayList<BlockPos> innerBlockList = new ArrayList<BlockPos>(); //The list of blockposes of the inner blocks
    public int blockPlaceProgress = 0;
    private boolean doWorldLoadSetup = false; //Used to make sure a block of code only runs once on chunk load
    public boolean isPowered = false; //Does the FF Projector has enough power to keep running
    private ItemStack[] inventory; //The inventory of the FF Projector
    private String customName;
    public int uptime = 0;
    public String owner = ""; //The owner UUID
    public String ownerName = ""; //The owner username
    public List<List<String>> permittedPlayers = new ArrayList<List<String>>(); //The list of permitted players defined by the security tab on the gui
    public List<List<Object>> permissionGroups = new ArrayList<List<Object>>();
    /* List containing lists of groups containing the group ID and its permissions (in that order) defined by the security tab on the gui
     * Perm 1: Boolean: Should player be killed
     */
    public List<Object> generalPermissions = new ArrayList<Object>();
    /* List containing general permissions defined by the security tab on the gui
     * Perm 1: Boolean: Should hostile mobs be killed
     * Perm 2: Boolean: Should peaceful mobs be killed
     */
    public List<Double> powerUsagePerTickForPastMinute = new ArrayList<Double>();
    public List<Double> powerUsagePerSecondForPastHalfHour = new ArrayList<Double>();
    private int tickTimeSinceLastSecond = 0;
    private double powerUsageSinceLastSecond = 0;

    //<editor-fold desc="Inventory stuff"> Used by IntelliJ to define a custom code folding section
    public TEFFProjector() {
        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return null;
        return this.inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != null) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).stackSize <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, null);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).stackSize <= 0) {
                    this.setInventorySlotContents(index, null);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= this.getSizeInventory())
            return;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();

        if (stack != null && stack.stackSize == 0)
            stack = null;

        this.inventory[index] = stack;
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        //NO-OP
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        //NO-OP
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        //NO-OP
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSizeInventory(); i++)
            this.setInventorySlotContents(i, null);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.mff:ffProjector";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }
    //</editor-fold>

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        writeSyncableDataToNBT(nbt);
        writePowerStatsToNBT(nbt);

        //Inventory stuff
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        nbt.setTag("Items", list);

        if (this.hasCustomName()) {
            nbt.setString("CustomName", this.getCustomName());
        }

    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        readSyncableDataFromNBT(nbt);
        readPowerStatsFromNBT(nbt);

        //Inventory Stuff
        NBTTagList list = nbt.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
        }

        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }

    }

    public void getWallBlocks() {

        for (BlockPos ffPos : wallBlockList) {
            if (worldObj.getBlockState(ffPos) == ModBlocks.forcefield.getDefaultState()) {
                worldObj.setBlockState(ffPos, Blocks.air.getDefaultState());
            }
        }

        BlockPos pos = this.getPos(); //Get this projector's position
        wallBlockList.clear(); //Clear the list of blockposes of the forcefield walls

        for (int x = minX; x <= maxX; x++) { //Calculate the +/- Z walls
            for (int y = minY; y <= maxY; y++) {
                wallBlockList.add(pos.add(x, y, minZ));
                wallBlockList.add(pos.add(x, y, maxZ));
            }
        }

        for (int z = minZ + 1; z <= maxZ - 1; z++) { //Calculate the +/- X walls
            for (int y = minY; y <= maxY; y++) {
                wallBlockList.add(pos.add(minX, y, z));
                wallBlockList.add(pos.add(maxX, y, z));
            }
        }

        for (int x = minX + 1; x <= maxX - 1; x++) { //Calculate the +/- Y walls
            for (int z = minZ + 1; z <= maxZ - 1; z++) {
                wallBlockList.add(pos.add(x, maxY, z));
                wallBlockList.add(pos.add(x, minY, z));
            }
        }

    }
    
    public void getInnerBlocks() {

        innerBlockList.clear(); //Clear the list of blockposes of the inner blocks

        if (hasCalcInnerBlocksUpgrade) {
            for (int x = minX + 1; x < maxX; x++) {
                for (int y = minY + 1; y < maxY; y++) {
                    for (int z = minZ + 1; z < maxZ; z++) {
                        innerBlockList.add(new BlockPos(x, y, z));
                    }
                }
            }
        }

    }

    private void doPermissionChecks() {
        //Do checks in case an update adds more permissions
        for (List<Object> permissionGroup : permissionGroups) {
            if (permissionGroup.size() < 2) {
                permissionGroup.add(false); //Perm 1: Should players be killed
            }
        }

        if (generalPermissions.size() < 1) {
            generalPermissions.add(false); //Perm 1: Should hostile mobs be killed
        }
        if (generalPermissions.size() < 2) {
            generalPermissions.add(false); //Perm 2: Should peaceful mobs be killed
        }
    }

    @Override
    public void update() { //Runs every game tick (20 times a second)

        super.update();

        if (!doWorldLoadSetup) { //This only runs when the chunk loads
            ArrayList<Object> al = new ArrayList<Object>();
            al.add(this.getWorld()); //Add this world to the list
            al.add(this.getPos()); //Add this blockpos to the list
            PreventFFBlockBreak.ffProjectors.add(al); //Register itself with the event handler which prevents FF block breaking
            doWorldLoadSetup = true;
            getWallBlocks(); //Calculate the blockposes of the walls
            getInnerBlocks(); //Calculate the blockposes of the inner blocks
            init(PowerConf.ffProjectorMaxPower, PowerConf.ffProjectorDrawRate);

            if (permissionGroups.size() == 0) { //Setup the everyone permission group
                List<Object> everyoneGroup = new ArrayList<Object>();
                everyoneGroup.add("gui.mff:everyone");
                everyoneGroup.add(false);
                permissionGroups.add(everyoneGroup);
            }

            doPermissionChecks();

        }

        updateTime--;

        //Draw power from above
        if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) != null) { //If a tile entity exists above
            if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) instanceof TEPoweredBlock) { //If the tile entity is a power cube
                drawPower((TEPoweredBlock) worldObj.getTileEntity(this.getPos().add(0, 1, 0))); //Draw some power
            }
        }

        //Use some power/block/t
        if (power >= PowerConf.ffProjectorUsagePerWallBlock * wallBlockList.size() + PowerConf.ffProjectorUsagePerInnerBlock * innerBlockList.size()) {
            power -= PowerConf.ffProjectorUsagePerWallBlock * wallBlockList.size() + PowerConf.ffProjectorUsagePerInnerBlock * innerBlockList.size();
            isPowered = true;
            uptime++;
            downTime = 0;
        } else {
            if (downTime > PowerConf.ffProjectorMaxDownTime) {
                uptime = 0; //Reset the uptime
                isPowered = false;
            } else {
                downTime++;
            }
        }

        //Executed every 5s (100t)
        if (updateTime <= 0) {
            updateTime = 100; //5s (100t)

            int index = 0;
            for (BlockPos ffPos : wallBlockList) { //Loop through every blockpos

                if (worldObj.isBlockLoaded(ffPos)) {
                    if (worldObj.getBlockState(ffPos) == Blocks.air.getDefaultState()) { //If the block found is air
                        if (power >= PowerConf.ffProjectorUsagePerBlockToGenerate) { //If we have enough power to place an FF block
                            worldObj.setBlockState(ffPos, ModBlocks.forcefield.getDefaultState()); //Place an FF block
                            power -= PowerConf.ffProjectorUsagePerBlockToGenerate; //Minus the power used
                        } else {
                            break;
                        }
                    } else if (worldObj.getBlockState(ffPos) == ModBlocks.forcefield.getDefaultState()) { //If the block found is an FF
                        if (power >= PowerConf.ffProjectorUsagePerWallBlock * wallBlockList.size()) { //If we have enough power to sustain the FF
                            refreshFFTimer(ffPos); //Refresh the decay timer of the FF
                        } else {
                            break;
                        }
                    }

                }


                if (index >= blockPlaceProgress) {
                    break;
                }

                index++;

            }

        }

        if (hasSecurityUpgrade && isPowered && blockPlaceProgress >= wallBlockList.size()) { //Security related stuff goes here

            //<editor-fold desc="Damage targeted players">
            List<String> targetedPlayers = new ArrayList<String>();

            for (List<String> plr : permittedPlayers) {

                for (List<Object> group : permissionGroups) {

                    if (plr.get(2).equals(group.get(0))) { //If the player is in that group
                        if ((Boolean) permissionGroups.get(0).get(1) && !(Boolean) group.get(1)) { //If people should be killed in the Everyone group, and the specified player is exempt
                            targetedPlayers.add(plr.get(0));
                        } else if (!(Boolean) permissionGroups.get(0).get(1) && (Boolean) group.get(1)) { //If people shouldn't be killed in the Everyone group, and the specified player should
                            targetedPlayers.add(plr.get(0));
                        }
                    }
                }

            }

            if ((Boolean) permissionGroups.get(0).get(1)) { //If everyone should be targeted
                targetedPlayers.add(owner); //Exempt the owner
            }

            damagePlayersInArea((Boolean) permissionGroups.get(0).get(1), new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ), targetedPlayers);
            //</editor-fold>

            //<editor-fold desc="Damage hostile mobs">
            if ((Boolean) generalPermissions.get(0)) { //If hostile mobs should be damaged
                for (Class entityClass : ModEntityTracker.hostileMobs) { //Loop through all registered hostile mobs
                    damageEntitiesInArea(entityClass, new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ));
                }
            }
            //</editor-fold>

            //<editor-fold desc="Damage peaceful mobs">
            if ((Boolean) generalPermissions.get(1)) { //If peaceful mobs should be damaged
                for (Class entityClass : ModEntityTracker.peacefulMobs) { //Loop through all registered hostile mobs
                    damageEntitiesInArea(entityClass, new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ));
                }
            }
            //</editor-fold>

        }

        if (blockPlaceProgress < wallBlockList.size() + innerBlockList.size() && isPowered) {
            blockPlaceProgress++;
        } else if (blockPlaceProgress > wallBlockList.size() + innerBlockList.size() && isPowered) {
            blockPlaceProgress = wallBlockList.size() + innerBlockList.size();
        } else if (!isPowered) {
            blockPlaceProgress = 0;
        }

        //Track power usage
        powerUsagePerTickForPastMinute.add(0, Math.abs(powerUsage));
        if (powerUsagePerTickForPastMinute.size() > 1200) {
            powerUsagePerTickForPastMinute.remove(1200);
        }

        if (tickTimeSinceLastSecond >= 20) {
            powerUsagePerSecondForPastHalfHour.add(0, powerUsageSinceLastSecond / 20);
            tickTimeSinceLastSecond = 0;
            powerUsageSinceLastSecond = 0;
            if (powerUsagePerSecondForPastHalfHour.size() > 1800) {
                powerUsagePerSecondForPastHalfHour.remove(1800);
            }
        }

        tickTimeSinceLastSecond++;
        powerUsageSinceLastSecond += Math.abs(powerUsage);

    }

    private void damagePlayersInArea(boolean shouldTargetEveryone, BlockPos p1, BlockPos p2, List<String> players) {

        List<EntityPlayer> playersWithinAABB = worldObj.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(p1.add(this.getPos()), p2.add(this.getPos())));

        for (EntityPlayer plr : playersWithinAABB) {

            if (shouldTargetEveryone) {
                if (!plr.capabilities.isCreativeMode && !players.contains(plr.getUniqueID().toString())) {

                    if (power > PowerConf.ffProjectorUsageToDamageEntity) {
                        plr.attackEntityFrom(DamageSourceFFSecurityKill.causeElectricDamage(), 1);
                        plr.hurtResistantTime = 0;
                        power -= PowerConf.ffProjectorUsageToDamageEntity;
                    } else {
                        break;
                    }

                }
            } else {
                if (!plr.capabilities.isCreativeMode && players.contains(plr.getUniqueID().toString())) {

                    if (power > PowerConf.ffProjectorUsageToDamageEntity) {
                        plr.attackEntityFrom(DamageSourceFFSecurityKill.causeElectricDamage(), 1);
                        plr.hurtResistantTime = 0;
                        power -= PowerConf.ffProjectorUsageToDamageEntity;
                    } else {
                        break;
                    }

                }
            }

        }

    }

    private void damageEntitiesInArea(Class entityType, BlockPos p1, BlockPos p2) {

        List<Entity> entitiesWithinAABB = worldObj.getEntitiesWithinAABB(entityType, new AxisAlignedBB(p1.add(this.getPos()), p2.add(this.getPos())));

        for (Entity entity : entitiesWithinAABB) {
            entity.attackEntityFrom(DamageSourceFFSecurityKill.causeElectricDamage(), 1);
            entity.hurtResistantTime = 0;
            power -= PowerConf.ffProjectorUsageToDamageEntity;
        }

    }

    //Refresh the forcefield decay timer
    private void refreshFFTimer(BlockPos ffPos) {

        TileEntity te = worldObj.getTileEntity(ffPos); //Get the tileentity

        if (te != null) { //Prevent it from trying to update forcefields that are not loaded (in an unloaded chunk)
            if (te instanceof TEForcefield) {
                ((TEForcefield) te).decayTimer = 300; //15s (300t)
            }
        }

    }

    void writeSyncableDataToNBT(NBTTagCompound tagCompound) {
        super.writeSyncableDataToNBT(tagCompound);

        tagCompound.setInteger("x1", minX);
        tagCompound.setInteger("y1", minY);
        tagCompound.setInteger("z1", minZ);
        tagCompound.setInteger("x2", maxX);
        tagCompound.setInteger("y2", maxY);
        tagCompound.setInteger("z2", maxZ);

        tagCompound.setInteger("uptime", uptime);
        tagCompound.setString("owner", owner);
        tagCompound.setString("ownerName", ownerName);
        tagCompound.setInteger("blockPlaceProgress", blockPlaceProgress);

        doPermissionChecks();

        if (hasSecurityUpgrade) {
            //Set players list
            NBTTagCompound players = new NBTTagCompound();
            int index = 0;
            for (Object plr : permittedPlayers) {
                List plrList = (List) plr;

                NBTTagCompound playerData = new NBTTagCompound();
                playerData.setString("uuid", (String) plrList.get(0)); //Set player UUID
                playerData.setString("name", (String) plrList.get(1)); //Set player name
                playerData.setString("groupID", (String) plrList.get(2)); //Set player group ID

                players.setTag(String.valueOf(index), playerData);
                index++;
            }
            tagCompound.setTag("permittedPlayers", players);

            //Set groups list
            NBTTagCompound groups = new NBTTagCompound();
            index = 0;
            for (Object group : permissionGroups) {
                List groupList = (List) group;

                NBTTagCompound groupData = new NBTTagCompound();
                groupData.setString("id", (String) groupList.get(0)); //Set group ID
                groupData.setBoolean("perm1", (Boolean) groupList.get(1)); //Set perm 1: Should kill players?

                groups.setTag(String.valueOf(index), groupData);
                index++;
            }
            tagCompound.setTag("permissionGroups", groups);

            //Set general permissions list
            NBTTagCompound perms = new NBTTagCompound();
            perms.setBoolean("perm1", (Boolean) generalPermissions.get(0)); //Set perm 1: Should kill hostile mobs?
            perms.setBoolean("perm2", (Boolean) generalPermissions.get(1)); //Set perm 2: Should kill peaceful mobs?
            tagCompound.setTag("generalPermissions", perms);
        }

    }

    void readSyncableDataFromNBT(NBTTagCompound tagCompound) {
        super.readSyncableDataFromNBT(tagCompound);

        uptime = tagCompound.getInteger("uptime");
        owner = tagCompound.getString("owner");
        ownerName = tagCompound.getString("ownerName");
        blockPlaceProgress = tagCompound.getInteger("blockPlaceProgress");

        if (tagCompound.getInteger("x1") != minX || tagCompound.getInteger("y1") != minY || tagCompound.getInteger("z1") != minZ ||
                tagCompound.getInteger("x2") != maxX || tagCompound.getInteger("y2") != maxY || tagCompound.getInteger("z2") != maxZ) { //The sizing changed on the server side - Recalculate the sizing
            minX = tagCompound.getInteger("x1");
            minY = tagCompound.getInteger("y1");
            minZ = tagCompound.getInteger("z1");
            maxX = tagCompound.getInteger("x2");
            maxY = tagCompound.getInteger("y2");
            maxZ = tagCompound.getInteger("z2");
            getWallBlocks();
            getInnerBlocks();
        }

        //Read players
        if (tagCompound.hasKey("permittedPlayers")) {
            NBTTagCompound players = tagCompound.getCompoundTag("permittedPlayers");
            List<List<String>> playersList = new ArrayList<List<String>>();

            int index = 0;
            while (players.hasKey(String.valueOf(index))) {

                List<String> playerData = new ArrayList<String>();

                playerData.add(((NBTTagCompound) (players.getTag(String.valueOf(index)))).getString("uuid"));
                playerData.add(((NBTTagCompound) (players.getTag(String.valueOf(index)))).getString("name"));
                playerData.add(((NBTTagCompound) (players.getTag(String.valueOf(index)))).getString("groupID"));

                playersList.add(playerData);

                index++;
            }
            this.permittedPlayers = playersList;
        }

        //Read groups
        if (tagCompound.hasKey("permissionGroups")) {
            NBTTagCompound groups = tagCompound.getCompoundTag("permissionGroups");
            List<List<Object>> groupsList = new ArrayList<List<Object>>();

            int index = 0;
            while (groups.hasKey(String.valueOf(index))) {

                List<Object> groupData = new ArrayList<Object>();

                groupData.add(((NBTTagCompound) (groups.getTag(String.valueOf(index)))).getString("id"));
                groupData.add(((NBTTagCompound) (groups.getTag(String.valueOf(index)))).getBoolean("perm1")); //Get perm 1: Should kill players?

                groupsList.add(groupData);

                index++;
            }
            this.permissionGroups = groupsList;
        }

        //Read general permissions
        if (tagCompound.hasKey("generalPermissions")) {
            NBTTagCompound perms = tagCompound.getCompoundTag("generalPermissions");
            this.generalPermissions.clear();
            this.generalPermissions.add(perms.getBoolean("perm1")); //Get perm 1: Should kill hostile mobs?
            this.generalPermissions.add(perms.getBoolean("perm2")); //Get perm 1: Should kill peaceful mobs?
        }

    }

    private void writePowerStatsToNBT(NBTTagCompound tagCompound) {

        List<Integer> powerUsagePerTickForPastMinuteForNBT = new ArrayList<Integer>();
        for (Double item : powerUsagePerTickForPastMinute) {
            powerUsagePerTickForPastMinuteForNBT.add((int) (item * 100));
        }
        tagCompound.setIntArray("powerUsagePerTickForPastMinute", ArrayUtils.toPrimitive(
                Arrays.copyOf(powerUsagePerTickForPastMinuteForNBT.toArray(), powerUsagePerTickForPastMinuteForNBT.toArray().length, Integer[].class)));

        List<Integer> powerUsagePerSecondForPastHalfHourForNBT = new ArrayList<Integer>();
        for (Double item : powerUsagePerSecondForPastHalfHour) {
            powerUsagePerSecondForPastHalfHourForNBT.add((int) (item * 100));
        }
        tagCompound.setIntArray("powerUsagePerSecondForPastHalfHour", ArrayUtils.toPrimitive(
                Arrays.copyOf(powerUsagePerSecondForPastHalfHourForNBT.toArray(), powerUsagePerSecondForPastHalfHourForNBT.toArray().length, Integer[].class)));

        tagCompound.setInteger("tickTimeSinceLastSecond", tickTimeSinceLastSecond);
        tagCompound.setDouble("powerUsageSinceLastSecond", powerUsageSinceLastSecond);

    }

    public void readPowerStatsFromNBT(NBTTagCompound tagCompound) {

        int[] powerUsagePerTickForPastMinuteFromNBT = tagCompound.getIntArray("powerUsagePerTickForPastMinute");
        List<Double> powerUsagePerTickForPastMinuteFromNBTAsDoubleList = new ArrayList<Double>();
        for (int item : powerUsagePerTickForPastMinuteFromNBT) {
            powerUsagePerTickForPastMinuteFromNBTAsDoubleList.add(item / 100d);
        }
        powerUsagePerTickForPastMinute.clear();
        powerUsagePerTickForPastMinute.addAll(powerUsagePerTickForPastMinuteFromNBTAsDoubleList);

        int[] powerUsagePerSecondForPastHalfHourFromNBT = tagCompound.getIntArray("powerUsagePerSecondForPastHalfHour");
        List<Double> powerUsagePerSecondForPastHalfHourFromNBTAsDoubleList = new ArrayList<Double>();
        for (int item : powerUsagePerSecondForPastHalfHourFromNBT) {
            powerUsagePerSecondForPastHalfHourFromNBTAsDoubleList.add(item / 100d);
        }
        powerUsagePerSecondForPastHalfHour.clear();
        powerUsagePerSecondForPastHalfHour.addAll(powerUsagePerSecondForPastHalfHourFromNBTAsDoubleList);

        tickTimeSinceLastSecond = tagCompound.getInteger("tickTimeSinceLastSecond");
        powerUsageSinceLastSecond = tagCompound.getDouble("powerUsageSinceLastSecond");

    }

    public NBTTagIntArray getPowerUsagePerTickForPastMinuteFromNBT() {

        List<Integer> powerUsagePerTickForPastMinuteForNBT = new ArrayList<Integer>();

        for (Double item : powerUsagePerTickForPastMinute) {
            powerUsagePerTickForPastMinuteForNBT.add((int) (item * 100));
        }

        return new NBTTagIntArray(ArrayUtils.toPrimitive(
                Arrays.copyOf(powerUsagePerTickForPastMinuteForNBT.toArray(), powerUsagePerTickForPastMinuteForNBT.toArray().length, Integer[].class)));

    }

    public NBTTagIntArray getPowerUsagePerSecondForPastHalfHourFromNBT() {

        List<Integer> powerUsagePerSecondForPastHalfHourForNBT = new ArrayList<Integer>();

        for (Double item : powerUsagePerSecondForPastHalfHour) {
            powerUsagePerSecondForPastHalfHourForNBT.add((int) (item * 100));
        }

        return new NBTTagIntArray(ArrayUtils.toPrimitive(
                Arrays.copyOf(powerUsagePerSecondForPastHalfHourForNBT.toArray(), powerUsagePerSecondForPastHalfHourForNBT.toArray().length, Integer[].class)));

    }

}
