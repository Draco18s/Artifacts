package draco18s.artifacts.item;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.network.PacketDispatcher;
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
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemArtifact extends Item {
    public static Item instance;
	private float weaponDamage;
    //private HashMap icons = new HashMap();
    
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
		setUnlocalizedName("artifact");
	}
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconReg)
    {
		itemIcon = ArtifactsAPI.itemicons.registerIcons(iconReg);
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
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(ca[i]);
					y = c.getStrVsBlock(par1ItemStack, par2Block);
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
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null && par2World.isRemote) {
			effectID = data.getInteger("onItemRightClick");
			if(effectID != 0) {
				//System.out.println("Activating...");
				int d = data.getInteger("onItemRightClickDelay");
				System.out.println("R-click: " + d);
				if(d <= 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					par1ItemStack = c.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
					d = par1ItemStack.stackTagCompound.getInteger("onItemRightClickDelay");
					ByteArrayOutputStream bt = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(bt);
					try
					{
						out.writeInt(4096);
						out.writeInt(d);
						out.writeInt(par3EntityPlayer.inventory.currentItem);
						Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
						PacketDispatcher.sendPacketToServer(packet);
					}
					catch (IOException ex)
					{
						System.out.println("couldnt send packet!");
					}
				}
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
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
				boolean r = c.hitEntity(par1ItemStack, par2EntityLivingBase, par3EntityLivingBase);
				par1ItemStack.damageItem(1, par2EntityLivingBase);
				return r;
			}
		}
		return false;
    }

	@Override
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int blockID, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase)
    {
    	NBTTagCompound data = par1ItemStack.getTagCompound();
		int effectID = 0;
		if(data != null) {
			if(!par2World.isRemote) {
				effectID = data.getInteger("onBlockDestroyed");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					return c.onBlockDestroyed(par1ItemStack, par2World, blockID, par4, par5, par6, par7EntityLivingBase);
				}
				effectID = data.getInteger("onDig");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
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
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(ca[i]);
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
				IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
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
					//System.out.println("Dropped: " + del + "<=" + entityItem.age);
					if(del <= entityItem.age) {
						IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
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
    
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
    	NBTTagCompound data = par1ItemStack.stackTagCompound;
		int effectID = 0;
		if(data != null) {
			if(!par2World.isRemote) {
				effectID = data.getInteger("onUpdate");
				if(effectID != 0) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
				}
				effectID = data.getInteger("onHeld");
				if(effectID != 0 && par5) {
					IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
					c.onHeld(par1ItemStack, par2World, par3Entity, par4, par5);
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
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when used.", advTooltip);
			}
			effectID = data.getInteger("hitEntity");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when inflicting damage.", advTooltip);
			}
			effectID = data.getInteger("onUpdate");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "passively.", advTooltip);
			}
			effectID = data.getInteger("onEntityItemUpdate");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "passively.", advTooltip);
			}
			effectID = data.getInteger("onBlockDestroyed");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when destroying blocks.", advTooltip);
			}
			effectID = data.getInteger("itemInteractionForEntity");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when interacting with entities.", advTooltip);
			}
			effectID = data.getInteger("onDig");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, advTooltip);
			}
			effectID = data.getInteger("onDroppedByPlayer");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when dropped.", advTooltip);
			}
			effectID = data.getInteger("onDropped");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when dropped.", advTooltip);
			}
			effectID = data.getInteger("onHeld");
			if(effectID != 0) {
				c = ArtifactsAPI.artifacts.getComponent(effectID);
				c.addInformation(par1ItemStack, par2EntityPlayer, par3List, "when held.", advTooltip);
			}
		}
		else {
			par3List.add(StatCollector.translateToLocal("tool.All tools, swords, etc."));
		}
    }

    public String getItemDisplayName(ItemStack par1ItemStack)
    {
    	String n = "";
    	if(par1ItemStack.stackTagCompound != null) {
        	if(par1ItemStack.stackTagCompound.getString("matName").length() <= 0)
        		par1ItemStack = ArtifactsAPI.artifacts.applyRandomEffects(par1ItemStack);
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
			n = StatCollector.translateToLocal("type.Artifact");
		}
        return n;//("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + ".name")).trim();
    }
    
    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
    	if(par1ItemStack.itemID == par2ItemStack.itemID) {
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
}
