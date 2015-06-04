package com.draco18s.artifacts.entity;

import io.netty.buffer.Unpooled;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;

import com.draco18s.artifacts.ArtifactServerEventHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.block.BlockAntibuilder;
import com.draco18s.artifacts.block.BlockStoneBrickMovable;
import com.draco18s.artifacts.block.BlockSword;
import com.draco18s.artifacts.block.BlockTrap;
import com.draco18s.artifacts.network.PacketHandlerClient;
import com.draco18s.artifacts.network.SToCMessage;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class TileEntityAntibuilder extends TileEntity {
	public static class AntibuilderLocation {
		public int x, y, z, dimension;
		
		public AntibuilderLocation(int xToSet, int yToSet, int zToSet, int dimToSet) {
			x = xToSet;
			y = yToSet;
			z = zToSet;
			dimension = dimToSet;
		}
	}
	
	//Comparator for comparing locations (for quick adding/deleting in a set/map). 
	private static Comparator<AntibuilderLocation> ALcomparator = new Comparator<AntibuilderLocation>() {
		@Override
		public int compare(AntibuilderLocation first, AntibuilderLocation second) {
			if(first.x != second.x){
				return first.x - second.x;
			}
			if(first.y != second.y){
				return first.y - second.y;
			}
			if(first.z != second.z){
				return first.z - second.z;
			}
			if(first.dimension != second.dimension){
				return first.dimension - second.dimension;
			}
			
			return 0;
		}
	};
	
	public static TreeMap<AntibuilderLocation, Integer> antibuilders = new TreeMap<AntibuilderLocation, Integer>(ALcomparator);
	
	private short[] blocks = new short[1332];
	private byte[] metas = new byte[1332];
	private boolean active = true;
	private boolean stored = false;
	private int lastupdate = 10;
	private Random rand = new Random();
	//private ArrayList<Vec3> particles = new ArrayList<Vec3>();
	private int maxTNT = 3;
	private int expTNT = 0;
	
	@Override
	public void updateEntity() {
		if(active) {
			antibuilders.put(new AntibuilderLocation(this.xCoord, this.yCoord, this.zCoord, this.worldObj.provider.dimensionId), 10);
			
			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(xCoord-32, yCoord-32, zCoord-32, xCoord+32, yCoord+32, zCoord+32);
			List w = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			if(w.size() == 0) {
				expTNT = 0;
				return;
			}
			if(lastupdate > 0) {
				lastupdate--;
			}
			else if(!stored) {
				stored = true;
				for(int ox = 0; ox <= 10; ox++) {
					for(int oy = 0; oy <= 10; oy++) {
						for(int oz = 0; oz <= 10; oz++) {
							blocks[ox*121+oy*11+oz] = (short)Block.getIdFromBlock(worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5));
							metas[ox*121+oy*11+oz] = (byte)worldObj.getBlockMetadata(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
							if(blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(BlockTrap.instance)) {
								TileEntityTrap te = (TileEntityTrap)worldObj.getTileEntity(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								te.setAntibuilder(xCoord, yCoord, zCoord);
							}
						}
					}
				}
				expTNT = 0;
			}
			else {
				lastupdate = 10;
				
				//Check the area for any block changes
				
				for(int ox = 0; ox <= 10; ox++) {
					for(int oy = 0; oy <= 10; oy++) {
						for(int oz = 0; oz <= 10; oz++) {
							
							Block wBlock = worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
							int wMeta = worldObj.getBlockMetadata(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
							Block sBlock = Block.getBlockById(blocks[ox*121+oy*11+oz]);
							int sMeta = metas[ox*121+oy*11+oz];
							
							//Ignore certain blocks
							
							if(sBlock != Blocks.air && 
									(sBlock.getMaterial() == Material.circuits 
									|| sBlock == BlockTrap.instance
									|| sBlock == Blocks.daylight_detector
									|| sBlock == Blocks.iron_door
									|| sBlock == Blocks.wooden_door
									|| sBlock == Blocks.trapdoor
									|| sBlock == Blocks.wooden_pressure_plate
									|| sBlock == Blocks.stone_pressure_plate
									|| sBlock == Blocks.heavy_weighted_pressure_plate
									|| sBlock == Blocks.light_weighted_pressure_plate)
									&& sBlock != Blocks.torch) {
								if(wBlock != sBlock && (wBlock == null || wBlock.getMaterial() != Material.circuits)) {
									
									//Delete drops from these blocks
									
									List l = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord-3, yCoord-3, zCoord-3, xCoord+3, yCoord+3, zCoord+3));
									
									while(l.size() > 0) {
										EntityItem i = (EntityItem) l.remove(0);
										if(Item.getIdFromItem(i.getEntityItem().getItem()) == blocks[ox*121+oy*11+oz]) {
											i.setDead();
											i.getEntityItem().stackSize = 0;
										}
										else if(i.getEntityItem().getItem() == Block.getBlockById(blocks[ox*121+oy*11+oz]).getItemDropped(blocks[ox*121+oy*11+oz],worldObj.rand,metas[ox*121+oy*11+oz])) {
											i.setDead();
											i.getEntityItem().stackSize = 0;
										}
									}
									
									worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, sBlock);
									worldObj.setBlockMetadataWithNotify(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, sMeta, 3);
									drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
								}
							}
							else if(sBlock == Blocks.fire
									|| sBlock == BlockAntibuilder.instance
									|| sBlock == Blocks.lit_furnace
									|| sBlock == Blocks.furnace
									|| sBlock == Blocks.web
									|| sBlock == Blocks.redstone_lamp
									|| sBlock == Blocks.lit_redstone_lamp
									|| sBlock == BlockStoneBrickMovable.instance
									|| sBlock == Blocks.sticky_piston
									|| sBlock == Blocks.piston_head
									|| sBlock == Blocks.piston
									|| sBlock == Blocks.piston_extension
									|| sBlock == Blocks.torch
									|| sBlock == BlockSword.instance
									|| sBlock instanceof IShearable
									|| sBlock instanceof IPlantable
									|| (sBlock instanceof IGrowable && !sBlock.isOpaqueCube())
									|| sBlock instanceof BlockFalling) {
								
								//Do nothing for these; ignore them completely
								
							}
							else {
								if(wBlock != Blocks.fire
										&& wBlock != Blocks.torch
										&& wBlock != BlockStoneBrickMovable.instance
										&& wBlock != Blocks.sticky_piston 
										&& wBlock != Blocks.piston_head
										&& wBlock != Blocks.piston
										&& wBlock != Blocks.piston_extension
										&& wBlock != BlockSword.instance
										&& 	   !((wBlock == Blocks.dirt
												&& blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.grass))
												|| (wBlock == Blocks.grass
												&& blocks[ox*121+oy*11+oz] ==  Block.getIdFromBlock(Blocks.dirt)))) {
											
									if(Block.getIdFromBlock(wBlock) != blocks[ox*121+oy*11+oz] && blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.tnt)) {
										
										//replace TNT a certain number of times.
										if(expTNT < maxTNT) {
											expTNT++;
											worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, Block.getBlockById(blocks[ox*121+oy*11+oz]), metas[ox*121+oy*11+oz], 3);
											drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
										}
									}

									else if(Block.getIdFromBlock(wBlock) != blocks[ox*121+oy*11+oz] || wMeta != metas[ox*121+oy*11+oz]) {

										//Drop any placed blocks
										if(blocks[ox*121+oy*11+oz] == 0) {
											ArtifactServerEventHandler.ignore = true;
											wBlock.dropBlockAsItem(worldObj, xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, wMeta, 0);
											ArtifactServerEventHandler.ignore = false;
										}
										
										worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, sBlock);
										worldObj.setBlockMetadataWithNotify(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, sMeta, 3);
										drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
									}
								}
								else {
									
									blocks[ox*121+oy*11+oz] = (short) Block.getIdFromBlock(wBlock);
									metas[ox*121+oy*11+oz] = (byte)wMeta;
								}
							}
						}
					}
				}
			}
		}
		else {
			stored = false;
		}
	}
	
	public void setActive(boolean a) {
		//active = a;
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
    }
    
    protected void drawParticleLine(double srcX, double srcY, double srcZ, double destX, double destY, double destZ)
    {
    	if(this.worldObj.isRemote) {
    		return;
    	}
    	
    	int particles = 16;
    	int r = worldObj.rand.nextInt(8);
    	for (int i = 0; i < particles; i++)
    	{
    		double trailFactor = i / (particles - 1.0D);

    		double tx = srcX + (destX - srcX) * trailFactor + rand.nextFloat() * 0.005D;
    		double ty = srcY + (destY - srcY) * trailFactor + rand.nextFloat() * 0.005D;
    		double tz = srcZ + (destZ - srcZ) * trailFactor + rand.nextFloat() * 0.005D;
			try
			{
				PacketBuffer out = new PacketBuffer(Unpooled.buffer());
				out.writeInt(PacketHandlerClient.ANTI_BUILDER);
				out.writeDouble(tx);
				out.writeDouble(ty);
				out.writeDouble(tz);
				out.writeInt(i-r);
				SToCMessage packet = new SToCMessage(out);
				DragonArtifacts.artifactNetworkWrapper.sendToAllAround(packet, new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 32));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.out.println("couldnt send packet!");
			}
    		//worldObj.spawnParticle("reddust", tx, ty, tz, 0.0D, 0.0D, 0.0D);
    		//Minecraft.getMinecraft().effectRenderer.addEffect(new RadarParticle(worldObj, tx, ty, tz, 3, 20));
    	}
    }
}
