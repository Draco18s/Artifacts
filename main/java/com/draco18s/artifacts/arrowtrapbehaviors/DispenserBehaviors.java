package com.draco18s.artifacts.arrowtrapbehaviors;

import com.draco18s.artifacts.block.BlockTrap;
import com.draco18s.artifacts.item.ItemFakeSwordRenderable;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class DispenserBehaviors
{
    public static void registerBehaviors()
    {
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.arrow, new DispenserBehaviorArrow());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.snowball, new DispenserBehaviorSnowball());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.egg, new DispenserBehaviorEgg());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.potionitem, new DispenserBehaviorPotion());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new DispenserBehaviorExperienceBottle());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new DispenserBehaviorMobEgg());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.fireworks, new DispenserBehaviorFireworks());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.fire_charge, new DispenserBehaviorFireball());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new DispenserBehaviorFire());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.golden_sword, new DispenserBehaviorSword(4));
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.wooden_sword, new DispenserBehaviorSword(4));
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.stone_sword, new DispenserBehaviorSword(5));
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.iron_sword, new DispenserBehaviorSword(6));
    	BlockTrap.dispenseBehaviorRegistry.putObject(Items.diamond_sword, new DispenserBehaviorSword(7));
    }
}
