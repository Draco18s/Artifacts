package com.draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.block.BlockLight;

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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentLight implements IArtifactComponent {

	public ComponentLight() {
	}
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onArmorTickUpdate";
		}
		return "onUpdate";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		i.stackTagCompound.setInteger("lastLightX", -1);
		i.stackTagCompound.setInteger("lastLightY", -1);
		i.stackTagCompound.setInteger("lastLightZ", -1);
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
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,	EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
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
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean par5) {
		if(!world.isRemote) {
			int lx = stack.stackTagCompound.getInteger("lastLightX");
			int ly = stack.stackTagCompound.getInteger("lastLightY");
			int lz = stack.stackTagCompound.getInteger("lastLightZ");
			if(entity instanceof EntityPlayer) {
				int nlx = MathHelper.floor_double(entity.posX);
				int nly = MathHelper.floor_double(entity.posY);
				int nlz = MathHelper.floor_double(entity.posZ);
				if(nlx != lx || nly != ly || nlz != lz) {
					int d = (nlx - lx)*(nlx - lx)+(nly - ly)*(nly - ly)+(nlz - lz)*(nlz - lz);
					if(d > 13 && world.getBlockLightValue(nlx,nly,nlz) < 10) {
						//updating lighting info isn't fast :(
						if(ly >= 0 && ly < 256 && world.getBlock(lx, ly, lz) == BlockLight.instance) {
							world.setBlockToAir(lx, ly, lz);
							//System.out.println("Removed: " + lx + "," + ly + "," + lz);
						}
						if(nly >= 0 && nly < 256 && world.isAirBlock(nlx, nly, nlz)) {
							world.setBlock(nlx, nly, nlz, BlockLight.instance);
							stack.stackTagCompound.setInteger("lastLightX",nlx);
							stack.stackTagCompound.setInteger("lastLightY",nly);
							stack.stackTagCompound.setInteger("lastLightZ",nlz);
						}
					}
				}
			}
		}
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Provides illumination"));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Provides illumination"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Illuminating";
				break;
			case 1:
				str = "Bright";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Light";
				break;
			case 1:
				str = "of the Sun";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return 2559;
	}

	@Override
	public int getNegTextureBitflags() {
		return 5632;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		if(type == "onUpdate") {
			World world = entityItem.worldObj;
			NBTTagCompound data = entityItem.getEntityItem().stackTagCompound;
			int lx = data.getInteger("lastLightX");
			int ly = data.getInteger("lastLightY");
			int lz = data.getInteger("lastLightZ");
			//System.out.println("Last: " + lx + "," + ly + "," + lz);
			int nlx = (int) entityItem.posX;
			int nly = (int) entityItem.posY;
			int nlz = (int) entityItem.posZ;
			if(nlx != lx || nly != ly || nlz != lz) {
				int d = (nlx - lx)*(nlx - lx)+(nly - ly)*(nly - ly)+(nlz - lz)*(nlz - lz);
				if(d > 13) {
					if(ly >= 0 && ly < 256 && world.getBlock(nlx, nly, nlz) == BlockLight.instance)
						world.setBlockToAir(lx, ly, lz);
					if(nly >= 0 && nly < 256 && world.isAirBlock(nlx, nly, nlz)) {
						world.setBlock(nlx, nly, nlz, BlockLight.instance);
						data.setInteger("lastLightX",nlx);
						data.setInteger("lastLightY",nly);
						data.setInteger("lastLightZ",nlz);
						//System.out.println("Placed: " + nlx + "," + nly + "," + nlz);
					}
				}
			}
			//entityItem.getEntityItem().stackTagCompound = data;
		}
		else {
			System.out.println("Hmm. " + type);
		}
		return false;
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		onUpdate(itemStack, world, player, 0, worn);
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
