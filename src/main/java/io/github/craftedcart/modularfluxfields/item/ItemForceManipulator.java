package io.github.craftedcart.modularfluxfields.item;

import io.github.craftedcart.modularfluxfields.ModModularFluxFields;
import io.github.craftedcart.mcliquidui.util.UIColor;
import io.github.craftedcart.modularfluxfields.handler.GuiHandler;
import io.github.craftedcart.modularfluxfields.reference.Names;
import io.github.craftedcart.modularfluxfields.tileentity.TEPowerRelay;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * Created by CraftedCart on 02/03/2016 (DD/MM/YYYY)
 */
public class ItemForceManipulator extends ModItem {

    public ItemForceManipulator() {
        super();
        this.setUnlocalizedName(Names.ItemForceManipulator);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TEPowerRelay) {
            Minecraft.getMinecraft().thePlayer.openGui(ModModularFluxFields.instance, GuiHandler.ConfigPowerRelay_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;

    }

    public static class renderWorldOverlayHandler {

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void renderWorldOverlay(DrawBlockHighlightEvent e) {

            if (Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null &&
                    Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemForceManipulator &&
                    e.target.getBlockPos() != null) {
                GL11.glPushMatrix();

                Vec3 target = new Vec3(e.target.getBlockPos().getX(), e.target.getBlockPos().getY(), e.target.getBlockPos().getZ());

                target = target.subtract(Minecraft.getMinecraft().thePlayer.getPositionVector());

                GL11.glTranslated(target.xCoord + 0.5, target.yCoord + 0.5, target.zCoord+ 0.5);
                GL11.glLineWidth(4);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glBegin(GL11.GL_LINES);
                {
                    //X
                    GL11.glColor3d(UIColor.matRed().r, UIColor.matRed().g, UIColor.matRed().b);
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(1, 0, 0);
                    //Y
                    GL11.glColor3d(UIColor.matGreen().r, UIColor.matGreen().g, UIColor.matGreen().b);
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, 1, 0);
                    //Z
                    GL11.glColor3d(UIColor.matBlue().r, UIColor.matBlue().g, UIColor.matBlue().b);
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, 0, 1);
                }
                GL11.glEnd();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBegin(GL11.GL_LINES);
                {
                    //X
                    GL11.glColor4d(UIColor.matRed().r, UIColor.matRed().g, UIColor.matRed().b, 0.25);
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(1, 0, 0);
                    //Y
                    GL11.glColor4d(UIColor.matGreen().r, UIColor.matGreen().g, UIColor.matGreen().b, 0.25);
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, 1, 0);
                    //Z
                    GL11.glColor4d(UIColor.matBlue().r, UIColor.matBlue().g, UIColor.matBlue().b, 0.25);
                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, 0, 1);
                }
                GL11.glEnd();
                GL11.glTranslated(-0.5, -0.5, -0.5);
                GL11.glLineWidth(2);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                //<editor-fold desc="Black box border">
                GL11.glBegin(GL11.GL_LINES);
                {
                    GL11.glColor4d(0, 0, 0, 0.5);

                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(1, 0, 0);

                    GL11.glVertex3d(1, 0, 0);
                    GL11.glVertex3d(1, 0, 1);

                    GL11.glVertex3d(1, 0, 1);
                    GL11.glVertex3d(0, 0, 1);

                    GL11.glVertex3d(0, 0, 1);
                    GL11.glVertex3d(0, 0, 0);

                    /////////////////////////

                    GL11.glVertex3d(0, 1, 0);
                    GL11.glVertex3d(1, 1, 0);

                    GL11.glVertex3d(1, 1, 0);
                    GL11.glVertex3d(1, 1, 1);

                    GL11.glVertex3d(1, 1, 1);
                    GL11.glVertex3d(0, 1, 1);

                    GL11.glVertex3d(0, 1, 1);
                    GL11.glVertex3d(0, 1, 0);

                    /////////////////////////

                    GL11.glVertex3d(0, 0, 0);
                    GL11.glVertex3d(0, 1, 0);

                    GL11.glVertex3d(1, 0, 0);
                    GL11.glVertex3d(1, 1, 0);

                    GL11.glVertex3d(1, 0, 1);
                    GL11.glVertex3d(1, 1, 1);

                    GL11.glVertex3d(0, 0, 1);
                    GL11.glVertex3d(0, 1, 1);

                }
                GL11.glEnd();
                //</editor-fold>
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                GL11.glPopMatrix();
            }

        }

    }

}
