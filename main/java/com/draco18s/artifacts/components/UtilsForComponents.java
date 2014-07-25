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
			CToSMessage packet = new CToSMessage(entity.getUniqueID(), out);
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
			CToSMessage packet = new CToSMessage(player.getUniqueID(), out);
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
}
