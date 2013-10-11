package draco18s.artifacts.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public class BlockInvisibleBedrock extends IInvisibleBlock {
	public static Block instance;

	public BlockInvisibleBedrock(int par1) {
		super(par1, Material.rock);
		setUnlocalizedName("Invisible Bedrock");
		setResistance(-1F);
		setStepSound(soundStoneFootstep);
		setHardness(-1F);
	}
	
	@Override
    public void registerIcons(IconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("artifacts:adainvisible");
    }
}
