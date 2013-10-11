package draco18s.artifacts.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class PseudoBlockIllusionary extends Block {
	public static Block instance;

	public PseudoBlockIllusionary(int par1) {
		super(par1, Material.rock);
	}
	
	public Icon getIcon(int par1, int par2)
    {
		return this.blockIcon;
    }
	
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("artifacts:invisible");
    }
}
