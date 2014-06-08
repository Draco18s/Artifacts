package com.draco18s.artifacts.api;

import com.draco18s.artifacts.api.interfaces.IBehaviorTrapItem;
import net.minecraft.item.Item;
/**
 * If you know what you're doing, you can add additional trap behaviors with this API
 * @author Draco18s
 *
 */
public interface ITrapAPI {
	/**
	 * If you know what you're doing, you can add additional trap behaviors.
	 * Even I only copied vanilla's behaviors and modified a handful to suit my needs.<br/><br/>
	 * Wait until PostInit to register these.
	 * @param item the item that causes the behavior
	 * @param behavior a new instance of the behavior
	 * @see net.minecraft.dispenser
	 * @Note Probably not advisable to try and create a behavior for Artifacts.  The one
	 * behavior would have to handle all variations.
	 */
	public void addArrowTrapBehavior(Item item, IBehaviorTrapItem behavior);
}
