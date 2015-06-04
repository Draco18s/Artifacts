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
import net.minecraft.entity.projectile.EntityLargeFireball;
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
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentFireball extends BaseComponent {

	public ComponentFireball() {
	}
	
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "onItemRightClick";
				break;
			case 1:
				str = "hitEntity";
				break;
		}
		return str;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,	EntityPlayer player) {
		PacketBuffer out =  new PacketBuffer(Unpooled.buffer());
		out.writeInt(PacketHandlerServer.FIREBALLS);
		out.writeInt(player.getEntityId());
		out.writeInt(player.inventory.currentItem);
		CToSMessage packet = new CToSMessage(out);
		DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		UtilsForComponents.sendItemDamagePacket(player, player.inventory.currentItem, 1);
		itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 20);
		
		return itemStack;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLivingHit, EntityLivingBase entityLivingPlayer) {
		
		if(!entityLivingPlayer.worldObj.isRemote && entityLivingPlayer instanceof EntityPlayer) {
			System.out.println("Entity was hit. Summoning fireball.");
			EntityPlayer player = (EntityPlayer) entityLivingPlayer;
			World world = player.worldObj;
			
			Vec3 vec3 = player.getLook(1.0F);
			double d8 = 4.0D;
			//System.out.println(vec3);
			//EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, p.posX + vec3.xCoord * d8, p.posY, p.posZ + vec3.zCoord * d8, vec3.xCoord, vec3.yCoord, vec3.zCoord);
			EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, player, vec3.xCoord, vec3.yCoord, vec3.zCoord);
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
			itemStack.damageItem(1, player);
			
		}
		return false;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Shoots fireballs") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Shoots fireballs"));
	}
	
	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "Flaming";
				break;
			case 1:
				str = "Burning";
				break;
			case 2:
				str = "Incinerating";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "of Flames";
				break;
			case 1:
				str = "of Fire";
				break;
			case 2:
				str = "of Incineration";
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
