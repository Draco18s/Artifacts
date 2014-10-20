package com.draco18s.artifacts;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Session;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

import com.draco18s.artifacts.components.ComponentRepair;
import com.draco18s.artifacts.components.UtilsForComponents;
import com.draco18s.artifacts.entity.TileEntityAntibuilder;
import com.draco18s.artifacts.entity.TileEntityAntibuilder.AntibuilderLocation;
import com.draco18s.artifacts.item.ItemArtifact;
import com.draco18s.artifacts.item.ItemArtifactArmor;
import com.draco18s.artifacts.item.ItemOrichalcumDust;

public class ArtifactTickHandler {
    private World world;
    private EntityPlayer eobj;
    public static ArtifactTickHandler instance;
    private boolean shouldRun = false;
    private int lastLevel = 0;
    private int trigger1 = 0;
    private int trigger2 = 1;
    private int trigger3 = 18000;
    private int trigger4 = 2;
    private boolean randomized = false;
    
    private int healthTick = 0;
    public static int repairCount = 0;
    
	public ArtifactTickHandler() {
		instance = this;
	}
	
	@SubscribeEvent
	public void onTick(ServerTickEvent event) {
		if(event.phase == Phase.END) {
			//System.out.println("Tick End.");
			if(!shouldRun)
				return;
			if(eobj.openContainer == null || eobj.openContainer == eobj.inventoryContainer) {
				shouldRun = false;
				long state2 = world.getWorldTime();
				int state1 = world.provider.getMoonPhase(state2);
				int calc = (int) Math.abs((state2%24000) - trigger3);
				boolean flag = ((state1 == trigger1 || state1 == trigger2) && calc < 3000);
				if(flag) {
					if(eobj.experienceLevel >= trigger4 && lastLevel > eobj.experienceLevel) {
						int m = Math.min((eobj.experienceLevel+1)/3, 4);
						eobj.addExperienceLevel(-1*m);
						EntityItem ent = new EntityItem(world, eobj.posX, eobj.posY, eobj.posZ, new ItemStack(ItemOrichalcumDust.instance, m));
						world.spawnEntityInWorld(ent);
					}
				}
			}
		}
		else {
			//System.out.println("Tick Start.");
			List<EntityPlayer> playerList = MinecraftServer.getServer().getConfigurationManager().playerEntityList;

			//Loop through each player's inventory and update the number of health-boosting artifacts.
			//Only runs every 20 ticks (1 second).
			healthTick++;
			if(healthTick >= 20) {
				//System.out.println("Starting Health Tick.");
				healthTick = 0;

				for(EntityPlayer player :  playerList) {
					int artifactHealthBoostCount = 0;
					int artifactKnockbackCount = 0;
					int artifactSpeedBoostCount = 0;

					//Loop through main inventory.
					for(int i = 0; i < player.inventory.mainInventory.length; i++) {
						ItemStack current = player.inventory.mainInventory[i];
						

						if(current != null && current.getTagCompound() != null && current.getItem() instanceof ItemArtifact) {
							if( !(DragonArtifacts.baublesLoaded && DragonArtifacts.baublesMustBeEquipped &&
									UtilsForComponents.equipableByBaubles(current.stackTagCompound.getString("iconName")))){
								int effectID = current.getTagCompound().getInteger("onUpdate");
								if(effectID == 16) { //ComponentHealth
									artifactHealthBoostCount++;
								}
							}
						}
					}

					//Loop through equipped armour slots.
					for(int i = 0; i < player.inventory.armorInventory.length; i++) {
						ItemStack current = player.inventory.armorInventory[i];
						if(current != null && current.getTagCompound() != null && current.getItem() instanceof ItemArtifactArmor) {
							int effectID = current.getTagCompound().getInteger("onArmorTickUpdate");
							int effectID2 = current.getTagCompound().getInteger("onArmorTickUpdate2");
							//ComponentHealth
							if(effectID == 16 || effectID2 == 16) {
								artifactHealthBoostCount++;
							}
							//ComponentKnockbackResist
							if(effectID == 21 || effectID2 ==21) { 
								artifactKnockbackCount++;
							}
							//ComponentSpeed
							if(effectID == 15 || effectID2 == 15) { 
								artifactSpeedBoostCount++;
							}
						}
					}
					
					//Look through Baubles slots if Baubles is loaded.
					if(DragonArtifacts.baublesLoaded) {
						IInventory baublesSlots = BaublesApi.getBaubles(player);
						
						for(int i = 0; i < baublesSlots.getSizeInventory(); i++) {
							ItemStack current = baublesSlots.getStackInSlot(i);
							
							if(current != null && current.getTagCompound() != null && current.getItem() instanceof ItemArtifact) {
								int effectID = current.getTagCompound().getInteger("onUpdate");
								if(effectID == 16) { //ComponentHealth
									artifactHealthBoostCount++;
								}
							}
						}
					}

					//Update health boost attributes
					updateHealthBoost(artifactHealthBoostCount, player);

					//Update knockback attributes
					updateKnockbackResistance(artifactKnockbackCount, player);

					//Update speed attributes
					updateSpeedBoost(artifactSpeedBoostCount, player);
				}
			}

			//Update Repair Tick.
			repairCount++;
			if(repairCount > 1200) {
				repairCount = 0;
			}
			
			//Update antibuilders present
			ArrayList<AntibuilderLocation> toRemove = new ArrayList<AntibuilderLocation>();
			for(AntibuilderLocation location : TileEntityAntibuilder.antibuilders.keySet()) {
				int strength = TileEntityAntibuilder.antibuilders.get(location);
				TileEntityAntibuilder.antibuilders.put(location, strength - 1);
				if(strength - 1 < 0) {
					toRemove.add(location);
				}
			}
			
			for(AntibuilderLocation location : toRemove) {
				TileEntityAntibuilder.antibuilders.remove(location);
			}
		}
	}

