package draco18s.artifacts.components;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

public class ComponentKnockbackResist implements IArtifactComponent {

	public ComponentKnockbackResist() {
		
	}

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor)
			return "onArmorTickUpdate";//onUpdate?
		else
			return "onUpdate";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand) {
		
		return i;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
		
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack,
			EntityLivingBase par2EntityLivingBase,
			EntityLivingBase par3EntityLivingBase) {
		
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World,
			int par3, int par4, int par5, int par6,
			EntityLivingBase par7EntityLivingBase) {
		
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		
		return false;
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
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		String uu = par1ItemStack.stackTagCompound.getString("KnockResUUID");
		UUID healthID;
		if(uu.equals("")) {
			healthID = UUID.randomUUID();
			par1ItemStack.stackTagCompound.setString("KnockResUUID", healthID.toString());
		}
		else {
			healthID = UUID.fromString(uu);
		}
		if(par3Entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)par3Entity;
			AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
			AttributeModifier mod;
			mod = new AttributeModifier(healthID,"KnockbackResComponent",0.2F,2);
			if(player.openContainer != null && player.openContainer != player.inventoryContainer || player.capabilities.isCreativeMode) {
				if(atinst.getModifier(healthID) != null)
				{
					atinst.removeModifier(mod);
				}
			}
			else {
				if(atinst.getModifier(healthID) == null)
				{
					atinst.applyModifier(mod);
				}
				par1ItemStack.stackTagCompound.setInteger("KnockbackResist", player.entityId);
			}
		}
		else {
			int eid = par1ItemStack.stackTagCompound.getInteger("KnockbackResist");
			EntityPlayer player = (EntityPlayer) par2World.getEntityByID(eid);
			AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
			AttributeModifier mod;
			mod = new AttributeModifier(healthID,"KnockbackResComponent",0.2,0);
			if(atinst.getModifier(healthID) != null)
			{
				atinst.removeModifier(mod);
			}	
		}
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		
		return EnumAction.none;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer, int par4) {
		

	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Knockback Resistance");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add("Knockback Resistance");
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Stable";
	}

	@Override
	public String getPostAdj(Random rand) {
		
		return "of Stability";
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
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(worn)
			onUpdate(itemStack, world, player, 0, true);
		else {
			String uu = itemStack.stackTagCompound.getString("KnockResUUID");
			if(uu.equals("")) {
				return;
			}
			UUID healthID = UUID.fromString(uu);
			AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
			AttributeModifier mod;
			mod = new AttributeModifier(healthID,"KnockbackResComponent",0.2F,2);
			if(player.capabilities.isCreativeMode && player.openContainer != null && player.openContainer == player.inventoryContainer) {
				if(atinst.getModifier(healthID) != null)
				{
					atinst.removeModifier(mod);
				}
			}
		}
	}
}
