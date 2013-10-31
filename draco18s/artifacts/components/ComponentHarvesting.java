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
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class ComponentHarvesting implements IArtifactComponent {

	public ComponentHarvesting() {
	}
	
	public String getName() {
		return "Harvesting";
	}
	
	public String getRandomTrigger(Random rand) {
		return "onBlockDestroyed";
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
		if (par2Block != Block.oreCoal && par2Block != Block.oreIron && par2Block != Block.oreEmerald && par2Block != Block.oreGold && par2Block != Block.oreDiamond && par2Block != Block.oreNetherQuartz && par2Block != Block.oreLapis && par2Block != Block.oreRedstone && par2Block != Block.oreRedstoneGlowing)
        {
			return -1;
        }
		else {
			return (EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial() / 2);
			//return 15;
		}
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
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World world, int blockID, int x, int y, int z, EntityLivingBase player) {
		if (blockID != Block.oreCoal.blockID && blockID != Block.oreIron.blockID && blockID != Block.oreEmerald.blockID && blockID != Block.oreGold.blockID && blockID != Block.oreDiamond.blockID && blockID != Block.oreNetherQuartz.blockID && blockID != Block.oreLapis.blockID && blockID != Block.oreRedstone.blockID && blockID != Block.oreRedstoneGlowing.blockID)
        {
			return false;
        }
		else if(world.rand.nextInt(4) == 0) {
			//drop another
			ItemStack is = new ItemStack(Block.blocksList[blockID], 1);
			EntityItem ei = new EntityItem(world, x, y, z, is);
			world.spawnEntityInWorld(ei);
			par1ItemStack.damageItem(1, player);
		}
		return true;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		int blockID = par1Block.blockID;
		if (blockID != Block.oreCoal.blockID && blockID != Block.oreIron.blockID && blockID != Block.oreEmerald.blockID && blockID != Block.oreGold.blockID && blockID != Block.oreDiamond.blockID && blockID != Block.oreNetherQuartz.blockID && blockID != Block.oreLapis.blockID && blockID != Block.oreRedstone.blockID && blockID != Block.oreRedstoneGlowing.blockID)
        {
			return false;
        }
		else {
			return true;
		}
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
		par3List.add("Extra drops from ores");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Extra drops from ores");
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Harvesting";
	}

	@Override
	public String getPostAdj(Random rand) {
		return "of Prospecting";
	}

	@Override
	public int getTextureBitflags() {
		return 148;
	}

	@Override
	public int getNegTextureBitflags() {
		return 327;
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
		
	}
}
