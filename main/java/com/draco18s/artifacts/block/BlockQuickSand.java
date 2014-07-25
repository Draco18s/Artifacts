package com.draco18s.artifacts.block;

import java.util.Random;

import com.draco18s.artifacts.DamageSourceQuicksand;
import com.draco18s.artifacts.DragonArtifacts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockQuickSand extends Block
{
	public static Block instance;
	public int renderType = 0;
	private static final boolean debug = false;
	
	private int flowDelay = 15;
	
	public BlockQuickSand()
    {
        super(Material.sand);
        setCreativeTab(DragonArtifacts.tabGeneral);
		setResistance(10F);
		this.setStepSound(Block.soundTypeSand);
		this.setTickRandomly(true);
		setHardness(2.0F);
    }
	
	/**
     * Checks if the specified tool type is efficient on this block, 
     * meaning that it digs at full speed.
     * 
     * @param type
     * @param metadata
     * @return
     */
	@Override
    public boolean isToolEffective(String type, int metadata)
    {
    	if(type.equals("shovel")) {
    		return true;
    	}
    	
    	return super.isToolEffective(type, metadata);
    }
	
    @Override
    public int getRenderType() {
    	return renderType;
    }
	
	 /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	tryFlowing(world, x, y, z);
    }
    
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
    	world.scheduleBlockUpdate(x, y, z, this.instance, flowDelay);
    }
	
	/**
    * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
    * their own) Args: x, y, z, neighbor Block
    */
	@Override
   public void onNeighborBlockChange(World world, int x, int y, int z, Block blockChanged)
   {
		world.scheduleBlockUpdate(x, y, z, this.instance, flowDelay);
   }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
   @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
    
    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
    	int meta = world.getBlockMetadata(x, y, z);
        return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY - (meta / 16), (double)z + this.maxZ);
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
    	this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
    	this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + (float)(((double)getQuicksandLevel(world, x, y, z)) * 0.0625), 1.0F);  
    }
    
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("artifacts:pourable_quicksand");
    }
    
    @Override
    public Item getItemDropped(int meta, Random rand, int fortune)
    {
    	//Always drop the block if meta is 0. Chance for dropping decreases after that. 
    	//No chance (even with fortune) at meta 14 and 15.
    	if(meta == 0 || rand.nextInt(16 - meta) > (6 - (fortune > 4 ? 4 : fortune))) {
    		return Item.getItemFromBlock(this);
    	}
    	
    	return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
    	if(entity instanceof EntityLivingBase && headInQuicksand(world, x, y, z, entity)) {
    		//If the entity is inside of a quicksand block, give some damage. It's only 0.5f damage, so a quarter heart.
    		entity.attackEntityFrom(DamageSourceQuicksand.instance, 0.5f);
    		
    		//I tried to make it render the texture on your screen like if you're in a block,
    		//but it turned out to be a private method.
    	}
    	if(insideQuicksand(world, x, y, z, entity)) {
    		//If the entity inside of the quicksand is a player, make the player fall slowly.
    		//If the player is moving horizontally ("struggling"), make them fall faster.
    		//If the player is sneaking, make them "swim" upwards.
    		if(entity instanceof EntityPlayer) {
				double horizMotion = Math.sqrt(entity.motionX*entity.motionX + entity.motionZ*entity.motionZ);
				if(horizMotion > 0.0) {
				if(debug) System.out.println("Horizonal Motion: " + horizMotion);
				}
				if(entity.isSneaking()) {
					//Make the player go up slowly. Weird numbers, right?
					entity.motionY = 0.085102044; 
				}
				else {
					//Make the player go down slowly. Seriously, I don't know why these numbers are so exact.
					entity.motionY = 0.074897955; 
				}
				if(horizMotion > 0.001) {
					//If the player is moving significantly horizontally, make them sink faster.
					entity.motionY -= horizMotion*0.3;
				}
				entity.fallDistance = 0;
    		}
    		else { 
    			//If not a player, just set the entity in a web. 
    			//It's a bit glitchy otherwise... especially with chickens, who don't sink.
    			entity.setInWeb();	
    		}
     	}
    }
    
    //Returns true if the colliding entity is inside the quicksand.
    private boolean insideQuicksand(World world, int x, int y, int z, Entity entity)
    {
    	if(world.getBlock(x, y, z) != BlockQuickSand.instance) {
    		return false;
    	}
    	
    	//Need to add height for players! I have NO idea why this works. It shouldn't.
    	if(entity instanceof EntityPlayer) {
			return entity.posY < (double)y + (double)entity.height + getQuicksandBlockLevel(world, x, y, z);
    	}
    	else {
    		//This is what I would expect to work... And it does work for all entities except players.
    		return entity.posY < (double)y + getQuicksandBlockLevel(world, x, y, z);
    	}
    }
    
    //Returns true if the entity's head is in inside the quicksand.
    public static boolean headInQuicksand(World world, int x, int y, int z, Entity entity)
    {
    	x = MathHelper.floor_double(entity.posX);
    	int headY = MathHelper.floor_double(entity.posY + entity.getEyeHeight());    	
    	z = MathHelper.floor_double(entity.posZ);

    	if(world.getBlock(x, headY, z) != BlockQuickSand.instance) {
    		return false;
    	}
    	
    	return entity.posY + entity.getEyeHeight() < y + getQuicksandBlockLevel(world, x, y, z);
    }
    
    private void tryFlowing(World world, int x, int y, int z) {
		//Try to flow downwards.
    	//if(debug) System.out.println("Trying to flow down.");
		boolean flowedDown = flowDown(world, x, y, z);
		
		//Try to flow sideways.
		if(!flowedDown) {
	    	//if(debug) System.out.println("Trying to flow sideways.");
			Random rand = new Random();
			int r = rand.nextInt(4);
			int xOffset, zOffset;
			
			for(int i = 0; i < 4; i++) {
				//Set a random offset (0 means x=0, z=1, 1 means x=0, z=-1, 2 means x=1, z=0, and 3 means x=-1, z=0).
				xOffset = (r == 2 ? 1 : r == 3 ? -1 : 0);
				zOffset = (r == 0 ? 1 : r == 1 ? -1 : 0);
				
				flowSideways(world, x, y, z, xOffset, zOffset);
				
				r++;
				if(r > 3) {
					r = 0;
				}
			}			
		}
    }

    //Try to flow downwards.
    private boolean flowDown(World world, int x, int y, int z)
    {
    	if(world.getBlock(x, y, z) == BlockQuickSand.instance) {
    		//Flow the entire block downwards, or as much as possible.
			int sandAmount = getQuicksandLevel(world, x, y, z);
			int amountFlowed = flowIntoBlock(world, x, y-1, z, sandAmount);
			
			if(amountFlowed > 0) {
				if(sandAmount <= amountFlowed) {
					world.setBlockToAir(x, y, z);
					return true;
				}
				else {
					world.setBlockMetadataWithNotify(x, y, z, 16 - (sandAmount - amountFlowed), 3);
					return true;
				}
			}
		}
    	
    	return false;
    }
    
    //Try to flow into an adjacent block.
    private boolean flowSideways(World world, int x, int y, int z, int xOffset, int zOffset) {
    	if(world.getBlock(x, y, z) == BlockQuickSand.instance) {
    		//A bit viscous; won't flow into an adjacent quicksand block unless its quicksand level is at least 2 less. 
	    	if(world.getBlockMetadata(x, y, z) < 15 && 
	    			(world.isAirBlock(x + xOffset, y, z + zOffset) || (world.getBlock(x + xOffset, y, z + zOffset) == BlockQuickSand.instance && 
	    			getQuicksandLevel(world, x, y, z) > getQuicksandLevel(world, x + xOffset, y, z + zOffset) + 2))) {
	    		//Only flow one piece of the block sideways, in the given direction.
	    		int sandAmount = getQuicksandLevel(world, x, y, z);
	    		int amountFlowed = flowIntoBlock(world, x+xOffset, y, z+zOffset, 1);
	    		
	    		if(amountFlowed > 0) {
	    			if(sandAmount <= amountFlowed) {
	    				world.setBlockToAir(x, y, z);
						return true; //This should never happen! But it's here just in case.
	    			}
	    			else {
						world.setBlockMetadataWithNotify(x, y, z, 16 - (sandAmount - amountFlowed), 3);
						return true;
					}
	    		}
	    	}
    	}
    	return false;
    }
    
    /**
     * Tries to add quicksand into this block. Returns the amount of quicksand meta that was added.
     * 
     * @param world World of the block to flow into.
     * @param x X of the block to flow into.
     * @param y Y of the block to flow into.
     * @param z Z of the block to flow into.
     * @param flowAmount Amount of quicksand that should try to flow into this block.
     * @return True if the quicksand can flow into this block.
     */
    public int flowIntoBlock(World world, int x, int y, int z, int flowAmount)
    {
    	Block toFlowInto = world.getBlock(x, y, z);
    	if (flowAmount > 0 && ((toFlowInto == BlockQuickSand.instance && world.getBlockMetadata(x, y, z) != 0) || world.isAirBlock(x, y, z))) {
    		//Try to flow into this block; Calculate how much metadata to subtract from it.
    		if(toFlowInto == Blocks.air){
    			if(debug) System.out.println("Flowing into air block at x="+x+", y="+y+", z="+z+". Setting metadata to " + (16 - flowAmount));
    			world.setBlock(x, y, z, BlockQuickSand.instance, 16 - flowAmount, 3);
    			
    			return flowAmount;
    		}
    		else if(toFlowInto == BlockQuickSand.instance) {
	    		int emptyAmount = world.getBlockMetadata(x, y, z); //Amount of "air space" or empty space in the quicksand block.
	    		int finalFlowAmount = Math.min(emptyAmount, flowAmount);
	    		
	    		if(finalFlowAmount > 0) {
	    			if(debug) System.out.println("Flowing into quicksand block at x="+x+", y="+y+", z="+z+". Setting metadata to " + (emptyAmount - finalFlowAmount));
	    			world.setBlockMetadataWithNotify(x, y, z, emptyAmount - finalFlowAmount, 3);
	    		}
	    		
	    		return finalFlowAmount;
    		}
    		
    		if(debug) System.out.println("How did I even get here?");
    		//Shouldn't ever reach this point.
    		return 0;
    	}
    	else {
    		//Can't flow into this block.
//    		if(debug) System.out.println("Can't flow into a '" + world.getBlock(x, y, z).getUnlocalizedName() + "' block with meta " + world.getBlockMetadata(x, y, z));
    		return 0;
    	}

    }

    
    //Get the quicksand level (for rendering, and to see if an entity's head is inside it).
    public static int getQuicksandLevel(IBlockAccess world, int x, int y, int z)
    {
    	if(world.getBlock(x, y, z) == BlockQuickSand.instance)
    		return getQuicksandLevelFromMeta(world.getBlockMetadata(x, y, z));
    	
    	return 0;
    }
    
    public static double getQuicksandBlockLevel(IBlockAccess world, int x, int y, int z) {
    	return ((double)getQuicksandLevel(world, x, y, z)) * 0.0625;
    }
    
    public static int getQuicksandLevelFromMeta(int meta) {
    	return 16 - meta;
    }
    
    public static int getMetaFromQuicksandLevel(int level) {
    	return 16 - level;
    }
}
