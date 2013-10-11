package draco18s.artifacts.item;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;

import draco18s.artifacts.components.*;

public class FactoryArtifact {
	public static FactoryArtifact instance;
	private HashMap effects = new HashMap();
	private Random rand = new Random();
	//Texture bitflags
	private static int Amulet;      //1
	private static int Dagger;      //2
	private static int Figurine;    //4
	private static int Ring;        //8
	private static int Staff;       //16
	private static int Sword;       //32
	private static int Trinket;     //64
	private static int Wand;        //128
	private static int Armor;       //256
	
	private static int Boots;       //512
	private static int Chestplate;  //1024
	private static int Helm;        //2048
	private static int Leggings;    //4096
	private final static IArtifactComponent baseDamage = new ComponentNormalDamage();
	//pyramid
	
	public FactoryArtifact() {
		effects.put(1, new ComponentHeal());
		effects.put(2, new ComponentDamage());
		effects.put(3, new ComponentFireball());
		effects.put(4, new ComponentHarvesting());
		effects.put(5, new ComponentLightning());
		effects.put(6, new ComponentMining());
		effects.put(7, new ComponentExplosive());
		effects.put(8, new ComponentLight());
		effects.put(9, new ComponentCashout());
		effects.put(10, new ComponentResistance());
		effects.put(11, new ComponentJumping());
		effects.put(12, new ComponentVision());
		effects.put(13, new ComponentBreathing());
		effects.put(14, new ComponentFoodie());
		effects.put(15, new ComponentSpeed());
		effects.put(16, new ComponentHealth());
		effects.put(17, new ComponentRepair());
		effects.put(18, new ComponentMassWeb());
		effects.put(19, new ComponentAirWalk());
	}
	
	public static IArtifactComponent getComponent(int componentID) {
		IArtifactComponent eff = (IArtifactComponent) instance.effects.get(componentID);
		if(eff == null) {
			System.out.print("ERROR: NO EFFECT ID "+ componentID +" REGISTERED!");
		}
		return eff;
	}
	
	public static ItemStack generateRandomArtifact() {
		ItemStack artifact = new ItemStack(ItemArtifact.instance, 1, 0);
		return applyRandomEffects(artifact);
	}
	
