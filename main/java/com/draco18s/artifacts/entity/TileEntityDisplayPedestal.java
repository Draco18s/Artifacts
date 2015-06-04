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
import com.draco18s.artifacts.network.SToCMessage;

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

	private ItemStack contents = null;
	public EntityItem itemEnt = null;
	public UUID ownerUUID = new UUID(0, 0);
	public String ownerName = "";
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
			return contents;
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
		if (this.contents != null)
		{
			ItemStack itemstack;

			if (this.contents.stackSize <= j)
			{
				itemstack = this.contents;
				this.contents = null;
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack = this.contents.splitStack(j);

				if (this.contents.stackSize == 0)
				{
					this.contents = null;
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
		if (this.contents != null)
		{
			ItemStack itemstack = this.contents;
			this.contents = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.contents = itemstack;

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
		boolean sendNamePacket = false;
		
		if(entityplayer.worldObj.isRemote) {
			return false;
		}
		
		if( (ownerName == null || ownerName.equals("")) && (ownerUUID == null || ownerUUID.equals(new UUID(0, 0))) ) {
			ownerName = entityplayer.getCommandSenderName();
			ownerUUID = entityplayer.getUniqueID();
			this.markDirty();
			sendNamePacket = true;
		}
		
		if(ownerName.equals(entityplayer.getCommandSenderName()) && (ownerName == null || !ownerUUID.equals(entityplayer.getUniqueID()))) {
			ownerUUID = entityplayer.getUniqueID();
			this.markDirty();
		}
		
		if((ownerName == null || !ownerName.equals(entityplayer.getCommandSenderName())) && ownerUUID.equals(entityplayer.getUniqueID())) {
			ownerName = entityplayer.getCommandSenderName();
			this.markDirty();
			sendNamePacket = true;
		}
		
		if(sendNamePacket) {
			PacketBuffer out = new PacketBuffer(Unpooled.buffer());
			out.writeInt(PacketHandlerClient.PEDESTAL);
			out.writeInt(xCoord);
			out.writeInt(yCoord);
			out.writeInt(zCoord);
			out.writeInt(ownerName.length());
			for(int i = 0; i < ownerName.length(); i++) {
				out.writeChar(ownerName.charAt(i));
			}
			SToCMessage namePacket = new SToCMessage(out);
			DragonArtifacts.artifactNetworkWrapper.sendToAll(namePacket);
		}
		
		if(!(ownerName.equals(entityplayer.getCommandSenderName()) && ownerUUID.equals(entityplayer.getUniqueID()))) {
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
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		NBTTagList nbttaglist = tag.getTagList("Items", 10);
		this.contents = null;

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			this.contents = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
		ownerName = tag.getString("OwnerName");
		ownerUUID = new UUID(tag.getLong("OwnerUUIDMost"), tag.getLong("OwnerUUIDLeast"));
		rotation = tag.getInteger("rotation");
		markDirty();
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		NBTTagList nbttaglist = new NBTTagList();

		if (this.contents != null)
		{
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setByte("Slot", (byte) 0);
			this.contents.writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		tag.setTag("Items", nbttaglist);
		tag.setString("OwnerName", ownerName);
		tag.setLong("OwnerUUIDLeast", ownerUUID.getLeastSignificantBits());
		tag.setLong("OwnerUUIDMost", ownerUUID.getMostSignificantBits());
		tag.setInteger("rotation", rotation);
	}

	public String getModelTexture() {
		return "artifacts:textures/blocks/pedestal.png";
	}

	@Override
	//Used to be onInventoryChanged
	public void markDirty() {
		super.markDirty();
		if(worldObj != null && contents != null) {
			itemEnt = new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, contents);
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
