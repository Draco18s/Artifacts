package com.draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;

public class ComponentMedkit extends BaseComponent {

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor)
			return "onTakeDamage";
		return "";
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(worn) {
			if(itemStack.stackTagCompound.getInteger("medkitDelay_armor") == 1) {
				player.heal(0.5F);
				/*ByteArrayOutputStream bt = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bt);
				try
				{
					//System.out.println("Building packet...");
					out.writeInt(1);
					out.writeFloat(1);
					Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
					PacketDispatcher.sendPacketToServer(packet);
					//itemStack.damageItem(1, player);
				}
				catch (IOException ex)
				{
					System.out.println("couldnt send packet!");
				}*/
			}
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Heals for a half-heart ten seconds after taking damage");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Heals for a half-heart"));
		par3List.add("   10 " + StatCollector.translateToLocal("time.seconds") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Healing";
				break;
			case 1:
				str = "Reinvigorating";
				break;
			case 2:
				str = "Staunching";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Healing";
				break;
			case 1:
				str = "of Rejuvenation";
				break;
			case 2:
				str = "of First Aid";
				break;
		}
		return str;
	}

	@Override
	public int getTextureBitflags() {
		
		return Flags.CHESTPLATE;
	}

	@Override
	public int getNegTextureBitflags() {
		
		return ~(Flags.ARMOR | Flags.CHESTPLATE);
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {
		if(isWornArmor) {
			if(itemStack.stackTagCompound.getInteger("medkitDelay_armor") <= 0)
				itemStack.stackTagCompound.setInteger("medkitDelay_armor", 200);
		}
	}
}
