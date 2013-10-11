package draco18s.artifacts.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class PseudoBlockTrap extends Block {
	public static Block instance;
    @SideOnly(Side.CLIENT)
    protected Icon furnaceTopIcon;
    @SideOnly(Side.CLIENT)
    protected Icon furnaceFrontIcon;

	public PseudoBlockTrap(int par1) {
		super(par1, Material.rock);
	}
	
	public Icon getIcon(int par1, int par2)
    {
    	int k = par2 & 7;
        return par1 == k ? (k != 1 && k != 0 ? this.blockIcon : this.blockIcon) : (k != 1 && k != 0 ? (par1 != 1 && par1 != 0 ? this.furnaceFrontIcon : this.furnaceTopIcon) : this.furnaceTopIcon);
    }
	
	public void registerIcons(IconRegister par1IconRegister)
    {
		this.furnaceFrontIcon = Block.furnaceIdle.getBlockTextureFromSide(2);
        this.furnaceTopIcon = Block.furnaceIdle.getBlockTextureFromSide(0);
        this.blockIcon = par1IconRegister.registerIcon("artifacts:pseudo_trap_front");
    }
}
