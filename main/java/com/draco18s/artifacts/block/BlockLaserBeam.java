package com.draco18s.artifacts.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLaserBeam extends Block {

	public static Block instance;
	public int renderID;

	public BlockLaserBeam() {
		super(Material.circuits);
		this.setTickRandomly(true);
        this.setBlockTextureName("artifacts:laser");
	}
	
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
		return null;
    }
	
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }
	
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
		//System.out.println("Collide");
        if (!par1World.isRemote)
        {
            if ((par1World.getBlockMetadata(par2, par3, par4) & 1) != 1)
            {
                this.updateTripWireState(par1World, par2, par3, par4);
            }
        }
    }
	
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
        	int meta = par1World.getBlockMetadata(par2, par3, par4);
        	boolean ff = false;
            if ((meta & 1) == 1)
            {
                ff = this.updateTripWireState(par1World, par2, par3, par4);
            }
            
            int i1 = 0;
            boolean foundSource = false;
            while (i1 < 4)
            {
                int j1 = 1;

                while (true)
                {
                    if (j1 < 16)
                    {
                    	int k1 = par2 + Direction.offsetX[i1] * j1;
                        int l1 = par4 + Direction.offsetZ[i1] * j1;
                        

                        Block i2 = par1World.getBlock(k1, par3, l1);

                        if (i2 == BlockLaserBeamSource.instance)
                        {
                        	int mm = par1World.getBlockMetadata(k1, par3, l1)&3;
                        	//System.out.println(i1 + " [" + Direction.rotateOpposite[i1] + "] " + mm);
                        	if(mm == Direction.rotateOpposite[i1]) {
	                            foundSource = true;
	                            BlockLaserBeamSource.instance.rebuildLaser(par1World, k1, par3, l1, mm);
                        	}
                        }
                        else if (i2 == this.instance || !par1World.getBlock(k1, par3, l1).isOpaqueCube())
                        {
                            ++j1;
                            continue;
                        }
                    }

                    ++i1;
                    break;
                }
            }
            if(!foundSource) {
            	par1World.setBlockToAir(par2, par3, par4);
            }
            else if(!ff) {
            	//par1World.setBlockMetadataWithNotify(par2, par3, par4, meta, 3);
            	//notifyNeighborOfChange(par1World, par2, par3, par4);
            }
        }
    }
	
	private void notifyNeighborOfChange(World par1World, int par2, int par3, int par4)
    {
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.instance);

        par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.instance);
        par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.instance);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.instance);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.instance);
    }

    private boolean updateTripWireState(World par1World, int par2, int par3, int par4)
    {
        int l = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = (l & 1) == 1;
        boolean flag1 = false;
        List list = par1World.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ));

        if (!list.isEmpty())
        {
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();
                if (entity instanceof EntityLivingBase && !entity.doesEntityNotTriggerPressurePlate())
                {
                    flag1 = true;
                    break;
                }
            }
        }

        if (flag1 && !flag)
        {
            l |= 1;
        }

        if (!flag1 && flag)
        {
            l &= -2;
        }
        boolean ff = false;
        if (flag1 != flag)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 3);
            this.func_72149_e(par1World, par2, par3, par4, l);
            ff = true;
        }

        if (flag1)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, this.instance, this.tickRate(par1World));
        }
        return ff;
    }
    
    private void func_72149_e(World par1World, int par2, int par3, int par4, int par5)
    {
        int i1 = 0;
        boolean foundSource = false;
        boolean[] flag = new boolean[4];
        while (i1 < 4)
        {
            int j1 = 1;

            while (true)
            {
                if (j1 < 16)
                {
                    int k1 = par2 + Direction.offsetX[i1] * j1;
                    int l1 = par4 + Direction.offsetZ[i1] * j1;
                    
                    Block i2 = par1World.getBlock(k1, par3, l1);

                    if (i2 == BlockLaserBeamSource.instance.instance)
                    {
                        BlockLaserBeamSource.instance.updateLaserState(par1World, k1, par3, l1, i2, par1World.getBlockMetadata(k1, par3, l1), true, j1, par5);
                        foundSource = true;
                        flag[i1] = true;
                    }
                    else if (i2 == this.instance || !par1World.getBlock(k1, par3, l1).isOpaqueCube())
                    {
                        ++j1;
                        continue;
                    }
                }

                ++i1;
                break;
            }
        }
        if(!foundSource) {
        	par1World.setBlockToAir(par2, par3, par4);
        }
    }
    
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    	par1World.scheduleBlockUpdate(par2, par3, par4, this.instance, 1);
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
    	int i1 = 0;
        boolean[] flag = new boolean[4];
        while (i1 < 4)
        {
            int j1 = 1;

            while (true)
            {
                if (j1 < 16)
                {
                    int k1 = par2 + Direction.offsetX[i1] * j1;
                    int l1 = par4 + Direction.offsetZ[i1] * j1;
                    
                    Block i2 = par1IBlockAccess.getBlock(k1, par3, l1);

                    if (i2 == BlockLaserBeamSource.instance)
                    {
                        flag[i1] = true;
                    }
                    else if (i2 == this.instance || !par1IBlockAccess.getBlock(k1, par3, l1).isOpaqueCube())
                    {
                        ++j1;
                        continue;
                    }
                }

                ++i1;
                break;
            }
        }
    	if(flag[0] || flag[2]) {
    		l = l|2;
    	}
    	else {
    		l = (l|2)-2;
    	}
    	if(flag[1] || flag[3]) {
    		l = l|4;
    	}
    	else {
    		l = (l|4)-4;
    	}
    	
    	boolean flag1 = (l & 4) == 4;
        boolean flag2 = (l & 2) == 2;

        if (flag2 && !flag1)
        {
            this.setBlockBounds(0.4F, 0.5F, 0.0F, 0.6F, 0.59375F, 1.0F);
        }
        else if (flag1 && !flag2)
        {
            this.setBlockBounds(0.0F, 0.5F, 0.4F, 1.0F, 0.59375F, 0.6F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 0.59375F, 1.0F);
        }
    }
    
    public int getRenderType()
    {
        return renderID;
    }

	public static boolean func_72148_a(IBlockAccess world, int x, int y, int z, int l, int i) {
		int j1 = x + Direction.offsetX[i];
        int k1 = z + Direction.offsetZ[i];
        Block l1 = world.getBlock(j1, y, k1);
        boolean flag = (l & 2) == 2;
        
        int i2;

        if (l1 == BlockLaserBeamSource.instance)
        {
        	i2 = world.getBlockMetadata(j1, y, k1);
            int j2 = i2 & 3;
            return j2 == Direction.rotateOpposite[i];
            //return true;
        }
        else if (l1 == BlockLaserBeam.instance)
        {
            i2 = world.getBlockMetadata(j1, y, k1);
            boolean flag1 = (i2 & 2) == 2;
            return flag == flag1;
        }
        else
        {
            return false;
        }
	}
	
	public boolean isBlockReplaceable(World world, int x, int y, int z) {
		if(world.isRemote)
			return true;
		int i1 = 0;
		while (i1 < 4)
        {
            int j1 = 1;

            while (true)
            {
                if (j1 < 16)
                {
                    int k1 = x + Direction.offsetX[i1] * j1;
                    int l1 = z + Direction.offsetZ[i1] * j1;
                    
                    Block i2 = world.getBlock(k1, y, l1);

                    if (i2 == BlockLaserBeamSource.instance) {
                    	int m = world.getBlockMetadata(k1, y, l1)|8;
                    	world.setBlockMetadataWithNotify(k1, y, l1, m, 3);
                    	BlockLaserBeamSource.instance.notifyNeighborOfChange(world, k1, y, l1, m);
                    	world.scheduleBlockUpdate(k1, y, l1, BlockLaserBeamSource.instance, BlockLaserBeamSource.instance.tickRate(world));
                    }
                    else if (i2 == this.instance) {
                    	world.setBlockMetadataWithNotify(k1, y, l1, world.getBlockMetadata(k1, y, l1)|1, 3);
                    	++j1;
                        continue;
                    }
                    else if(!world.getBlock(k1, y, l1).isOpaqueCube())
                    {
                        ++j1;
                        continue;
                    }
                }

                ++i1;
                break;
            }
        }
		
		return true;
	}
}
