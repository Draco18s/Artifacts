package draco18s.artifacts.entity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;
import draco18s.artifacts.block.BlockAntibuilder;
import draco18s.artifacts.block.BlockTrap;
import draco18s.artifacts.block.BlockSword;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileEntityTrap extends TileEntityDispenser
{
    private ItemStack[] dispenserContents = new ItemStack[9];
    private ArrayList<Vec3> antibuilders = new ArrayList<Vec3>();
    private int numab = 0;

    /**
     * random number generator for instance. Used in random item stack selection.
     */
    private Random dispenserRandom = new Random();
    protected String customName;
    
    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 9;
    }
    
    public void setAntibuilder(int x, int y, int z) {
    	antibuilders.add(Vec3.createVectorHelper(x, y, z));
    	numab++;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1)
    {
        return this.dispenserContents[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.dispenserContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.dispenserContents[par1].stackSize <= par2)
            {
                itemstack = this.dispenserContents[par1];
                this.dispenserContents[par1] = null;
                this.onInventoryChanged();
                return itemstack;
            }
            else
            {
                itemstack = this.dispenserContents[par1].splitStack(par2);

                if (this.dispenserContents[par1].stackSize == 0)
                {
                    this.dispenserContents[par1] = null;
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

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.dispenserContents[par1] != null)
        {
            ItemStack itemstack = this.dispenserContents[par1];
            this.dispenserContents[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    public int getRandomStackFromInventory()
    {
        int i = -1;
        int j = 1;

        for (int k = 0; k < this.dispenserContents.length; ++k)
        {
            if (this.dispenserContents[k] != null && this.dispenserRandom.nextInt(j++) == 0)
            {
                i = k;
            }
        }

        return i;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.dispenserContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    /**
     * Add item stack in first available inventory slot
     */
    public int addItem(ItemStack par1ItemStack)
    {
        for (int i = 0; i < this.dispenserContents.length; ++i)
        {
            if (this.dispenserContents[i] == null || this.dispenserContents[i].itemID == 0)
            {
                this.setInventorySlotContents(i, par1ItemStack);
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns the name of the inventory.
     */
    public String getInvName()
    {
        return this.isInvNameLocalized() ? this.customName : "container.dispenser";
    }

    public void setCustomName(String par1Str)
    {
        this.customName = par1Str;
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    public boolean isInvNameLocalized()
    {
        return this.customName != null;
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.dispenserContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.dispenserContents.length)
            {
                this.dispenserContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        
        if (par1NBTTagCompound.hasKey("CustomName"))
        {
            this.customName = par1NBTTagCompound.getString("CustomName");
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.dispenserContents.length; ++i)
        {
            if (this.dispenserContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.dispenserContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);

        if (this.isInvNameLocalized())
        {
            par1NBTTagCompound.setString("CustomName", this.customName);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
    	if(numab > 0) {
    		for(int a = numab-1; a >=0; a--) {
    			Vec3 v = antibuilders.get(a);
    			int wid = worldObj.getBlockId((int)v.xCoord, (int)v.yCoord, (int)v.zCoord);
    			if(wid == BlockAntibuilder.instance.blockID) {
    				return false;
    			}
    		}
    	}
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack)
    {
        return true;
    }
	
	@Override
	public void updateEntity() {
		/*if(swordObj != null) {
			++swordObj.age;
			if(swordObj.age > 10) {
				endSwordRender();
			}
		}*/
	}
	
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
	}

	public void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}

	public void startSwordRender(World world, int xInt, int yInt, int zInt, int metadata, ItemStack par2ItemStack) {
		if(world.getBlockId(xInt, yInt+1, zInt) != BlockTrap.instance.blockID) {
			world.setBlock(xInt, yInt+1, zInt, BlockSword.instance.blockID);
			TileEntitySword te = (TileEntitySword)world.getBlockTileEntity(xInt, yInt+1, zInt);
			if(te != null) {
				//te.blockMetadata = metadata;
				//te.itemEnt = new EntityItem(world, xInt, yInt, zInt, par2ItemStack);
				te.setSword(par2ItemStack, metadata);
			}
			else {
				System.out.println("TE was null!");
			}
		}
		else if((metadata&7) == 0 && world.getBlockId(xInt, yInt-1, zInt) == 0) {
			world.setBlock(xInt, yInt-1, zInt, BlockSword.instance.blockID);
			TileEntitySword te = (TileEntitySword)world.getBlockTileEntity(xInt, yInt-1, zInt);
			if(te != null) {
				//te.blockMetadata = metadata;
				//te.itemEnt = new EntityItem(world, xInt, yInt, zInt, par2ItemStack);
				te.setSword(par2ItemStack, metadata);
			}
			else {
				System.out.println("TE was null!");
			}
		}
	}
}
