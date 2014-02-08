package draco18s.artifacts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;
import draco18s.artifacts.api.ArtifactsAPI;
import draco18s.artifacts.api.interfaces.IArtifactComponent;
import draco18s.artifacts.client.ClientProxy;
import draco18s.artifacts.client.TextureCalendar;
import draco18s.artifacts.components.ComponentOreRadar;
import draco18s.artifacts.components.ComponentResurrect;
import draco18s.artifacts.item.ItemArtifactArmor;
import draco18s.artifacts.item.ItemCalendar;

public class ArtifactEventHandler {
	@ForgeSubscribe
	public void EntityHurtEvent(LivingHurtEvent event) {
		//System.out.println("Hurt event detected");
		
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.entity;
			//AttributeInstance atinst = player.getEntityAttribute(ArtifactsAPI.OnHurtAttribute);
			//AttributeModifier mod;
			//mod = new AttributeModifier(resID,"ResurrectComponent",1F,2);
			//if(atinst != null) {
				for(int a=0; a < 4; a++) {
					if(player.inventory.armorInventory[a] != null && player.inventory.armorInventory[a].getItem() instanceof ItemArtifactArmor) {
						NBTTagCompound data = player.inventory.armorInventory[a].getTagCompound();
						int effectID = data.getInteger("onTakeDamage");
						if(effectID != 0) {
							IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
							c.onTakeDamage(player.inventory.armorInventory[a], event, true);
						}
					}
				}
				for(int a = player.inventory.mainInventory.length-1; a >= 0; a--) {
					if(player.inventory.mainInventory[a] != null && player.inventory.mainInventory[a].getItem() instanceof ItemArtifactArmor) {
						NBTTagCompound data = player.inventory.mainInventory[a].getTagCompound();
						int effectID = data.getInteger("onTakeDamage");
						if(effectID != 0) {
							IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
							c.onTakeDamage(player.inventory.mainInventory[a], event, false);
						}
					}
				}
			//}
		}
	}
	
	@ForgeSubscribe
	public void EntityDeathEvent(LivingDeathEvent event) {
		//System.out.println("Death event detected");
		if(event.entity instanceof EntityPlayer) {
			System.out.println("Is a player");
			EntityPlayer player = (EntityPlayer)event.entity;
			//AttributeInstance atinst = player.getEntityAttribute(ArtifactsAPI.OnDeathAttribute);
			//AttributeModifier mod;
			//mod = new AttributeModifier(resID,"ResurrectComponent",1F,2);
			//if(atinst != null) {
				//System.out.println("Has attribute");
				for(int a=0; a < 4; a++) {
					if(player.inventory.armorInventory[a] != null && player.inventory.armorInventory[a].getItem() instanceof ItemArtifactArmor) {
						NBTTagCompound data = player.inventory.armorInventory[a].getTagCompound();
						int effectID = data.getInteger("onDeath");
						//System.out.println("EffectID: " + effectID);
						if(effectID != 0) {
							IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
							c.onDeath(player.inventory.armorInventory[a], event, true);
						}
					}
				}
				for(int a = player.inventory.mainInventory.length-1; a >= 0; a--) {
					if(player.inventory.mainInventory[a] != null && player.inventory.mainInventory[a].getItem() instanceof ItemArtifactArmor) {
						NBTTagCompound data = player.inventory.mainInventory[a].getTagCompound();
						int effectID = data.getInteger("onDeath");
						if(effectID != 0) {
							IArtifactComponent c = ArtifactsAPI.artifacts.getComponent(effectID);
							c.onDeath(player.inventory.mainInventory[a], event, false);
						}
					}
				}
			//}
		}
	}
	
	@ForgeSubscribe
	public void oreRegisterEvent(OreDictionary.OreRegisterEvent event) {
		if(event.Name.indexOf("ore") >= 0 || event.Name.indexOf("gem") >= 0) {
			ComponentOreRadar.addOre(event.Ore);
		}
	}
	
	@ForgeSubscribe
	public void interactEvent(PlayerInteractEvent event) {
		World world = event.entityPlayer.worldObj;
		if(!world.isRemote) {
			int id = world.getBlockId(event.x, event.y, event.z);
			if(id == Block.enchantmentTable.blockID) {
				ArtifactTickHandler.instance.readyTickHandler(world, event.entityPlayer);
			}
		}
	}
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void registerTextures(TextureStitchEvent.Pre event) {
		//System.out.println("before: " + event.map.getTextureExtry("artifacts:calendar"));
		//TextureMap tm = (TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationItemsTexture);
		//boolean re = tm.setTextureEntry("artifacts:calendar", new TextureCalendar("artifacts:calendar"));
		//System.out.println("Able to register calendar texture: " + re);
		//ItemCalendar.instance.setTextureName("artifacts:calendar");
		//System.out.println(event.map.getTextureExtry("artifacts:calendar"));
		if(event.map.textureType == 1 ) {
			event.map.setTextureEntry("artifacts:calendar", ClientProxy.calendar = new TextureCalendar("artifacts:calendar"));
		}
	}
}
