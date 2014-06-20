package com.draco18s.artifacts.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.draco18s.artifacts.entity.ArrowEffect;
import com.draco18s.artifacts.entity.EntitySpecialArrow;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class PacketHandlerServer implements IMessageHandler<CToSMessageComponent,IMessage> {

	//Effect Ids
	public static final int HEALING = 1;
	public static final int FIREBALLS = 3;
	public static final int LIGHTNING = 5;
	public static final int EXPLOSIONS = 7;
	public static final int POTIONS = 10;
	public static final int REPAIRING = 26;
	public static final int EXPLODING_ARROWS = 27;
	public static final int DAMAGE_ITEM = 28;

	/**
	 * Handles Server Side Packets (specifically for artifact components). Only returns null.
	 */
	@Override
	public IMessage onMessage(CToSMessageComponent packet, MessageContext context)
	{
		System.out.println("Caught a packet " + packet.getData() + " for player " + packet.getUUID());

		ByteBuf buff = Unpooled.wrappedBuffer(packet.getData());

		try
		{
			int effectID = buff.readInt();
			UUID uuid = packet.getUUID();
			EntityPlayerMP p = null;
			
			List<EntityPlayerMP> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
			
			for(EntityPlayerMP entityPlayer : playerList) {
				if(entityPlayer.getUniqueID().equals(uuid)) {
					p = entityPlayer;
					break;
				}
			}
	        
	        if(p == null) {
	        	System.out.println("Couldn't find a player with UUID " + uuid.toString());
	        	return null;
	        }
	        
			World world = p.worldObj;
			ItemStack is;

			switch(effectID) {
			case HEALING:
				//p.setHealth(p.getHealth()+dis.readFloat());
				p.heal(buff.readFloat());
				break;
			case FIREBALLS:
				if(buff.readInt() == p.getEntityId()) {
					Vec3 vec3 = p.getLook(1.0F);
					double d8 = 4.0D;
					//System.out.println(vec3);
					//EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, p.posX + vec3.xCoord * d8, p.posY, p.posZ + vec3.zCoord * d8, vec3.xCoord, vec3.yCoord, vec3.zCoord);
					EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, p, vec3.xCoord, vec3.yCoord, vec3.zCoord);
					entitylargefireball.posX += vec3.xCoord * d8;
					entitylargefireball.posZ += vec3.zCoord * d8;
					entitylargefireball.accelerationX = vec3.xCoord;
					entitylargefireball.accelerationY = vec3.yCoord;
					entitylargefireball.accelerationZ = vec3.zCoord;
					entitylargefireball.field_92057_e = 1;
					/*entitylargefireball.posX = p.posX;// + vec3.xCoord * d8;
                        entitylargefireball.posY = p.posY + (double)(p.height / 2.0F);
                        entitylargefireball.posZ = p.posZ;// + vec3.zCoord * d8;*/
					//System.out.println(entitylargefireball.posX + "," + entitylargefireball.posY + "," + entitylargefireball.posZ);
					world.spawnEntityInWorld(entitylargefireball);
					is = p.inventory.getStackInSlot(buff.readInt());
					if(is != null)
						is.damageItem(1, p);
					//System.out.println(is.getItemDamage());
				}
				else {
					System.out.println("Oh god, what player! D:");
				}
				break;
			case LIGHTNING:
				if(buff.readInt() == p.getEntityId()) {
					/*Vec3 vec3 = p.getLook(1.0F);
                        double d8 = 4.0D;
                        System.out.println(vec3);
                        double dx = p.posX;
                        double dy = p.posY+0.5;
                        double dz = p.posZ;
                        Vec3 look = p.getLookVec();
                        int count = 0;
                        do {
                        	count++;
                        	dx += look.xCoord;
                        	dy += look.yCoord;
                        	dz += look.zCoord;
                        }while(count < 20 && world.getBlockId((int)dx, (int)dy, (int)dz) == 0);*/
					float f = 1.0F;
					float f1 = p.prevRotationPitch + (p.rotationPitch - p.prevRotationPitch) * f;
					float f2 = p.prevRotationYaw + (p.rotationYaw - p.prevRotationYaw) * f;
					double d0 = p.prevPosX + (p.posX - p.prevPosX) * (double)f;
					double d1 = p.prevPosY + (p.posY - p.prevPosY) * (double)f + 1.62D - (double)p.yOffset;
					double d2 = p.prevPosZ + (p.posZ - p.prevPosZ) * (double)f;
					Vec3 vec3 = world.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
					float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
					float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
					float f5 = -MathHelper.cos(-f1 * 0.017453292F);
					float f6 = MathHelper.sin(-f1 * 0.017453292F);
					float f7 = f4 * f5;
					float f8 = f3 * f5;
					double d3 = 5.0D;
					Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
					MovingObjectPosition movingobjectposition = world.func_147447_a/*rayTraceBlocks_do_do*/(vec3, vec31, false, true, false);
					if (movingobjectposition == null) {
						return null;
					}
					if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
					{
						int ix = movingobjectposition.blockX;
						int iy = movingobjectposition.blockY;
						int iz = movingobjectposition.blockZ;
						if (!world.getBlock(ix, iy, iz).isBlockNormalCube())
						{
							--iy;
						}
						EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, ix, iy, iz);
						world.addWeatherEffect(entityLightningBolt);
					}
					if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
						//System.out.println("Hit entity");
						//System.out.println(movingobjectposition.hitVec);
						double ix = movingobjectposition.hitVec.xCoord;
						double iy = movingobjectposition.hitVec.yCoord;
						double iz = movingobjectposition.hitVec.zCoord;
						EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, ix, iy, iz);
						world.addWeatherEffect(entityLightningBolt);
					}
				}
				else {
					System.out.println("Oh god, what player! D:");
				}
				is = p.inventory.getStackInSlot(buff.readInt());
				if(is != null)
					is.damageItem(1, p);
				break;
			case EXPLOSIONS:
				int id = buff.readInt();
				if(id >= 0) {
					//System.out.println("Reading entity position");
					Entity ent = world.getEntityByID(id);
					world.newExplosion(p, ent.posX, ent.posY, ent.posZ, 3F, false, true);
				}
				else {
					//System.out.println("Reading block position");
					int px = buff.readInt();
					int py = buff.readInt();
					int pz = buff.readInt();
					//System.out.println(px+","+py+","+pz);
					world.newExplosion(p, px, py, pz, 3F, false, true);
				}
				is = p.inventory.getStackInSlot(buff.readInt());
				if(is != null)
					is.damageItem(3, p);
				break;
			case POTIONS:
				Entity ent = world.getEntityByID(buff.readInt());
				int pid = buff.readInt();
				int dur = buff.readInt();
				int aug = buff.readInt();
				if(ent instanceof EntityLivingBase) {
					EntityLivingBase living = (EntityLivingBase) ent;
					living.addPotionEffect(new PotionEffect(pid, dur, aug));
				}
				break;
			case REPAIRING:
				is = p.inventory.getStackInSlot(buff.readInt());
				is.setItemDamage(is.getItemDamage()-5);
				is = p.inventory.getStackInSlot(buff.readInt());
				if(is != null)
					is.damageItem(2, (EntityLivingBase) p);
				break;
			case EXPLODING_ARROWS:
				if(buff.readInt() == p.getEntityId()) {
					//Vec3 vec3 = p.getLook(1.0F);
					//double d8 = 4.0D;
					is = p.inventory.getStackInSlot(buff.readInt());
					if(is != null) {
						EntitySpecialArrow arrow = new EntitySpecialArrow(world, p, 2, 2, ArrowEffect.EXPLOSIVE);
						world.spawnEntityInWorld(arrow);
						is.damageItem(1, p);
						world.playSoundAtEntity(p, "random.bow", 1.0F, 1.2F);
					}
					//System.out.println(is.getItemDamage());
				}
				else {
					System.out.println("Oh god, what player! D:");
				}
				break;
			case DAMAGE_ITEM:
				if(buff.readInt() == p.getEntityId()) {
					int slot = buff.readInt();
					int damageToDeal = buff.readInt();
					
					p.inventory.getStackInSlot(slot).damageItem(damageToDeal, p);
				}
				break;
			case 4096:
				//add right-click delay
				int d = buff.readInt();
				is = p.inventory.getStackInSlot(buff.readInt());
				if(is!=null) {
					is.stackTagCompound.setInteger("onItemRightClickDelay", d);
				}
				break;
			}
		}
		catch (Exception e)
		{
			System.err.println("Problem while handling artifact components!");
			e.printStackTrace();
		}
		
		return null;
	}
}
