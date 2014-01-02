package draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import draco18s.artifacts.api.ArtifactsAPI;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.RangedAttribute;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentResurrect implements IArtifactComponent {
	public ComponentResurrect() {
	}
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onDeath";
		}
		return "";
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
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,	EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
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
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		/*String uu = par1ItemStack.stackTagCompound.getString("ResurrectUUID");
		UUID resID;
		if(uu.equals("")) {
			resID = UUID.randomUUID();
			par1ItemStack.stackTagCompound.setString("ResurrectUUID", resID.toString());
		}
		else {
			resID = UUID.fromString(uu);
		}
		if(par3Entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)par3Entity;
			AttributeInstance atinst = player.getEntityAttribute(ArtifactsAPI.OnDeathAttribute);
			if(atinst != null) {
				AttributeModifier mod;
				mod = new AttributeModifier(resID,"ResurrectComponent",1F,2);
				if(player.openContainer != null && player.openContainer != player.inventoryContainer || player.capabilities.isCreativeMode) {
					if(atinst.getModifier(resID) != null)
					{
						atinst.removeModifier(mod);
					}
				}
				else {
					if(atinst.getModifier(resID) == null)
					{
						atinst.applyModifier(mod);
						System.out.println("Applied resurecting");
					}
					par1ItemStack.stackTagCompound.setInteger("Resurrecting", player.entityId);
				}
			}
		}
		else {
			int eid = par1ItemStack.stackTagCompound.getInteger("Resurrecting");
			EntityPlayer player = (EntityPlayer) par2World.getEntityByID(eid);
			AttributeInstance atinst = player.getEntityAttribute(ArtifactsAPI.OnDeathAttribute);
			AttributeModifier mod;
			mod = new AttributeModifier(resID,"ResurrectComponent",1,2);
			if(atinst.getModifier(resID) != null)
			{
				atinst.removeModifier(mod);
			}	
		}*/
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		NBTTagCompound data = par1ItemStack.getTagCompound();
		int c = data.getInteger("resCooldown_armor");
		if(c < 1) {
			par3List.add(StatCollector.translateToLocal("effect.Restores health") + " " + StatCollector.translateToLocal("tool."+trigger));
			par3List.add("   (5 " + StatCollector.translateToLocal("time.minute") + " " + StatCollector.translateToLocal("time.cooldown") + ")");
		}
		else {
			String m = "";
			if(c >= 1200) {
				m = ((c+30)/1200) + " "+StatCollector.translateToLocal("time.minutes");
			}
			else {
				m = (c/20) + " "+StatCollector.translateToLocal("time.seconds");
			}
			par3List.add(StatCollector.translateToLocal("effect.Restores health") + " " + StatCollector.translateToLocal("tool."+trigger));
			par3List.add("   (" + StatCollector.translateToLocal("time.on") + " " + StatCollector.translateToLocal("time.cooldown") + ": " + m + ")");
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Restores health when the player takes");
		par3List.add("leathal damage. (5 minute cooldown)");
	}

	@Override
	public String getPreAdj(Random rand) {
		/*String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "Hardy";
				break;
			case 1:
				str = "Bulky";
				break;
			case 2:
				str = "Sturdy";
				break;
		}*/
		return "Regenerative";
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Regeneration";
				break;
			case 1:
				str = "of Lazarus";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return 4864;
	}

	@Override
	public int getNegTextureBitflags() {
		return 255;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		/*if(type == "onUpdate") {
			String uu = entityItem.getEntityItem().stackTagCompound.getString("ResurrectUUID");
			UUID resID;
			if(uu.equals("")) {
				resID = UUID.randomUUID();
				entityItem.getEntityItem().stackTagCompound.setString("ResurrectUUID", resID.toString());
			}
			else {
				resID = UUID.fromString(uu);
			}
			int eid = entityItem.getEntityItem().stackTagCompound.getInteger("Resurrecting");
			Entity ent = entityItem.worldObj.getEntityByID(eid);
			if(ent instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) ent;
				AttributeInstance atinst = player.getEntityAttribute(ArtifactsAPI.OnDeathAttribute);
				AttributeModifier mod;
				mod = new AttributeModifier(resID,"ResurrectComponent",1F,2);
				if(atinst.getModifier(resID) != null)
				{
					atinst.removeModifier(mod);
				}
			}
		}*/
		return false;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		/*if(worn)
			onUpdate(itemStack, world, player, 0, true);
		else {
			String uu = itemStack.stackTagCompound.getString("ResurrectUUID");
			if(uu.equals("")) {
				return;
			}
			UUID resID = UUID.fromString(uu);
			AttributeInstance atinst = player.getEntityAttribute(ArtifactsAPI.OnDeathAttribute);
			AttributeModifier mod;
			mod = new AttributeModifier(resID,"ResurrectComponent",1F,2);
			if(player.capabilities.isCreativeMode && player.openContainer != null && player.openContainer == player.inventoryContainer) {
				if(atinst.getModifier(resID) != null)
				{
					atinst.removeModifier(mod);
				}
			}
		}*/
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {
		System.out.println("ABORTING DEATH");
		if(isWornArmor && !event.isCanceled()) {
			EntityPlayer player = (EntityPlayer)event.entity;
			NBTTagCompound data = itemStack.getTagCompound();
			//System.out.println("Cooldown: " + data.getInteger("resCooldown"));
			if(data.getInteger("resCooldown_armor") <= 0) {
				event.setCanceled(true);
				player.setHealth(20);
				data.setInteger("resCooldown_armor", 6000);
				itemStack.damageItem(5, player);
				return;
			}
		}
	}
}
