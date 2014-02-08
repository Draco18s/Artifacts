package draco18s.artifacts.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import draco18s.artifacts.ArtifactEventHandler;
import draco18s.artifacts.client.ClientProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;

public class ItemCalendar extends Item {
	public static Item instance;

	public ItemCalendar(int par1) {
		super(par1);
		this.setUnlocalizedName("Moon Calendar");
		//this.setTextureName("artifacts:calendar");
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1) {
		return ClientProxy.calendar;
	}
}
