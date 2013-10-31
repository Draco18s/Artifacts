package draco18s.artifacts.api;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public interface IItemIconAPI {
	
	/**
	 * Internal use only.
	 * @see #registerArtifactIcon(String icon, String type)
	 * @see #registerArtifactIcon(String icon, String overlay, String type)
	 */
    public HashMap icons = new HashMap();
    
	/**
	 * Registers a single, flat, icon to be used by the artifact factory
	 * @param type the item type.  Valid strings are amulet, dagger, figurine,
	 * ring, staff, sword, trinket, wand
	 * @param icon the icon registration string (to be passed to the IconRegister)
	 * @see #registerArtifactIcon(String icon, String overlay, String type)
	 * @Note Dagger icons should be mirrored horizontally compared to swords.
	 * Staffs should idealy be vertical but if diagonal, should also be horizontally mirrored
	 */
	public void registerArtifactIcon(String type, String icon) throws Exception;

	/**
	 * Registers a two icons to be used by the artifact factory.  The overlay is grayscale
	 * and gets a colorized multiplier during rendering.
	 * @param type the item type.  Valid strings are amulet, dagger, figurine,
	 * ring, staff, sword, trinket, wand
	 * @param icon the base icon registration string (to be passed to the IconRegister)
	 * @param overlay the overlay icon registration string
	 * @see #registerArtifactIcon(String icon, String type)
	 * @Note Dagger icons should be mirrored horizontally compared to swords.
	 * Staffs should idealy be vertical but if diagonal, should also be horizontally mirrored
	 */
	public void registerArtifactIcon(String type, String icon, String overlay) throws Exception;
	
	/**
	 * Internal use only
	 */
	public Icon registerIcons(IconRegister iconReg);
}
