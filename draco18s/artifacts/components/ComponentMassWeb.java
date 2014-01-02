package draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentMassWeb implements IArtifactComponent {

	public ComponentMassWeb() {
	}
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		return "onDropped";
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
		
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Slows down all mobs") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Slows down all mobs"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Webbing";
				break;
			case 1:
				str = "Mob Slowing";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Trapping";
				break;
			case 1:
				str = "of Quicksand";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return 68;
	}

	@Override
	public int getNegTextureBitflags() {
		return 256;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		if(type == "onDropped") {
			//int del = entityItem.getEntityItem().stackTagCompound.getInteger("dropDelay");
			//System.out.println("Delay: " + del);
			//if(del > 0) {
			//	--del;
			//}
			//else {
				double par1 = entityItem.posX-10;
				double par2 = entityItem.posY-10;
				double par3 = entityItem.posZ-10;
				double par4 = entityItem.posX+10;
				double par5 = entityItem.posY+10;
				double par6 = entityItem.posZ+10;
				AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(par1, par2, par3, par4, par5, par6);
				List<EntityLivingBase> ents = entityItem.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
				EntityPlayer player = null;
				EntityLivingBase someEnt = null;
				if(ents.size() > 0) {
		        	for(int l=ents.size() - 1; l >= 0; l--) {
		        		EntityLivingBase ent = ents.get(l);
		        		someEnt = ent;
		        		if(!(ent instanceof EntityPlayer)) {
		        			ent.addPotionEffect(new PotionEffect(2, 20, 2));
		        		}
		        		else if(player == null) {
		        			player = (EntityPlayer) ent;
		        		}
		        	}
		        }
				//del = 20;
				if(player != null) {
					entityItem.getEntityItem().damageItem(1, player);
					//entityItem.worldObj.newExplosion(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, 0, false, true);
				}
				else if(someEnt != null) {
					entityItem.getEntityItem().damageItem(1, someEnt);
					//entityItem.worldObj.newExplosion(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, 0, false, true);
				}
			//}
			//entityItem.getEntityItem().stackTagCompound.setInteger("dropDelay", del);
		}
		return false;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) { }

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {	}
}
