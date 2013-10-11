package draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraft.network.packet.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ComponentBreathing implements IArtifactComponent {

	public ComponentBreathing() {
	}
	
	public String getName() {
		return "Water Breathing";
	}
	
	public String getRandomTrigger(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "onItemRightClick";
				break;
			case 1:
				str = "hitEntity";
				break;
			case 2:
				str = "onHeld";
				break;
		}
		return str;
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand) {
		return i;
	}

	@Override
	public Icon getIcon(ItemStack stack, int pass) {
		return null;
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
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,	EntityPlayer par3EntityPlayer) {
		ByteArrayOutputStream bt = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bt);
		try
		{
			out.writeInt(10);
			out.writeInt(par3EntityPlayer.entityId);
			out.writeInt(13);
			out.writeInt(1200);
			out.writeInt(1);
			Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
			PacketDispatcher.sendPacketToServer(packet);
		}
		catch (IOException ex)
		{
			System.out.println("couldnt send packet!");
		}
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		if(par3EntityLivingBase instanceof EntityClientPlayerMP) {
			if(par2EntityLivingBase.hurtTime == 0) {
				ByteArrayOutputStream bt = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bt);
				try
				{
					out.writeInt(10);
					out.writeInt(par3EntityLivingBase.entityId);
					out.writeInt(13);
					out.writeInt(300);
					out.writeInt(1);
					EntityPlayer player = (EntityPlayer)par3EntityLivingBase;
					Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
					PacketDispatcher.sendPacketToServer(packet);
				}
				catch (IOException ex)
				{
					System.out.println("couldnt send packet!");
				}
			}
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
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
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		int time = 0;
		if(trigger == "when inflicting damage.") {
			time = 15;
		}
		else if(trigger == "when used.") {
			time = 60;
		}
		par3List.add(EnumChatFormatting.AQUA + "Water Breathing");
		par3List.add(EnumChatFormatting.AQUA + trigger + " (" + time + "seconds)");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(EnumChatFormatting.AQUA + "Water Breathing");
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "Aerated";
				break;
			case 1:
				str = "Breathy";
				break;
			case 2:
				str = "Oxygenated";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Breathing";
				break;
			case 1:
				str = "of Fresh Air";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return 349;
	}

	@Override
	public int getNegTextureBitflags() {
		return 130;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		return false;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
	}
}
