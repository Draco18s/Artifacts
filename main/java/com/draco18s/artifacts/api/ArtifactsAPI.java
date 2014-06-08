package com.draco18s.artifacts.api;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

/**
 * Main artifact API class.  Check for null values before referencing.
 * @author Draco18s
 *
 */
public class ArtifactsAPI {
	/**
	 * Apply these monster attributes with your components to get the event handler to detect that an effect is in effect.
	 * See an example segment in this java file.
	 */
	public static IAttribute OnDeathAttribute = (new RangedAttribute("artifact.ondeath", 0.0D, 0.0D, 1.0D)).setDescription("Death Event").setShouldWatch(true);
	/**
	 * Apply these monster attributes with your components to get the event handler to detect that an effect is in effect.
	 * See an example segment in this java file.
	 */
	public static IAttribute OnHurtAttribute = (new RangedAttribute("artifact.onhurt", 0.0D, 0.0D, 1.0D)).setDescription("Hurt Event").setShouldWatch(true);
	
	/**
	 * API for artifacts
	 */
	public static IArtifactAPI				artifacts				= null;
	/**
	 * API for item icons
	 */
	public static IItemIconAPI				itemicons				= null;
	/**
	 * API for trap components
	 */
	public static ITrapAPI					traps					= null;
	
	/**
	 * Static variable to hold this API usage example.  It will always be null.<br/>
	 * You will probably want to open up this file to read it in a clean manner.<br/><br/>
	 * This is an example usage of an attribute modifier.  NBT tag keys are probably not named well, this is merely an example.
	 * In this case, this would cause the {@link draco18s.artifacts.api.interfaces.IArtifactComponent#onDeath} function to trigger.<br/>
	 * 
	 * public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
	 *     String uu = par1ItemStack.stackTagCompound.getString("HealthUUID");
	 *     UUID hpID;
	 *     if(uu.equals("")) {
	 *         hpID = UUID.randomUUID();
	 *         par1ItemStack.stackTagCompound.setString("HealthUUID", hpID.toString());
	 *     }
	 *     else {
	 *         hpID = UUID.fromString(uu);
	 *     }
	 *     if(par3Entity instanceof EntityPlayer) {
	 *         EntityPlayer player = (EntityPlayer)par3Entity;
	 *         AttributeInstance atinst = player.getEntityAttribute(ArtifactsAPI.OnDeathAttribute);
	 *         AttributeModifier mod = new AttributeModifier(hpID,"MaxHealthComponent",1F,2);
	 *         //because items can be deleted from the creative inventory without any functions firing, we need to remove the attribute
	 *         //any time the creative inventory is open.  Likewise other inventories can remove the item from the player without an event firing
	 *         if(player.openContainer != null && player.openContainer != player.inventoryContainer || player.capabilities.isCreativeMode) {
	 *             if(atinst.getModifier(hpID) != null) {
	 *                 atinst.removeModifier(mod);
	 *                 //set player's health back to normal maximum
     *             	   if(player.getHealth() > player.getMaxHealth()) {
     *                     player.setHealth(player.getMaxHealth());
     *                 }
	 *             }
	 *         }
	 *         else {
	 *             if(atinst.getModifier(hpID) == null) {
	 *                 atinst.applyModifier(mod);
	 *             }
	 *             par1ItemStack.stackTagCompound.setInteger("MaxHealth", player.entityId);
	 *         }
     *     }
     *     else {
     *         int eid = par1ItemStack.stackTagCompound.getInteger("MaxHealth");
     *         EntityPlayer player = (EntityPlayer) par2World.getEntityByID(eid);
     *         AttributeInstance atinst = player.getEntityAttribute(ArtifactsAPI.OnDeathAttribute);
     *         AttributeModifier mod = new AttributeModifier(hpID,"MaxHealthComponent",1,2);
     *         if(atinst.getModifier(hpID) != null) {
     *             atinst.removeModifier(mod);
     *             if(player.getHealth() > player.getMaxHealth()) {
     *                 player.setHealth(player.getMaxHealth());
     *             }
     *         }
     *     }
     * }
	 */
	public static Object example = null;
}
