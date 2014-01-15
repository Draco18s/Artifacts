package draco18s.artifacts.item;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import draco18s.artifacts.api.ArtifactsAPI;
import draco18s.artifacts.api.interfaces.IArtifactComponent;
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
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemArtifactArmor extends ItemArmor {
	public static ItemArtifactArmor hcloth;
	public static ItemArtifactArmor hchain;
	public static ItemArtifactArmor hiron;
	public static ItemArtifactArmor hgold;
	public static ItemArtifactArmor hdiamond;
	public static ItemArtifactArmor ccloth;
	public static ItemArtifactArmor cchain;
	public static ItemArtifactArmor ciron;
	public static ItemArtifactArmor cgold;
	public static ItemArtifactArmor cdiamond;
	public static ItemArtifactArmor lcloth;
	public static ItemArtifactArmor lchain;
	public static ItemArtifactArmor liron;
	public static ItemArtifactArmor lgold;
	public static ItemArtifactArmor ldiamond;
	public static ItemArtifactArmor bcloth;
	public static ItemArtifactArmor bchain;
	public static ItemArtifactArmor biron;
	public static ItemArtifactArmor bgold;
	public static ItemArtifactArmor bdiamond;
    
    public static boolean doEnchName = true;
    public static boolean doMatName = true;
    public static boolean doAdjName = true;
    private int iconn;
    
    public static Item[] clothArray;// = {hcloth, ccloth, lcloth, bcloth};
    public static Item[] chainArray;// = {hchain, cchain, lchain, bchain};
    public static Item[] ironArray;// = {hiron, ciron, liron, biron};
    public static Item[] goldArray;// = {hgold, cgold, lgold, bgold};
    public static Item[] diamondArray;// = {hdiamond, cdiamond, ldiamond, bdiamond};

	public ItemArtifactArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int renderID, int iconNum, int damageIndex) {
		super(par1, par2EnumArmorMaterial, renderID, damageIndex);
		setUnlocalizedName("artifact.armor"+par2EnumArmorMaterial.name());
		iconn = iconNum;
		if(iconNum != 2) {
	        this.setCreativeTab(null);
		}
		this.setMaxDamage(par2EnumArmorMaterial.getDurability(damageIndex)*2);
	}
	
	public static void setupArrays() {
		clothArray = new Item[]{hcloth, ccloth, lcloth, bcloth};
	    chainArray = new Item[]{hchain, cchain, lchain, bchain};
	    ironArray = new Item[]{hiron, ciron, liron, biron};
	    goldArray = new Item[]{hgold, cgold, lgold, bgold};
	    diamondArray = new Item[]{hdiamond, cdiamond, ldiamond, bdiamond};
	}
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconReg)
    {
		itemIcon = iconReg.registerIcon("artifacts:artifact"+iconn);
    }
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		//slot will tell us helmet vs. boots
		//type will be either null or overlay (cloth armor)
		//can use stack.stackTagCompound.getString("matName") for material, etc.
		//return "artifacts:textures/armor/Models/artifact_layer1.png";
		String layer = "1";
		String material = stack.stackTagCompound.getString("matName").toLowerCase();
		if(type == null) {
			type = "";
			material = "iron";
		}
		if(slot == 2) {
			layer="2";
		}
		return "artifacts:textures/models/armor/"+material+"_layer_"+layer+type+".png";
		//return super.getArmorTexture(stack, entity, slot, type);
	}
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	public Icon getIcon(ItemStack stack, int pass)
    {
		Icon i = itemIcon;
		if(pass == 0) {
			if(stack.stackTagCompound == null) {
				return itemIcon;
			}
			i = (Icon) ArtifactsAPI.itemicons.icons.get(stack.stackTagCompound.getString("icon").toLowerCase());
			if(i == null) {
				i = itemIcon;
			}
		}
		else {
			if(stack.stackTagCompound == null) {
				return (Icon) ArtifactsAPI.itemicons.icons.get("overlay_artifact1");
			}
			i = (Icon) ArtifactsAPI.itemicons.icons.get("overlay_"+stack.stackTagCompound.getString("icon").toLowerCase());
			if(i == null) {
				i = (Icon) ArtifactsAPI.itemicons.icons.get("overlay_artifact1");
			}
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
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		NBTTagCompound data = item.getTagCompound();
		int effectID = 0;
		if(data != null) {
			effectID = data.getInteger("onDroppedByPlayer");
			if(effectID != 0) {
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				return c.onDroppedByPlayer(item, player);
			}
			effectID = data.getInteger("onArmorTickUpdate");
			if(effectID != 0) {
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				return c.onDroppedByPlayer(item, player);
			}
			effectID = data.getInteger("onArmorTickUpdate2");
			if(effectID != 0) {
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				return c.onDroppedByPlayer(item, player);
			}
		}
		return true;
	}
	
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
    {
		NBTTagCompound data = itemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			if(!world.isRemote) {
				IArtifactComponent c;
				effectID = data.getInteger("onArmorTickUpdate");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(world, player, itemStack, true);
				}
				effectID = data.getInteger("onArmorTickUpdate2");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(world, player, itemStack, true);
				}
				effectID = data.getInteger("onTakeDamage");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(world, player, itemStack, true);
				}
				effectID = data.getInteger("onDeath");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(world, player, itemStack, true);
				}
			}
			ArrayList<String> keys = ArtifactsAPI.artifacts.getNBTKeys();
			String kk = "";
			int n = 0;
			for(int k = keys.size() - 1; k >= 0; k--) {
				kk = keys.get(k)+"_armor";
				if(data.hasKey(kk)) {
					n = data.getInteger(kk);
					if(n > 0)
						n--;
					data.setInteger(kk,n);
				}
			}
		}
		else if(!world.isRemote) {
			itemStack = ArtifactsAPI.artifacts.applyRandomEffects(itemStack);
		}
    }
	
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        return false;
    }
    
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
		return 0.25F;
    }
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
    }
	
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
		return false;
    }

	@Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int blockID, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase)
    {
    	return false;
    }
    
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
    {
    	return false;
    }
    
    public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase)
    {
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
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onUpdate");
				}
				effectID = data.getInteger("onEntityItemUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onEntityItemUpdate");
				}
				effectID = data.getInteger("onDropped");
				if(effectID != 0) {
					int del = data.getInteger("droppedDelay");
					if(del <= entityItem.age) {
						IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
						c.onEntityItemUpdate(entityItem,"onDropped");
					}
				}
				effectID = data.getInteger("onArmorTickUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onUpdate");
				}
				effectID = data.getInteger("onArmorTickUpdate2");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onEntityItemUpdate(entityItem,"onUpdate");
				}
			}
			ArrayList<String> keys = ArtifactsAPI.artifacts.getNBTKeys();
			String kk = "";
			int n = 0;
			for(int k = keys.size() - 1; k >= 0; k--) {
				kk = keys.get(k)+"_dropped";
				if(data.hasKey(kk)) {
					n = data.getInteger(kk);
					if(n > 0)
						n--;
					data.setInteger(kk,n);
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
				IArtifactComponent c;
				effectID = data.getInteger("onUpdate");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
				}
				effectID = data.getInteger("onArmorTickUpdate");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(par2World, (EntityPlayer)par3Entity, par1ItemStack, false);
				}
				effectID = data.getInteger("onArmorTickUpdate2");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(par2World, (EntityPlayer)par3Entity, par1ItemStack, false);
				}
				effectID = data.getInteger("onTakeDamage");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(par2World, (EntityPlayer)par3Entity, par1ItemStack, false);
				}
				effectID = data.getInteger("onDeath");
				if(effectID != 0) {
					c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onArmorTickUpdate(par2World, (EntityPlayer)par3Entity, par1ItemStack, false);
				}
			}
			ArrayList<String> keys = ArtifactsAPI.artifacts.getNBTKeys();
			String kk = "";
			int n = 0;
			for(int k = keys.size() - 1; k >= 0; k--) {
				kk = keys.get(k);
				if(data.hasKey(kk)) {
					n = data.getInteger(kk);
					if(n > 0)
						n--;
					data.setInteger(kk,n);
				}
			}
		}
		else if(!par2World.isRemote) {
			par1ItemStack = ArtifactsAPI.artifacts.applyRandomEffects(par1ItemStack);
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
    	return super.getItemAttributeModifiers();
    }
    
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			IArtifactComponent c;
			
			effectID = data.getInteger("onArmorTickUpdate");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when equipped.", advTooltip);
			}
			effectID = data.getInteger("onArmorTickUpdate2");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when equipped.", advTooltip);
			}
			effectID = data.getInteger("onUpdate");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "passively.", advTooltip);
			}
			effectID = data.getInteger("onTakeDamage");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "after taking damage.", advTooltip);
			}
			effectID = data.getInteger("onDeath");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "after taking lethal damage.", advTooltip);
			}
			effectID = data.getInteger("onHeld");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when held.", advTooltip);
			}
		}
		else {
			par3List.add(StatCollector.translateToLocal("tool.Helms, boots, etc."));
		}
    }

    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	String n = "";
    	if(par1ItemStack.stackTagCompound != null) {
    		if(doEnchName) {
    			if(par1ItemStack.stackTagCompound.getString("enchName").length() > 0)
					n += StatCollector.translateToLocal(par1ItemStack.stackTagCompound.getString("enchName")) + " ";
			}
			if(doAdjName) {
				if(par1ItemStack.stackTagCompound.getString("preadj").length() > 0)
					n += StatCollector.translateToLocal("pre."+par1ItemStack.stackTagCompound.getString("preadj")) + " ";
			}
			if(doMatName) {
				n += StatCollector.translateToLocal("mat."+par1ItemStack.stackTagCompound.getString("matName")) + " ";
			}
			if(!(doEnchName || doMatName || doAdjName)) {
				n += StatCollector.translateToLocal("type.Artifact") + " ";
			}
			n += StatCollector.translateToLocal("type."+par1ItemStack.stackTagCompound.getString("iconName"));
			if(doAdjName) {
				if(par1ItemStack.stackTagCompound.getString("postadj").length() > 0)
					n += " " + StatCollector.translateToLocal("post."+par1ItemStack.stackTagCompound.getString("postadj"));
			}
    	}
		if(n.length() < 1) {
			n = (StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim();
		}
        return n;
    }
    
    @Override
    public int getColor(ItemStack par1ItemStack)
    {
		if(par1ItemStack.stackTagCompound != null) {
			return (int) par1ItemStack.stackTagCompound.getLong("overlay_color");	
		}
		return 16777215;
    }
    
    @Override
    public boolean hasColor(ItemStack par1ItemStack)
    {
        return true;
    }
    
    public void func_82813_b(ItemStack par1ItemStack, int par2)
    {
    	par1ItemStack.stackTagCompound.setLong("overlay_color", par2);
    	super.func_82813_b(par1ItemStack, par2);
    }
    
    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
    	if(par1ItemStack.itemID == par2ItemStack.itemID) {
    		return false;
    	}
    	else if (par2ItemStack.getItem() instanceof ItemOrichalcumDust) {
    		int dam = par2ItemStack.getItemDamage();//kit
    		--dam;
    		int mat = par1ItemStack.stackTagCompound.getInteger("material"); //armor
    		if(mat == 1)
    			mat = 2;
    		if(mat == 0)
    			mat = 5;
    		if(dam == mat)
    			return true;
    		return false;
    	}
    	else {
    		return false;
    	}
    }
}
