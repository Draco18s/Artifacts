package draco18s.artifacts.arrowtrapbehaviors;

import draco18s.artifacts.block.BlockTrap;
import draco18s.artifacts.item.ItemFakeSwordRenderable;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class DispenserBehaviors
{
    public static void registerBehaviors()
    {
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.arrow, new DispenserBehaviorArrow());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.snowball, new DispenserBehaviorSnowball());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.potion, new DispenserBehaviorPotion());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.monsterPlacer, new DispenserBehaviorMobEgg());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.firework, new DispenserBehaviorFireworks());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.fireballCharge, new DispenserBehaviorFireball());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.flintAndSteel, new DispenserBehaviorFire());
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.swordGold, new DispenserBehaviorSword(4));
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.swordWood, new DispenserBehaviorSword(4));
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.swordStone, new DispenserBehaviorSword(5));
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.swordIron, new DispenserBehaviorSword(6));
    	BlockTrap.dispenseBehaviorRegistry.putObject(Item.swordDiamond, new DispenserBehaviorSword(7));
    }
}
