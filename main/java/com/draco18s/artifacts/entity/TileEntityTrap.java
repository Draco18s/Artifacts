package com.draco18s.artifacts.entity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import com.draco18s.artifacts.block.BlockAntibuilder;
import com.draco18s.artifacts.block.BlockTrap;
import com.draco18s.artifacts.block.BlockSword;

import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
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
    @Override
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
    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.dispenserContents[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.dispenserContents[par1] != null)
        {
            ItemStack itemstack;

            if (this.dispenserContents[par1].stackSize <= par2)
            {
                itemstack = this.dispenserContents[par1];
                this.dispenserContents[par1] = null;
                this.markDirty();
                return itemstack;
            }
            else
            {
                itemstack = this.dispenserContents[par1].splitStack(par2);

                if (this.dispenserContents[par1].stackSize <= 0)
                {
                    this.dispenserContents[par1] = null;
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

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    @Override
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

    @Override
    //getRandomStackFromInventory
    public int func_146017_i()
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
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.dispenserContents[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    /**
     * Add item stack in first available inventory slot
     */
    @Override
    //addItem
    public int func_146019_a(ItemStack par1ItemStack)
    {
        for (int i = 0; i < this.dispenserContents.length; ++i)
        {
            if (this.dispenserContents[i] == null || Block.getBlockFromItem(this.dispenserContents[i].getItem()) == Blocks.air)
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
    @Override
    public String getInventoryName()
    {
        return this.hasCustomInventoryName() ? this.customName : "container.dispenser";
    }

    @Override
    //setCustomName
    public void func_146018_a(String par1Str)
    {
        this.customName = par1Str;
    }

    /**
     * If this returns false, the inventory name will be used as an unlocalized name, and translated into the player's
     * language. Otherwise it will be used directly.
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return this.customName != null;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
        this.dispenserContents = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.getCompoundTagAt(i);
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
    @Override
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

        if (this.hasCustomInventoryName())
        {
            par1NBTTagCompound.setString("CustomName", this.customName);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
    	if(numab > 0) {
    		for(int a = numab-1; a >=0; a--) {
    			Vec3 v = antibuilders.get(a);
    			Block wBlock = worldObj.getBlock((int)v.xCoord, (int)v.yCoord, (int)v.zCoord);
    			if(wBlock == BlockAntibuilder.instance) {
    				return false;
    			}
    		}
    	}
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : par1EntityPlayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
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

	public void startSwordRender(World world, int xInt, int yInt, int zInt, int metadata, ItemStack itemStack) {

		if(world.getBlock(xInt, yInt+1, zInt) != BlockTrap.instance && world.isAirBlock(xInt, yInt+1, zInt)) {
			world.setBlock(xInt, yInt+1, zInt, BlockSword.instance);
			TileEntity te = world.getTileEntity(xInt, yInt+1, zInt);
			if(te != null && te instanceof TileEntitySword) {
				TileEntitySword tes = (TileEntitySword) te;
				//te.blockMetadata = metadata;
				//te.itemEnt = new EntityItem(world, xInt, yInt, zInt, par2ItemStack);
				tes.setSword(itemStack, metadata);
			}
			else {
				System.out.println("TE above was null!");
			}
		}
		else if((metadata&7) == 0 && world.isAirBlock(xInt, yInt-1, zInt)) {
			world.setBlock(xInt, yInt-1, zInt, BlockSword.instance);
			TileEntity te = world.getTileEntity(xInt, yInt-1, zInt);
			if(te != null && te instanceof TileEntitySword) {
				TileEntitySword tes = (TileEntitySword) te;
				//te.blockMetadata = metadata;
				//te.itemEnt = new EntityItem(world, xInt, yInt, zInt, par2ItemStack);
				tes.setSword(itemStack, metadata);
			}
			else {
				System.out.println("TE below was null!");
			}
		}
	}
}
