package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerServer;

public class ComponentMusicPlayer extends BaseComponent {
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(!isArmor) {
			return "onItemRightClick";
		}
		return "";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		ArrayList<ItemRecord> recordList = new ArrayList<ItemRecord>();
		
		//Fill record list with everything considered a record (vanilla or modded).
		Iterator<Item> it = Item.itemRegistry.iterator();
		while(it.hasNext()) {
			Item item = it.next();
			
			if(item instanceof ItemRecord) {
				recordList.add((ItemRecord)item);
			}
		}
		
		//If records were found (and there should always be some!), choose one randomly.
		if(recordList.size() > 0) {
			String record = recordList.get(rand.nextInt(recordList.size())).recordName;
			
			i.getTagCompound().setString("record", record);
		}
		else {
			System.out.println("No records?!?");
		}
		
		return i;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,	EntityPlayer player) {
		NBTTagCompound tag = itemStack.getTagCompound();
		String record = tag.getString("record");
		
		//Determine if the record should play.
		boolean shouldPlay = !tag.getBoolean("playing");
		
		int x = tag.getInteger("playingX");
		int y = tag.getInteger("playingY");
		int z = tag.getInteger("playingZ");
		
		if(shouldPlay) {
			x = MathHelper.floor_double(player.posX);
			y = MathHelper.floor_double(player.posY);
			z = MathHelper.floor_double(player.posZ);
		}
		
		//Send the packet to the server, to play the record (or stop playing).
		tag.setBoolean("playing", shouldPlay);
		
		PacketBuffer out = new PacketBuffer(Unpooled.buffer());
		out.writeInt(PacketHandlerServer.MUSIC_PLAYER);
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(z);
		out.writeBoolean(shouldPlay);
		out.writeInt(player.inventory.currentItem);
		if(shouldPlay) {
			out.writeInt(record.length());
			for(int i = 0; i < record.length(); i++) {
				out.writeChar(record.charAt(i));
			}
		}
		CToSMessage packet = new CToSMessage(out);
		DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		
		tag.setInteger("onItemRightClickDelay", 10);
		return itemStack;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, String trigger, boolean advTooltip) {
		String play = StatCollector.translateToLocal("effect.Play");
		if(play.equals("{Play}"))
			play = "" + EnumChatFormatting.GREEN + ((char) 0x266A) + EnumChatFormatting.LIGHT_PURPLE + ((char) 0x266B) + EnumChatFormatting.GOLD + ((char) 0x266A);
		
		//Get the localized record description
		String recordDescription = "Unknown";
		ItemRecord record = ItemRecord.getRecord("records."+itemStack.getTagCompound().getString("record"));
		if(record != null) { //If the record was from a mod that is no longer loaded, the description will stay "Unknown"
			recordDescription = record.getRecordNameLocal();
		}
		
		list.add(StatCollector.translateToLocal("effect.Plays the record") + " " + recordDescription + " " + StatCollector.translateToLocal("tool." + trigger) + " " + (itemStack.getTagCompound().getBoolean("playing") ? play : ""));
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advTooltip) {
		list.add(StatCollector.translateToLocal("effect.Plays the record") + " " + StatCollector.translateToLocal("item.record." + itemStack.getTagCompound().getString("record") + ".desc") + ".");
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Musical";
				break;
			case 1:
				str = "Harmonious";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Tunes";
				break;
			case 1:
				str = "of Music";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.AMULET | Flags.STAFF | Flags.RING | Flags.FIGURINE | Flags.TRINKET | Flags.WAND;
	}

	@Override
	public int getNegTextureBitflags() {
		return Flags.ARMOR | Flags.CHESTPLATE | Flags.HELM | Flags.BOOTS | Flags.LEGGINGS;
	}
}
