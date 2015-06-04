package com.draco18s.artifacts.components;

import java.util.Iterator;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerServer;

public class UtilsForComponents {
	
	public static class Flags {
		public static final int AMULET = 1;
		public static final int DAGGER = 2;
		public static final int FIGURINE = 4;
		public static final int RING = 8;
		public static final int STAFF = 16;
		public static final int SWORD = 32;
		public static final int TRINKET = 64;
		public static final int WAND = 128;
		public static final int ARMOR = 256;
		public static final int BOOTS = 512;
		public static final int CHESTPLATE = 1024;
		public static final int HELM = 2048;
		public static final int LEGGINGS = 4096;
		public static final int BELT = 8192;
	}

	/**
	 * Sends a packet to the server with potion information.
	 * 
	 * @param potionID
	 * @param duration
	 * @param level
	 * @param entity The handler expects a player.
	 */
	public static void sendPotionPacket(int potionID, int duration, int level, Entity entity) {
		if(entity.worldObj.isRemote && entity instanceof EntityPlayer) {
			PacketBuffer out =  new PacketBuffer(Unpooled.buffer());
		
			out.writeInt(PacketHandlerServer.POTIONS);
			out.writeInt(entity.getEntityId());
			out.writeInt(potionID);
			out.writeInt(duration);
			out.writeInt(level);
			CToSMessage packet = new CToSMessage(out);
			DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		}
	}
	
	public static void sendItemDamagePacket(EntityPlayer player, int inventorySlot, int damageToDeal) {
		if(player.worldObj.isRemote) {
			PacketBuffer out =  new PacketBuffer(Unpooled.buffer());
		
			out.writeInt(PacketHandlerServer.DAMAGE_ITEM);
			out.writeInt(player.getEntityId());
			out.writeInt(inventorySlot);
			out.writeInt(damageToDeal);
			CToSMessage packet = new CToSMessage(out);
			DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		}
	}
	
	public static EntityPlayerMP getPlayerFromUsername(String username) {
		EntityPlayerMP player = null;
		Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                player = null;
                break;
            }

            player = (EntityPlayerMP)iterator.next();
        }
        while (!player.getCommandSenderName().equalsIgnoreCase(username));
        
        return player;
	}
	
	public static boolean equipableByBaubles(String iconType) {
		return iconType.equals("Amulet") || iconType.equals("Ring") || iconType.equals("Belt");
	}
	
	public static boolean isArmor(String iconType) {
		return iconType.equals("Helm") || iconType.equals("Boots") || iconType.equals("Leggings") || iconType.equals("Chestplate");
	}
}