	//TODO refactor this so that it is more agnostic.
	public static ItemStack applyRandomEffects(ItemStack artifact) {
		artifact.stackTagCompound = createDefault();
		String nameChunk = "";
		Amulet = 1;
		Dagger = 1;
		Figurine = 1;
		Ring = 1;
		Staff = 1;
		Sword = 1;
		Trinket = 1;
		Wand = 1;
		Armor = 1;
		Boots = 1;
		Chestplate = 1;
		Helm = 1;
		Leggings = 1;
		int flags,effID;
		String artiName = "";
		Vector effectsOnItem = new Vector();
		IArtifactComponent c;
		int count = 0, a[];
		int numEff = instance.rand.nextInt(5)+1;
		a = new int[numEff];
		for(; numEff > 0; numEff--) {
			effID = instance.rand.nextInt(instance.effects.size())+1;
			if(effID == 17 && a.length < 3) {
				numEff++;
				a = new int[numEff];
			}
			c = getComponent(effID);
			if(effectsOnItem.contains(c)) {
				numEff++;
				continue;
			}
			String trigName = c.getRandomTrigger(instance.rand);
			if(artifact.stackTagCompound.hasKey(trigName)) {
				//make NBTTagLists to remove this condition;
				numEff++;
				continue;
			}
			effectsOnItem.add(c);
			if(effID == 9) {
				int bonus = a.length*5;
				if(numEff == a.length) {
					numEff = 1;
					bonus = 0;
					artifact.stackSize = 10;
				}
				artifact.stackTagCompound.setInteger("cashBonus", bonus);
			}
			if(effID == 7) {
				if(numEff == a.length) {
					if(trigName == "onDropped") {
						numEff = 1;
						artifact.stackSize = 10;
					}
				}
			}
			//System.out.println(c.getName());
			artifact.stackTagCompound.setInteger(trigName, effID);
			if(trigName == "onHeld") {
				artifact = c.attached(artifact, instance.rand);
			}
			a[numEff-1] = effID;
			flags = c.getTextureBitflags();
			Amulet += flags % 2;
			flags >>= 1;
			Dagger += flags % 2;
			flags >>= 1;
			Figurine += flags % 2;
			flags >>= 1;
			Ring += flags % 2;
			flags >>= 1;
			Staff += flags % 2;
			flags >>= 1;
			Sword += flags % 2;
			flags >>= 1;
			Trinket += flags % 2;
			flags >>= 1;
			Wand += flags % 2;
			flags >>= 1;
			//Armor += flags % 2;
			flags >>= 1;
			Boots += flags % 2;
			flags >>= 1;
			Chestplate += flags % 2;
			flags >>= 1;
			Helm += flags % 2;
			flags >>= 1;
			Leggings += flags % 2;

			flags = c.getNegTextureBitflags();
			Amulet -= flags % 2;
			flags >>= 1;
			Dagger -= flags % 2;
			flags >>= 1;
			Figurine -= flags % 2;
			flags >>= 1;
			Ring -= flags % 2;
			flags >>= 1;
			Staff -= flags % 2;
			flags >>= 1;
			Sword -= flags % 2;
			flags >>= 1;
			Trinket -= flags % 2;
			flags >>= 1;
			Wand -= flags % 2;
			flags >>= 1;
			Armor -= flags % 2;
			flags >>= 1;
			Boots -= flags % 2;
			flags >>= 1;
			Chestplate -= flags % 2;
			flags >>= 1;
			Helm -= flags % 2;
			flags >>= 1;
			Leggings -= flags % 2;
			if(instance.rand.nextInt(4) == 0) {
				numEff--;
				if(numEff > 0)
					a[numEff-1] = 0;
			}
		}
		Amulet = Math.max(Amulet, 0);
		Dagger = Math.max(Dagger, 0);
		Figurine = Math.max(Figurine, 0);
		Ring = Math.max(Ring, 0);
		Staff = Math.max(Staff, 0);
		Sword = Math.max(Sword, 0);
		Trinket = Math.max(Trinket, 0);
		Wand = Math.max(Wand, 0);
		Armor = Math.max(Armor*4, 0);
		Boots = Math.max(Boots, 0);
		Chestplate = Math.max(Chestplate, 0);
		Helm = Math.max(Helm, 0);
		Leggings = Math.max(Leggings, 0);
		//end loop
		int t = Amulet + Dagger + Figurine + Ring + Staff + Sword + Trinket + Wand;
		int r = 0;
		String iconType;
		artifact.stackTagCompound.setInteger("armorType", -1);
		if(t > 0) {
			r = instance.rand.nextInt(t);
			if((r -= Amulet) < 0) {
				iconType = "Amulet";
				t = 4;
			}
			else if((r -= Dagger) < 0) {
				iconType = "Dagger";
				t = 4;
				if(!effectsOnItem.contains(2)) {
					artifact = baseDamage.attached(artifact, instance.rand);
				}
			}
			else if((r -= Figurine) < 0) {
				iconType = "Figurine";
				t = 2;
			}
			else if((r -= Ring) < 0) {
				iconType = "Ring";
				t = 7;
			}
			else if((r -= Staff) < 0) {
				iconType = "Staff";
				t = 4;
			}
			else if((r -= Sword) < 0) {
				iconType = "Sword";
				t = 3;
				if(!effectsOnItem.contains(2)) {
					artifact = baseDamage.attached(artifact, instance.rand);
				}
			}
			else if((r -= Trinket) < 0) {
				iconType = "Trinket";
				t = 7;
			}
			else if((r -= Wand) < 0) {
				iconType = "Wand";
				t = 5;
			}
			else if((r -= Armor) < 0) {
				t = Boots+Chestplate+Helm+Leggings;
				if(t < 1)
					t = 1;
				r = instance.rand.nextInt(t);
				if((r -= Boots) < 0) {
					iconType = "Boots";
					t = 1;
					artifact.stackTagCompound.setInteger("armorType", 3);
				}
				else if((r -= Chestplate) < 0) {
					iconType = "Chestplate";
					t = 1;
					artifact.stackTagCompound.setInteger("armorType", 1);
				}
				else if((r -= Helm) < 0) {
					iconType = "Helm";
					t = 1;
					artifact.stackTagCompound.setInteger("armorType", 0);
				}
				else if((r -= Leggings) < 0) {
					iconType = "Leggings";
					t = 1;
					artifact.stackTagCompound.setInteger("armorType", 2);
				}
				else {
					iconType = "Artifact";
					t = 1;
				}
			}
			else {
				iconType = "Artifact";
				t = 1;
			}
		}
		else {
			iconType = "Artifact";
			t = 1;
		}
		int r2=-1,r3=-1,r4 = instance.rand.nextInt(10);
		String matName = "[Material]";
		switch(r4) {
			case 0:
				matName = "Wood";
				r4 = 0;
				break;
			case 1:
			case 8:
				r4 = 1;
				matName = "Stone";
				break;
			case 2:
			case 7:
			case 9:
				r4 = 2;
				matName = "Iron";
				break;
			case 4:
				r4 = 4;
				matName = "Diamond";
				break;
			case 3:
			case 5:
			case 6:
			case 10:
				r4 = 3;
				matName = "Gold";
				break;
		}
		artifact.stackTagCompound.setString("iconName", iconType);
		artifact.stackTagCompound.setString("matName", matName);
		if(effectsOnItem.size() > 1) {
			r2 = instance.rand.nextInt(effectsOnItem.size());
			c = (IArtifactComponent) effectsOnItem.get(r2);
			//System.out.println("Pre: " + c.getPreAdj(instance.rand));
			nameChunk = c.getPreAdj(instance.rand);
			artifact.stackTagCompound.setString("preadj", nameChunk);
			artiName = nameChunk + " ";
			artiName += matName + " " + iconType;
			do {
				r3 = instance.rand.nextInt(effectsOnItem.size());
			} while(r2 == r3);
			c = (IArtifactComponent) effectsOnItem.get(r3);
			nameChunk = c.getPostAdj(instance.rand);
			artifact.stackTagCompound.setString("postadj", nameChunk);
			artiName += " " + nameChunk;
		}
		else {
			//System.out.println("Singular");
			if(instance.rand.nextBoolean()) {
				r2 = instance.rand.nextInt(effectsOnItem.size());
				c = (IArtifactComponent) effectsOnItem.get(r2);
				nameChunk = c.getPreAdj(instance.rand);
				artifact.stackTagCompound.setString("preadj", nameChunk);
				artifact.stackTagCompound.setString("postadj", "");
				artiName = nameChunk + " " + matName + " " + iconType;
			}
			else {
				r3 = instance.rand.nextInt(effectsOnItem.size());
				c = (IArtifactComponent) effectsOnItem.get(r3);
				nameChunk = c.getPostAdj(instance.rand);
				artifact.stackTagCompound.setString("preadj", "");
				artifact.stackTagCompound.setString("postadj", nameChunk);
				artiName = matName + " " + iconType + " " + nameChunk;
			}
		}
		r = instance.rand.nextInt(t);
		artifact.stackTagCompound.setString("name", artiName);
		artifact.stackTagCompound.setString("icon", iconType+(r+1));
		int col = Color.HSBtoRGB((float)(instance.rand.nextInt(360) / 360F), .8f, 1);
		artifact.stackTagCompound.setLong("overlay_color", col);
		artiName = "";
		artifact.stackTagCompound.setInteger("material", r4);
		
		artifact.stackTagCompound.setIntArray("allComponents", a);
		if(instance.rand.nextInt(8) == 0) {
			artifact = enchantArtifact(artifact, effectsOnItem, (iconType == "Sword" || iconType == "Dagger"));
		}
		return artifact;
	}
	

