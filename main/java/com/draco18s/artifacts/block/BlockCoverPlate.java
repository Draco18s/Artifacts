package com.draco18s.artifacts.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import com.draco18s.artifacts.DragonArtifacts;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCoverPlate extends Block {
	public static Block instance;
	public int renderType = 0;

	public BlockCoverPlate() {
		super(Material.rock);
		setLightOpacity(0);
		setResistance(2F);
		setStepSound(Block.soundTypeStone);
		setHardness(0.5F);
        //setCreativeTab(CreativeTabs.tabBlock);
		setCreativeTab(DragonArtifacts.tabGeneral);
	}
	
	@Override
	public int getRenderType() {
		return renderType;
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		return blockIcon;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("artifacts:cover_hole");
	}
	
	@Override
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	@Override
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return false;
    }

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
		return null;
    }
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int par1 = (par1IBlockAccess.getBlockMetadata(par2, par3, par4));
        float f = 0.125F;

        if (par1 == 2)
        {
            this.setBlockBounds(0.0F, -1F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (par1 == 3)
        {
            this.setBlockBounds(0.0F, -1F, 0.0F, 1.0F, 1.0F, f);
        }

        if (par1 == 4)
        {
            this.setBlockBounds(1.0F - f, -1F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (par1 == 5)
        {
            this.setBlockBounds(0.0F, -1F, 0.0F, f, 1.0F, 1.0F);
        }
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
	public int onBlockPlaced(World world, int x, int y, int z, int par5, float par6, float par7, float par8, int par9)
    {
        int j1 = par9;

        if ((j1 == 0 || par5 == 2) && world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, NORTH))
        {
            j1 = 2;
        }

        if ((j1 == 0 || par5 == 3) && world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, SOUTH))
        {
            j1 = 3;
        }

        if ((j1 == 0 || par5 == 4) && world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, WEST))
        {
            j1 = 4;
        }

        if ((j1 == 0 || par5 == 5) && world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, EAST))
        {
            j1 = 5;
        }

        return j1;
    }

	@Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        int i1 = world.getBlockMetadata(x, y, z);
        boolean flag = false;

        if (i1 == 2 && world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, NORTH))
        {
            flag = true;
        }

        if (i1 == 3 && world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, SOUTH))
        {
            flag = true;
        }

        if (i1 == 4 && world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, WEST))
        {
            flag = true;
        }

        if (i1 == 5 && world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, EAST))
        {
            flag = true;
        }

        if (!flag)
        {
            this.dropBlockAsItem(world, x, y, z, i1, 0);
            world.setBlockToAir(x, y, z);
        }

        super.onNeighborBlockChange(world, x, y, z, block);
    }
    
	@Override
    public int getRenderBlockPass()
    {
        return 1;
    }
    
	@Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
		int l = world.getBlockMetadata(x, y, z);
		switch(l) {
	        case 2:
	        	z++;
	        	break;
	        case 3:
	        	z--;
	        	break;
	        case 4:
	        	x++;
	        	break;
	        case 5:
	        	x--;
	        	break;
	    }
		switch(side) {
	        case 2:
	        	z++;
	        	break;
	        case 3:
	        	z--;
	        	break;
	        case 4:
	        	x++;
	        	break;
	        case 5:
	        	x--;
	        	break;
	    }
	    y--;
	    boolean solid = world.getBlock(x, y, z).isOpaqueCube();
	    if(side == 0 || side == 1)
	    	solid = true;
        return !solid;
    }
}
