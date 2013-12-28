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
import draco18s.artifacts.api.interfaces.IArtifactComponent;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentSpeed implements IArtifactComponent {
	//private UUID speedID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");

	public ComponentSpeed() {
	}
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onArmorTickUpdate";
		}
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "onHeld";
				break;
			case 1:
				str = "onUpdate";
				break;
		}
		return str;
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		NBTTagCompound inbt = i.stackTagCompound;
		NBTTagCompound nnbt = new NBTTagCompound();
		NBTTagList nnbtl = new NBTTagList();
		AttributeModifier att = new AttributeModifier("generic.movementSpeed", 0.01D + rand.nextInt(5)/200D + rand.nextInt(5)/200D, 2);
		nnbt.setLong("UUIDMost", att.getID().getMostSignificantBits());
		nnbt.setLong("UUIDLeast", att.getID().getLeastSignificantBits());
		nnbt.setString("Name", att.getName());
		nnbt.setDouble("Amount", att.getAmount());
		nnbt.setInteger("Operation", att.getOperation());
		nnbt.setString("AttributeName", att.getName());
		nnbtl.appendTag(nnbt);
		inbt.setTag("AttributeModifiers", nnbtl);
		//i.addEnchantment(Enchantment.sharpness, rand.nextInt(5)+1);
		return i;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		System.out.println("Dropped");
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
		String uu = par1ItemStack.stackTagCompound.getString("SpeedUUID");
		UUID speedID;
		if(uu.equals("")) {
			speedID = UUID.randomUUID();
			par1ItemStack.stackTagCompound.setString("SpeedUUID", speedID.toString());
		}
		else {
			speedID = UUID.fromString(uu);
		}
		if(par3Entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)par3Entity;
			AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			AttributeModifier mod;
			mod = new AttributeModifier(speedID,"SpeedBoostComponent",0.05F,2);
			if(player.openContainer != null && player.openContainer != player.inventoryContainer || player.capabilities.isCreativeMode) {
				if(atinst.getModifier(speedID) != null)
				{
					atinst.removeModifier(mod);
				}
			}
			else {
				if(atinst.getModifier(speedID) == null)
				{
					atinst.applyModifier(mod);
				}
				par1ItemStack.stackTagCompound.setInteger("SpeedBoosting", player.entityId);
			}
		}
		else {
			int eid = par1ItemStack.stackTagCompound.getInteger("SpeedBoosting");
			EntityPlayer player = (EntityPlayer) par2World.getEntityByID(eid);
			AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			AttributeModifier mod;
			mod = new AttributeModifier(speedID,"SpeedBoostComponent",5,0);
			if(atinst.getModifier(speedID) != null)
			{
				atinst.removeModifier(mod);
			}	
		}
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add("Speed Boost " + trigger + " " + EnumChatFormatting.BLUE + "+5% Speed");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Speed Boost when held.");
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Swift";
				break;
			case 1:
				str = "Quick";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(3)) {
			case 0:
				str = "of Swiftness";
				break;
			case 1:
				str = "of Movement";
				break;
			case 2:
				str = "of Speed";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return 4957;
	}

	@Override
	public int getNegTextureBitflags() {
		return 3106;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return false;
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		if(type == "onUpdate") {
			String uu = entityItem.getEntityItem().stackTagCompound.getString("SpeedUUID");
			UUID speedID;
			if(uu.equals("")) {
				speedID = UUID.randomUUID();
				entityItem.getEntityItem().stackTagCompound.setString("SpeedUUID", speedID.toString());
			}
			else {
				speedID = UUID.fromString(uu);
			}
			int eid = entityItem.getEntityItem().stackTagCompound.getInteger("SpeedBoosting");
			Entity ent = entityItem.worldObj.getEntityByID(eid);
			if(ent instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) ent;
				AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
				AttributeModifier mod;
				mod = new AttributeModifier(speedID,"SpeedBoostComponent",0.01F,2);
				if(atinst.getModifier(speedID) != null)
				{
					atinst.removeModifier(mod);
				}
			}
		}
		return false;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		
	}
	
	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(worn)
			onUpdate(itemStack, world, player, 0, true);
		else {
			String uu = itemStack.stackTagCompound.getString("SpeedUUID");
			if(uu.equals("")) {
				return;
			}
			UUID speedID = UUID.fromString(uu);
			AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			AttributeModifier mod;
			mod = new AttributeModifier(speedID,"SpeedBoostComponent",0.05F,2);
			if(player.capabilities.isCreativeMode && player.openContainer != null && player.openContainer == player.inventoryContainer) {
				if(atinst.getModifier(speedID) != null)
				{
					atinst.removeModifier(mod);
				}
			}
		}
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {	}
}
