package com.draco18s.artifacts.item;

import java.util.List;

import com.draco18s.artifacts.DragonArtifacts;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

public class ItemOrichalcumDust extends Item {
	//public final int repairMaterial;
	public static Item instance;
    public static final String[] matName = new String[] {"Raw", "Wood", "Stone", "Iron", "Diamond", "Gold", "Leather"};
    private static IIcon[] icons;

	public ItemOrichalcumDust() {
		super();
		//repairMaterial = material;
		setNoRepair();
		setMaxDamage(0);
		setCreativeTab(DragonArtifacts.tabGeneral);
		setHasSubtypes(true);
//		setTextureName("artifacts:orichalcum_dust");
	}
	
	public void registerIcons(IIconRegister register) {
		icons = new IIcon[7];
		
		icons[0] = register.registerIcon("artifacts:orichalcum_dust");
		icons[1] = register.registerIcon("artifacts:orichalcum_dust_wood");
		icons[2] = register.registerIcon("artifacts:orichalcum_dust_stone");
		icons[3] = register.registerIcon("artifacts:orichalcum_dust_iron");
		icons[4] = register.registerIcon("artifacts:orichalcum_dust_diamond");
		icons[5] = register.registerIcon("artifacts:orichalcum_dust_gold");
		icons[6] = register.registerIcon("artifacts:orichalcum_dust_leather");
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs creativeTabs, List list)
    {
		for(int i = 0; i < 7; i++) {
			list.add(new ItemStack(item, 1, i));
		}
    }
	
	public IIcon getIconFromDamage(int damage) {
		if(damage < 16) {
			return icons[damage];
		}
			return icons[0];
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack itemStack)
    {
		int d = itemStack.getItemDamage();
		return StatCollector.translateToLocal("mat."+matName[d]) + (d>0?(" " + StatCollector.translateToLocal("mat.infused")):"") + " " + StatCollector.translateToLocal("item.dust_orichalcum.name");
    }
}
