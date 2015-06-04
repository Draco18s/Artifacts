package com.draco18s.artifacts.item;

import java.util.List;
import java.util.Random;

import io.netty.buffer.Unpooled;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerServer;
import com.draco18s.artifacts.worldgen.StructureApprenticeTower;
import com.draco18s.artifacts.worldgen.StructureApprenticeTowerAncient;
import com.draco18s.artifacts.worldgen.StructureJourneymanTower;
import com.draco18s.artifacts.worldgen.StructureJourneymanTowerAncient;
import com.draco18s.artifacts.worldgen.StructureMasterTower;
import com.draco18s.artifacts.worldgen.StructureMasterTowerAncient;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemStructureGenerator extends Item {
	public static Item structureGenItem;

	public ItemStructureGenerator() {
		super();
		this.setCreativeTab(DragonArtifacts.tabGeneral);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	//When right clicked attempt to create the structure
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
		if(!player.capabilities.isCreativeMode) {
			is.stackSize--;
		}
		
		//System.out.println("Placing Structure!");
		if(is != null && is.stackTagCompound != null) {
			String type = is.stackTagCompound.getString("Type");

			if(type.equals("Tier1Tower")) {
				StructureApprenticeTower.build(world, new Random(), x-3, y-2, z+1);
			}
			if(type.equals("Tier1TowerAncient")) {
				StructureApprenticeTowerAncient.build(world, new Random(), x-3, y-2, z+1);
			}
			if(type.equals("Tier2Tower")) {
				StructureJourneymanTower.build(world, new Random(), x-3, y, z+1);
			}
			if(type.equals("Tier2TowerAncient")) {
				StructureJourneymanTowerAncient.build(world, new Random(), x-3, y, z+1);
			}
			if(type.equals("Tier3Tower")) {
				StructureMasterTower.build(world, new Random(), x-3, y-2, z+1);
			}
			if(type.equals("Tier3TowerAncient")) {
				StructureMasterTowerAncient.build(world, new Random(), x-3, y-2, z+1);
			}

			world.playSoundEffect((double)x + 0.5, (double)y + 0.5, (double)z + 0.5, "portal.trigger", 1.0f, 1.0f);
			
			return true;
		}
		
		return false;
    }

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean held) {
		if(!world.isRemote && itemStack.stackTagCompound != null) {
			int d = itemStack.stackTagCompound.getInteger("onItemRightClickDelay");
			if(d > 0) {
				//System.out.println(d);
				d--;
				itemStack.stackTagCompound.setInteger("onItemRightClickDelay",d);
			}
		}
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		ItemStack stack = new ItemStack(item);
		stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setString("Type", "Tier1Tower");
		list.add(stack);

		stack = new ItemStack(item);
		stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setString("Type", "Tier1TowerAncient");
		list.add(stack);

		stack = new ItemStack(item);
		stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setString("Type", "Tier2Tower");
		list.add(stack);

		stack = new ItemStack(item);
		stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setString("Type", "Tier2TowerAncient");
		list.add(stack);

		stack = new ItemStack(item);
		stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setString("Type", "Tier3Tower");
		list.add(stack);

		stack = new ItemStack(item);
		stack.stackTagCompound = new NBTTagCompound();
		stack.stackTagCompound.setString("Type", "Tier3TowerAncient");
		list.add(stack);

	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		this.itemIcon = register.registerIcon("Artifacts:structure_placer");
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
	 * different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack itemStack)
	{
		if(itemStack.stackTagCompound != null) {
			return getUnlocalizedName() + "." + itemStack.stackTagCompound.getString("Type");
		}
		else {
			return getUnlocalizedName();
		}

	}
}
