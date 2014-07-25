package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.draco18s.artifacts.ArtifactClientEventHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerClient;
import com.draco18s.artifacts.network.PacketHandlerServer;
import com.draco18s.artifacts.network.SToCMessage;

public class ComponentBaking implements IArtifactComponent {
	
	public ComponentBaking() {
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
		PacketBuffer out = new PacketBuffer(Unpooled.buffer());
		out.writeInt(PacketHandlerServer.BAKING);
		out.writeInt(player.inventory.currentItem);
		CToSMessage packet = new CToSMessage(player.getUniqueID(), out);
		DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 20);
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
		list.add(StatCollector.translateToLocal("effect.Places cakes on solid surfaces") + " " + StatCollector.translateToLocal("tool." + trigger));
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advTooltip) {
		list.add(StatCollector.translateToLocal("effect.Places cakes on solid surfaces"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Baker's";
				break;
			case 1:
				str = "Confectioner's";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Baking";
				break;
			case 1:
				str = "of Icing";
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
