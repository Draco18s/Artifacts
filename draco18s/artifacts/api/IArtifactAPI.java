package draco18s.artifacts.api;

import java.awt.Color;
import java.util.Vector;

import draco18s.artifacts.api.interfaces.IArtifactComponent;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
	 * Adds a component to the component list
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
	 * configuration options for enabling/disabling and altering the rarity.
	 * @param treasureString
	 * @param rarity
	 */
	public void setTreasureGeneration(String treasureString, int rarity);
}
