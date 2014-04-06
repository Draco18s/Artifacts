package draco18s.artifacts.entity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import draco18s.artifacts.block.*;
import draco18s.artifacts.client.RadarParticle;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntityAntibuilder extends TileEntity {
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
			AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(xCoord-32, yCoord-32, zCoord-32, xCoord+32, yCoord+32, zCoord+32);
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
							blocks[ox*121+oy*11+oz] = (short)worldObj.getBlockId(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
							metas[ox*121+oy*11+oz] = (byte)worldObj.getBlockMetadata(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
							if(blocks[ox*121+oy*11+oz] == BlockTrap.instance.blockID) {
								TileEntityTrap te = (TileEntityTrap)worldObj.getBlockTileEntity(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
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
									(Block.blocksList[blocks[ox*121+oy*11+oz]].blockMaterial == Material.circuits 
									|| blocks[ox*121+oy*11+oz] == BlockTrap.instance.blockID
									|| blocks[ox*121+oy*11+oz] == Block.daylightSensor.blockID
									|| blocks[ox*121+oy*11+oz] == Block.doorIron.blockID
									|| blocks[ox*121+oy*11+oz] == Block.doorWood.blockID
									|| blocks[ox*121+oy*11+oz] == Block.trapdoor.blockID
									|| blocks[ox*121+oy*11+oz] == Block.pressurePlatePlanks.blockID
									|| blocks[ox*121+oy*11+oz] == Block.pressurePlateStone.blockID
									|| blocks[ox*121+oy*11+oz] == Block.pressurePlateIron.blockID
									|| blocks[ox*121+oy*11+oz] == Block.pressurePlateGold.blockID)
									&& blocks[ox*121+oy*11+oz] != Block.torchWood.blockID) {
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
								int wid = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								if(wid != blocks[ox*121+oy*11+oz] && (Block.blocksList[wid] == null || Block.blocksList[wid].blockMaterial != Material.circuits)) {
									List l = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(xCoord-3, yCoord-3, zCoord-3, xCoord+3, yCoord+3, zCoord+3));
									//System.out.println("Block was broken: " + l.size());
									while(l.size() > 0) {
										EntityItem i = (EntityItem) l.remove(0);
										if(i.getEntityItem().itemID == blocks[ox*121+oy*11+oz]) {
											i.setDead();
											i.getEntityItem().stackSize = 0;
										}
										else if(i.getEntityItem().itemID == Block.blocksList[blocks[ox*121+oy*11+oz]].idDropped(blocks[ox*121+oy*11+oz],worldObj.rand,metas[ox*121+oy*11+oz])) {
											i.setDead();
											i.getEntityItem().stackSize = 0;
										}
									}
									System.out.println("Bad Block ID (A)");
									worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, blocks[ox*121+oy*11+oz]);
									worldObj.setBlockMetadataWithNotify(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, metas[ox*121+oy*11+oz], 3);
									drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
									//particles.add(Vec3.createVectorHelper(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5));
								}
								else {
									
								}
							}
							else if(blocks[ox*121+oy*11+oz] == Block.fire.blockID 
									|| blocks[ox*121+oy*11+oz] == BlockAntibuilder.instance.blockID 
									/*|| blocks[ox*121+oy*11+oz] == Block.redstoneComparatorIdle.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.redstoneComparatorActive.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.redstoneRepeaterIdle.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.redstoneRepeaterActive.blockID */
									|| blocks[ox*121+oy*11+oz] == Block.furnaceBurning.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.furnaceIdle.blockID 
									/*|| blocks[ox*121+oy*11+oz] == Block.tripWireSource.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.tripWire.blockID*/ 
									|| blocks[ox*121+oy*11+oz] == Block.web.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.redstoneLampActive.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.redstoneLampIdle.blockID 
									/*|| blocks[ox*121+oy*11+oz] == Block.torchRedstoneIdle.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.torchRedstoneActive.blockID */
									|| blocks[ox*121+oy*11+oz] == BlockStoneBrickMovable.instance.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.pistonStickyBase.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.pistonMoving.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.pistonBase.blockID 
									|| blocks[ox*121+oy*11+oz] == Block.pistonExtension.blockID
									|| blocks[ox*121+oy*11+oz] == Block.torchWood.blockID
									|| blocks[ox*121+oy*11+oz] == BlockSword.instance.blockID) {

								int wid = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								int wmd = worldObj.getBlockMetadata(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								if(blocks[ox*121+oy*11+oz] == BlockStoneBrickMovable.instance.blockID && wid == 0) {
									int a1 = worldObj.getBlockId(xCoord+ox-4, yCoord+oy-5, zCoord+oz-5);
									int a2 = worldObj.getBlockId(xCoord+ox-6, yCoord+oy-5, zCoord+oz-5);
									int a3 = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-4, zCoord+oz-5);
									int a4 = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-6, zCoord+oz-5);
									int a5 = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-5, zCoord+oz-4);
									int a6 = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-5, zCoord+oz-6);
									
									int b1 = worldObj.getBlockId(xCoord+ox-3, yCoord+oy-5, zCoord+oz-5);
									int b2 = worldObj.getBlockId(xCoord+ox-7, yCoord+oy-5, zCoord+oz-5);
									int b3 = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-3, zCoord+oz-5);
									int b4 = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-7, zCoord+oz-5);
									int b5 = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-5, zCoord+oz-3);
									int b6 = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-5, zCoord+oz-7);
									if((a1 == BlockStoneBrickMovable.instance.blockID && b1 == Block.pistonStickyBase.blockID)
											|| (a2 == BlockStoneBrickMovable.instance.blockID && b2 == Block.pistonStickyBase.blockID)
											|| (a3 == BlockStoneBrickMovable.instance.blockID && b3 == Block.pistonStickyBase.blockID)
											|| (a4 == BlockStoneBrickMovable.instance.blockID && b4 == Block.pistonStickyBase.blockID)
											|| (a5 == BlockStoneBrickMovable.instance.blockID && b5 == Block.pistonStickyBase.blockID)
											|| (a6 == BlockStoneBrickMovable.instance.blockID && b6 == Block.pistonStickyBase.blockID)) {
										blocks[ox*121+oy*11+oz] = (short) wid;
										metas[ox*121+oy*11+oz] = (byte)wmd;
									}
									else {
										System.out.println("Bad Block ID (B)");
										worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, blocks[ox*121+oy*11+oz], metas[ox*121+oy*11+oz], 3);
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
								int wid = worldObj.getBlockId(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								int wmd = worldObj.getBlockMetadata(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5);
								if(wid != Block.fire.blockID 
										&& wid != Block.torchWood.blockID 
										&& wid != BlockStoneBrickMovable.instance.blockID 
										&& wid != Block.pistonStickyBase.blockID 
										&& wid != Block.pistonMoving.blockID 
										&& wid != Block.pistonBase.blockID 
										&& wid != Block.pistonExtension.blockID
										&& wid != BlockSword.instance.blockID
										&& 	   !((wid == Block.dirt.blockID
												&& blocks[ox*121+oy*11+oz] == Block.grass.blockID)
												|| (wid == Block.grass.blockID
												&& blocks[ox*121+oy*11+oz] == Block.dirt.blockID))) {
											
									if(wid != blocks[ox*121+oy*11+oz] && blocks[ox*121+oy*11+oz] == Block.tnt.blockID) {
										if(expTNT < maxTNT) {
											expTNT++;
											List l = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(xCoord-3, yCoord-3, zCoord-3, xCoord+3, yCoord+3, zCoord+3));
											while(l.size() > 0) {
												EntityItem i = (EntityItem) l.remove(0);
												if(i.getEntityItem().itemID == blocks[ox*121+oy*11+oz]) {
													i.setDead();
													i.getEntityItem().stackSize = 0;
												}
											}
											System.out.println("Bad Block ID (C)");
											worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, blocks[ox*121+oy*11+oz], metas[ox*121+oy*11+oz], 3);
											drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
											//particles.add(Vec3.createVectorHelper(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5));
										}
										else {
											//blocks[ox*121+oy*11+oz] = 0;
										}
									}
									else if(blocks[ox*121+oy*11+oz] != 0 && (Block.blocksList[blocks[ox*121+oy*11+oz]].getUnlocalizedName().toLowerCase().contains("leaves") || Block.blocksList[blocks[ox*121+oy*11+oz]].getUnlocalizedName().toLowerCase().contains("leaf"))) {
										//don't touch leaves
										blocks[ox*121+oy*11+oz] = (short)wid;
										metas[ox*121+oy*11+oz] = (byte)wmd;
									}
									else if(wid != blocks[ox*121+oy*11+oz] || wmd != metas[ox*121+oy*11+oz]) {
										System.out.println("Bad Block ID (D)");
										if(blocks[ox*121+oy*11+oz] == 0) {
											//EntityItem ei = new EntityItem(worldObj, ox, oy, oz, new ItemStack(Block.blocksList[wid], 1, wmd));
											//worldObj.spawnEntityInWorld(ei);
											//xCoord+ox-5, yCoord+oy-5, zCoord+oz-5
											Block.blocksList[wid].dropBlockAsItem(worldObj, xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, wmd, 0);
										}
										else {
											//System.out.println("Cleaning dropped items");
											List l = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(xCoord-10, yCoord-10, zCoord-10, xCoord+10, yCoord+10, zCoord+10));
											while(l.size() > 0) {
												EntityItem i = (EntityItem) l.remove(0);
												if(i.getEntityItem().itemID == blocks[ox*121+oy*11+oz]) {
													i.setDead();
													i.getEntityItem().stackSize = 0;
												}
											}
										}
										worldObj.setBlock(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, blocks[ox*121+oy*11+oz]);
										worldObj.setBlockMetadataWithNotify(xCoord+ox-5, yCoord+oy-5, zCoord+oz-5, metas[ox*121+oy*11+oz], 3);
										drawParticleLine(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5, xCoord+0.5, yCoord+0.5, zCoord+0.5);
										//particles.add(Vec3.createVectorHelper(xCoord+ox-5+0.5, yCoord+oy-5+0.5, zCoord+oz-5+0.5));
									}
								}
								else {
									//System.out.println("Block special: " + blocks[ox*121+oy*11+oz] + "->" + wid);
									blocks[ox*121+oy*11+oz] = (short) wid;
									metas[ox*121+oy*11+oz] = (byte)wmd;
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
    		ByteArrayOutputStream bt = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(bt);
			try
			{
				out.writeInt(4097);
				out.writeDouble(tx);
				out.writeDouble(ty);
				out.writeDouble(tz);
				out.writeInt(i-r);
				Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 32, worldObj.provider.dimensionId, packet);
			}
			catch (IOException ex)
			{
				System.out.println("couldnt send packet!");
			}
    		//worldObj.spawnParticle("reddust", tx, ty, tz, 0.0D, 0.0D, 0.0D);
    		//Minecraft.getMinecraft().effectRenderer.addEffect(new RadarParticle(worldObj, tx, ty, tz, 3, 20));
    	}
    }
}
