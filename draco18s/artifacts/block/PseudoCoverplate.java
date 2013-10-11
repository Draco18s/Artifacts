package draco18s.artifacts.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class PseudoCoverplate extends Block {
	public static Block instance;

	public PseudoCoverplate(int par1) {
		super(par1, Material.rock);
	}
	
	public Icon getIcon(int par1, int par2)
    {
		if(par1 == 4 || par1 == 5)
			return this.blockIcon;
		return Block.stoneBrick.getIcon(par1, par2);
    }
	
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("artifacts:pseudo_coverplate");
    }
	
	public void setBlockBoundsForItemRender()
    {
        float f1 = 0.125F;
        this.setBlockBounds(0.5F, 0, 0, 0.5F + f1, 1, 1);
    }
}
