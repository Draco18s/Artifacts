package draco18s.artifacts.item;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import draco18s.artifacts.components.IArtifactComponent;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemArtifact extends Item {
    public static Item instance;
	private float weaponDamage;
    private HashMap icons = new HashMap();
    
    public static boolean doEnchName = true;
    public static boolean doMatName = true;
    public static boolean doAdjName = true;
    
	public ItemArtifact(int par1) {
		super(par1);
		setMaxStackSize(1);
		setNoRepair();
		weaponDamage = 0.0F;
		setMaxDamage(128);
		setCreativeTab(CreativeTabs.tabCombat);
	}
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconReg)
    {
        itemIcon = iconReg.registerIcon("artifacts:artifact1");
        icons.put("artifact1",itemIcon);
        icons.put("amulet1",iconReg.registerIcon("artifacts:amulet1"));
        icons.put("amulet2",iconReg.registerIcon("artifacts:amulet2"));
        icons.put("amulet3",iconReg.registerIcon("artifacts:amulet3"));
        icons.put("amulet4",iconReg.registerIcon("artifacts:amulet4"));
        icons.put("dagger1",iconReg.registerIcon("artifacts:dagger1"));
        icons.put("dagger2",iconReg.registerIcon("artifacts:dagger2"));
        icons.put("dagger3",iconReg.registerIcon("artifacts:dagger3"));
        icons.put("dagger4",iconReg.registerIcon("artifacts:dagger4"));
        icons.put("figurine1",iconReg.registerIcon("artifacts:figurine1"));
        icons.put("figurine2",iconReg.registerIcon("artifacts:figurine2"));
        icons.put("ring1",iconReg.registerIcon("artifacts:ring1"));
        icons.put("ring2",iconReg.registerIcon("artifacts:ring2"));
        icons.put("ring3",iconReg.registerIcon("artifacts:ring3"));
        icons.put("ring4",iconReg.registerIcon("artifacts:ring4"));
        icons.put("ring5",iconReg.registerIcon("artifacts:ring5"));
        icons.put("ring6",iconReg.registerIcon("artifacts:ring6"));
        icons.put("ring7",iconReg.registerIcon("artifacts:ring7"));
        icons.put("staff1",iconReg.registerIcon("artifacts:staff1"));
        icons.put("staff2",iconReg.registerIcon("artifacts:staff2"));
        icons.put("staff3",iconReg.registerIcon("artifacts:staff3"));
        icons.put("staff4",iconReg.registerIcon("artifacts:staff4"));
        icons.put("sword1",iconReg.registerIcon("artifacts:sword1"));
        icons.put("sword2",iconReg.registerIcon("artifacts:sword2"));
        icons.put("sword3",iconReg.registerIcon("artifacts:sword3"));
        icons.put("trinket1",iconReg.registerIcon("artifacts:trinket1"));
        icons.put("trinket2",iconReg.registerIcon("artifacts:trinket2"));
        icons.put("trinket3",iconReg.registerIcon("artifacts:trinket3"));
        icons.put("trinket4",iconReg.registerIcon("artifacts:trinket4"));
        icons.put("trinket5",iconReg.registerIcon("artifacts:trinket5"));
        icons.put("trinket6",iconReg.registerIcon("artifacts:trinket6"));
        icons.put("trinket7",iconReg.registerIcon("artifacts:trinket7"));
        icons.put("wand1",iconReg.registerIcon("artifacts:wand1"));
        icons.put("wand2",iconReg.registerIcon("artifacts:wand2"));
        icons.put("wand3",iconReg.registerIcon("artifacts:wand3"));
        icons.put("wand4",iconReg.registerIcon("artifacts:wand4"));
        icons.put("wand5",iconReg.registerIcon("artifacts:wand5"));

        icons.put("boots1",iconReg.registerIcon("artifacts:boots1"));
        icons.put("chestplate1",iconReg.registerIcon("artifacts:chestplate1"));
        icons.put("helm1",iconReg.registerIcon("artifacts:helm1"));
        icons.put("leggings1",iconReg.registerIcon("artifacts:leggings1"));

        icons.put("overlay_artifact1",iconReg.registerIcon("artifacts:blank_overlay"));
        icons.put("overlay_amulet1",iconReg.registerIcon("artifacts:amulet1_overlay"));
        icons.put("overlay_amulet2",iconReg.registerIcon("artifacts:amulet2_overlay"));
        icons.put("overlay_amulet3",iconReg.registerIcon("artifacts:amulet3_overlay"));
        icons.put("overlay_amulet4",iconReg.registerIcon("artifacts:amulet4_overlay"));
        icons.put("overlay_dagger1",iconReg.registerIcon("artifacts:dagger1_overlay"));
        icons.put("overlay_dagger2",iconReg.registerIcon("artifacts:dagger2_overlay"));
        icons.put("overlay_dagger3",iconReg.registerIcon("artifacts:dagger3_overlay"));
        icons.put("overlay_dagger4",iconReg.registerIcon("artifacts:dagger4_overlay"));
        icons.put("overlay_figurine2",iconReg.registerIcon("artifacts:figurine2_overlay"));
        icons.put("overlay_ring1",iconReg.registerIcon("artifacts:ring1_overlay"));
        icons.put("overlay_ring2",iconReg.registerIcon("artifacts:ring2_overlay"));
        icons.put("overlay_ring3",iconReg.registerIcon("artifacts:ring3_overlay"));
        icons.put("overlay_ring4",iconReg.registerIcon("artifacts:ring4_overlay"));
        icons.put("overlay_ring5",iconReg.registerIcon("artifacts:ring5_overlay"));
        icons.put("overlay_ring6",iconReg.registerIcon("artifacts:ring6_overlay"));
        icons.put("overlay_staff1",iconReg.registerIcon("artifacts:staff1_overlay"));
        icons.put("overlay_staff2",iconReg.registerIcon("artifacts:staff2_overlay"));
        icons.put("overlay_staff4",iconReg.registerIcon("artifacts:staff4_overlay"));
        icons.put("overlay_sword1",iconReg.registerIcon("artifacts:sword1_overlay"));
        icons.put("overlay_sword2",iconReg.registerIcon("artifacts:sword2_overlay"));
        icons.put("overlay_sword3",iconReg.registerIcon("artifacts:sword3_overlay"));
        icons.put("overlay_trinket1",iconReg.registerIcon("artifacts:trinket1_overlay"));
        icons.put("overlay_trinket2",iconReg.registerIcon("artifacts:trinket2_overlay"));
        icons.put("overlay_trinket6",iconReg.registerIcon("artifacts:trinket6_overlay"));
        icons.put("overlay_trinket7",iconReg.registerIcon("artifacts:trinket7_overlay"));
        icons.put("overlay_wand1",iconReg.registerIcon("artifacts:wand1_overlay"));
        icons.put("overlay_wand2",iconReg.registerIcon("artifacts:wand2_overlay"));
        icons.put("overlay_wand3",iconReg.registerIcon("artifacts:wand3_overlay"));
        icons.put("overlay_wand4",iconReg.registerIcon("artifacts:wand4_overlay"));
        icons.put("overlay_wand5",iconReg.registerIcon("artifacts:wand5_overlay"));
    }
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	public Icon getIcon(ItemStack stack, int pass)
    {
		Icon i = itemIcon;
		//System.out.println("pass: " + pass);
		if(pass == 0) {
			if(stack.stackTagCompound == null) {
				return itemIcon;
			}
			i = (Icon) icons.get(stack.stackTagCompound.getString("icon").toLowerCase());
			if(i == null) {
				i = itemIcon;
			}
			//System.out.println("base icon: " + i);
		}
		else {
			if(stack.stackTagCompound == null) {
				return (Icon) icons.get("overlay_artifact1");
			}
			i = (Icon) icons.get("overlay_"+stack.stackTagCompound.getString("icon").toLowerCase());
			if(i == null) {
				i = (Icon) icons.get("overlay_artifact1");
			}
			//System.out.println("overlay icon: " + i);
		}
		return i;
    }
	
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int pass)
    {
		if(pass == 0)
			return 16777215;
		else {
			if(par1ItemStack.stackTagCompound != null) {
				return (int) par1ItemStack.stackTagCompound.getLong("overlay_color");	
			}
			return 255;
		}
    }
	
	@Override
	public int getMaxDamage(ItemStack stack)
    {
		float base = 1;
		if(stack.stackTagCompound != null) {
			//hope this works
			//stack = FactoryArtifact.applyRandomEffects(stack);	
			base = (stack.stackTagCompound.getInteger("material") / 2);
			if(base == 2) {
				base += 3;
			}
			else {
				base += 7.5;
			}		
		}
		return (int) (Math.pow(2, base)-1);
    }

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		NBTTagCompound data = item.getTagCompound();
		int effectID = 0;
		if(data != null) {
			effectID = data.getInteger("onDroppedByPlayer");
			if(effectID != 0) {
				IArtifactComponent c = FactoryArtifact.getComponent(effectID);
				return c.onDroppedByPlayer(item, player);
			}
		}
		return true;
	}
	
	public boolean isValidArmor(ItemStack stack, int armorType, Entity entity)
    {
        return armorType == stack.stackTagCompound.getInteger("armorType");
    }
	
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
		onUpdate(itemStack, world, player, 0, false);
    }
	
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        return false;
    }
    
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
		NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		float s;
		if(data != null) {
			/*effectID = data.getInteger("onBlockDestroyed");
			if(effectID != 0) {
				ArtifactComponent c = Artifact.getComponent(effectID);
				s = c.getStrVsBlock(par1ItemStack, par2Block);
				if(s > 0)
					return s;
			}
			if(effectID != 0) {
				ArtifactComponent c = Artifact.getComponent(effectID);
				s = c.getStrVsBlock(par1ItemStack, par2Block);
				if(s > 0)
					return s;
			}*/
			int ca[] = data.getIntArray("allComponents");
			float r = 0, y=0;
			for(int i=ca.length-1; i >= 0; i--) {
				if(ca[i] != 0) {
					IArtifactComponent c = FactoryArtifact.getComponent(ca[i]);
					y = c.getStrVsBlock(par1ItemStack, par2Block);
					if(y > r)
						r = y;
				}
			}
			if(r > 0)
				return r;
		}
        return 0.25F * EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial();
    }
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null && par2World.isRemote) {
			effectID = data.getInteger("onItemRightClick");
			if(effectID != 0) {
				//System.out.println("Activating...");
				IArtifactComponent c = FactoryArtifact.getComponent(effectID);
				return c.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
			}
		}
        return par1ItemStack;
    }
	
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
		NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			//System.out.println("Hitting...");
			effectID = data.getInteger("hitEntity");
			if(effectID != 0) {
				IArtifactComponent c = FactoryArtifact.getComponent(effectID);
				boolean r = c.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
				par1ItemStack.damageItem(1, par2EntityLivingBase);
				return r;
			}
		}
		return false;
    }

    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int blockID, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase)
    {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			if(!par2World.isRemote) {
				effectID = data.getInteger("onBlockDestroyed");
				if(effectID != 0) {
					IArtifactComponent c = FactoryArtifact.getComponent(effectID);
					return c.onBlockDestroyed(par1ItemStack, par2World, blockID, par4, par5, par6, par7EntityLivingBase);
				}
				else {
					if(canHarvestBlock(Block.blocksList[blockID], par1ItemStack)) {
						par1ItemStack.damageItem(1, par7EntityLivingBase);
					}
					else {
						par1ItemStack.damageItem(2, par7EntityLivingBase);
					}
				}
			}
		}
        return false;
    }
    
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
    {
    	NBTTagCompound data = itemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			/*effectID = data.getInteger("onBlockDestroyed");
			if(effectID != 0) {
				ArtifactComponent c = Artifact.getComponent(effectID);
				return c.canHarvestBlock(par1Block, itemStack);
			}
			effectID = data.getInteger("canHarvestBlock");
			if(effectID != 0) {
				ArtifactComponent c = Artifact.getComponent(effectID);
				return c.canHarvestBlock(par1Block, itemStack);
			}*/
			int ca[] = data.getIntArray("allComponents");
			boolean r = false;
			for(int i=ca.length-1; i >= 0; i--) {
				if(ca[i] != 0) {
					IArtifactComponent c = FactoryArtifact.getComponent(ca[i]);
					r = r||c.canHarvestBlock(par1Block, itemStack);
				}
			}
		}
		return false;
    }
    
    public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase)
    {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			effectID = data.getInteger("itemInteractionForEntity");
			if(effectID != 0) {
				IArtifactComponent c = FactoryArtifact.getComponent(effectID);
				return c.itemInteractionForEntity(par1ItemStack, par2EntityPlayer, par3EntityLivingBase);
			}
		}
        return false;
    }

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		ItemStack stack = entityItem.getEntityItem();
		NBTTagCompound data = stack.stackTagCompound;
		World par2World = entityItem.worldObj; 
		int effectID = 0;
		if(data != null && entityItem.age % 15 == 0) {
			if(!par2World.isRemote) {
				effectID = data.getInteger("onUpdate");
				if(effectID != 0) {
					IArtifactComponent c = FactoryArtifact.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onUpdate");
				}
				effectID = data.getInteger("onEntityItemUpdate");
				if(effectID != 0) {
					IArtifactComponent c = FactoryArtifact.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onEntityItemUpdate");
				}
				effectID = data.getInteger("onDropped");
				if(effectID != 0) {
					int del = data.getInteger("droppedDelay");
					if(del <= entityItem.age) {
						IArtifactComponent c = FactoryArtifact.getComponent(effectID);
						c.onEntityItemUpdate(entityItem,"onDropped");
					}
				}
			}
		}
		return false;
	}
    
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			if(!par2World.isRemote) {
				effectID = data.getInteger("onUpdate");
				if(effectID != 0) {
					IArtifactComponent c = FactoryArtifact.getComponent(effectID);
					c.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
				}
				effectID = data.getInteger("onHeld");
				if(effectID != 0 && par5) {
					IArtifactComponent c = FactoryArtifact.getComponent(effectID);
					c.onHeld(par1ItemStack, par2World, par3Entity, par4, par5);
				}
			}
			else {
				int d = par1ItemStack.stackTagCompound.getInteger("onItemRightClickDelay");
				if(d > 0)
					d--;
				else
					d = 0;
				par1ItemStack.stackTagCompound.setInteger("onItemRightClickDelay",d);
			}
		}
		else if(!par2World.isRemote) {
			par1ItemStack = FactoryArtifact.applyRandomEffects(par1ItemStack);
		}
    }
    
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.none;
    }
    
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
    	
    }
    
    public Multimap getItemAttributeModifiers()
    {
    	Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.weaponDamage, 0));
        return multimap;
    }
    
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			//par1ItemStack = Artifact.applyRandomEffects(par1ItemStack);
			//data = par1ItemStack.getTagCompound();
		//}
			IArtifactComponent c;
			effectID = data.getInteger("onItemRightClick");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when used.", advTooltip);
			}
			effectID = data.getInteger("hitEntity");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when inflicting damage.", advTooltip);
			}
			effectID = data.getInteger("onUpdate");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "passively.", advTooltip);
			}
			effectID = data.getInteger("onEntityItemUpdate");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "passively.", advTooltip);
			}
			effectID = data.getInteger("onBlockDestroyed");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when destroying blocks.", advTooltip);
			}
			effectID = data.getInteger("itemInteractionForEntity");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when interacting with entities.", advTooltip);
			}
			effectID = data.getInteger("onDig");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, advTooltip);
			}
			effectID = data.getInteger("onDroppedByPlayer");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when dropped.", advTooltip);
			}
			effectID = data.getInteger("onDropped");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when dropped.", advTooltip);
			}
			effectID = data.getInteger("onHeld");
			if(effectID != 0) {
				c = FactoryArtifact.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when held.", advTooltip);
			}
		}
    }

    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	String n = "";
    	if(par1ItemStack.stackTagCompound != null) {
    		if(doEnchName && doMatName && doAdjName) {
	    		n = par1ItemStack.stackTagCompound.getString("name");
    		}
    		else {
    			if(doEnchName) {
    				n += par1ItemStack.stackTagCompound.getString("enchName") + " ";
    			}
    			if(doAdjName) {
    				n += par1ItemStack.stackTagCompound.getString("preadj") + " ";
    			}
    			if(doMatName) {
    				n += par1ItemStack.stackTagCompound.getString("matName") + " ";
    			}
    			if(!(doEnchName || doMatName || doAdjName)) {
    				n += "Artifact ";
    			}
    			n += par1ItemStack.stackTagCompound.getString("iconName");
    			if(doAdjName) {
    				n += " " + par1ItemStack.stackTagCompound.getString("postadj");
    			}
    		}
    	}
		if(n.length() < 1) {
			n = "Artifact";
		}
        return n;//("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim();
    }
}
