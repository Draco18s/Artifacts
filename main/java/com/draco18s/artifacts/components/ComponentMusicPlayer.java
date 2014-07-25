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
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerServer;

public class ComponentMusicPlayer implements IArtifactComponent {
	
	public ComponentMusicPlayer() {
	}
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		String str = "";
		if(!isArmor) {
			str = "onItemRightClick";
		}
		return str;
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
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

	@Override
	public float getDigSpeed(ItemStack par1ItemStack, Block par2Block, int meta) {
		return 0;
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
		CToSMessage packet = new CToSMessage(player.getUniqueID(), out);
		DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		
		tag.setInteger("onItemRightClickDelay", 10);
		return itemStack;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityVictim, EntityLivingBase entityAttacker) {
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block block, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		return false;
	}

	//works great
	@Override
	public void onUpdate(ItemStack par1ItemStack, World world, Entity par3Entity, int par4, boolean par5) {
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, String trigger, boolean advTooltip) {
		String play = StatCollector.translateToLocal("effect.Play");
		if(play.equals("{Play}"))
			play = "" + EnumChatFormatting.GREEN + ((char) 0x266A) + EnumChatFormatting.LIGHT_PURPLE + ((char) 0x266B) + EnumChatFormatting.GOLD + ((char) 0x266A);
		list.add(StatCollector.translateToLocal("effect.Plays the record") + " " + StatCollector.translateToLocal("item.record." + itemStack.getTagCompound().getString("record") + ".desc") + " " + StatCollector.translateToLocal("tool." + trigger) + " " + (itemStack.getTagCompound().getBoolean("playing") ? play : ""));
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
		return 213;
	}

	@Override
	public int getNegTextureBitflags() {
		return 7680;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		return false;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return -1;
	}
}
