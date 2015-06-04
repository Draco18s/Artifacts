package com.draco18s.artifacts.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import com.draco18s.artifacts.ArtifactClientEventHandler;
import com.draco18s.artifacts.ArtifactServerEventHandler;
import com.draco18s.artifacts.client.*;
import com.draco18s.artifacts.entity.*;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketHandlerClient implements IMessageHandler<SToCMessage, IMessage> {
	
	public static final int ORE_RADAR = 23;
	public static final int OBSCURITY = 28;
	public static final int CAKE_PARTICLES = 29;
	public static final int PLAY_RECORD = 30;
	public static final int PEDESTAL = 256;
	public static final int ANTI_BUILDER = 4097;
	
	public PacketHandlerClient() {
		
	}

    public IMessage onMessage(SToCMessage packet, MessageContext context)
    {
    	
    	EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;
        World world = p.worldObj;
        ByteArrayInputStream stream = new ByteArrayInputStream(packet.getData());
        DataInputStream dis = new DataInputStream(stream);
        //System.out.println("Packet get");
        TileEntity te;
        try
        {
            int effectID = dis.readInt();
            switch(effectID) {
	        	case ORE_RADAR:
	        		int x = dis.readInt();
	        		int y = dis.readInt();
	        		int z = dis.readInt();
	        		//if(rand.nextBoolean() && rand.nextBoolean())
	        		if(y >= p.posY)
	        			drawParticle(p.worldObj, x+.5, y+.5, z+.5, "radar", 0);
	        		else
	        			drawParticle(p.worldObj, x+.5, y+.5, z+.5, "radar", 0);
	        		//System.out.println("Server particles");
	        		break;
	        	case OBSCURITY:
	        		p.addPotionEffect(new PotionEffect(14, 600, 0));
	        		ArtifactClientEventHandler.cloaked = true;
	        		break;
            	case PEDESTAL:
            		//Update the display pedestal name on the client

            		te = world.getTileEntity(dis.readInt(), dis.readInt(), dis.readInt());
            		if(te instanceof TileEntityDisplayPedestal) {
            			TileEntityDisplayPedestal ted = (TileEntityDisplayPedestal)te;
            			
            			int nameLength = dis.readInt();
            			String name = "";
            			
            			for(int s = 0; s < nameLength; s++) {
            				name += dis.readChar();
            			}
            			
            			ted.ownerName = name;
            		}
            		break;
            	case CAKE_PARTICLES:
            		//Spawn some particles for when the cake is placed down, and play a sound.
            		
            		Random rand = new Random();
            		int cakeX = dis.readInt();
            		int cakeY = dis.readInt();
            		int cakeZ = dis.readInt();
            		
            		for (int i = 0; i < 20; ++i)
                    {
                        double pX = cakeX + rand.nextDouble();
                        double pY = cakeY + rand.nextDouble()*0.5;
                        double pZ = cakeZ + rand.nextDouble();
                        double vX = rand.nextGaussian() * 0.02D;
                        double vY = rand.nextGaussian() * 0.02D;
                        double vZ = rand.nextGaussian() * 0.02D;
                        
                        Minecraft.getMinecraft().theWorld.spawnParticle("explode", pX, pY, pZ, vX, vY, vZ);
                    }
            		
            		break;
            	case PLAY_RECORD:
            		//Play the given record track, if it exists.
            		
            		int recordX = dis.readInt();
            		int recordY = dis.readInt();
            		int recordZ = dis.readInt();
            		boolean play = dis.readBoolean();
            		
            		if(play) {
            			String record = "";
            			int length = dis.readInt();
            			for(int i = 0; i < length; i++) {
            				record += dis.readChar();
            			}
            			
            			if(ItemRecord.getRecord("records."+record) != null) {
            				//Record exists; play it.
            				p.worldObj.playRecord("records."+record , recordX, recordY, recordZ);
            			}
            			else {
            				System.out.println("The record " + record + "doesn't exist!");
            				p.worldObj.playRecord(null, recordX, recordY, recordZ);
            			}
    				}
    				else {
    					//Stop the current record which is playing.
    					p.worldObj.playRecord(null, recordX, recordY, recordZ);
    				}
            		break;
            	case ANTI_BUILDER:
            		double tx = dis.readDouble();
            		double ty = dis.readDouble();
	        		double tz = dis.readDouble();
	        		int a = dis.readInt();
	        		//if(rand.nextBoolean() && rand.nextBoolean())
	        		drawParticle(p.worldObj, tx, ty, tz, "reset", a);
	        		break;
            	default:
            		return null;
            }
        }
        catch  (IOException e)
        {
        	e.printStackTrace();
            System.out.println("Failed to read packet");
        }
        
        //Don't return anything.
    	return null;
    }
    
    @SideOnly(Side.CLIENT)
    private static void drawParticle(World worldObj, double srcX, double srcY, double srcZ, String par1Str, int age)
    {
		double tx = srcX;
		double ty = srcY;
		double tz = srcZ;
		EntityFX particle = null;
		if(par1Str.equals("radar")) {
			particle = new RadarParticle(worldObj, tx, ty, tz, 3, 20);
		}
		if(par1Str.equals("reset")) {
			particle = new AntibuilderParticle(worldObj, tx, ty, tz, 1, age, 48);
		}
		if(particle != null)
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }
}
