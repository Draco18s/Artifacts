package com.draco18s.artifacts.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWallPlate extends BlockBasePressurePlate {
	public static Block instance;
	public static Block obsidian;
    private BlockPressurePlate.Sensitivity triggerMobType;

	public BlockWallPlate(String textureName, Material material, BlockPressurePlate.Sensitivity triggerMob) {
		super(textureName, material);
        this.triggerMobType = triggerMob;
        setHardness(0.5F);
        setStepSound(Block.soundTypeStone);
        setCreativeTab(DragonArtifacts.tabGeneral);
	}

	@Override
	//getMetaFromWeight
	protected int func_150066_d(int weight)
    {
        return weight > 0 ? 1 : 0;
    }

	@Override
	//getPowerSupply
    protected int func_150060_c(int meta)
    {
        return meta >= 8 ? 15 : 0;
    }
	
    @Override
    //getPlateState
    protected int func_150065_e(World world, int x, int y, int z)
    {
        List list = null;
        int meta = world.getBlockMetadata(x, y, z);
        if (this.triggerMobType == BlockPressurePlate.Sensitivity.everything)
        {
            list = world.getEntitiesWithinAABBExcludingEntity((Entity)null, this.getMetaSensitiveAABB(x, y, z, meta));
        }

        if (this.triggerMobType == BlockPressurePlate.Sensitivity.mobs)
        {
            list = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getMetaSensitiveAABB(x, y, z, meta));
        }

        if (this.triggerMobType == BlockPressurePlate.Sensitivity.players)
        {
            list = world.getEntitiesWithinAABB(EntityPlayer.class, this.getMetaSensitiveAABB(x, y, z, meta));
        }

        if (!list.isEmpty())
        {
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();
                if (!entity.doesEntityNotTriggerPressurePlate())
                {
                    return 15;
                }
            }
        }
        

        return 0;
    }    
    
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        this.updatePlateBounds(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
    
    public void updatePlateBounds(int meta)
    {
        float f = 0.125F;
        int sideMeta = meta & 0x07;
//        if(meta > 7) {
//        	f /= 2;
//        }
        if (sideMeta == 2)
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (sideMeta == 3)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }

        if (sideMeta == 4)
        {
            this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (sideMeta == 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }
    }
    
    @Override
    protected void func_150064_a_(World world, int x, int y, int z)
    {
        world.notifyBlocksOfNeighborChange(x+1, y, z, this.instance);
        world.notifyBlocksOfNeighborChange(x-1, y, z, this.instance);
        world.notifyBlocksOfNeighborChange(x, y, z+1, this.instance);
        world.notifyBlocksOfNeighborChange(x, y, z-1, this.instance);
        world.notifyBlocksOfNeighborChange(x, y, z, this.instance);
    }
    
    @Override
    //setStateIfMobInteractsWithPlate
    protected void func_150062_a(World world, int x, int y, int z, int meta)
    {
        int i1 = this.func_150065_e/*getPlateState*/(world, x, y, z);
        boolean flag = meta > 0;
        boolean flag1 = (i1 > 0);
        if (meta != i1)
        {
        	int side = world.getBlockMetadata(x, y, z)&7;
        	int m = this.func_150060_c/*getMetaFromWeight*/(i1)*8 + side;
            world.setBlockMetadataWithNotify(x, y, z, m, 2);
            this.func_150064_a_(world, x, y, z);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        if (!flag1 && flag)
        {
            world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
        }
        else if (flag1 && !flag)
        {
            world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (flag1)
        {
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
        }
    }


    
    @Override
    //getSensitiveAABB
    protected AxisAlignedBB func_150061_a(int x, int y, int z)
    {
        float f = 0.125F;
        return AxisAlignedBB.getAABBPool().getAABB((double)((float)x), (double)y, (double)((float)z), (double)((float)(x + 1)), (double)y + 1D, (double)((float)(z + 1)));
    }
    
    protected AxisAlignedBB getMetaSensitiveAABB(int x, int y, int z, int meta)
    {
        float f = 0.125F;
        switch(meta) {
        	case 2:
        		return AxisAlignedBB.getAABBPool().getAABB((double)((float)x + f), (double)((float)y + f), (double)((float)(z + 1)-0.25f), (double)((float)(x + 1) - f), (double)((float)(y + 1) - f), (double)(z + 1));
        		//break;
        	case 3:
        		return AxisAlignedBB.getAABBPool().getAABB((double)((float)x + f), (double)((float)y + f), (double)z, (double)((float)(x + 1) - f), (double)((float)(y + 1) - f), (double)z + 0.25D);
        		//break;
        	case 4:
        		return AxisAlignedBB.getAABBPool().getAABB((double)((float)(x + 1)-0.25f), (double)((float)y + f), (double)((float)z + f), (double)(x + 1), (double)((float)(y + 1) - f), (double)((float)(z + 1) - f));
        		//break;
        	case 5:
        		return AxisAlignedBB.getAABBPool().getAABB((double)x, (double)((float)y + f), (double)((float)z + f), (double)x + 0.25D, (double)((float)(y + 1) - f), (double)((float)(z + 1) - f));
        		//break;
        }
        return AxisAlignedBB.getAABBPool().getAABB((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)y + 0.25D, (double)((float)(z + 1) - f));
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, EAST ) ||
               world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, WEST ) ||
               world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, SOUTH) ||
               world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, NORTH);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
    {
        int newMeta = meta;

        if ((newMeta == 0 || side == 2) && world.isSideSolid(x, y, z + 1, NORTH))
        {
            newMeta = 2;
        }

        if ((newMeta == 0 || side == 3) && world.isSideSolid(x, y, z - 1, SOUTH))
        {
            newMeta = 3;
        }

        if ((newMeta == 0 || side == 4) && world.isSideSolid(x + 1, y, z, WEST))
        {
            newMeta = 4;
        }

        if ((newMeta == 0 || side == 5) && world.isSideSolid(x - 1, y, z, EAST))
        {
            newMeta = 5;
        }

        return newMeta;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        int meta = world.getBlockMetadata(x, y, z) & 0x07;
        boolean flag = false;

        if (meta == 2 && world.isSideSolid(x, y, z+1, NORTH))
        {
            flag = true;
        }

        if (meta == 3 && world.isSideSolid(x, y, z-1, SOUTH))
        {
            flag = true;
        }

        if (meta == 4 && world.isSideSolid(x+1, y, z, WEST))
        {
            flag = true;
        }

        if (meta == 5 && world.isSideSolid(x-1, y, z, EAST))
        {
            flag = true;
        }

        if (!flag)
        {
        	System.out.println("Wall plate is invalid! Side is :" + meta);
            this.dropBlockAsItem(world, x, y, z, meta, 0);
            world.setBlockToAir(x, y, z);
        }

        //super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
    	if(this.triggerMobType == BlockPressurePlate.Sensitivity.players)
    		this.blockIcon = Blocks.obsidian.getBlockTextureFromSide(0);
    	else
    		this.blockIcon = Blocks.stone.getBlockTextureFromSide(0);
    }
    
    @Override
    public void setBlockBoundsForItemRender()
    {
        float f1 = 0.125F;
        this.setBlockBounds(0.5F, 0, 0, 0.5F + f1, 1, 1);
    }
    
    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return this.func_150060_c/*getPowerSupply*/(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
    
    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    { 
        return this.func_150060_c/*getPowerSupply*/(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
}
