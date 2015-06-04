package com.draco18s.artifacts.item;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.ArtifactsAPI;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.factory.FactoryItemIcons;
import com.draco18s.artifacts.network.CToSMessage;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemArtifact extends Item implements IBauble {
    public static Item instance;
	private float weaponDamage;
    //private HashMap icons = new HashMap();
    
    public static boolean doEnchName = true;
    public static boolean doMatName = true;
    public static boolean doAdjName = true;
    
	public ItemArtifact() {
		super();
		this.setHasSubtypes(true);
		setMaxStackSize(1);
		weaponDamage = 0.0F;
		setMaxDamage(128);
		setCreativeTab(DragonArtifacts.tabArtifacts);
	}
	
	/**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
    	list.add(new ItemStack(instance));
        for (int i = 0; i < 8; ++i)
        {
            list.add(ArtifactsAPI.artifacts.generateRandomArtifact());
        }
    }
    
    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D()
    {
    	ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
    	
    	//Render in 3D if the held artifact is a sword, dagger, staff, or wand.
    	if(itemStack != null && itemStack.getItem() instanceof ItemArtifact && itemStack.stackTagCompound != null) {
	    	String type = itemStack.getTagCompound().getString("iconName");
	        return type.equals("Sword") || type.equals("Dagger") || type.equals("Staff") || type.equals("Wand");
    	}

    	return false;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IIconRegister iconReg)
    {
		itemIcon = ArtifactsAPI.itemicons.registerIcons(iconReg);
    }
	
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
    {
		IIcon i = itemIcon;
		if(pass == 0) {
			if(stack.stackTagCompound == null) {
				return itemIcon;
			}
			i = (IIcon) ArtifactsAPI.itemicons.icons.get(stack.stackTagCompound.getString("icon").toLowerCase());
			if(i == null) {
				i = itemIcon;
			}
		}
		else {
			if(stack.stackTagCompound == null) {
				return (IIcon) ArtifactsAPI.itemicons.icons.get("overlay_artifact1");
			}
			i = (IIcon) ArtifactsAPI.itemicons.icons.get("overlay_"+stack.stackTagCompound.getString("icon").toLowerCase());
			if(i == null) {
				i = (IIcon) ArtifactsAPI.itemicons.icons.get("overlay_artifact1");
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
	public int getMaxDamage(ItemStack stack)
    {
		float base = 1;
		if(stack.stackTagCompound != null) {
			base = (stack.stackTagCompound.getInteger("material") / 2);
			if(base == 2) {
				base += 5;
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
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
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
    
	@Override
	public float getDigSpeed(ItemStack itemStack, Block block, int meta)
    {
		NBTTagCompound data = itemStack.getTagCompound();
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
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(ca[i]);
					if(c != null)
						y = c.getDigSpeed(itemStack, block, meta);
					if(y > r)
						r = y;
				}
			}
			//System.out.println("str: " + r);
			if(r > 0)
				return r;
		}
		//System.out.println("No strength");
        return 0.25F;// * EnumToolMaterial.values()[par1ItemStack.stackTagCompound.getInteger("material")].getEfficiencyOnProperMaterial();
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
		NBTTagCompound data = itemStack.getTagCompound();
		int effectID = 0;
		if(data != null && world.isRemote) {
			effectID = data.getInteger("onItemRightClick");
			if(effectID != 0) {
				//System.out.println("Activating...");
				int d = data.getInteger("onItemRightClickDelay");
				//System.out.println("R-click: " + d);
				if(d <= 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					if(c != null)
						itemStack = c.onItemRightClick(itemStack, world, player);
					d = itemStack.stackTagCompound.getInteger("onItemRightClickDelay");
					
					PacketBuffer out = new PacketBuffer(Unpooled.buffer());
					out.writeInt(4096);
					out.writeInt(d);
					out.writeInt(player.inventory.currentItem);
					CToSMessage packet = new CToSMessage(out);
					DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
				}
			}
		}
        return itemStack;
    }
	
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityVictim, EntityLivingBase entityAttacker)
    {
		NBTTagCompound data = itemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			//System.out.println("Hitting...");
			effectID = data.getInteger("hitEntity");
			if(effectID != 0) {
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				boolean r = false;
				if(c != null)
					r = c.hitEntity(itemStack, entityVictim, entityAttacker);
				itemStack.damageItem(1, entityVictim);
				return r;
			}
			NBTTagList tagList = data.getTagList("AttributeModifiers", 10);
			for(int i = 0; i < tagList.tagCount(); i++) {
				if(tagList.getCompoundTagAt(i).getString("AttributeName").equals("generic.attackDamage")) {
					itemStack.damageItem(1, entityVictim);
					break; //break out of for loop.
				}
			}
		}
		return false;
    }

	@Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entityLivingBase)
    {
    	NBTTagCompound data = itemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			if(!world.isRemote) {
				effectID = data.getInteger("onBlockDestroyed");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					if(c != null)
						return c.onBlockDestroyed(itemStack, world, block, x, y, z, entityLivingBase);
				}
				effectID = data.getInteger("onDig");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					if(c != null)
						return c.onBlockDestroyed(itemStack, world, block, x, y, z, entityLivingBase);
				}
				else {
					if(canHarvestBlock(block, itemStack)) {
						itemStack.damageItem(1, entityLivingBase);
					}
					else {
						itemStack.damageItem(2, entityLivingBase);
					}
				}
			}
		}
        return false;
    }
	
	@Override
	public boolean canHarvestBlock(Block block, ItemStack itemStack) {
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
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(ca[i]);
					if(c != null)
						r = r || c.canHarvestBlock(block, itemStack);
				}
			}
		}
		return false;
    }
    
	@Override
    public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase)
    {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			effectID = data.getInteger("itemInteractionForEntity");
			if(effectID != 0) {
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
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
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					if(c != null)
						c.onEntityItemUpdate(entityItem,"onUpdate");
				}
				effectID = data.getInteger("onEntityItemUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					if(c != null)
						c.onEntityItemUpdate(entityItem,"onEntityItemUpdate");
				}
				effectID = data.getInteger("onDropped");
				if(effectID != 0) {
					int del = data.getInteger("droppedDelay");
					//System.out.println("Dropped: " + del + "<=" + entityItem.age);
					if(del <= entityItem.age) {
						IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
						if(c != null)
							c.onEntityItemUpdate(entityItem,"onDropped");
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
		}
		return false;
	}
    
	@Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean held) {
    	NBTTagCompound data = itemStack.stackTagCompound;
		int effectID = 0;
		if(data != null) {
			if(!world.isRemote) {
	        	if(itemStack.stackTagCompound.getString("matName").length() <= 0) {
	        		itemStack.stackTagCompound = null;//ArtifactsAPI.artifacts.applyRandomEffects(par1ItemStack.copy()).stackTagCompound;
	        		return;
	        	}
				effectID = data.getInteger("onUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					if(c != null)
						c.onUpdate(itemStack, world, entity, slot, held);
				}
				effectID = data.getInteger("onHeld");
				if(effectID != 0 && held) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					if(c != null)
						c.onHeld(itemStack, world, entity, slot, held);
				}
				//
			/*}
			else {*/
				int d = data.getInteger("onItemRightClickDelay");
				if(d > 0)
					d--;
				else
					d = 0;
				data.setInteger("onItemRightClickDelay",d);
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
		}
		else if(!world.isRemote) {
			itemStack = ArtifactsAPI.artifacts.applyRandomEffects(itemStack);
		}
	}
    
	@Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.none;
    }
    
	@Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
    	
    }
    
	@Override
    public Multimap getAttributeModifiers(ItemStack itemStack)
    {
    	Multimap multimap = super.getAttributeModifiers(itemStack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.weaponDamage, 0));
        return multimap;
    }
    
	@Override
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
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when used.", advTooltip);
			}
			effectID = data.getInteger("hitEntity");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when inflicting damage.", advTooltip);
			}
			effectID = data.getInteger("onUpdate");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "passively.", advTooltip);
			}
			effectID = data.getInteger("onEntityItemUpdate");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "passively.", advTooltip);
			}
			effectID = data.getInteger("onBlockDestroyed");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when destroying blocks.", advTooltip);
			}
			effectID = data.getInteger("itemInteractionForEntity");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when interacting with entities.", advTooltip);
			}
			effectID = data.getInteger("onDig");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, advTooltip);
			}
			effectID = data.getInteger("onDroppedByPlayer");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when dropped.", advTooltip);
			}
			effectID = data.getInteger("onDropped");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when dropped.", advTooltip);
			}
			effectID = data.getInteger("onHeld");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				if(c != null)
					c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when held.", advTooltip);
			}
		}
		else {
			par3List.add(StatCollector.translateToLocal("tool.All tools, swords, etc."));
		}
    }

	@Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
    	String n = "";
    	if(itemStack.stackTagCompound != null) {
    		if(doEnchName) {
    			if(itemStack.stackTagCompound.getString("enchName").length() > 0)
					n += StatCollector.translateToLocal(itemStack.stackTagCompound.getString("enchName")) + " ";
			}
			if(doAdjName) {
				if(itemStack.stackTagCompound.getString("preadj").length() > 0)
					n += StatCollector.translateToLocal("pre."+itemStack.stackTagCompound.getString("preadj")) + " ";
			}
			if(doMatName) {
				n += StatCollector.translateToLocal("mat."+itemStack.stackTagCompound.getString("matName")) + " ";
			}
			if(!(doEnchName || doMatName || doAdjName)) {
				n += StatCollector.translateToLocal("type.Artifact") + " ";
			}
			n += StatCollector.translateToLocal("type."+itemStack.stackTagCompound.getString("iconName"));
			if(doAdjName) {
				if(itemStack.stackTagCompound.getString("postadj").length() > 0)
					n += " " + StatCollector.translateToLocal("post."+itemStack.stackTagCompound.getString("postadj"));
			}
    	}
		if(n.length() < 1) {
			n = StatCollector.translateToLocal("type.Artifact");
		}
        return n;//("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim();
    }
    
    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
    	if(par1ItemStack.getItem() == par2ItemStack.getItem()) {
    		return false;
    	}
    	else if (par2ItemStack.getItem() instanceof ItemOrichalcumDust) {
    		if(par2ItemStack.getItemDamage()-1 == par1ItemStack.stackTagCompound.getInteger("material"))
    			return true;
    		return false;
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
    public int getHarvestLevel(ItemStack itemStack, String toolClass)
    {
    	NBTTagCompound data = itemStack.getTagCompound();
		int effectID = 0;
		int r = -1;
		if(data != null) {
			int ca[] = data.getIntArray("allComponents");
			
			//Loop through the components and find the one with the highest harvest level.
			for(int i=ca.length-1; i >= 0; i--) {
				if(ca[i] != 0) {
					int temp = 0;
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(ca[i]);
					if(c != null)
						temp = c.getHarvestLevel(itemStack, toolClass);
					if(temp > r) {
						r = temp;
					}
				}
			}
		}
		
		//Return the highest harvest level.
		//System.out.println("Tool Type is: '"+toolClass+"', and Harvest Level is " + r);
		return r;
    }

	@Override
	public BaubleType getBaubleType(ItemStack itemStack) {
		if(itemStack.stackTagCompound != null) 
		{
			String type = itemStack.stackTagCompound.getString("iconName");
			
			if(type.equals("Ring")) 
				return BaubleType.RING;
			if(type.equals("Amulet"))
				return BaubleType.AMULET;
			if(type.equals("Belt"))
				return BaubleType.BELT;
		}
		
		return null;
	}

	@Override
	public void onWornTick(ItemStack itemStack, EntityLivingBase player) {
		NBTTagCompound data = itemStack.stackTagCompound;
		int effectID = 0;
		if(data != null) {
			if(!player.worldObj.isRemote) {
	        	if(itemStack.stackTagCompound.getString("matName").length() <= 0) {
	        		itemStack.stackTagCompound = null;//ArtifactsAPI.artifacts.applyRandomEffects(par1ItemStack.copy()).stackTagCompound;
	        		return;
	        	}
				effectID = data.getInteger("onUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					if(c != null)
						c.onUpdate(itemStack, player.worldObj, player, -1, false);
				}
				//
			/*}
			else {*/
				int d = data.getInteger("onItemRightClickDelay");
				if(d > 0)
					d--;
				else
					d = 0;
				data.setInteger("onItemRightClickDelay",d);
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
		}
		else if(!player.worldObj.isRemote) {
			itemStack = ArtifactsAPI.artifacts.applyRandomEffects(itemStack);
		}
	}

	@Override
	public void onEquipped(ItemStack itemStack, EntityLivingBase player) {
		
	}

	@Override
	public void onUnequipped(ItemStack itemStack, EntityLivingBase player) {
		
	}

	@Override
	public boolean canEquip(ItemStack itemStack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack itemStack, EntityLivingBase player) {
		return true;
	}
}
