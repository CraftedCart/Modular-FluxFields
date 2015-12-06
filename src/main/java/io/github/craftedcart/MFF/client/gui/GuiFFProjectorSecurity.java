package io.github.craftedcart.MFF.client.gui;

import io.github.craftedcart.MFF.container.ContainerFFProjectorInfo;
import io.github.craftedcart.MFF.handler.GuiHandler;
import io.github.craftedcart.MFF.handler.NetworkHandler;
import io.github.craftedcart.MFF.network.MessageRequestOpenGui;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by CraftedCart on 06/12/2015 (DD/MM/YYYY)
 */

public class GuiFFProjectorSecurity extends GuiContainer {

    private IInventory playerInv;
    private TEFFProjector te;
    private EntityPlayer player;

    //Gui Elements
    private GuiTextField addPlayerTextField;

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

        this.fontRendererObj.drawString("Power: " + String.format("%012.2f", power) + " / " + String.format("%09.2f", PowerConf.ffProjectorMaxPower) + " FE",
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
            this.addPlayerTextField.drawTextBox();
        }



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
    protected void keyTyped(char typedChar, int keyCode) {
        this.addPlayerTextField.textboxKeyTyped(typedChar, keyCode);
        if(!(keyCode == Keyboard.KEY_E  &&  this.addPlayerTextField.isFocused())){
            try {
                super.keyTyped(typedChar, keyCode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
