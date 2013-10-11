package draco18s.artifacts.block;

import draco18s.artifacts.DragonArtifacts;
import draco18s.artifacts.entity.EntitySpikes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockSpikes extends BlockContainer {
	public static Block instance;
	public int renderID = 0;

	public BlockSpikes(int par1) {
		super(par1, Material.iron);
		setUnlocalizedName("Upright Spikes");
		setResistance(5F);
		setStepSound(soundMetalFootstep);
        //setCreativeTab(CreativeTabs.tabBlock);
		setCreativeTab(DragonArtifacts.tabTraps);
		setHardness(2F);
	}
	
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
		return canBlockStay(par1World, par2, par3, par4);
    }
	
	public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
		return par1World.isBlockNormalCube(par2, par3-1, par4);
    }
	
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!this.canBlockStay(par1World, par2, par3, par4))
        {
            par1World.destroyBlock(par2, par3, par4, true);
        }
    }
	
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
		if(par5Entity instanceof EntityLivingBase) {
	        par5Entity.attackEntityFrom(DamageSource.generic, 2);
	        par5Entity.motionX *= 0.7D;
	        par5Entity.motionZ *= 0.7D;
	        EntitySpikes es = (EntitySpikes)par1World.getBlockTileEntity(par2, par3, par4);
	        es.setBloody(2);
		}
    }
	
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("artifacts:iron_spikes");
    }
	
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        float f = 0.0625F;
        return AxisAlignedBB.getAABBPool().getAABB((double)((float)par2), (double)par3, (double)((float)par4), (double)((float)(par2 + 1)), (double)(par3 + 1 - f), (double)((float)(par4 + 1)));
    }
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        float f = 0.0625F;
        return AxisAlignedBB.getAABBPool().getAABB((double)((float)par2), (double)par3, (double)((float)par4), (double)((float)(par2 + 1)), (double)(par3 + 1 - f), (double)((float)(par4 + 1)));
    }
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	public int getRenderType()
    {
        return -1;
    }
	
	public boolean isOpaqueCube()
    {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new EntitySpikes();
	}
}
