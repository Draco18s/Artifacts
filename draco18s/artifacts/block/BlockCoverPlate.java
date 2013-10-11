package draco18s.artifacts.block;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import draco18s.artifacts.DragonArtifacts;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCoverPlate extends Block {
	public static Block instance;
	public int renderType = 0;

	public BlockCoverPlate(int par1) {
		super(par1, Material.rock);
		setUnlocalizedName("Tripwire Cover");
		setLightOpacity(0);
		setResistance(2F);
		setStepSound(soundStoneFootstep);
		setHardness(0.5F);
        //setCreativeTab(CreativeTabs.tabBlock);
		setCreativeTab(DragonArtifacts.tabTraps);
	}
	
	@Override
	public int getRenderType() {
		return renderType;
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		return blockIcon;
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon("artifacts:cover_hole");
	}
	
	public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	public boolean isOpaqueCube()
    {
        return false;
    }
	
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return false;
    }

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
		return null;
    }
	
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
        int i1 = par1World.getBlockMetadata(par2, par3, par4);
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

        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
    }
    
    public int getRenderBlockPass()
    {
        return 1;
    }
    
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
	    boolean solid = world.isBlockOpaqueCube(x, y, z);
	    if(side == 0 || side == 1)
	    	solid = true;
        return !solid;
    }
}