	private void updateHealthBoost(int artifactHealthBoostCount, EntityPlayer player) {
		NBTTagCompound playerData = player.getEntityData();
		int oldHealthBoostCount = playerData.getInteger("artifactHealthBoostCount");
		
		if(oldHealthBoostCount != artifactHealthBoostCount) {
			String uu = playerData.getString("artifactHealthBoostUUID");
			UUID healthID;
			
			if(uu.equals("")) {
				healthID = UUID.randomUUID();
				playerData.setString("artifactHealthBoostUUID", healthID.toString());
			}
			else {
				healthID = UUID.fromString(uu);
			}
			
			IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
			
			atinst.removeModifier(new AttributeModifier(healthID, "HealthBoostComponent", 5F * oldHealthBoostCount, 0));
			atinst.applyModifier(new AttributeModifier(healthID, "HealthBoostComponent", 5F * artifactHealthBoostCount, 0));
			
			if(player.getHealth() > player.getMaxHealth()) {
				player.setHealth(player.getMaxHealth());
			}
			int diff = (artifactHealthBoostCount - oldHealthBoostCount);
			if(diff > 0 && player.getHealth() < player.getMaxHealth()) {
				player.heal(5*diff);
			}
			
			playerData.setInteger("artifactHealthBoostCount", artifactHealthBoostCount);
		}
	}
	
	private void updateKnockbackResistance(int artifactKnockbackCount, EntityPlayer player) {
		NBTTagCompound playerData = player.getEntityData();
		int oldKnockbackCount = playerData.getInteger("artifactKnockbackCount");
		
		if(oldKnockbackCount != artifactKnockbackCount) {
			String uu = playerData.getString("artifactKnockbackUUID");
			UUID knockbackID;
			
			if(uu.equals("")) {
				knockbackID = UUID.randomUUID();
				playerData.setString("artifactKnockbackUUID", knockbackID.toString());
			}
			else {
				knockbackID = UUID.fromString(uu);
			}
			
			IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
			
			atinst.removeModifier(new AttributeModifier(knockbackID, "KnockbackComponent", 0.2F * oldKnockbackCount, 0));
			atinst.applyModifier(new AttributeModifier(knockbackID, "KnockbackComponent", 0.2F * artifactKnockbackCount, 0));
			
			playerData.setInteger("artifactKnockbackCount", artifactKnockbackCount);
		}
	}

	private void updateSpeedBoost(int artifactSpeedBoostCount, EntityPlayer player) {
		NBTTagCompound playerData = player.getEntityData();
		int oldSpeedBoostCount = playerData.getInteger("artifactSpeedBoostCount");
		
		if(oldSpeedBoostCount != artifactSpeedBoostCount) {
			String uu = playerData.getString("artifactSpeedBoostUUID");
			UUID speedID;
			
			if(uu.equals("")) {
				speedID = UUID.randomUUID();
				playerData.setString("artifactSpeedBoostUUID", speedID.toString());
			}
			else {
				speedID = UUID.fromString(uu);
			}
			
			IAttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			
			atinst.removeModifier(new AttributeModifier(speedID, "SpeedBoostComponent", 0.05F * oldSpeedBoostCount, 2));
			atinst.applyModifier(new AttributeModifier(speedID, "SpeedBoostComponent", 0.05F * artifactSpeedBoostCount, 2));
			
			playerData.setInteger("artifactSpeedBoostCount", artifactSpeedBoostCount);
		}
	}

	public void readyTickHandler(World w, EntityPlayer pl) {
		world = w;
		eobj = pl;
		shouldRun = true;
		lastLevel = eobj.experienceLevel;
		if(!randomized) {
			Random r = new Random(world.provider.getSeed());
			trigger3 = r.nextInt(4) * 6000;
			trigger1 = r.nextInt(4);
			trigger2 = trigger1 + 4;
		}
		randomized = true;
	}
}
