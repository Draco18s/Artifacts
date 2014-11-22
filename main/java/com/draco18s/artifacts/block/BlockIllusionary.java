package com.draco18s.artifacts.block;

import java.util.Random;

import com.draco18s.artifacts.DragonArtifacts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockIllusionary extends Block {
	public static Block instance;
	public int renderType = 0;

	public BlockIllusionary() {
		super(Material.rock);
		setResistance(2F);
		setStepSound(Block.soundTypeStone);
		setHardness(0.5F);
        this.setCreativeTab(DragonArtifacts.tabGeneral);
	}
	
	@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("artifacts:invisible");
    }
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
		return true;
    }

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
		//int mymeta = world.getBlockMetadata(x, y, z);
		//if(mymeta == 0) {
	        Block[] blockArray = {world.getBlock(x, y-1, z),world.getBlock(x-1, y, z),world.getBlock(x+1, y, z),world.getBlock(x, y, z-1),world.getBlock(x, y, z+1),world.getBlock(x, y+1, z)};
	        int[] meta = {world.getBlockMetadata(x, y-1, z),world.getBlockMetadata(x-1, y, z),world.getBlockMetadata(x+1, y, z),world.getBlockMetadata(x, y, z-1),world.getBlockMetadata(x, y, z+1),world.getBlockMetadata(x, y+1, z)};
	        //.get.getBlockTextureFromSideAndMetadata(par5, par1IBlockAccess.getBlockMetadata(par2, par3, par4));
	        for(int i = 0; i < blockArray.length; i++) {
	        	Block block = blockArray[i];
	        	if(block != null) {
	        		if(block != this && block.isOpaqueCube()) {
		        		IIcon icon = block.getIcon(side, meta[i]);
		        		if(icon != null) {
		        			return icon;
		        		}
	        		}
	        		else if(block == this) {
	        			IIcon icon = Blocks.stonebrick.getBlockTextureFromSide(1);
	        			switch(i) {
	        				case 0:
		        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x, y-1, z, side, 0);
		        				break;
	        				case 1:
		        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x-1, y, z, side, 2);
		        				break;
	        				case 2:
		        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x+1, y, z, side, 3);
		        				break;
	        				case 3:
		        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x, y, z-1, side, 4);
		        				break;
	        				case 4:
		        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x, y, z+1, side, 5);
		        				break;
	        				case 5:
		        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x, y+1, z, side, 1);
		        				break;
	        			}
	        			if(icon != null) {
		        			return icon;
		        		}
	        		}
	        	}
	        }
	        return Blocks.stonebrick.getBlockTextureFromSide(1);
		/*}
		else {
			return blockIcon;
		}*/
    }
	
	public IIcon getBlockTextureDirectional(IBlockAccess world, int x, int y, int z, int side, int direction) {
		
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		if(block != null) {
			if(block != this) {
				IIcon icon = block.getIcon(side, meta);
        		if(icon != null) {
        			return icon;
        		}
			}
			else {
				IIcon icon = Blocks.stonebrick.getBlockTextureFromSide(1);
    			switch(direction) {
    				case 0:
        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x, y-1, z, side, 0);
        				break;
    				case 1:
        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x, y+1, z, side, 1);
        				break;
    				case 2:
        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x-1, y, z, side, 2);
        				break;
    				case 3:
        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x+1, y, z, side, 3);
        				break;
    				case 4:
        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x, y, z-1, side, 4);
        				break;
    				case 5:
        				icon = ((BlockIllusionary)block).getBlockTextureDirectional(world, x, y, z+1, side, 5);
        				break;
    			}
    			return icon;
			}
		}
		return Blocks.stonebrick.getBlockTextureFromSide(1);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
		return null;
    }
	
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return false;
    }
	
	/**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
    	if(world.getBlock(x, y, z) == this || world.getBlock(x, y, z).isOpaqueCube())
    		return false;
        return true;
    }
    
    @Override
    public int getRenderType() {
    	return renderType;
    }

	@Override
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
