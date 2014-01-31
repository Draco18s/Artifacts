package draco18s.artifacts.block;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import draco18s.artifacts.DragonArtifacts;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;

public class BlockLaserBeamSource extends Block {

	public static BlockLaserBeamSource instance;
	public int renderID;

	public BlockLaserBeamSource(int par1) {
		super(par1, Material.rock);
		setUnlocalizedName("Laser Beam Source");
		setLightOpacity(0);
		setResistance(2F);
		setStepSound(soundStoneFootstep);
		setHardness(0.5F);
		setCreativeTab(DragonArtifacts.tabTraps);
		setTextureName("artifacts:lasersource_front");
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
    
    public int getRenderType()
    {
        return renderID;
    }

    public int tickRate(World par1World)
    {
        return 10;
    }

    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(par5);
        return (dir == NORTH && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) ||
               (dir == SOUTH && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) ||
               (dir == WEST  && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST )) ||
               (dir == EAST  && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST ));
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
        byte b0 = 0;

        if (par5 == 2 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true))
        {
            b0 = 2;
        }

        if (par5 == 3 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true))
        {
            b0 = 0;
        }

        if (par5 == 4 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true))
        {
            b0 = 1;
        }

        if (par5 == 5 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true))
        {
            b0 = 3;
        }

        return b0;
    }

    public void onPostBlockPlaced(World par1World, int par2, int par3, int par4, int par5)
    {
        this.updateLaserState(par1World, par2, par3, par4, this.blockID, par5, false, -1, 0);
    }
    
    public void rebuildLaser(World world, int x, int y, int z, int meta) {
    	int l1 = meta & 3;
        boolean flag1 = (meta & 8) == 8;
        int offsetX = Direction.offsetX[l1];
        int offsetZ = Direction.offsetZ[l1];
        int l2;
        int i3;
        int j3;
        int k3;
        boolean shouldbetriggered = false;
        boolean quitEarly = false;
        for (i3 = 1; i3 < 16 && !quitEarly; ++i3)
        {
        	l2 = x + offsetX * i3;
            k3 = z + offsetZ * i3;
            j3 = world.getBlockId(l2, y, k3);
            if(j3 == 0 || (j3 != BlockLaserBeam.instance.blockID && Block.blocksList[j3].blockMaterial == Material.air)) {
            	world.setBlock(l2, y, k3, BlockLaserBeam.instance.blockID, 0, 3);
            }
            if (world.isBlockOpaqueCube(l2, y, k3)) {
            	quitEarly = true;
            }
            if(j3 == BlockLaserBeam.instance.blockID) {
            	if(world.getBlockMetadata(l2, y, k3) != 0) {
            		shouldbetriggered = true;
            	}
            }
        }
        /*if(flag1 && shouldbetriggered) {
        	System.out.println("trigger?" + shouldbetriggered);
    		world.setBlockMetadataWithNotify(x, y, z, l1|8, 3);
    		notifyNeighborOfChange(world, x, y, z, l1);
    	}*/
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
    	int i1 = par1World.getBlockMetadata(par2, par3, par4);
        if (par5 != this.blockID)
        {
            if (this.func_72144_l(par1World, par2, par3, par4))
            {
                
                int j1 = i1 & 3;
                boolean flag = false;

                if (!par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST) && j1 == 3)
                {
                    flag = true;
                }

                if (!par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST) && j1 == 1)
                {
                    flag = true;
                }

                if (!par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) && j1 == 0)
                {
                    flag = true;
                }

                if (!par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH) && j1 == 2)
                {
                    flag = true;
                }

                if (flag)
                {
                    this.dropBlockAsItem(par1World, par2, par3, par4, i1, 0);
                    par1World.setBlockToAir(par2, par3, par4);
                }
            }
        }
        rebuildLaser(par1World, par2, par3, par4, i1);
    }
    
    public void updateLaserState(World world, int x, int y, int z, int id, int meta, boolean triggered, int par8, int par9)
    {
        int l1 = meta & 3;
        boolean flag1 = (meta & 8) == 8;
        //boolean flag2 = (meta & 8) == 8;
        //boolean isThis = id == this.blockID;
        //boolean flag4 = false;
        //boolean solidTop = !world.isBlockSolidOnSide(x, y - 1, z, UP);
        int offsetX = Direction.offsetX[l1];
        int offsetZ = Direction.offsetZ[l1];
        //int k2 = 0;
        //int[] aint = new int[42];
        int l2;
        int i3;
        int j3;
        int k3;
        //int l3;
        boolean shouldbetriggered = false;
        boolean quitEarly = false;
        for (i3 = 1; i3 < 16; ++i3)
        {
        	l2 = x + offsetX * i3;
            k3 = z + offsetZ * i3;
            j3 = world.getBlockId(l2, y, k3);
            if(j3 == 0 || (j3 != BlockLaserBeam.instance.blockID && Block.blocksList[j3].blockMaterial == Material.air)) {
            	if(!quitEarly)
            		world.setBlock(l2, y, k3, BlockLaserBeam.instance.blockID, 0, 3);
            }
            if(j3 == BlockLaserBeam.instance.blockID) {
            	if(quitEarly) {
            		world.setBlockToAir(l2, y, k3);
            	}
            	else if(world.getBlockMetadata(l2, y, k3) != 0) {
            		shouldbetriggered = true;
            		//world.scheduleBlockUpdate(l2, y, k3, blockID, 10);
            		//world.setBlockMetadataWithNotify(l2, y, k3, 0, 3);
            	}
            }
            if (world.isBlockOpaqueCube(l2, y, k3)) {
            	quitEarly = true;
            }
        }
        
        if(triggered) {
        	if(shouldbetriggered) {
        		world.setBlockMetadataWithNotify(x, y, z, l1|8, 3);
        		world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
        		notifyNeighborOfChange(world, x, y, z, l1);
        	}
        }
    }
    
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	int meta = par1World.getBlockMetadata(par2, par3, par4);
        this.updateLaserState(par1World, par2, par3, par4, this.blockID, meta, true, -1, 0);
        if((meta&8) == 8) {
        	par1World.setBlockMetadataWithNotify(par2, par3, par4, meta&3, 3);
        	notifyNeighborOfChange(par1World, par2, par3, par4, meta&3);
            par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
        }
    }
    
    public void notifyNeighborOfChange(World par1World, int x, int y, int z, int meta)
    {
    	int side = meta&3;
        par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);

        if (side == 3)
        {
            par1World.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
        }
        else if (side == 1)
        {
            par1World.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
        }
        else if (side == 0)
        {
            par1World.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
        }
        else if (side == 2)
        {
            par1World.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
        }
    }

    private boolean func_72144_l(World par1World, int par2, int par3, int par4)
    {
        if (!this.canPlaceBlockAt(par1World, par2, par3, par4))
        {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) == 8 ? 15 : 0;
    }


    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        int i1 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        if ((i1 & 8) != 8)
        {
            return 0;
        }
        else
        {
            int j1 = i1 & 3;
            return j1 == 2 && par5 == 2 ? 15 : (j1 == 0 && par5 == 3 ? 15 : (j1 == 1 && par5 == 4 ? 15 : (j1 == 3 && par5 == 5 ? 15 : 0)));
        }
    }

    public boolean canProvidePower()
    {
        return true;
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, int blockID, int meta)
    {
        int l1 = meta & 3;
        boolean flag1 = (meta & 8) == 8;
        int offsetX = Direction.offsetX[l1];
        int offsetZ = Direction.offsetZ[l1];
        int l2;
        int i3;
        int j3;
        int k3;
        boolean quitEarly = false;
        for (i3 = 1; i3 < 16 && !quitEarly; ++i3)
        {
        	l2 = x + offsetX * i3;
            k3 = z + offsetZ * i3;
            j3 = world.getBlockId(l2, y, k3);
            if(j3 == BlockLaserBeam.instance.blockID) {
            	//world.setBlock(l2, y, k3, BlockLaserBeam.instance.blockID, 0, 3);
            	//world.scheduleBlockUpdate(l2, y, k3, this.blockID, 1);
            	world.setBlockToAir(l2, y, k3);
            	//System.out.println(l2 + "," + k3);
            }
            /*else if(world.isBlockOpaqueCube(l2, y, k3)) {
            	quitEarly = true;
            }
            else {
            	
            }*/
        }
        if (flag1)
        {
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            int j1 = meta & 3;

            if (j1 == 3)
            {
                world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
            }
            else if (j1 == 1)
            {
                world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
            }
            else if (j1 == 0)
            {
                world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
            }
            else if (j1 == 2)
            {
                world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
            }
        }
        super.breakBlock(world, x, y, z, blockID, meta);
    }
    
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
		//System.out.println("Collide");
        if (!par1World.isRemote)
        {
        	/*int m = par1World.getBlockMetadata(par2, par3, par4);
            if ((m & 8) != 8)
            {
            	par1World.setBlockMetadataWithNotify(par2, par3, par4, m, 3);
            }*/
        	int l = par1World.getBlockMetadata(par2, par3, par4);
            boolean flag = (l & 8) == 8;
        	boolean flag1 = false;
        	List list = par1World.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getAABBPool().getAABB((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ));

            if (!list.isEmpty())
            {
                Iterator iterator = list.iterator();

                while (iterator.hasNext())
                {
                    Entity entity = (Entity)iterator.next();

                    if (entity instanceof EntityLivingBase && !entity.doesEntityNotTriggerPressurePlate())
                    {
                        flag1 = true;
                        break;
                    }
                }
            }
            if (flag1 && !flag)
            {
                l |= 8;
                par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 3);
                notifyNeighborOfChange(par1World, par2, par3, par4, l);
            //}
            //if (flag1)
            //{
                par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
            }
        }
    }
    
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }
    
    public boolean canHarvestBlock(EntityPlayer player, int meta)
    {
        return true;
    }
    
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 3;
        float f = 0.1875F;

        if (l == 3)
        {
            this.setBlockBounds(0.0F, 0.3125F, 0.5F - f, f * 1.0F, 0.6875F, 0.5F + f);
        }
        else if (l == 1)
        {
            this.setBlockBounds(1.0F - f * 1.0F, 0.3125F, 0.5F - f, 1.0F, 0.6875F, 0.5F + f);
        }
        else if (l == 0)
        {
            this.setBlockBounds(0.5F - f, 0.3125F, 0.0F, 0.5F + f, 0.6875F, f * 1.0F);
        }
        else if (l == 2)
        {
            this.setBlockBounds(0.5F - f, 0.3125F, 1.0F - f * 1.0F, 0.5F + f, 0.6875F, 1.0F);
        }
    }
    
    @Override
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5) {
    	int m = par2World.getBlockMetadata(par3, par4, par5);
    	if((m&8) != 8) {
	    	par2World.setBlockMetadataWithNotify(par3, par4, par5, m|8, 3);
	    	notifyNeighborOfChange(par2World, par3, par4, par5, m);
	    	par2World.scheduleBlockUpdate(par3, par4, par5, this.blockID, this.tickRate(par2World));
    	}
    	return super.getPlayerRelativeBlockHardness(par1EntityPlayer, par2World, par3, par4, par5);
    }
}
