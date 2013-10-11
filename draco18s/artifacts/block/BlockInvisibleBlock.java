package draco18s.artifacts.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public class BlockInvisibleBlock extends IInvisibleBlock {
	public static Block instance;

	public BlockInvisibleBlock(int par1) {
		super(par1, Material.rock);
		setUnlocalizedName("Invisible Block");
		setResistance(10F);
		setStepSound(soundStoneFootstep);
		setHardness(2.0F);
	}
}
