package com.draco18s.artifacts.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class PseudoBlockIllusionary extends Block {
	public static Block instance;

	public PseudoBlockIllusionary() {
		super(Material.rock);
	}
	
	@Override
	public IIcon getIcon(int par1, int par2)
    {
		return this.blockIcon;
    }
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = iconRegister.registerIcon("artifacts:invisible");
    }
}
