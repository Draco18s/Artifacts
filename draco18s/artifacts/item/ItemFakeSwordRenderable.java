package draco18s.artifacts.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ItemFakeSwordRenderable extends ItemSword {

	public static Item wood;
	public static Item stone;
	public static Item iron;
	public static Item gold;
	public static Item diamond;
	private String regString;

	public ItemFakeSwordRenderable(int par1, EnumToolMaterial par2EnumToolMaterial, String str) {
		super(par1, par2EnumToolMaterial);
		this.setUnlocalizedName("Test Sword");
		regString = str;
		this.setCreativeTab(null);
	}

	public void registerIcons(IconRegister iconReg)
	{
		itemIcon = iconReg.registerIcon(regString);
	}
}
