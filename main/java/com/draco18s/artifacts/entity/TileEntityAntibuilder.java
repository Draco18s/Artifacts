package com.draco18s.artifacts.entity;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;

import com.draco18s.artifacts.ArtifactServerEventHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.block.*;
import com.draco18s.artifacts.client.RadarParticle;
import com.draco18s.artifacts.network.PacketHandlerClient;
import com.draco18s.artifacts.network.SToCMessage;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;

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
				//while(lastupdate % 2 == 0 && particles.size() > 0) {
				//	Vec3 v = particles.remove(0);
				//	drawParticleLine(v.xCoord, v.yCoord, v.zCoord, xCoord+0.5, yCoord+0.5, zCoord+0.5);
				//}
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
				//System.out.println("Block info saved");
			}
			else {
				lastupdate = 10;
				//System.out.println("Checking area");
				for(int ox = 0; ox <= 10; ox++) {
					for(int oy = 0; oy <= 10; oy++) {
						for(int oz = 0; oz <= 10; oz++) {
							//chests too?
							if(blocks[ox*121+oy*11+oz] != 0 && 
									(Block.getBlockById(blocks[ox*121+oy*11+oz]).getMaterial() == Material.circuits 
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(BlockTrap.instance)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.daylight_detector)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.iron_door)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.wooden_door)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.trapdoor)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.wooden_pressure_plate)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.stone_pressure_plate)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.heavy_weighted_pressure_plate)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.light_weighted_pressure_plate))
									&& blocks[ox*121+oy*11+oz] != Block.getIdFromBlock(Blocks.torch)) {
							/*if(blocks[ox*121+oy*11+oz] == Block.woodenButton.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.stoneButton.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.pressurePlatePlanks.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.pressurePlateStone.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.pressurePlateIron.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.pressurePlateGold.blockID //
								|| blocks[ox*121+oy*11+oz] == BlockInvisiblePressurePlate.obsidian.blockID //
								|| blocks[ox*121+oy*11+oz] == BlockInvisiblePressurePlate.instance.blockID //
								|| blocks[ox*121+oy*11+oz] == BlockWallPlate.obsidian.blockID //
								|| blocks[ox*121+oy*11+oz] == BlockWallPlate.instance.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.trapdoor.blockID //
								|| blocks[ox*121+oy*11+oz] == BlockTrap.instance.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.doorIron.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.doorWood.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.lever.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.redstoneWire.blockID //
								|| blocks[ox*121+oy*11+oz] == Block.daylightSensor.blockID //
								|| Block.blocksList[blocks[ox*121+oy*11+oz]].blockMaterial == Material.circuits) {*/
								Block wBlock = worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								if(Block.getIdFromBlock(wBlock) != blocks[ox*121+oy*11+oz] && (wBlock == null || wBlock.getMaterial() != Material.circuits)) {
									List l = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord-3, yCoord-3, zCoord-3, xCoord+3, yCoord+3, zCoord+3));
									//System.out.println("Block was broken: " + l.size());
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
									//System.out.println("Bad Block ID (A)");
									worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, Block.getBlockById(blocks[ox*121+oy*11+oz]));
									worldObj.setBlockMetadataWithNotify(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, metas[ox*121+oy*11+oz], 3);
									drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
									//particles.add(Vec3.createVectorHelper(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5));
								}
								else {
									
								}
							}
							else if(blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.fire)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(BlockAntibuilder.instance)
									/*|| blocks[ox*121+oy*11+oz] == Block.redstoneComparatorIdle.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.redstoneComparatorActive.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.redstoneRepeaterIdle.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.redstoneRepeaterActive.blockID */
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.lit_furnace)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.furnace)
									/*|| blocks[ox*121+oy*11+oz] == Block.tripWireSource.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.tripWire.blockID*/ 
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.web)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.redstone_lamp)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.lit_redstone_lamp) 
									/*|| blocks[ox*121+oy*11+oz] == Block.torchRedstoneIdle.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.torchRedstoneActive.blockID */
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(BlockStoneBrickMovable.instance) 
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.sticky_piston)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.piston_head) 
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.piston)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.piston_extension)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(Blocks.torch)
									|| blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(BlockSword.instance)
									|| Block.getBlockById(blocks[ox*121+oy*11+oz]).getUnlocalizedName().toLowerCase().contains("leaves") 
									|| Block.getBlockById(blocks[ox*121+oy*11+oz]).getUnlocalizedName().toLowerCase().contains("leaf")
									|| Block.getBlockById(blocks[ox*121+oy*11+oz]) instanceof BlockFalling) {

								Block wBlock = worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								int wMeta = worldObj.getBlockMetadata(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								if(blocks[ox*121+oy*11+oz] == Block.getIdFromBlock(BlockStoneBrickMovable.instance) && wBlock == Blocks.air) {
									Block a1 = worldObj.getBlock(xCoord+ox-4, yCoord+oy-5, zCoord+oz-5);
									Block a2 = worldObj.getBlock(xCoord+ox-6, yCoord+oy-5, zCoord+oz-5);
									Block a3 = worldObj.getBlock(xCoord+ox-5, yCoord+oy-4, zCoord+oz-5);
									Block a4 = worldObj.getBlock(xCoord+ox-5, yCoord+oy-6, zCoord+oz-5);
									Block a5 = worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-4);
									Block a6 = worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-6);
									
									Block b1 = worldObj.getBlock(xCoord+ox-3, yCoord+oy-5, zCoord+oz-5);
									Block b2 = worldObj.getBlock(xCoord+ox-7, yCoord+oy-5, zCoord+oz-5);
									Block b3 = worldObj.getBlock(xCoord+ox-5, yCoord+oy-3, zCoord+oz-5);
									Block b4 = worldObj.getBlock(xCoord+ox-5, yCoord+oy-7, zCoord+oz-5);
									Block b5 = worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-3);
									Block b6 = worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-7);
									if((a1 == BlockStoneBrickMovable.instance && b1 == Blocks.sticky_piston)
											|| (a2 == BlockStoneBrickMovable.instance && b2 == Blocks.sticky_piston)
											|| (a3 == BlockStoneBrickMovable.instance && b3 == Blocks.sticky_piston)
											|| (a4 == BlockStoneBrickMovable.instance && b4 == Blocks.sticky_piston)
											|| (a5 == BlockStoneBrickMovable.instance && b5 == Blocks.sticky_piston)
											|| (a6 == BlockStoneBrickMovable.instance && b6 == Blocks.sticky_piston)) {
										blocks[ox*121+oy*11+oz] = (short) Block.getIdFromBlock(wBlock);
										metas[ox*121+oy*11+oz] = (byte)wMeta;
									}
									else {
										//System.out.println("Bad Block ID (B)");
										worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, Block.getBlockById(blocks[ox*121+oy*11+oz]), metas[ox*121+oy*11+oz], 3);
										drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
										//particles.add(Vec3.createVectorHelper(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5));
									}
									/*if(a1 != BlockStoneBrickMovable.instance.blockID 
											&& a2 != BlockStoneBrickMovable.instance.blockID 
											&& a3 != BlockStoneBrickMovable.instance.blockID 
											&& a4 != BlockStoneBrickMovable.instance.blockID 
											&& a5 != BlockStoneBrickMovable.instance.blockID 
											&& a6 != BlockStoneBrickMovable.instance.blockID) {
										worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, blocks[ox*121+oy*11+oz], metas[ox*121+oy*11+oz], 3);
										drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
										particles.add(Vec3.createVectorHelper(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5));
									}
									else {
										blocks[ox*121+oy*11+oz] = (short) wid;
										metas[ox*121+oy*11+oz] = (byte)wmd;
									}*/
								}
							}
							else {
								//System.out.println("Bad Block ID (A)");
								Block wBlock = worldObj.getBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								int wMeta = worldObj.getBlockMetadata(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
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
										if(expTNT < maxTNT) {
											expTNT++;
//											List l = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord-3, yCoord-3, zCoord-3, xCoord+3, yCoord+3, zCoord+3));
//											while(l.size() > 0) {
//												EntityItem i = (EntityItem) l.remove(0);
//												if(Item.getIdFromItem(i.getEntityItem().getItem()) == blocks[ox*121+oy*11+oz]) {
//													i.setDead();
//													i.getEntityItem().stackSize = 0;
//												}
//											}
											//System.out.println("Bad Block ID (C)");
											worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, Block.getBlockById(blocks[ox*121+oy*11+oz]), metas[ox*121+oy*11+oz], 3);
											drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
											//particles.add(Vec3.createVectorHelper(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5));
										}
										else {
											//blocks[ox*121+oy*11+oz] = 0;
										}
									}
