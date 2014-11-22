package com.draco18s.artifacts.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.MathHelper;

public class StructureGenHelper {
	
	public static Block randomBlock(Random rand, Block[] blocks) {
		return blocks[rand.nextInt(blocks.length)];
	}
	
	public static Block cobbleOrMossy(Random rand) {
		return StructureGenHelper.randomBlock(rand, new Block[]{Blocks.cobblestone, Blocks.cobblestone, Blocks.cobblestone, Blocks.mossy_cobblestone});
	}
	
	public static Block cobbleMossyOrAir(Random rand) {
		return StructureGenHelper.randomBlock(rand, new Block[]{Blocks.cobblestone, Blocks.mossy_cobblestone, Blocks.mossy_cobblestone, Blocks.mossy_cobblestone, Blocks.mossy_cobblestone, Blocks.mossy_cobblestone, Blocks.air});
	}
	
	public static Block blockChance90p(Random rand, Block block) {
		return rand.nextInt(10) == 0 ? Blocks.air : block;
	}
	
	public static int stoneBrickMeta(Random rand) {
		int randInt = rand.nextInt(20);
		if(randInt >= 3) {
			randInt = 0;
		}
		
		return randInt;
	}
	
	public static int stoneBrickMetaAncient(Random rand) {
		int randInt = rand.nextInt(15)+4;
		randInt = randInt / 6;
		
		if(randInt >= 3) {
			randInt = 0;
		}
		
		return randInt;
	}
	
	public static void generatePotContents(Random rand, TileEntityFlowerPot te) {
		int[] possibleContents = {6, 32, 37, 38, 39, 40, 81}; //{"minecraft:sapling", "minecraft:deadbush", "minecraft:yellow_flower", "minecraft:red_flower", "minecraft:brown_mushroom", "minecraft:red_mushroom", "minecraft:cactus"}
		int[][] possibleMeta = {{0, 1, 2, 3, 4, 5}, {0}, {0}, {0, 1, 2, 3, 4, 5, 6, 7, 8}, {0}, {0}, {0}};
		
		int contentIndex = rand.nextInt(possibleContents.length);
		int metaIndex = rand.nextInt(possibleMeta[contentIndex].length);
		
		te.func_145964_a(Item.getItemById(possibleContents[contentIndex]), possibleMeta[contentIndex][metaIndex]);
	}

}
