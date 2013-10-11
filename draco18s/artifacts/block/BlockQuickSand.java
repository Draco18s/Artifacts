package draco18s.artifacts.block;

import draco18s.artifacts.DragonArtifacts;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockQuickSand extends Block
{
	public static Block instance;
    public BlockQuickSand(int par1)
    {
        super(par1, Material.sand);
        //setCreativeTab(CreativeTabs.tabBlock);
        setCreativeTab(DragonArtifacts.tabTraps);
		setUnlocalizedName("Quicksand");
		setResistance(10F);
		this.setStepSound(soundSandFootstep);
		setHardness(2.0F);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        float f = 0.5F;
        return AxisAlignedBB.getAABBPool().getAABB((double)par2, (double)par3, (double)par4, (double)(par2 + 1), (double)((float)(par3 + 1) - f), (double)(par4 + 1));
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
    	if(par5Entity instanceof EntityLivingBase) {
    		par5Entity.attackEntityFrom(DamageSource.generic, 0.5f);
    	}
        par5Entity.motionX *= 0.5D;
        par5Entity.motionZ *= 0.5D;
    }
    
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        blockIcon = Block.dirt.getBlockTextureFromSide(0);
    }
}
