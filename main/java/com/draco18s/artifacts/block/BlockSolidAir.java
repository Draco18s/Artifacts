package com.draco18s.artifacts.block;

import java.util.Random;

import com.draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSolidAir extends IInvisibleBlock {
	public static Block instance;

	public BlockSolidAir() {
		super(Material.air);
		setResistance(0F);
		setStepSound(Block.soundTypeStone);
		setHardness(0F);
		setCreativeTab(null);
		this.setTickRandomly(true); //So it destroys itself over time.
	}
	
	@Override
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
		return null;
    }
	
	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random)
    {
		//Set itself to air (self-destruction).
		world.setBlockToAir(x, y, z);
    }
	
	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity)
    {
        super.onEntityWalking(world, x, y, z, entity);
        if(entity instanceof EntityPlayer && !entity.isSneaking()) {
        	world.setBlockMetadataWithNotify(x, y, z, 0, 3);
        }
    }
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
		return false;
    }
    
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
