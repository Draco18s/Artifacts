package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.ArtifactsAPI;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;
import com.draco18s.artifacts.network.PacketHandlerClient;
import com.draco18s.artifacts.network.SToCMessage;

public class ComponentOreRadar extends BaseComponent {
	private static ArrayList<ItemStack> oreBlocks = new ArrayList<ItemStack>();
	private ArrayList<Vec3> detectedBlocks = new ArrayList<Vec3>();
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) {
			return "onArmorTickUpdate";
		}
		return "";
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer entityPlayer, ItemStack itemStack, boolean worn) {
		if(!itemStack.stackTagCompound.hasKey("orePingDelay_armor")) {
			itemStack.stackTagCompound.setInteger("orePingDelay_armor", 0);
		}
		if(itemStack.stackTagCompound.getInteger("orePingDelay_armor") > 0) {
			//System.out.println(itemStack.stackTagCompound.getInteger("orePingDelay"));
			return;
		}
		itemStack.stackTagCompound.setInteger("orePingDelay_armor", 9);
		int x = (int)entityPlayer.posX;
		int y = (int)entityPlayer.posY;
		int z = (int)entityPlayer.posZ;
		EntityPlayerMP player = UtilsForComponents.getPlayerFromUsername(entityPlayer.getCommandSenderName());
		
		//+/- 4
		boolean found = false;
		for(int yy = y - 2; yy <= y + 3; ++yy) {
			for(int xx = x - 4; xx <= x + 4; ++xx) {
				for(int zz = z - 4; zz <= z + 4; ++zz) {
					boolean already = false;
					for(int b = detectedBlocks.size()-1; b >= 0; b--) {
						Vec3 s = detectedBlocks.get(b);
						if((int)s.xCoord == xx && (int)s.yCoord == yy && (int)s.zCoord == zz) {
							already = true;
						}
					}
					if(!already) {
						Block block = world.getBlock(xx, yy,zz);
						if(block == Blocks.diamond_ore || block == Blocks.iron_ore || block == Blocks.coal_ore || block == Blocks.redstone_ore || block == Blocks.lit_redstone_ore || block == Blocks.emerald_ore || block == Blocks.gold_ore || block == Blocks.lapis_ore || block == Blocks.quartz_ore ||
								block.getUnlocalizedName().contains("ore") || block.getUnlocalizedName().contains("ORE") || block.getUnlocalizedName().contains("Ore")){
							PacketBuffer out = new PacketBuffer(Unpooled.buffer());
							
							out.writeInt(PacketHandlerClient.ORE_RADAR);
							out.writeInt(xx);
							out.writeInt(yy);
							out.writeInt(zz);
							SToCMessage packet = new SToCMessage(out);
							DragonArtifacts.artifactNetworkWrapper.sendTo(packet, player);
						
							Vec3 s = Vec3.createVectorHelper(xx, yy, zz);
							detectedBlocks.add(s);
							found = true;
							xx = x + 10;
							zz = z + 10;
							yy = y + 10;
						}
						else {
							for(int a=ComponentOreRadar.oreBlocks.size()-1; a >= 0; a--) {
								if(Item.getItemFromBlock(block) == ComponentOreRadar.oreBlocks.get(a).getItem()) {
									PacketBuffer out = new PacketBuffer(Unpooled.buffer());

									out.writeInt(PacketHandlerClient.ORE_RADAR);
									out.writeInt(xx);
									out.writeInt(yy);
									out.writeInt(zz);
									SToCMessage packet = new SToCMessage(out);
									DragonArtifacts.artifactNetworkWrapper.sendTo(packet, player);

									Vec3 s = Vec3.createVectorHelper(xx, yy, zz);
									detectedBlocks.add(s);
									found = true;
									xx = x + 5;
									zz = z + 5;
									yy = y + 5;
	                			}
							}
						}
					}
				}				
			}
		}
		if(!found) {
			for(int b = detectedBlocks.size()-1; b >= 0; b--) {
				detectedBlocks.remove(b);
			}
		}
		/**/
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Senses nearby ores"));
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Senses nearby ores") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Sensitive";
	}

	@Override
	public String getPostAdj(Random rand) {
		return "of Finding";
	}

	@Override
	public int getTextureBitflags() {
		return Flags.ARMOR | Flags.HELM;
	}

	@Override
	public int getNegTextureBitflags() {
		return ~(getTextureBitflags() | Flags.CHESTPLATE);
	}

	public static void addOre(ItemStack ore) {
		oreBlocks.add(ore);
	}
}
