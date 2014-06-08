package com.draco18s.artifacts.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class PseudoCoverplate extends Block {
	public static Block instance;

	public PseudoCoverplate() {
		super(Material.rock);
	}
	
	@Override
	public IIcon getIcon(int par1, int par2)
    {
		if(par1 == 4 || par1 == 5)
			return this.blockIcon;
		return Blocks.stonebrick.getIcon(par1, par2);
    }
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("artifacts:pseudo_coverplate");
    }
	
	@Override
	public void setBlockBoundsForItemRender()
    {
        float f1 = 0.125F;
        this.setBlockBounds(0.5F, 0, 0, 0.5F + f1, 1, 1);
    }
}
