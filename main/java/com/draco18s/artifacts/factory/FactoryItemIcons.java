package com.draco18s.artifacts.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.IIcon;

import com.draco18s.artifacts.api.IItemIconAPI;
import com.draco18s.artifacts.client.RadarParticle;

public class FactoryItemIcons implements IItemIconAPI {
	public int numberAmulets = 0;
	public int numberDaggers = 0;
	public int numberFigurines = 0;
	public int numberRings = 0;
	public int numberStaffs = 0;
	public int numberSwords = 0;
	public int numberTrinkets = 0;
	public int numberWands = 0;
	public int numberBoots = 0;
	public int numberChestplates = 0;
	public int numberHelms = 0;
	public int numberLeggings = 0;
	public int numberBelts = 0;
	private final ArrayList<AbstractIcon> iconList = new ArrayList<AbstractIcon>();
	private final HashMap<String, HashMap<ArmorMaterial, AbstractModelTexture>> modelMap = new HashMap<String, HashMap<ArmorMaterial, AbstractModelTexture>>();

	public FactoryItemIcons() {
		System.out.println("Instancing default icon list");
		try {
			registerArtifactIcon("amulet","artifacts:amulet1","artifacts:amulet1_overlay");
			registerArtifactIcon("amulet","artifacts:amulet2","artifacts:amulet2_overlay");
			registerArtifactIcon("amulet","artifacts:amulet3","artifacts:amulet3_overlay");
			registerArtifactIcon("amulet","artifacts:amulet4","artifacts:amulet4_overlay");
			registerArtifactIcon("amulet","artifacts:amulet5","artifacts:amulet5_overlay");
			registerArtifactIcon("amulet","artifacts:amulet6","artifacts:amulet6_overlay");
			registerArtifactIcon("amulet","artifacts:amulet7","artifacts:amulet7_overlay");
			registerArtifactIcon("dagger","artifacts:dagger1","artifacts:dagger1_overlay");
			registerArtifactIcon("dagger","artifacts:dagger2","artifacts:dagger2_overlay");
			registerArtifactIcon("dagger","artifacts:dagger3","artifacts:dagger3_overlay");
			registerArtifactIcon("dagger","artifacts:dagger4","artifacts:dagger4_overlay");
			registerArtifactIcon("dagger","artifacts:dagger5","artifacts:dagger5_overlay");
			registerArtifactIcon("dagger","artifacts:dagger6","artifacts:dagger6_overlay");
			registerArtifactIcon("figurine","artifacts:figurine1");
			registerArtifactIcon("figurine","artifacts:figurine2","artifacts:figurine2_overlay");
			registerArtifactIcon("figurine","artifacts:figurine3");
			registerArtifactIcon("ring","artifacts:ring1","artifacts:ring1_overlay");
			registerArtifactIcon("ring","artifacts:ring2","artifacts:ring2_overlay");
			registerArtifactIcon("ring","artifacts:ring3","artifacts:ring3_overlay");
			registerArtifactIcon("ring","artifacts:ring4","artifacts:ring4_overlay");
			registerArtifactIcon("ring","artifacts:ring5","artifacts:ring5_overlay");
			registerArtifactIcon("ring","artifacts:ring6","artifacts:ring6_overlay");
			registerArtifactIcon("ring","artifacts:ring7");
			registerArtifactIcon("ring","artifacts:ring8","artifacts:ring8_overlay");
			registerArtifactIcon("staff","artifacts:staff1","artifacts:staff1_overlay");
			registerArtifactIcon("staff","artifacts:staff2","artifacts:staff2_overlay");
			registerArtifactIcon("staff","artifacts:staff3");
			registerArtifactIcon("staff","artifacts:staff4","artifacts:staff4_overlay");
			registerArtifactIcon("staff","artifacts:staff5");
			registerArtifactIcon("staff","artifacts:staff6","artifacts:staff6_overlay");
			registerArtifactIcon("sword","artifacts:sword1","artifacts:sword1_overlay");
			registerArtifactIcon("sword","artifacts:sword2","artifacts:sword2_overlay");
			registerArtifactIcon("sword","artifacts:sword3","artifacts:sword3_overlay");
			registerArtifactIcon("sword","artifacts:sword4","artifacts:sword4_overlay");
			registerArtifactIcon("sword","artifacts:sword5","artifacts:sword5_overlay");
			registerArtifactIcon("sword","artifacts:sword6");
			registerArtifactIcon("sword","artifacts:sword7","artifacts:sword7_overlay");
			registerArtifactIcon("trinket","artifacts:trinket1","artifacts:trinket1_overlay");
			registerArtifactIcon("trinket","artifacts:trinket2","artifacts:trinket2_overlay");
			registerArtifactIcon("trinket","artifacts:trinket3");
			registerArtifactIcon("trinket","artifacts:trinket4");
			registerArtifactIcon("trinket","artifacts:trinket5");
			registerArtifactIcon("trinket","artifacts:trinket6","artifacts:trinket6_overlay");
			registerArtifactIcon("trinket","artifacts:trinket7","artifacts:trinket7_overlay");
			registerArtifactIcon("trinket","artifacts:trinket8","artifacts:trinket8_overlay");
			registerArtifactIcon("trinket","artifacts:trinket9","artifacts:trinket9_overlay");
			registerArtifactIcon("trinket","artifacts:trinket10");
			registerArtifactIcon("wand","artifacts:wand1","artifacts:wand1_overlay");
			registerArtifactIcon("wand","artifacts:wand2","artifacts:wand2_overlay");
			registerArtifactIcon("wand","artifacts:wand3","artifacts:wand3_overlay");
			registerArtifactIcon("wand","artifacts:wand4","artifacts:wand4_overlay");
			registerArtifactIcon("wand","artifacts:wand5","artifacts:wand5_overlay");
			registerArtifactIcon("wand","artifacts:wand6","artifacts:wand6_overlay");
			registerArtifactIcon("wand","artifacts:wand7","artifacts:wand7_overlay");
			registerArtifactIcon("belt","artifacts:belt1","artifacts:belt1_overlay");
			registerArtifactIcon("belt","artifacts:belt2","artifacts:belt2_overlay");
			registerArtifactIcon("belt","artifacts:belt3","artifacts:belt3_overlay");
			registerArtifactIcon("belt","artifacts:belt4","artifacts:belt4_overlay");

			registerArtifactIcon("boots","artifacts:boots1","artifacts:boots1_overlay");
			registerModelTexture("artifacts:boots1", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:boots1", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:boots1", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:boots1", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:boots1", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			
			registerArtifactIcon("boots","artifacts:boots2","artifacts:boots2_overlay");
			registerModelTexture("artifacts:boots2", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:boots2", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:boots2", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:boots2", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:boots2", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");

			registerArtifactIcon("boots","artifacts:boots3","artifacts:boots3_overlay");
			registerModelTexture("artifacts:boots3", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:boots3", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:boots3", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:boots3", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:boots3", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");

			registerArtifactIcon("boots","artifacts:boots4","artifacts:boots4_overlay");
			registerModelTexture("artifacts:boots4", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:boots4", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:boots4", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:boots4", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:boots4", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");

			registerArtifactIcon("boots","artifacts:boots5","artifacts:boots5_overlay");
			registerModelTexture("artifacts:boots5", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:boots5", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:boots5", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:boots5", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:boots5", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");

			registerArtifactIcon("chestplate","artifacts:chestplate1","artifacts:chestplate1_overlay");
			registerModelTexture("artifacts:chestplate1", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:chestplate1", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:chestplate1", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:chestplate1", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:chestplate1", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");

			registerArtifactIcon("chestplate","artifacts:chestplate2","artifacts:chestplate2_overlay");
			registerModelTexture("artifacts:chestplate2", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:chestplate2", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:chestplate2", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:chestplate2", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:chestplate2", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");

			registerArtifactIcon("chestplate","artifacts:chestplate3","artifacts:chestplate3_overlay");
			registerModelTexture("artifacts:chestplate3", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:chestplate3", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:chestplate3", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:chestplate3", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:chestplate3", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");

			registerArtifactIcon("chestplate","artifacts:chestplate4","artifacts:chestplate4_overlay");
			registerModelTexture("artifacts:chestplate4", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:chestplate4", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:chestplate4", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:chestplate4", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:chestplate4", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");

			registerArtifactIcon("chestplate","artifacts:chestplate5","artifacts:chestplate5_overlay");
			registerModelTexture("artifacts:chestplate5", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:chestplate5", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:chestplate5", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:chestplate5", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:chestplate5", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");

			registerArtifactIcon("helm","artifacts:helm1","artifacts:helm1_overlay");
			registerModelTexture("artifacts:helm1", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:helm1", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:helm1", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:helm1", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");
			registerModelTexture("artifacts:helm1", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond1_layer1.png", "artifacts:textures/models/armor/color1_layer1.png");

			registerArtifactIcon("helm","artifacts:helm2","artifacts:helm2_overlay");
			registerModelTexture("artifacts:helm2", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:helm2", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:helm2", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:helm2", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");
			registerModelTexture("artifacts:helm2", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond2_layer1.png", "artifacts:textures/models/armor/color2_layer1.png");

			registerArtifactIcon("helm","artifacts:helm3","artifacts:helm3_overlay");
			registerModelTexture("artifacts:helm3", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:helm3", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:helm3", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:helm3", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");
			registerModelTexture("artifacts:helm3", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond3_layer1.png", "artifacts:textures/models/armor/color3_layer1.png");

			registerArtifactIcon("helm","artifacts:helm4","artifacts:helm4_overlay");
			registerModelTexture("artifacts:helm4", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:helm4", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:helm4", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:helm4", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");
			registerModelTexture("artifacts:helm4", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond4_layer1.png", "artifacts:textures/models/armor/color4_layer1.png");

			registerArtifactIcon("helm","artifacts:helm5","artifacts:helm5_overlay");
			registerModelTexture("artifacts:helm5", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:helm5", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:helm5", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:helm5", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");
			registerModelTexture("artifacts:helm5", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond5_layer1.png", "artifacts:textures/models/armor/color5_layer1.png");

			registerArtifactIcon("leggings","artifacts:leggings1","artifacts:leggings1_overlay");
			registerModelTexture("artifacts:leggings1", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather1_layer2.png", "artifacts:textures/models/armor/color1_layer2.png");
			registerModelTexture("artifacts:leggings1", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain1_layer2.png", "artifacts:textures/models/armor/color1_layer2.png");
			registerModelTexture("artifacts:leggings1", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron1_layer2.png", "artifacts:textures/models/armor/color1_layer2.png");
			registerModelTexture("artifacts:leggings1", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold1_layer2.png", "artifacts:textures/models/armor/color1_layer2.png");
			registerModelTexture("artifacts:leggings1", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond1_layer2.png", "artifacts:textures/models/armor/color1_layer2.png");

			registerArtifactIcon("leggings","artifacts:leggings2","artifacts:leggings2_overlay");
			registerModelTexture("artifacts:leggings2", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather2_layer2.png", "artifacts:textures/models/armor/color2_layer2.png");
			registerModelTexture("artifacts:leggings2", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain2_layer2.png", "artifacts:textures/models/armor/color2_layer2.png");
			registerModelTexture("artifacts:leggings2", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron2_layer2.png", "artifacts:textures/models/armor/color2_layer2.png");
			registerModelTexture("artifacts:leggings2", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold2_layer2.png", "artifacts:textures/models/armor/color2_layer2.png");
			registerModelTexture("artifacts:leggings2", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond2_layer2.png", "artifacts:textures/models/armor/color2_layer2.png");

			registerArtifactIcon("leggings","artifacts:leggings3","artifacts:leggings3_overlay");
			registerModelTexture("artifacts:leggings3", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather3_layer2.png", "artifacts:textures/models/armor/color3_layer2.png");
			registerModelTexture("artifacts:leggings3", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain3_layer2.png", "artifacts:textures/models/armor/color3_layer2.png");
			registerModelTexture("artifacts:leggings3", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron3_layer2.png", "artifacts:textures/models/armor/color3_layer2.png");
			registerModelTexture("artifacts:leggings3", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold3_layer2.png", "artifacts:textures/models/armor/color3_layer2.png");
			registerModelTexture("artifacts:leggings3", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond3_layer2.png", "artifacts:textures/models/armor/color3_layer2.png");

			registerArtifactIcon("leggings","artifacts:leggings4","artifacts:leggings4_overlay");
			registerModelTexture("artifacts:leggings4", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather4_layer2.png", "artifacts:textures/models/armor/color4_layer2.png");
			registerModelTexture("artifacts:leggings4", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain4_layer2.png", "artifacts:textures/models/armor/color4_layer2.png");
			registerModelTexture("artifacts:leggings4", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron4_layer2.png", "artifacts:textures/models/armor/color4_layer2.png");
			registerModelTexture("artifacts:leggings4", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold4_layer2.png", "artifacts:textures/models/armor/color4_layer2.png");
			registerModelTexture("artifacts:leggings4", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond4_layer2.png", "artifacts:textures/models/armor/color4_layer2.png");

			registerArtifactIcon("leggings","artifacts:leggings5","artifacts:leggings5_overlay");
			registerModelTexture("artifacts:leggings5", ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather5_layer2.png", "artifacts:textures/models/armor/color5_layer2.png");
			registerModelTexture("artifacts:leggings5", ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain5_layer2.png", "artifacts:textures/models/armor/color5_layer2.png");
			registerModelTexture("artifacts:leggings5", ArmorMaterial.IRON, "artifacts:textures/models/armor/iron5_layer2.png", "artifacts:textures/models/armor/color5_layer2.png");
			registerModelTexture("artifacts:leggings5", ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold5_layer2.png", "artifacts:textures/models/armor/color5_layer2.png");
			registerModelTexture("artifacts:leggings5", ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond5_layer2.png", "artifacts:textures/models/armor/color5_layer2.png");

		}
		catch (Exception e) {
			System.out.println("Error!" + e.getMessage());
		}

		/*registerArtifactIcon("boots","artifacts:boots1");
        registerArtifactIcon("chestplate","artifacts:chestplate1");
        registerArtifactIcon("helm","artifacts:helm1");
        registerArtifactIcon("leggings","artifacts:leggings1");*/
	}

	@Override
	public void registerArtifactIcon(String type, String icon) throws Exception {
		if(type.toLowerCase().equals("amulet")) {
			iconList.add(new AbstractIcon("amulet"+(++numberAmulets),icon));
		}
		else if(type.toLowerCase().equals("dagger")) {
			iconList.add(new AbstractIcon("dagger"+(++numberDaggers),icon));
		}
		else if(type.toLowerCase().equals("figurine")) {
			iconList.add(new AbstractIcon("figurine"+(++numberFigurines),icon));
		}
		else if(type.toLowerCase().equals("ring")) {
			iconList.add(new AbstractIcon("ring"+(++numberRings),icon));
		}
		else if(type.toLowerCase().equals("staff")) {
			iconList.add(new AbstractIcon("staff"+(++numberStaffs),icon));
		}
		else if(type.toLowerCase().equals("sword")) {
			iconList.add(new AbstractIcon("sword"+(++numberSwords),icon));
		}
		else if(type.toLowerCase().equals("trinket")) {
			iconList.add(new AbstractIcon("trinket"+(++numberTrinkets),icon));
		}
		else if(type.toLowerCase().equals("wand")) {
			iconList.add(new AbstractIcon("wand"+(++numberWands),icon));
		}
		else if(type.toLowerCase().equals("belt")) {
			iconList.add(new AbstractIcon("belt"+(++numberBelts),icon));
		}
		else if(type.toLowerCase().equals("boots")) {
			iconList.add(new AbstractIcon("boots"+(++numberBoots),icon));
		}
		else if(type.toLowerCase().equals("chestplate")) {
			iconList.add(new AbstractIcon("chestplate"+(++numberChestplates),icon));
		}
		else if(type.toLowerCase().equals("helm")) {
			iconList.add(new AbstractIcon("helm"+(++numberHelms),icon));
		}
		else if(type.toLowerCase().equals("leggings")) {
			iconList.add(new AbstractIcon("leggings"+(++numberLeggings),icon));
		}
		else {
			throw new Exception("Invalid artifact icon type for " + icon + " with type " + type + ".  Only valid types are: amulet, dagger, figurine, ring, staff, sword, trinket, wand");
		}
	}

	@Override
	public void registerArtifactIcon(String type, String icon, String overlay) throws Exception {
		if(type.toLowerCase().equals("amulet")) {
			iconList.add(new AbstractIcon("amulet"+(++numberAmulets),icon,overlay));
		}
		else if(type.toLowerCase().equals("dagger")) {
			iconList.add(new AbstractIcon("dagger"+(++numberDaggers),icon,overlay));
		}
		else if(type.toLowerCase().equals("figurine")) {
			iconList.add(new AbstractIcon("figurine"+(++numberFigurines),icon,overlay));
		}
		else if(type.toLowerCase().equals("ring")) {
			iconList.add(new AbstractIcon("ring"+(++numberRings),icon,overlay));
		}
		else if(type.toLowerCase().equals("staff")) {
			iconList.add(new AbstractIcon("staff"+(++numberStaffs),icon,overlay));
		}
		else if(type.toLowerCase().equals("sword")) {
			iconList.add(new AbstractIcon("sword"+(++numberSwords),icon,overlay));
		}
		else if(type.toLowerCase().equals("trinket")) {
			iconList.add(new AbstractIcon("trinket"+(++numberTrinkets),icon,overlay));
		}
		else if(type.toLowerCase().equals("wand")) {
			iconList.add(new AbstractIcon("wand"+(++numberWands),icon,overlay));
		}
		else if(type.toLowerCase().equals("belt")) {
			iconList.add(new AbstractIcon("belt"+(++numberBelts),icon,overlay));
		}
		else if(type.toLowerCase().equals("boots")) {
			iconList.add(new AbstractIcon("boots"+(++numberBoots),icon,overlay));
		}
		else if(type.toLowerCase().equals("chestplate")) {
			iconList.add(new AbstractIcon("chestplate"+(++numberChestplates),icon,overlay));
		}
		else if(type.toLowerCase().equals("helm")) {
			iconList.add(new AbstractIcon("helm"+(++numberHelms),icon,overlay));
		}
		else if(type.toLowerCase().equals("leggings")) {
			iconList.add(new AbstractIcon("leggings"+(++numberLeggings),icon,overlay));
		}
		else {
			throw new Exception("Invalid artifact icon type '" + icon + "' with type '" + type + "'.  Only valid types are: amulet, dagger, figurine, ring, staff, sword, trinket, wand");
		}
	}

	@Override
	public void registerModelTexture(String icon, ArmorMaterial material, String modelTexture) {
		registerModelTexture(icon, material, modelTexture, null);
	}


	@Override
	public void registerModelTexture(String icon, ArmorMaterial material, String modelTexture, String modelColor) {
		HashMap<ArmorMaterial, AbstractModelTexture> innerMap = modelMap.get(icon);

		if(innerMap == null) {
			innerMap = new HashMap<ArmorMaterial, AbstractModelTexture>();
		}

		innerMap.put(material, new AbstractModelTexture(modelTexture, modelColor));
		modelMap.put(icon, innerMap);
	}

	@Override
	public IIcon registerIcons(IIconRegister iconReg) {
		IIcon defaultIcon = iconReg.registerIcon("artifacts:artifact1");
		icons.put("artifact1", defaultIcon);
		icons.put("artifact2", iconReg.registerIcon("artifacts:artifact2"));
		icons.put("artifact3", iconReg.registerIcon("artifacts:artifact2"));
		icons.put("artifact4", iconReg.registerIcon("artifacts:artifact2"));
		icons.put("artifact5", iconReg.registerIcon("artifacts:artifact2"));
		icons.put("artifact6", iconReg.registerIcon("artifacts:artifact2"));
		IIcon overlay = iconReg.registerIcon("artifacts:blank_overlay");
		icons.put("overlay_artifact1",overlay);
		icons.put("overlay_artifact2",overlay);
		icons.put("overlay_artifact3",overlay);
		icons.put("overlay_artifact4",overlay);
		icons.put("overlay_artifact5",overlay);
		icons.put("overlay_artifact6",overlay);
		
		HashMap<ArmorMaterial, String> modelsLayer1 = new HashMap<ArmorMaterial, String>();
		modelsLayer1.put(ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather_default_layer1.png");
		modelsLayer1.put(ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain_default_layer1.png");
		modelsLayer1.put(ArmorMaterial.IRON, "artifacts:textures/models/armor/iron_default_layer1.png");
		modelsLayer1.put(ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold_default_layer1.png");
		modelsLayer1.put(ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond_default_layer1.png");
		
		HashMap<ArmorMaterial, String> modelsLayer2 = new HashMap<ArmorMaterial, String>();
		modelsLayer2.put(ArmorMaterial.CLOTH, "artifacts:textures/models/armor/leather_default_layer2.png");
		modelsLayer2.put(ArmorMaterial.CHAIN, "artifacts:textures/models/armor/chain_default_layer2.png");
		modelsLayer2.put(ArmorMaterial.IRON, "artifacts:textures/models/armor/iron_default_layer2.png");
		modelsLayer2.put(ArmorMaterial.GOLD, "artifacts:textures/models/armor/gold_default_layer2.png");
		modelsLayer2.put(ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/diamond_default_layer2.png");
		
		HashMap<ArmorMaterial, String> colorsLayer1 = new HashMap<ArmorMaterial, String>();
		colorsLayer1.put(ArmorMaterial.CLOTH, "artifacts:textures/models/armor/color_default_layer1.png");
		colorsLayer1.put(ArmorMaterial.CHAIN, "artifacts:textures/models/armor/color_default_layer1.png");
		colorsLayer1.put(ArmorMaterial.IRON, "artifacts:textures/models/armor/color_default_layer1.png");
		colorsLayer1.put(ArmorMaterial.GOLD, "artifacts:textures/models/armor/color_default_layer1.png");
		colorsLayer1.put(ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/color_default_layer1.png");
		
		HashMap<ArmorMaterial, String> colorsLayer2 = new HashMap<ArmorMaterial, String>();
		colorsLayer2.put(ArmorMaterial.CLOTH, "artifacts:textures/models/armor/color_default_layer2.png");
		colorsLayer2.put(ArmorMaterial.CHAIN, "artifacts:textures/models/armor/color_default_layer2.png");
		colorsLayer2.put(ArmorMaterial.IRON, "artifacts:textures/models/armor/color_default_layer2.png");
		colorsLayer2.put(ArmorMaterial.GOLD, "artifacts:textures/models/armor/color_default_layer2.png");
		colorsLayer2.put(ArmorMaterial.DIAMOND, "artifacts:textures/models/armor/color_default_layer2.png");
		
		armorModels.put("artifact2", modelsLayer1);
		armorModels.put("artifact3", modelsLayer1);
		armorModels.put("artifact4", modelsLayer1);
		armorModels.put("artifact5", modelsLayer2);
		
		armorModels.put("color_artifact2", colorsLayer1);
		armorModels.put("color_artifact3", colorsLayer1);
		armorModels.put("color_artifact4", colorsLayer1);
		armorModels.put("color_artifact5", colorsLayer2);
		
		AbstractIcon a;
		for(int i=0; i < iconList.size(); ++i) {
			a = (AbstractIcon)iconList.get(i);
			IIcon ico, ico2;
			if(a.overlay != null) {
				ico2 = iconReg.registerIcon(a.overlay);
				icons.put("overlay_" + a.type, ico2);
			}
			ico = iconReg.registerIcon(a.icon);
			icons.put(a.type, ico);
			
			if(modelMap.get(a.icon) != null) {
				HashMap<ArmorMaterial, AbstractModelTexture> thisMap = modelMap.get(a.icon);
				HashMap<ArmorMaterial, String> normalMap = new HashMap<ArmorMaterial, String>();
				HashMap<ArmorMaterial, String> colorMap = new HashMap<ArmorMaterial, String>();
				for(ArmorMaterial material : thisMap.keySet()) {
					String model = thisMap.get(material).model;
					String color = thisMap.get(material).color;
					if(model != null) {
						normalMap.put(material, model);
					}
					if(color != null) {
						colorMap.put(material, color);
					}
				}
				
				armorModels.put(a.type, normalMap);
				armorModels.put("color_" + a.type, colorMap);
			}
		}
		icons.put("radar",iconReg.registerIcon("artifacts:radarparticle"));
		return defaultIcon;
	}

	private class AbstractModelTexture {
		public String model;
		public String color;

		private AbstractModelTexture(String m) {
			model = m;
		}

		private AbstractModelTexture(String m, String o) {
			model = m;
			color = o;
		}
	}

	private class AbstractIcon {
		public String type;
		public String icon;
		public String overlay;

		private AbstractIcon(String t, String i) {
			type = t;
			icon = i;
		}

		private AbstractIcon(String t, String i, String o) {
			type = t;
			icon = i;
			overlay = o;
		}
	}
}
