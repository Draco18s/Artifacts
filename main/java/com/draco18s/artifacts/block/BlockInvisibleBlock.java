package com.draco18s.artifacts.block;

import com.draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInvisibleBlock extends IInvisibleBlock {
	public static Block instance;

	public BlockInvisibleBlock() {
		super(Material.rock);
		setResistance(10F);
		setStepSound(Block.soundTypeStone);
		setHardness(2.0F);
		this.setCreativeTab(DragonArtifacts.tabGeneral);
	}
}
