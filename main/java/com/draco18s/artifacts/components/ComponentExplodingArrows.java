package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerServer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentExplodingArrows extends BaseComponent {

	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		
		return "onItemRightClick";
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		PacketBuffer out = new PacketBuffer(Unpooled.buffer());
		out.writeInt(PacketHandlerServer.EXPLODING_ARROWS);
		out.writeInt(player.getEntityId());
		out.writeInt(player.inventory.currentItem);
		CToSMessage packet = new CToSMessage(out);
		DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 15);
		
		return itemStack;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Shoots exploding arrows") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Shoots exploding arrows"));
	}
	
	@Override
	public String getPreAdj(Random rand) {
		return "Sniping";
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Wrath";
				break;
			case 1:
				str = "of Fireworks";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.RING | Flags.STAFF | Flags.WAND;
	}

	@Override
	public int getNegTextureBitflags() {
		return Flags.AMULET | Flags.TRINKET | Flags.BELT | Flags.ARMOR;
	}
}
