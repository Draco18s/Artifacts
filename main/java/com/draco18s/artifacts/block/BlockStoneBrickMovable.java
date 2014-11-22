package com.draco18s.artifacts.block;

import java.util.Random;

import com.draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockStoneBrickMovable extends Block {
	public static Block instance;

	public BlockStoneBrickMovable() {
		super(Material.rock);
		setHardness(1.5F);
		setResistance(10.0F);
		setStepSound(Block.soundTypeStone);
        setCreativeTab(DragonArtifacts.tabGeneral);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = Blocks.stonebrick.getIcon(0, 0);
    }
	
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(Blocks.stonebrick);
    }
}
