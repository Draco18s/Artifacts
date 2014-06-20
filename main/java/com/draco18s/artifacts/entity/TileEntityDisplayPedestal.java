package com.draco18s.artifacts.entity;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.item.ItemArtifact;
import com.draco18s.artifacts.network.PacketHandlerClient;
import com.draco18s.artifacts.network.SToCMessageGeneral;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDisplayPedestal extends TileEntity implements IInventory {

	private ItemStack[] contents = new ItemStack[1];
	public EntityItem itemEnt = null;
	public UUID owner = new UUID(0,0);
	public int rotation = 0;

	public TileEntityDisplayPedestal() {
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if(i > 0)
			return null;
		else
			return contents[0];
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(itemEnt != null) {
			if(itemEnt.age > 359)
				itemEnt.age = 0;
			itemEnt.rotationYaw = itemEnt.age;
		}
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (this.contents[i] != null)
		{
			ItemStack itemstack;

			if (this.contents[i].stackSize <= j)
			{
				itemstack = this.contents[i];
				this.contents[i] = null;
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack = this.contents[i].splitStack(j);

				if (this.contents[i].stackSize == 0)
				{
					this.contents[i] = null;
				}

				this.markDirty();
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.contents[i] != null)
		{
			ItemStack itemstack = this.contents[i];
			this.contents[i] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.contents[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		return "Pedestal";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if(owner == null || owner.equals(new UUID(0, 0))) {
			owner = entityplayer.getPersistentID();
			
			try
			{
				PacketBuffer out = new PacketBuffer(Unpooled.buffer());
				out.writeInt(PacketHandlerClient.PEDESTAL);
				out.writeInt(this.xCoord);
				out.writeInt(this.yCoord);
				out.writeInt(this.zCoord);
				out.writeLong(owner.getLeastSignificantBits());
				out.writeLong(owner.getMostSignificantBits());
				SToCMessageGeneral packet = new SToCMessageGeneral(out);
				DragonArtifacts.artifactNetworkWrapper.sendToAllAround(packet, new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32));
			}
			catch (Exception ex)
			{
				System.out.println("couldnt send packet!");
				ex.printStackTrace();
			}
		}
		else if(!owner.equals(entityplayer.getPersistentID())) {
			return false;
		}
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack.getItem() == ItemArtifact.instance;
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		NBTTagList nbttaglist = tagCompound.getTagList("Items", 10);
		this.contents = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < this.contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		owner = new UUID(tagCompound.getLong("OwnerUUIDMost"), tagCompound.getLong("OwnerUUIDLeast"));
		rotation = tagCompound.getInteger("rotation");
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
				this.contents[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		par1NBTTagCompound.setTag("Items", nbttaglist);
		par1NBTTagCompound.setLong("OwnerUUIDLeast", owner.getLeastSignificantBits());
		par1NBTTagCompound.setLong("OwnerUUIDMost", owner.getMostSignificantBits());
		par1NBTTagCompound.setInteger("rotation", rotation);
	}

	public String getModelTexture() {
		return "artifacts:textures/blocks/pedestal.png";
	}

	@Override
	//Used to be onInventoryChanged
	public void markDirty() {
		super.markDirty();
		if(worldObj != null && contents[0] != null) {
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
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}
}
