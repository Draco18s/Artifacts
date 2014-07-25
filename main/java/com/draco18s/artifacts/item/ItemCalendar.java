package com.draco18s.artifacts.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import com.draco18s.artifacts.ArtifactServerEventHandler;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.ArtifactsAPI;
import com.draco18s.artifacts.client.ClientProxy;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class ItemCalendar extends Item {
	public static Item instance;

	public ItemCalendar() {
		super();
		//this.setTextureName("artifacts:calendar");
		this.setCreativeTab(DragonArtifacts.tabGeneral);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IIconRegister iconReg)
    {
		itemIcon = ClientProxy.calendar;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1) {
		return ClientProxy.calendar;
	}
}
