package com.draco18s.artifacts.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class PseudoBlockTrap extends Block {
	public static Block instance;
    @SideOnly(Side.CLIENT)
    protected IIcon furnaceTopIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon furnaceFrontIcon;

	public PseudoBlockTrap() {
		super(Material.rock);
	}
	
	@Override
	public IIcon getIcon(int par1, int par2)
    {
    	int k = par2 & 7;
        return par1 == k ? (k != 1 && k != 0 ? this.blockIcon : this.blockIcon) : (k != 1 && k != 0 ? (par1 != 1 && par1 != 0 ? this.furnaceFrontIcon : this.furnaceTopIcon) : this.furnaceTopIcon);
    }
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
    {
		this.furnaceFrontIcon = Blocks.furnace.getBlockTextureFromSide(2);
        this.furnaceTopIcon = Blocks.furnace.getBlockTextureFromSide(0);
        this.blockIcon = par1IconRegister.registerIcon("artifacts:pseudo_trap_front");
    }
}
