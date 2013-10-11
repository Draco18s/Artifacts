package draco18s.artifacts.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLight extends Block {

	public static Block instance;

	public BlockLight(int par1) {
		super(par1, Material.air);
		this.setLightValue(1);
		this.setUnlocalizedName("Invisible Light");
		this.setHardness(0);
		this.setResistance(0);
	}
	
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = Block.glowStone.getBlockTextureFromSide(0);//par1IconRegister.registerIcon("artifacts:light");
    }
	
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
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
    
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
    	return null;
    }
    
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
