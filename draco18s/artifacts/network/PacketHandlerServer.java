package draco18s.artifacts.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import draco18s.artifacts.entity.ArrowEffect;
import draco18s.artifacts.entity.EntitySpecialArrow;

public class PacketHandlerServer implements IPacketHandler{
	@Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
    	//System.out.println("Packet found: " + packet.channel);
        if (packet.channel.equals("Artifacts"))
        {
            handleRandom(packet, player);
        }
    }

    private void handleRandom(Packet250CustomPayload packet, Player player)
    {
        EntityPlayer p = (EntityPlayer) player;
        World world = p.worldObj;
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));
        ItemStack is;
        //System.out.println("Packet get");
        try
        {
            int effectID = dis.readInt();
            switch(effectID) {
            	case 1:
            		//Healing
            		//p.setHealth(p.getHealth()+dis.readFloat());
            		p.heal(dis.readFloat());
            		break;
            	case 3:
            		//Fireballs
            		if(dis.readInt() == p.entityId) {
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
                        is = p.inventory.getStackInSlot(dis.readInt());
                        if(is != null)
                        	is.damageItem(1, p);
                        //System.out.println(is.getItemDamage());
            		}
            		else {
            			System.out.println("Oh god, what player! D:");
            		}
            		break;
            	case 5:
            		//Lightning
            		if(dis.readInt() == p.entityId) {
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
                        MovingObjectPosition movingobjectposition = world.rayTraceBlocks_do_do(vec3, vec31, false, true);
                        if (movingobjectposition == null) {
                        	return;
                        }
                        if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
                        {
                            int ix = movingobjectposition.blockX;
                            int iy = movingobjectposition.blockY;
                            int iz = movingobjectposition.blockZ;
                            if (!world.isBlockFullCube(ix, iy, iz))
                            {
                                --iy;
                            }
                            EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, ix, iy, iz);
                            world.addWeatherEffect(entityLightningBolt);
                        }
                        if (movingobjectposition.typeOfHit == EnumMovingObjectType.ENTITY) {
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
            		is = p.inventory.getStackInSlot(dis.readInt());
            		if(is != null)
                    	is.damageItem(1, p);
            		break;
            	case 7:
            		//explosions
            		int id = dis.readInt();
            		if(id >= 0) {
            			//System.out.println("Reading entity position");
	            		Entity ent = world.getEntityByID(id);
	            		world.newExplosion(p, ent.posX, ent.posY, ent.posZ, 3F, false, true);
            		}
            		else {
            			//System.out.println("Reading block position");
            			int px = dis.readInt();
            			int py = dis.readInt();
            			int pz = dis.readInt();
            			//System.out.println(px+","+py+","+pz);
            			world.newExplosion(p, px, py, pz, 3F, false, true);
            		}
            		is = p.inventory.getStackInSlot(dis.readInt());
            		if(is != null)
                    	is.damageItem(3, p);
            		break;
            	case 10:
            		//all potions
            		Entity ent = world.getEntityByID(dis.readInt());
            		int pid = dis.readInt();
            		int dur = dis.readInt();
            		int aug = dis.readInt();
            		if(ent instanceof EntityLivingBase) {
            			EntityLivingBase living = (EntityLivingBase) ent;
            			living.addPotionEffect(new PotionEffect(pid, dur, aug));
            		}
            		break;
            	case 26:
            		is = p.inventory.getStackInSlot(dis.readInt());
            		is.setItemDamage(is.getItemDamage()-5);
            		is = p.inventory.getStackInSlot(dis.readInt());
            		if(is != null)
                    	is.damageItem(2, (EntityLivingBase) p);
            		break;
            	case 27:
            		//Exploding Arrows
            		if(dis.readInt() == p.entityId) {
                        //Vec3 vec3 = p.getLook(1.0F);
                        //double d8 = 4.0D;
                        is = p.inventory.getStackInSlot(dis.readInt());
                        if(is != null) {
	                        EntitySpecialArrow arrow = new EntitySpecialArrow(world, p, 2, 2, ArrowEffect.EXPLOSIVE);
	                        world.spawnEntityInWorld(arrow);
	                        is.damageItem(1, p);
	                        world.playSoundAtEntity((Entity) player, "random.bow", 1.0F, 1.2F);
                        }
                        //System.out.println(is.getItemDamage());
            		}
            		else {
            			System.out.println("Oh god, what player! D:");
            		}
            		break;
            	case 4096:
            		//add right-click delay
            		int d = dis.readInt();
            		is = p.inventory.getStackInSlot(dis.readInt());
            		if(is!=null) {
            			is.stackTagCompound.setInteger("onItemRightClickDelay", d);
            		}
            		break;
            }
        }
        catch  (IOException e)
        {
            System.out.println("Failed to read packet");
        }
        finally
        {
        }
    }
}
