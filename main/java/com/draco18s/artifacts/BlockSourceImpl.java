package com.draco18s.artifacts;

import com.draco18s.artifacts.api.internals.IBlockSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSourceImpl implements IBlockSource
{
    private final World worldObj;
    private final int xPos;
    private final int yPos;
    private final int zPos;
    private final int loc;

    public BlockSourceImpl(World par1World, int par2, int par3, int par4, int i)
    {
        this.worldObj = par1World;
        this.xPos = par2;
        this.yPos = par3;
        this.zPos = par4;
        this.loc = i;
    }

    public World getWorld()
    {
        return this.worldObj;
    }

    public double getX()
    {
    	double offset = 1D/8;
    	offset += (loc%2 == 0)?0.5D:0D;
        return (double)this.xPos + offset;
    }

    public double getY()
    {
    	double offset=0;
    	switch(loc) {
    		case 1:
    			offset = 0;
    			break;
    		case 2:
    			offset = 1D/4D;
    			break;
    		case 3:
    			offset = 2D/4D;
    			break;
    		case 4:
    			offset = 3D/4D;
    			break;
    	}
    	offset += 1D/8D;
        return (double)this.yPos + offset;
    }

    public double getZ()
    {
    	double offset = 1D/8;
    	offset += (loc%2 == 0)?0.5D:0D;
        return (double)this.zPos + offset;
    }

    public int getXInt()
    {
        return this.xPos;
    }

    public int getYInt()
    {
        return this.yPos;
    }

    public int getZInt()
    {
        return this.zPos;
    }

    public int getBlockMetadata()
    {
        return this.worldObj.getBlockMetadata(this.xPos, this.yPos, this.zPos);
    }

    public TileEntity getBlockTileEntity()
    {
        return this.worldObj.getTileEntity(this.xPos, this.yPos, this.zPos);
    }
}
