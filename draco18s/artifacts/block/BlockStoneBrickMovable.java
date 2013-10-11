package draco18s.artifacts.block;

import draco18s.artifacts.DragonArtifacts;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

public class BlockStoneBrickMovable extends Block {
	public static Block instance;

	public BlockStoneBrickMovable(int par1) {
		super(par1, Material.rock);
		setUnlocalizedName("Anti Anti-Builder Stone");
		setHardness(1.5F);
		setResistance(10.0F);
		setStepSound(soundStoneFootstep);
        setCreativeTab(DragonArtifacts.tabTraps);
	}
	
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = Block.stoneBrick.getIcon(0, 0);
    }
}
