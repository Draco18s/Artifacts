package com.draco18s.artifacts.factory;

import net.minecraft.item.Item;
import com.draco18s.artifacts.api.ITrapAPI;
import com.draco18s.artifacts.api.interfaces.IBehaviorTrapItem;
import com.draco18s.artifacts.block.BlockTrap;

public class FactoryTrapBehaviors implements ITrapAPI {

	@Override
	public void addArrowTrapBehavior(Item item, IBehaviorTrapItem behavior) {
		BlockTrap.dispenseBehaviorRegistry.putObject(item, behavior);
	}
}
