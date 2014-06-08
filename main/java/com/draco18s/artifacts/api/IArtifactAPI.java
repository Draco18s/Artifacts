package com.draco18s.artifacts.api;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;

import com.draco18s.artifacts.api.interfaces.IArtifactComponent;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * API for getting, setting, referencing, and registering artifact effects.
 * @author Draco18s
 *
 */
public interface IArtifactAPI {
	/**
	 * Returns the implemented effect Component from its ID value.  Returns null if no effect at that ID.
	 * @param componentID the ID to get
	 * @author Draco18s
	 * */
	public IArtifactComponent getComponent(int componentID);
	
	/**
	 * Returns a randomize artifact, up to 5 effects and randomly applied enchantments.
	 * @author Draco18s
	 */
	public ItemStack generateRandomArtifact();
		
	/**
	 * Adds a component to the component list.  Mods should supply their own configs and pass null for any
	 * disabled component to insure that the list remains static as the user changes settings.
	 * @param component the component to register
	 * @author Draco18s
	 */
	public void registerComponent(IArtifactComponent component);
	
	/**
	 * Gets the total number of components registered
	 * @author Draco18s
	 */
	public int getComponentCount();

	/**
	 * Takes a blank artifact and applies effects to it
	 *   **Not for Developers**
	 *   Used only by ItemArtifact to apply affects in case of null NBT tags.
	 * @param artifact the artifact to give effects
	 * @author Draco18s
	 */
	public ItemStack applyRandomEffects(ItemStack artifact);
	
	/**
	 * For use with debugging new effects (full mod required) or for specific treasure generation.
	 * @param artifact the artifact to modify
	 * @param id the effect ID to apply
	 * @param trigger the trigger string (see IArtifactComponent).  If null or empty string,
	 * one will be chosen at random from the component.
	 * @return the artifact with the applied effect
	 */
	public ItemStack applyEffectByID(ItemStack artifact, int id, String trigger);
	
	/**
	 * Automatically called by applyEffectByID.  Generates the following tags:
	 * @Integer "material"
	 * @String "name"
	 * @String "icon"
	 * @Long "overlay_color"
	 * @IntArray "allComponents"
	 * @return default artifact tags
	 */
	public NBTTagCompound createDefault();
	
	/**
	 * Allows for artifacts to generate in custom treasure gen.  Plugins should provide their own
	 * configuration options for enabling/disabling and altering the rarity.<br/>
	 * Depreciated, see now the typed version.
	 * @param treasureString
	 * @param rarity
	 */
	@Deprecated
	public void setTreasureGeneration(String treasureString, int rarity);
	
	/**
	 * Allows for artifacts to generate in custom treasure gen.  Plugins should provide their own
	 * configuration options for enabling/disabling and altering the rarity.
	 * @see {@link WeightedRandomArtifact}
	 * @param treasureString
	 * @param type
	 * @param rarity
	 */
	public void setTreasureGeneration(String treasureString, ArtifactType type, int rarity);
	
	/**
	 * Some effects may require some kind of delay; e.g. any right-click trigger has a built-in
	 * delay preventing the effect from being spammed, drop effects has a delay before activating,
	 * and so on.<br/><br/>In the event you need such a timer, register the NBT tag keyname you want
	 * to use here.<br/>The NBT tag must be an Integer.<br/>Every tick, if the value is greater
	 * than 0, then 1 will be subtracted and saved back to the NBT data.<br/>If you need a timer
	 * that ticks when the item is an entity, append "_dropped" to your key usage.<br/>
	 * If you wish to use it for equipped armor, append "_armor" instead.<br/><br/>
	 * Existing strings:<br/>
	 * "onItemRightClickDelay" (used to delay right-click effects)<br/>
	 * "droppedDelay" (used to delay drop effects, called for onEntityItemUpdate)<br/>
	 * "resCooldown" (a cooldown for Resurrection effect)<br/>
	 * "orePingDelay" (used by ore finder effect)<br/>
	 * @param key
	 */
	public void registerUpdateNBTKey(String key);
	
	/**
	 * Returns a clone of the keys list
	 * @return
	 */
	public ArrayList<String> getNBTKeys();
	
	/**
	 * These are very generic artifact types.<br/>
	 * Tool refers to the original artifact that comes in sword, trinket, and wand flavors.<br/>
	 * The rest are armors, divided by material type.
	 */
	public enum ArtifactType
    {
        TOOL,
        ARMOR_CLOTH,
        ARMOR_CHAIN,
        ARMOR_IRON,
        ARMOR_GOLD,
        ARMOR_DIAMOND;
    }
}
