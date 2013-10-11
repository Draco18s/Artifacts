package draco18s.artifacts.block;

import java.util.Random;

import draco18s.artifacts.DragonArtifacts;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSolidAir extends IInvisibleBlock {
	public static Block instance;

	public BlockSolidAir(int par1) {
		super(par1, Material.air);
		setUnlocalizedName("Floating Block");
		setResistance(0F);
		setStepSound(soundStoneFootstep);
		setHardness(0F);
		setCreativeTab(null);
	}
	
	@Override
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
		return null;
    }
	
	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z)
    {
        return true;
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
    {
		int m = world.getBlockMetadata(x, y, z);
		if(m == 12) {
			world.setBlockToAir(x, y, z);
		}
		else {
			world.setBlockMetadataWithNotify(x, y, z, m+1, 3);
		}
		world.scheduleBlockUpdate(x, y, z, BlockSolidAir.instance.blockID, 10);
    }
	
	@Override
	public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
        if(par5Entity instanceof EntityPlayer)
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 3);
    }
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
		return false;
    }
}
