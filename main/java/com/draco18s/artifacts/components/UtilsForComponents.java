package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.network.CToSMessageComponent;
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
			CToSMessageComponent packet = new CToSMessageComponent(entity.getUniqueID(), out);
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
			CToSMessageComponent packet = new CToSMessageComponent(player.getUniqueID(), out);
			DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		}
	}
	
}
