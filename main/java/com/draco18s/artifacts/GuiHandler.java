package com.draco18s.artifacts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

import com.draco18s.artifacts.client.GuiContPedestal;
import com.draco18s.artifacts.entity.TileEntityDisplayPedestal;
import com.draco18s.artifacts.inventory.ContainerPedestal;

public class GuiHandler implements IGuiHandler {
	@Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.println("This occurrs (client)");
		if(id == 0) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntityDisplayPedestal){
				return new ContainerPedestal(player.inventory, (TileEntityDisplayPedestal) tileEntity);
			}
		}
		return null;
    }

    //returns an instance of the Gui you made earlier
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.println("This occurrs (server)");
    	if(id == 0) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if(tileEntity instanceof TileEntityDisplayPedestal){
				return new GuiContPedestal(player.inventory, (TileEntityDisplayPedestal) tileEntity);
			}
    	}
		return null;
    }
}
