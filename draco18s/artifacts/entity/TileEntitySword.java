package draco18s.artifacts.entity;

import draco18s.artifacts.item.ItemFakeSwordRenderable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySword extends TileEntity {
	public EntityItem itemEnt = null;
	public int metadata = 0;
	private ItemStack[] contents = new ItemStack[1];
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		if(itemEnt != null) {
			itemEnt.hoverStart = 0;
			itemEnt.rotationYaw = 0;
			itemEnt.motionX = 0;
			itemEnt.motionY = 0;
			itemEnt.motionZ = 0;
			if(itemEnt.age > 8) {
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			}
			++itemEnt.age;
		}
	}
	
	public void setSword(ItemStack stack, int meta) {
		metadata = meta;
		if(stack.getItem() == Item.swordWood)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.wood);
		else if(stack.getItem() == Item.swordStone)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.stone);
		else if(stack.getItem() == Item.swordIron)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.iron);
		else if(stack.getItem() == Item.swordGold)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.gold);
		else if(stack.getItem() == Item.swordDiamond)
			contents[0] = new ItemStack(ItemFakeSwordRenderable.diamond);
		onInventoryChanged();
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
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
		this.contents = new ItemStack[1];

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			if (j >= 0 && j < this.contents.length)
			{
				this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		metadata = par1NBTTagCompound.getInteger("Meta");
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
				this.contents [i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		par1NBTTagCompound.setTag("Items", nbttaglist);
		par1NBTTagCompound.setInteger("Meta", metadata);
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
