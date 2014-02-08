package draco18s.artifacts.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemOrichalcumDust extends Item {
	//public final int repairMaterial;
	public static Item instance;
    public static final String[] matName = new String[] {"Raw", "Wood", "Stone", "Iron", "Diamond", "Gold", "Leather"};

	public ItemOrichalcumDust(int id) {
		super(id);
		//repairMaterial = material;
		setNoRepair();
		setMaxDamage(0);
		setUnlocalizedName("ArtifactOrichalcum");
		setCreativeTab(CreativeTabs.tabMisc);
		setHasSubtypes(true);
		setTextureName("artifacts:orichalcum_dust");
	}
	
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
        par3List.add(new ItemStack(par1, 1, 4));
        par3List.add(new ItemStack(par1, 1, 5));
        par3List.add(new ItemStack(par1, 1, 6));
    }
	
	public String getItemDisplayName(ItemStack par1ItemStack)
    {
		int d = par1ItemStack.getItemDamage();
		return StatCollector.translateToLocal("mat."+matName[par1ItemStack.getItemDamage()]) + (d>0?(" " + StatCollector.translateToLocal("mat.infused")):"") + " " + StatCollector.translateToLocal("item.orichalcum_dust");
    }
}
