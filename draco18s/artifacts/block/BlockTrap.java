package draco18s.artifacts.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import draco18s.artifacts.BlockSourceImpl;
import draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import draco18s.artifacts.arrowtrapbehaviors.BehaviorDefaultDispenseItem;
import draco18s.artifacts.arrowtrapbehaviors.IBehaviorDispenseItem;
import draco18s.artifacts.arrowtrapbehaviors.IBlockSource;
import draco18s.artifacts.arrowtrapbehaviors.IPosition;
import draco18s.artifacts.arrowtrapbehaviors.IRegistry;
import draco18s.artifacts.arrowtrapbehaviors.PositionImpl;
import draco18s.artifacts.arrowtrapbehaviors.RegistryDefaulted;
import draco18s.artifacts.entity.TileEntityTrap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrap extends BlockContainer
{
	public static Block instance;
    /** Registry for all dispense behaviors. */
    public static final IRegistry dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
    protected Random random = new Random();
    @SideOnly(Side.CLIENT)
    protected Icon furnaceTopIcon;
    @SideOnly(Side.CLIENT)
    protected Icon furnaceFrontIcon;
    @SideOnly(Side.CLIENT)
    protected Icon verticalFront;
    public int renderType = 0;

    public BlockTrap(int par1)
    {
        super(par1, Material.rock);
        //this.setCreativeTab(CreativeTabs.tabRedstone);
        setCreativeTab(DragonArtifacts.tabTraps);
		setUnlocalizedName("Arrow Trap");
		setResistance(10F);
		setStepSound(soundStoneFootstep);
		setHardness(2.0F);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return 4;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        this.setDispenserDefaultDirection(par1World, par2, par3, par4);
    }

    /**
     * sets Dispenser block direction so that the front faces an non-opaque block; chooses west to be direction if all
     * surrounding blocks are opaque.
     */
    private void setDispenserDefaultDirection(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            int l = par1World.getBlockId(par2, par3, par4 - 1);
            int i1 = par1World.getBlockId(par2, par3, par4 + 1);
            int j1 = par1World.getBlockId(par2 - 1, par3, par4);
            int k1 = par1World.getBlockId(par2 + 1, par3, par4);
            byte b0 = 3;

            if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i1])
            {
                b0 = 3;
            }

            if (Block.opaqueCubeLookup[i1] && !Block.opaqueCubeLookup[l])
            {
                b0 = 2;
            }

            if (Block.opaqueCubeLookup[j1] && !Block.opaqueCubeLookup[k1])
            {
                b0 = 5;
            }

            if (Block.opaqueCubeLookup[k1] && !Block.opaqueCubeLookup[j1])
            {
                b0 = 4;
            }

            par1World.setBlockMetadataWithNotify(par2, par3, par4, b0, 2);
        }
    }

    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
    	//if(par1 < 0) {
    		if(par2 <= 1)
    			return verticalFront;
    	//}
		return blockIcon;
    }
    
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
    	int m = world.getBlockMetadata(x, y, z) & 7;
    	int k = side & 7;
    	if(m == k) {
    		return neighborBlockTexture(world, x, y, z, side, m);
    	}
    	return getIcon(m,side);
    }
    
    private Icon neighborBlockTexture(IBlockAccess world, int x, int y, int z, int side, int mymeta) {
    	int[] id = {world.getBlockId(x, y, z+1),world.getBlockId(x, y, z-1),world.getBlockId(x+1, y, z),world.getBlockId(x-1, y, z)};
        int[] meta = {world.getBlockMetadata(x, y, z+1),world.getBlockMetadata(x, y, z-1),world.getBlockMetadata(x+1, y, z),world.getBlockMetadata(x-1, y, z)};
        for(int i = 0; i < id.length; i++) {
        	if(i+2 != mymeta) {
	        	Block block = Block.blocksList[id[i]];
	        	if(block != null) {
	        		if(block != this) {
	        			Icon icon = block.getIcon(side, meta[i]);
		        		if(icon != null) {
		        			return icon;
		        		}
	        		}
	        	}
        	}
        }
    	return Block.grass.getBlockTextureFromSide(1);
    }

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("artifacts:arrow_trap_front");
        this.verticalFront = par1IconRegister.registerIcon("artifacts:arrow_trap_front_vertical");
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityDispenser tileentitydispenser = (TileEntityDispenser)par1World.getBlockTileEntity(par2, par3, par4);

            if (tileentitydispenser != null)
            {
                par5EntityPlayer.displayGUIDispenser(tileentitydispenser);
            }

            return true;
        }
    }

    protected void dispense(World par1World, int par2, int par3, int par4, int i)
    {
        BlockSourceImpl blocksourceimpl = new BlockSourceImpl(par1World, par2, par3, par4, i);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.getBlockTileEntity();

        if (tileentitydispenser != null)
        {
            int l = tileentitydispenser.getRandomStackFromInventory();
            
            if (l < 0)
            {
                par1World.playAuxSFX(1001, par2, par3, par4, 0);
            }
            else
            {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(l);
                IBehaviorDispenseItem ibehaviordispenseitem = this.getBehaviorForItemStack(itemstack);
                
                if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider)
                {
                    //ItemStack itemstack1 = ibehaviordispenseitem.dispense((IBlockSource) blocksourceimpl, itemstack);
                    ItemStack itemstack1 = ibehaviordispenseitem.dispense(blocksourceimpl, itemstack);
                    tileentitydispenser.setInventorySlotContents(l, itemstack1.stackSize == 0 ? null : itemstack1);
                }
            }
        }
    }

    /**
     * Returns the behavior for the given ItemStack.
     */
    protected IBehaviorDispenseItem getBehaviorForItemStack(ItemStack par1ItemStack)
    {
        return (IBehaviorDispenseItem)dispenseBehaviorRegistry.func_82594_a(par1ItemStack.getItem());
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4) || par1World.isBlockIndirectlyGettingPowered(par2, par3 + 1, par4);
        int i1 = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag1 = (i1 & 8) != 0;

        if (flag && !flag1)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate(par1World));
            par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 | 8, 4);
        }
        else if (!flag && flag1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, i1 & -9, 4);
        }
        else if(flag1) {
        	//System.out.println("Stuck on");
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
    	if (!par1World.isRemote)
    	{
    		TileEntity te = par1World.getBlockTileEntity(par2, par3, par4);
    		TileEntityDispenser dis;
    		if(te instanceof TileEntityDispenser) {
    			dis = ((TileEntityDispenser) te);
	    		AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(par2-0.2, par3-0.2, par4-0.2, par2+1.2, par3+1.2, par4+1.2);
	    		List<EntityArrow> arrs = par1World.getEntitiesWithinAABB(EntityArrow.class, aabb);
	    		for(int a = arrs.size()-1; a>=0; a--) {
	    			EntityArrow arr = arrs.get(a);
	    			for(int s=0; s<9; s++) {
	    				ItemStack is = dis.getStackInSlot(s);
	    				if(is == null) {
	    					dis.addItem(new ItemStack(Item.arrow, 1));
	    					arr.setDead();
	    					break;
	    				}
	    				else if(is != null && is.getItem() == Item.arrow) {
	    					if(is.stackSize < 64) {
	    						is.stackSize++;
	    						arr.setDead();
	    						break;
	    					}
	    				}
	    			}
	    		}
	    		
	    		this.dispense(par1World, par2, par3, par4, par5Random.nextInt(4)+1);
	    		this.dispense(par1World, par2, par3, par4, par5Random.nextInt(4)+1);
    		}
    	}
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityTrap();
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack)
    {
        int l = BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLiving);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);

        if (par6ItemStack.hasDisplayName())
        {
            ((TileEntityDispenser)par1World.getBlockTileEntity(par2, par3, par4)).setCustomName(par6ItemStack.getDisplayName());
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)par1World.getBlockTileEntity(par2, par3, par4);

        if (!par1World.isRemote && tileentitydispenser != null)
        {
            for (int j1 = 0; j1 < tileentitydispenser.getSizeInventory(); ++j1)
            {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(j1);

                if (itemstack != null)
                {
                    float f = this.random.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.random.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int k1 = this.random.nextInt(21) + 10;

                        if (k1 > itemstack.stackSize)
                        {
                            k1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k1;
                        EntityItem entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.itemID, k1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.random.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.random.nextGaussian() * f3);
                        par1World.spawnEntityInWorld(entityitem);
                    }
                }
            }

            par1World.func_96440_m(par2, par3, par4, par5);
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    public static IPosition getIPositionFromBlockSource(IBlockSource par0IBlockSource)
    {
        EnumFacing enumfacing = getFacing(par0IBlockSource.getBlockMetadata());
        double d0 = par0IBlockSource.getX() + 0.7D * (double)enumfacing.getFrontOffsetX();
        double d1 = par0IBlockSource.getY() + 0.7D * (double)enumfacing.getFrontOffsetY();
        double d2 = par0IBlockSource.getZ() + 0.7D * (double)enumfacing.getFrontOffsetZ();
        return new PositionImpl(d0, d1, d2);
    }

    public static EnumFacing getFacing(int par0)
    {
        return EnumFacing.getFront(par0 & 7);
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory((IInventory)par1World.getBlockTileEntity(par2, par3, par4));
    }
    
    public boolean canCollideCheck(int par1, boolean par2)
    {
        return (par1 & 8) == 0;
    }
    
    public int getRenderType()
    {
        return renderType;
    }
    
    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }
    
    /*public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
    	return true;
    }*/
    
    @Override
    public boolean isOpaqueCube()
    {
        return true;
    }
    
    public boolean renderAsNormalBlock()
    {
        return true;
    }
}