//									else if(blocks[ox*121+oy*11+oz] != 0 && (Block.getBlockById(blocks[ox*121+oy*11+oz]).getUnlocalizedName().toLowerCase().contains("leaves") || Block.getBlockById(blocks[ox*121+oy*11+oz]).getUnlocalizedName().toLowerCase().contains("leaf"))) {
//										//don't touch leaves
//										blocks[ox*121+oy*11+oz] = (short)Block.getIdFromBlock(wBlock);
//										metas[ox*121+oy*11+oz] = (byte)wMeta;
//									}
									else if(Block.getIdFromBlock(wBlock) != blocks[ox*121+oy*11+oz] || wMeta != metas[ox*121+oy*11+oz]) {
										//System.out.println("Bad Block ID (D)");
										if(blocks[ox*121+oy*11+oz] == 0) {
//											EntityItem ei = new EntityItem(worldObj, xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, new ItemStack(Item.getItemFromBlock(wBlock), 1, wMeta));
//											worldObj.spawnEntityInWorld(ei);
//											xCoord+ox-5, yCoord+oy-5, zCoord+oz-5
											ArtifactServerEventHandler.ignore = true;
											wBlock.dropBlockAsItem(worldObj, xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, wMeta, 0);
											ArtifactServerEventHandler.ignore = false;
										}
										else {
											//System.out.println("Cleaning dropped items");
//											List l = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord-10, yCoord-10, zCoord-10, xCoord+10, yCoord+10, zCoord+10));
//											while(l.size() > 0) {
//												EntityItem i = (EntityItem) l.remove(0);
//												if(Item.getIdFromItem(i.getEntityItem().getItem()) == blocks[ox*121+oy*11+oz]) {
//													i.setDead();
//													i.getEntityItem().stackSize = 0;
//												}
//											}
										}
										worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, Block.getBlockById(blocks[ox*121+oy*11+oz]));
										worldObj.setBlockMetadataWithNotify(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, metas[ox*121+oy*11+oz], 3);
										drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
										//particles.add(Vec3.createVectorHelper(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5));
									}
								}
								else {
									//System.out.println("Block special: " + blocks[ox*121+oy*11+oz] + "->" + wid);
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
