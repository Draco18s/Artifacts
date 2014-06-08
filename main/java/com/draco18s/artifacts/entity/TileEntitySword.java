package com.draco18s.artifacts.entity;

import com.draco18s.artifacts.item.ItemFakeSwordRenderable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntitySword extends TileEntity {
	public EntityItem itemEnt = null;
	public int metadata = 0;
	private ItemStack[] contents = new ItemStack[1];
	
	public TileEntitySword() {
		itemEnt = new EntityItem(worldObj);
		contents[0] = new ItemStack(ItemFakeSwordRenderable.wood);
		itemEnt.setEntityItemStack(contents[0]);
	}
	
	public void setSword(ItemStack stack, int meta) {
		metadata = meta;
		if(stack.getItem() == Items.wooden_sword)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.wood);
		else if(stack.getItem() == Items.stone_sword)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.stone);
		else if(stack.getItem() == Items.iron_sword)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.iron);
		else if(stack.getItem() == Items.golden_sword)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.gold);
		else if(stack.getItem() == Items.diamond_sword)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.diamond);
		markDirty();
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if(itemEnt != null) {
			itemEnt.hoverStart = 0;
			itemEnt.rotationYaw = 0;
			itemEnt.motionX = 0;
			itemEnt.motionY = 0;
			itemEnt.motionZ = 0;
			if(itemEnt.age >= 9) {
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			}
			++itemEnt.age;
		}
	}
	
	@Override
	//was onInventoryChanged before.
	public void markDirty() {
		super.markDirty();
		if(contents[0] != null) {
			itemEnt = new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, contents[0]);
			itemEnt.hoverStart = 0;
			itemEnt.rotationYaw = 0;
			itemEnt.motionX = 0;
			itemEnt.motionY = 0;
			itemEnt.motionZ = 0;
		}
		else {
			itemEnt = null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
		this.contents = new ItemStack[1];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < this.contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		metadata = par1NBTTagCompound.getInteger("Meta");
		markDirty();
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.contents.length; ++i)
		{
			if (this.contents[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				this.contents [i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		par1NBTTagCompound.setTag("Items", nbttaglist);
		par1NBTTagCompound.setInteger("Meta", metadata);
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return true;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return AxisAlignedBB.getBoundingBox(xCoord+0.4, yCoord+0.0, zCoord+0.4, xCoord+0.6, yCoord+0.5, zCoord+0.6);
	}
}
