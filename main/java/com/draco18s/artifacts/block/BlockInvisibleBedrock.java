package com.draco18s.artifacts.block;

import com.draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockInvisibleBedrock extends IInvisibleBlock {
	public static Block instance;

	public BlockInvisibleBedrock() {
		super(Material.rock);
		setResistance(-1F);
		setStepSound(Block.soundTypeStone);
		setHardness(-1F);
		this.setCreativeTab(DragonArtifacts.tabGeneral);
	}
	
	@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("artifacts:adainvisible");
    }
}
