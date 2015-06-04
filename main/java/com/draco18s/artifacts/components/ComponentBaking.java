package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.draco18s.artifacts.ArtifactClientEventHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerClient;
import com.draco18s.artifacts.network.PacketHandlerServer;
import com.draco18s.artifacts.network.SToCMessage;

public class ComponentBaking extends BaseComponent {
	
	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		String str = "";
		if(!isArmor) {
			str = "onItemRightClick";
		}
		return str;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world,	EntityPlayer player) {
		PacketBuffer out = new PacketBuffer(Unpooled.buffer());
		out.writeInt(PacketHandlerServer.BAKING);
		out.writeInt(player.inventory.currentItem);
		CToSMessage packet = new CToSMessage(out);
		DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
		itemStack.stackTagCompound.setInteger("onItemRightClickDelay", 20);
		return itemStack;
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, String trigger, boolean advTooltip) {
		list.add(StatCollector.translateToLocal("effect.Places cakes on solid surfaces") + " " + StatCollector.translateToLocal("tool." + trigger));
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advTooltip) {
		list.add(StatCollector.translateToLocal("effect.Places cakes on solid surfaces"));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Baker's";
				break;
			case 1:
				str = "Confectioner's";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Baking";
				break;
			case 1:
				str = "of Icing";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		return Flags.AMULET | Flags.STAFF | Flags.FIGURINE | Flags.WAND | Flags.TRINKET;
	}

	@Override
	public int getNegTextureBitflags() {
		return ~this.getTextureBitflags();
	}
}
