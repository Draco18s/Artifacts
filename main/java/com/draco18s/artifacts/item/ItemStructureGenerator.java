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
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer p) {
		int d = is.stackTagCompound.getInteger("onItemRightClickDelay");
		if(d <= 0) {
			if(!p.capabilities.isCreativeMode) {
				is.stackSize--;
			}

			if(is.stackTagCompound != null && !world.isRemote) {

				//System.out.println("Reading" + d);


				//System.out.println("Placing Structure!");
				float f = 1.0F;
				float f1 = p.prevRotationPitch + (p.rotationPitch - p.prevRotationPitch) * f;
				float f2 = p.prevRotationYaw + (p.rotationYaw - p.prevRotationYaw) * f;
				double d0 = p.prevPosX + (p.posX - p.prevPosX) * (double)f;
				double d1 = p.prevPosY + (p.posY - p.prevPosY) * (double)f + 1.62D - (double)p.yOffset;
				double d2 = p.prevPosZ + (p.posZ - p.prevPosZ) * (double)f;
				Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
				float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
				float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
				float f5 = -MathHelper.cos(-f1 * 0.017453292F);
				float f6 = MathHelper.sin(-f1 * 0.017453292F);
				float f7 = f4 * f5;
				float f8 = f3 * f5;
				double d3 = 5.0D;
				Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
				MovingObjectPosition mop = world.func_147447_a/*rayTraceBlocks_do_do*/(vec3, vec31, false, true, false);
				if (mop == null) {
					return null;
				}
				int x = -1;
				int y = -1;
				int z = -1;
				if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					x = mop.blockX;
					y = mop.blockY;
					z = mop.blockZ;

					if (mop.sideHit == 0) --y;
					if (mop.sideHit == 1) ++y;
					if (mop.sideHit == 2) --z;
					if (mop.sideHit == 3) ++z;
					if (mop.sideHit == 4) --x;
					if (mop.sideHit == 5) ++x;
				}
				if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
					x = MathHelper.floor_double(mop.hitVec.xCoord);
					y = MathHelper.floor_double(mop.hitVec.yCoord);
					z = MathHelper.floor_double(mop.hitVec.zCoord);
				}

				//world.setBlock(x, y, z, Blocks.cake);

				Item item = is.getItem();
				if(is != null && is.stackTagCompound != null) {
					String type = is.stackTagCompound.getString("Type");

					if(type.equals("Tier1Tower")) {
						StructureApprenticeTower.build(world, new Random(), x-3, y-3, z+1);
					}
					if(type.equals("Tier1TowerAncient")) {
						StructureApprenticeTowerAncient.build(world, new Random(), x-3, y-3, z+1);
					}
					if(type.equals("Tier2Tower")) {
						StructureJourneymanTower.build(world, new Random(), x-3, y-1, z+1);
					}
					if(type.equals("Tier2TowerAncient")) {
						StructureJourneymanTowerAncient.build(world, new Random(), x-3, y-1, z+1);
					}
					if(type.equals("Tier3Tower")) {
						StructureMasterTower.build(world, new Random(), x-3, y-3, z+1);
					}
					if(type.equals("Tier3TowerAncient")) {
						StructureMasterTowerAncient.build(world, new Random(), x-3, y-3, z+1);
					}

					world.playSoundEffect((double)x + 0.5, (double)y + 0.5, (double)z + 0.5, "portal.trigger", 1.0f, 1.0f);
				}

				if(p.capabilities.isCreativeMode) {
					is.stackTagCompound.setInteger("onItemRightClickDelay", 20);
				}
				//				PacketBuffer out = new PacketBuffer(Unpooled.buffer());
				//				out.writeInt(PacketHandlerServer.PLACE_STRUCTURE);
				//				out.writeInt(player.inventory.currentItem);
				//				CToSMessage packet = new CToSMessage(player.getUniqueID(), out);
				//				DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
				//				
				//				if(!player.capabilities.isCreativeMode) {
				//					itemStack.stackSize--;
				//				}
				//				else {
				//					d = 20;
				//					itemStack.stackTagCompound.setInteger("onItemRightClickDelay",d);
				//					
				//					out = new PacketBuffer(Unpooled.buffer());
				//					out.writeInt(4096);
				//					out.writeInt(d);
				//					out.writeInt(player.inventory.currentItem);
				//					packet = new CToSMessage(player.getUniqueID(), out);
				//					DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
				//				}
			}
		}
		return is;
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
