package draco18s.artifacts.components;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

public class ComponentExcavation implements IArtifactComponent {

	public ComponentExcavation() {
	}

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		return "onBlockDestroyed";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand) {
		return null;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		EnumToolMaterial toolMaterial = EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")];
		if(toolMaterial == EnumToolMaterial.WOOD) {
			//System.out.println("Wood " + Item.pickaxeWood.getStrVsBlock(par1ItemStack, par2Block));
			return (Item.pickaxeWood.getStrVsBlock(par1ItemStack, par2Block) / 2 * EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10;
		}
		else if(toolMaterial == EnumToolMaterial.STONE) {
			//System.out.println("Stone " + Item.pickaxeStone.getStrVsBlock(par1ItemStack, par2Block));
			return (Item.pickaxeStone.getStrVsBlock(par1ItemStack, par2Block) / 2 * EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10;
		}
		else if(toolMaterial == EnumToolMaterial.EMERALD) {
			//System.out.println("Diamond " + Item.pickaxeDiamond.getStrVsBlock(par1ItemStack, par2Block));
			return (Item.pickaxeDiamond.getStrVsBlock(par1ItemStack, par2Block) / 2 * EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10;
		}
		else if(toolMaterial == EnumToolMaterial.IRON) {
			//System.out.println("Iron " + Item.pickaxeIron.getStrVsBlock(par1ItemStack, par2Block));
			return (Item.pickaxeIron.getStrVsBlock(par1ItemStack, par2Block) / 2 * EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10;
		}
		else if(toolMaterial == EnumToolMaterial.GOLD) {
			//System.out.println("Gold " + Item.pickaxeGold.getStrVsBlock(par1ItemStack, par2Block));
			return (Item.pickaxeGold.getStrVsBlock(par1ItemStack, par2Block) / 2 * EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10;
		}
		return (EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial()) / 10;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) { 
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World world, int par1, int x, int y, int z, EntityLivingBase player) {
		//System.out.println("Test (" + x + "," + y + "," + z + ")");
		//player.getLookVec();
		for(int i=-1;i<=1;i++) {
			for(int j=-1;j<=1;j++) {
				for(int k=-1;k<=1;k++) {
					int l = world.getBlockId(x+i, y+j, z+k);
					Block block = Block.blocksList[l];
					if(block != null) {
						System.out.println("block: " + block.getUnlocalizedName());
						System.out.println("can:  " + par1ItemStack.canHarvestBlock(block));
						if(this.canHarvestBlock(block, par1ItemStack)) {
							int par6 = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, par1ItemStack);
							block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x+i, y+j, z+k), par6);
							world.setBlockToAir(x+i, y+j, z+k);
						}
					}
				}
			}
		}
		par1ItemStack.damageItem(1, player);
		//Block block = Block.blocksList[l]
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		EnumToolMaterial toolMaterial = EnumToolMaterial.values()[itemStack.stackTagCompound.getInteger("material")];
		
		if(toolMaterial == EnumToolMaterial.WOOD) {
			return Item.pickaxeWood.canHarvestBlock(par1Block);
		}
		else if(toolMaterial == EnumToolMaterial.STONE) {
			return Item.pickaxeStone.canHarvestBlock(par1Block);
		}
		else if(toolMaterial == EnumToolMaterial.EMERALD) {
			return Item.pickaxeDiamond.canHarvestBlock(par1Block);
		}
		else if(toolMaterial == EnumToolMaterial.IRON) {
			return Item.pickaxeIron.canHarvestBlock(par1Block);
		}
		else if(toolMaterial == EnumToolMaterial.GOLD) {
			return Item.pickaxeGold.canHarvestBlock(par1Block);
		}
		return par1Block == Block.obsidian ? toolMaterial.getHarvestLevel() == 3 : (par1Block != Block.blockDiamond && par1Block != Block.oreDiamond ? (par1Block != Block.oreEmerald && par1Block != Block.blockEmerald ? (par1Block != Block.blockGold && par1Block != Block.oreGold ? (par1Block != Block.blockIron && par1Block != Block.oreIron ? (par1Block != Block.blockLapis && par1Block != Block.oreLapis ? (par1Block != Block.oreRedstone && par1Block != Block.oreRedstoneGlowing ? (par1Block.blockMaterial == Material.rock ? true : (par1Block.blockMaterial == Material.iron ? true : par1Block.blockMaterial == Material.anvil)) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 1) : toolMaterial.getHarvestLevel() >= 1) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 2) : toolMaterial.getHarvestLevel() >= 2);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
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
		
	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {

	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {

	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add("Digs big holes");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Digs big holes");
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Excavator's";
	}

	@Override
	public String getPostAdj(Random rand) {
		return "of Excavation";
	}

	@Override
	public int getTextureBitflags() {
		return 156;
	}

	@Override
	public int getNegTextureBitflags() {
		return 261;
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) { }

}
