package com.draco18s.artifacts.components;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

//This will be used for disabled components
public class ComponentNull extends BaseComponent {

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advTooltip) {}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, String trigger, boolean advTooltip) {}

	@Override
	public String getPreAdj(Random rand) {
		return "";
	}

	@Override
	public String getPostAdj(Random rand) {
		return "";
	}

	@Override
	public int getTextureBitflags() {
		return 0;
	}

	@Override
	public int getNegTextureBitflags() {
		return 0;
	}

}
