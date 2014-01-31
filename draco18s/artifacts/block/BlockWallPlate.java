package draco18s.artifacts.block;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWallPlate extends BlockBasePressurePlate {
	public static Block instance;
	public static Block obsidian;
    private EnumMobType triggerMobType;

	public BlockWallPlate(int par1, String par2Str, Material par2Material, EnumMobType par4EnumMobType) {
		super(par1, par2Str, par2Material);
        this.triggerMobType = par4EnumMobType;
        setHardness(0.5F);
        setStepSound(soundStoneFootstep);
        setCreativeTab(DragonArtifacts.tabTraps);
	}

	protected int getMetaFromWeight(int par1)
    {
        return par1 > 0 ? 1 : 0;
    }

    protected int getPowerSupply(int par1)
    {
        return par1 >= 8 ? 15 : 0;
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        this.updateLadderBounds(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
    
    public void updateLadderBounds(int par1)
    {
        float f = 0.125F/2;
        int side = par1&7;
        if(par1 > 7) {
        	f /= 2;
        }
        if (side == 2)
        {
            this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (side == 3)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }

        if (side == 4)
        {
            this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (side == 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }
    }
    
    @Override
    protected void func_94354_b_(World par1World, int par2, int par3, int par4)
    {
        par1World.notifyBlocksOfNeighborChange(par2+1, par3, par4, this.blockID);
        par1World.notifyBlocksOfNeighborChange(par2-1, par3, par4, this.blockID);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4+1, this.blockID);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4-1, this.blockID);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);
    }
    
    @Override
    protected void setStateIfMobInteractsWithPlate(World par1World, int par2, int par3, int par4, int par5)
    {
        int i1 = this.getPlateState(par1World, par2, par3, par4);
        boolean flag = par5 > 0;
        boolean flag1 = (i1 > 0);
        if (par5 != i1)
        {
        	int side = par1World.getBlockMetadata(par2, par3, par4)&7;
        	int m = this.getMetaFromWeight(i1)*8 + side;
            par1World.setBlockMetadataWithNotify(par2, par3, par4, m, 2);
            this.func_94354_b_(par1World, par2, par3, par4);
            par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
            /*switch(side) {
            	case 2:
            		par1World.scheduleBlockUpdate(par2, par3, par4+2, blockID, 0);
            		//z+1
            		break;
            	case 3:
            		par1World.scheduleBlockUpdate(par2, par3, par4-2, blockID, 0);
            		//z-1
            		break;
            	case 4:
            		par1World.scheduleBlockUpdate(par2+2, par3, par4, blockID, 0);
            		//x+1
            		break;
            	case 5:
            		par1World.scheduleBlockUpdate(par2-2, par3, par4, blockID, 0);
            		//x-1
            		break;
            }*/
        }

        if (!flag1 && flag)
        {
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.1D, (double)par4 + 0.5D, "random.click", 0.3F, 0.5F);
        }
        else if (flag1 && !flag)
        {
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.1D, (double)par4 + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (flag1)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
        }
    }

    protected int getPlateState(World par1World, int par2, int par3, int par4)
    {
        List list = null;
        int meta = par1World.getBlockMetadata(par2, par3, par4);
        if (this.triggerMobType == EnumMobType.everything)
        {
            list = par1World.getEntitiesWithinAABBExcludingEntity((Entity)null, this.getMetaSensitiveAABB(par2, par3, par4, meta));
        }

        if (this.triggerMobType == EnumMobType.mobs)
        {
            list = par1World.getEntitiesWithinAABB(EntityLivingBase.class, this.getMetaSensitiveAABB(par2, par3, par4, meta));
        }

        if (this.triggerMobType == EnumMobType.players)
        {
            list = par1World.getEntitiesWithinAABB(EntityPlayer.class, this.getMetaSensitiveAABB(par2, par3, par4, meta));
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
    
    protected AxisAlignedBB getSensitiveAABB(int par1, int par2, int par3)
    {
        float f = 0.125F;
        return AxisAlignedBB.getAABBPool().getAABB((double)((float)par1), (double)par2, (double)((float)par3), (double)((float)(par1 + 1)), (double)par2 + 1D, (double)((float)(par3 + 1)));
    }
    
    protected AxisAlignedBB getMetaSensitiveAABB(int par1, int par2, int par3, int meta)
    {
        float f = 0.125F;
        switch(meta) {
        	case 2:
        		return AxisAlignedBB.getAABBPool().getAABB((double)((float)par1 + f), (double)((float)par2 + f), (double)((float)(par3 + 1)-0.25f), (double)((float)(par1 + 1) - f), (double)((float)(par2 + 1) - f), (double)(par3 + 1));
        		//break;
        	case 3:
        		return AxisAlignedBB.getAABBPool().getAABB((double)((float)par1 + f), (double)((float)par2 + f), (double)par3, (double)((float)(par1 + 1) - f), (double)((float)(par2 + 1) - f), (double)par3 + 0.25D);
        		//break;
        	case 4:
        		return AxisAlignedBB.getAABBPool().getAABB((double)((float)(par1 + 1)-0.25f), (double)((float)par2 + f), (double)((float)par3 + f), (double)(par1 + 1), (double)((float)(par2 + 1) - f), (double)((float)(par3 + 1) - f));
        		//break;
        	case 5:
        		return AxisAlignedBB.getAABBPool().getAABB((double)par1, (double)((float)par2 + f), (double)((float)par3 + f), (double)par1 + 0.25D, (double)((float)(par2 + 1) - f), (double)((float)(par3 + 1) - f));
        		//break;
        }
        return AxisAlignedBB.getAABBPool().getAABB((double)((float)par1 + f), (double)par2, (double)((float)par3 + f), (double)((float)(par1 + 1) - f), (double)par2 + 0.25D, (double)((float)(par3 + 1) - f));
    }
    
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST ) ||
               par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST ) ||
               par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) ||
               par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH);
    }

    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        int j1 = par9;

        if ((j1 == 0 || par5 == 2) && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            j1 = 2;
        }

        if ((j1 == 0 || par5 == 3) && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            j1 = 3;
        }

        if ((j1 == 0 || par5 == 4) && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            j1 = 4;
        }

        if ((j1 == 0 || par5 == 5) && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            j1 = 5;
        }

        return j1;
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        int i1 = par1World.getBlockMetadata(par2, par3, par4)&7;
        boolean flag = false;

        if (i1 == 2 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            flag = true;
        }

        if (i1 == 3 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            flag = true;
        }

        if (i1 == 4 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            flag = true;
        }

        if (i1 == 5 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            flag = true;
        }

        if (!flag)
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, i1, 0);
            par1World.setBlockToAir(par2, par3, par4);
        }

        //super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
    }
    
    public void registerIcons(IconRegister par1IconRegister)
    {
    	if(this.triggerMobType == EnumMobType.players)
    		this.blockIcon = Block.obsidian.getBlockTextureFromSide(0);
    	else
    		this.blockIcon = Block.stone.getBlockTextureFromSide(0);
    }
    
    public void setBlockBoundsForItemRender()
    {
        float f1 = 0.125F;
        this.setBlockBounds(0.5F, 0, 0, 0.5F + f1, 1, 1);
    }
    
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return this.getPowerSupply(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
    
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    { 
        return this.getPowerSupply(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }
}
