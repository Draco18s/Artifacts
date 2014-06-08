package com.draco18s.artifacts.block;

import java.util.Random;

import com.draco18s.artifacts.DragonArtifacts;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class IInvisibleBlock extends Block {
	public static Block instance;

	public IInvisibleBlock(Material par2Material) {
		super(par2Material);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setLightOpacity(0);
	}

	@Override
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
		return DragonArtifacts.renderInvis;
    }
	
	@Override
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
		if(DragonArtifacts.boundingInvis) {
			return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
		}
		else {
			return null;
		}
    }
	
	@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("artifacts:invisible");
    }
	
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
