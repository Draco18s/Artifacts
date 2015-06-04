package com.draco18s.artifacts.item;

import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.draco18s.artifacts.entity.TileEntityDisplayPedestal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPedestalKey extends Item {
	
	public static Item pedestalKeyItem;
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		this.itemIcon = register.registerIcon("Artifacts:pedestal_key");
	}
	
	//Unlock the pedestal if there is one.
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
		
		if(is != null) {
			
			TileEntity te = world.getTileEntity(x, y, z);
			
			if(te != null && te instanceof TileEntityDisplayPedestal) {
				
				TileEntityDisplayPedestal ted = (TileEntityDisplayPedestal) te;
				
				if( (ted.ownerName == null || ted.ownerName.equals("")) && (ted.ownerUUID == null || ted.ownerUUID.equals(new UUID(0, 0))) ) {
					return false;
				}
				
				ted.ownerUUID = new UUID(0, 0);
				ted.ownerName = "";
				
				if(!player.capabilities.isCreativeMode) {
					is.stackSize--;
				}
				
				world.playSoundEffect((double)x + 0.5, (double)y + 0.5, (double)z + 0.5, "random.door_close", 1.0f, 1.5f);
				
				return true;
			}
			
			
		}
		
		return false;
    }
	
}
