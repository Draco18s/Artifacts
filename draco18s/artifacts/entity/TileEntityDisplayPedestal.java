package draco18s.artifacts.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cpw.mods.fml.common.network.PacketDispatcher;
import draco18s.artifacts.item.ItemArtifact;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDisplayPedestal extends TileEntity implements IInventory {

	private ItemStack[] contents = new ItemStack[1];
	public EntityItem itemEnt = null;
	public String owner = "";
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
				this.onInventoryChanged();
				return itemstack;
			}
			else
			{
				itemstack = this.contents[i].splitStack(j);

				if (this.contents[i].stackSize == 0)
				{
					this.contents[i] = null;
				}

				this.onInventoryChanged();
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

		this.onInventoryChanged();
	}

	@Override
	public String getInvName() {
		return "Pedestal";
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if(owner.equals("")) {
			owner = entityplayer.username;
			ByteArrayOutputStream bt = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(bt);
			try
			{
				out.writeInt(256);
				out.writeInt(this.xCoord);
				out.writeInt(this.yCoord);
				out.writeInt(this.zCoord);
				out.writeInt(owner.length());
				out.writeChars(owner);
				Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 35, this.worldObj.provider.dimensionId, packet);
			}
			catch (IOException ex)
			{
				System.out.println("couldnt send packet!");
			}
		}
		else if(!owner.equals(entityplayer.username)) {
			return false;
		}
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {

	}

	@Override
	public void closeChest() {

	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack.getItem() == ItemArtifact.instance;
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
		this.contents = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < this.contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		owner = par1NBTTagCompound.getString("Owner");
		rotation = par1NBTTagCompound.getInteger("rotation");
		onInventoryChanged();
	}

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
		par1NBTTagCompound.setString("Owner", owner);
		par1NBTTagCompound.setInteger("rotation", rotation);
	}

	public String getModelTexture() {
		return "artifacts:textures/blocks/pedestal.png";
	}

	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
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

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}
}