	public static NBTTagCompound createDefault()
	{
		return new NBTTagCompound("tag");
	}
	
	private static ItemStack enchantArtifact(ItemStack artifact, Vector effectsOnItem) {
		return enchantArtifact(artifact, effectsOnItem, false);
	}
	
	private static ItemStack enchantArtifact(ItemStack artifact, Vector effectsOnItem, boolean isSword) {
		Item item = Item.pickaxeWood;
		int level = 6;
		do {
			level += 2;
		} while(level < 40 && instance.rand.nextInt(8) != 0);
		if((isSword || effectsOnItem.contains(getComponent(2))) && instance.rand.nextBoolean()) {
			switch(artifact.stackTagCompound.getInteger("material")) {
				case 0:
					item = Item.swordWood;
					break;
				case 1:
					item = Item.swordStone;
					break;
				case 2:
					item = Item.swordIron;
					break;
				case 3:
					item = Item.swordGold;
					break;
				case 4:
					item = Item.swordDiamond;
					break;
			}
		}
		else if(effectsOnItem.contains(getComponent(4)) || effectsOnItem.contains(getComponent(6))) {
			switch(artifact.stackTagCompound.getInteger("material")) {
				case 0:
					item = Item.pickaxeWood;
					break;
				case 1:
					item = Item.pickaxeStone;
					break;
				case 2:
					item = Item.pickaxeIron;
					break;
				case 3:
					item = Item.pickaxeGold;
					break;
				case 4:
					item = Item.pickaxeDiamond;
					break;
			}
		}
		else {
			int r2 = (int) Math.min(Math.ceil(level / 7D), 3);
			artifact.addEnchantment(Enchantment.unbreaking, r2);
			artifact.stackTagCompound.setString("name", "Unbreaking " + artifact.stackTagCompound.getString("name"));
			return artifact;
		}
		//System.out.println(level + " levels to play with.");
		ItemStack stack = new ItemStack(item);
		stack = EnchantmentHelper.addRandomEnchantment(instance.rand, stack, level);
		if(stack.stackTagCompound != null) {
			artifact.stackTagCompound.setTag("ench", stack.stackTagCompound.getTag("ench").copy());
			NBTTagList tags = artifact.getEnchantmentTagList();
			int firstID = ((NBTTagCompound)tags.tagAt(0)).getShort("id");
			String enchName;
			switch(firstID) {
				case 0:
					enchName = "Protecting ";
					break;
				case 1:
					enchName = "Fireproof ";
					break;
				case 2:
					enchName = "Floating ";
					break;
				case 3:
					enchName = "Blast Protecting ";
					break;
				case 4:
					enchName = "Anti-Arrow ";
					break;
				case 5:
					enchName = "Breathing ";
					break;
				case 6:
					enchName = "Underwater ";
					break;
				case 7:
					enchName = "Thorny ";
					break;
				case 16:
					enchName = "Sharp ";
					break;
				case 17:
					enchName = "Smiting ";
					break;
				case 18:
					enchName = "Spider-Crushing ";
					break;
				case 19:
					enchName = "Forcefull ";
					break;
				case 20:
					enchName = "Firey";
					break;
				case 21:
					enchName = "Looting ";
					break;
				case 32:
					enchName = "Efficient ";
					break;
				case 33:
					enchName = "Gentle ";
					break;
				case 34:
					enchName = "Unbreaking ";
					break;
				case 35:
					enchName = "Fortuitous ";
					break;
				default:
					enchName = "";
			}
			artifact.stackTagCompound.setString("enchName", enchName);
			artifact.stackTagCompound.setString("name", enchName + artifact.stackTagCompound.getString("name"));
		}
		return artifact;
	}
}
