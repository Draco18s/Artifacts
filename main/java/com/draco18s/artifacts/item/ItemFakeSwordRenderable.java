package com.draco18s.artifacts.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ItemFakeSwordRenderable extends ItemSword {

	public static Item wood;
	public static Item stone;
	public static Item iron;
	public static Item gold;
	public static Item diamond;
	private String regString;

	public ItemFakeSwordRenderable(ToolMaterial toolMaterial, String str) {
		super(toolMaterial);
		regString = str;
		this.setCreativeTab(null);
	}

	@Override
	public void registerIcons(IIconRegister iconReg)
	{
		itemIcon = iconReg.registerIcon(regString);
	}
}
