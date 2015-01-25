package com.draco18s.artifacts.worldgen;

//TODO: Get the APIs working.
//import hunternif.mc.atlas.api.AtlasAPI;
//import hunternif.mc.atlas.ext.ExtTileIdMap;
//import com.xcompwiz.mystcraft.api.MystObjects;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;

import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.block.*;
import com.draco18s.artifacts.entity.TileEntityTrap;

public class PlaceTraps implements IWorldGenerator {
	WorldGenerator quicksandPit;
	WorldGenerator wizardTowerTier1;
	WorldGenerator wizardTowerTier1Ancient;
	WorldGenerator wizardTowerTier2;
	WorldGenerator wizardTowerTier2Ancient;
	WorldGenerator wizardTowerTier3;
	WorldGenerator wizardTowerTier3Ancient;
	public static boolean genPyramids;
	public static boolean genTemples;
	public static boolean genStrongholds;
	public static boolean genMystcraftLibraries;
	public static boolean genQuicksand;
	public static int weightTower1 = 5;
	public static int weightTower1A = 5;
	public static int weightTower2 = 4;
	public static int weightTower2A = 4;
	public static int weightTower3 = 3;
	public static int weightTower3A = 3;
	public static int quicksandRarity = 2;
	public static int towerRarity = 3;
	public static int towerRarityMod = 29;
	public static boolean whitelistEnabled;
	public static boolean blacklistEnabled;
	public static int[] whitelist;
	public static int[] blacklist;
	public static boolean iDontLikeAntibuilders;

	public PlaceTraps() {
		quicksandPit = new WorldGenLakes(BlockQuickSand.instance);
		wizardTowerTier1 = new StructureApprenticeTower();
		wizardTowerTier1Ancient = new StructureApprenticeTowerAncient();
		wizardTowerTier2 = new StructureJourneymanTower();
		wizardTowerTier2Ancient = new StructureJourneymanTowerAncient();
		wizardTowerTier3 = new StructureMasterTower();
		wizardTowerTier3Ancient = new StructureMasterTowerAncient();
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int dim = world.provider.dimensionId;
		boolean found = false;
		int tex;
		int tez;
		Block block;
		if (dim == 0 && (!whitelistEnabled || Arrays.binarySearch(whitelist, 0) >= 0) && !(blacklistEnabled && Arrays.binarySearch(blacklist, 0) >= 0))
		{
			tex = chunkX*16+10;
			tez = chunkZ*16+10;
			if(genPyramids && BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(tex, tez), BiomeDictionary.Type.DESERT)) {
				if(world.getBlock(tex, 64, tez) == Blocks.wool) {
					generatePyramidTrap(rand, world, tex, 64, tez);
				}
			}
			tex = chunkX*16+9;
			tez = chunkZ*16+11;
			if(genTemples && BiomeDictionary.isBiomeOfType(world.getBiomeGenForCoords(tex, tez), BiomeDictionary.Type.JUNGLE)) {
				for(int vary=60;vary < 130; vary++) {
					if(world.getBlock(tex, vary, tez) == Blocks.dispenser) {
						generateTempleTrap(rand, world, tex, vary, tez);
					}
					if(world.getBlock(tex+2, vary, tez-2) == Blocks.dispenser) {
						generateTempleTrap(rand, world, tex+2, vary, tez-2);
					}
				}
			}
			if(genStrongholds) {
				for(int vary=0;vary < 60; vary++) {
					tex = chunkX*16+8;
					tez = chunkZ*16+12;
					block = world.getBlock(tex, vary, tez);
					if(block == Blocks.stonebrick) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
					tex = chunkX*16+8;
					tez = chunkZ*16+4;
					block = world.getBlock(tex, vary, tez);
					if(block == Blocks.stonebrick) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
				
					tex = chunkX*16+4;
					tez = chunkZ*16+8;
					block = world.getBlock(tex, vary, tez);
					if(block == Blocks.stonebrick) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
					tex = chunkX*16+4;
					tez = chunkZ*16+12;
					block = world.getBlock(tex, vary, tez);
					if(block == Blocks.stonebrick) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
					if(found)
						break;
				}
			}
			int ch = 0;
			int bid = world.getBiomeGenForCoords(chunkX*16, chunkZ*16).biomeID;
			if(bid == 6) {
				ch = 4;
			}
			else if(genQuicksand && rand.nextInt(quicksandRarity) == 0 && !(bid == 2 || bid == 3 || bid == 22 || (bid >= 17 && bid <= 20))) {
				
				tex = chunkX*16 + rand.nextInt(16) + 8;
	            tez = chunkZ*16 + rand.nextInt(16) + 8;
	            
				quicksandPit.generate(world, rand, tex, 128, tez);
				
			}
			boolean ocean = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.WATER);
			boolean mushroom = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MUSHROOM);
			boolean mountain = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MOUNTAIN);
			boolean magical = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MAGICAL);//BiomeGenBase.biomeList[bid].biomeName.toLowerCase().contains("magic");
			boolean frozen = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.FROZEN);
			boolean flag = (mountain || magical || mushroom || ocean) && !frozen;
			int weightTotal = weightTower1 + weightTower1A + weightTower2 + weightTower2A + weightTower3 + weightTower3A;
			if(weightTotal > 0 && (bid == 3 || flag) && rand.nextInt(towerRarity) == 0 && chunkX%3 == 0 && chunkZ%3 == 0) {
				
				tex = chunkX*16+rand.nextInt(24);
				tez = chunkZ*16+rand.nextInt(24);
				
				int m = rand.nextInt(weightTotal);
				if(magical && m >= 3) {
					m -= 2;
				}
				
				if(m < weightTower3) {
					wizardTowerTier3.generate(world, rand, tex, 128, tez);
				}
				else if (m < weightTower3A+weightTower3) {
					wizardTowerTier3Ancient.generate(world, rand, tex, 128, tez);
				}
				else if (m < weightTower2+weightTower3A+weightTower3) {
					wizardTowerTier2.generate(world, rand, tex, 128, tez);
				}
				else if (m < weightTower2A+weightTower2+weightTower3A+weightTower3) {
					wizardTowerTier2Ancient.generate(world, rand, tex, 128, tez);
				}
				else if (m < weightTower1+weightTower2A+weightTower2+weightTower3A+weightTower3) {
					wizardTowerTier1.generate(world, rand, tex, 128, tez);
				}
				else {
					wizardTowerTier1Ancient.generate(world, rand, tex, 128, tez);
				}
				//TODO: Get Atlas API Working.
//				if(Loader.isModLoaded("antiqueatlas")) {
//					AtlasAPI.getTileAPI().putCustomTile(world, 0, "wizardtower",
//							chunkX,
//							chunkZ);
//				}
			}
		}
		else if(dim != 1 && dim != -1 && (!whitelistEnabled || Arrays.binarySearch(whitelist, dim) >= 0) && !(blacklistEnabled && Arrays.binarySearch(blacklist, dim) >= 0)	){
			int bid = world.getBiomeGenForCoords(chunkX*16, chunkZ*16).biomeID;
			boolean ocean = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.WATER);
			boolean mushroom = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MUSHROOM);
			boolean mountain = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MOUNTAIN);
			boolean magical = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MAGICAL);//BiomeGenBase.biomeList[bid].biomeName.toLowerCase().contains("magic");
			boolean frozen = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.FROZEN);
			boolean flag = (mountain || magical || mushroom || ocean) && !frozen;
			int weightTotal = weightTower1 + weightTower1A + weightTower2 + weightTower2A + weightTower3 + weightTower3A;
			if(weightTotal > 0 && (bid == 3 || flag) && rand.nextInt(towerRarity) == 0 && chunkX%3 == 0 && chunkZ%3 == 0) {
				tex = chunkX*16+rand.nextInt(24);
				tez = chunkZ*16+rand.nextInt(24);
				
				int m = rand.nextInt(weightTotal);
				if(magical && m >= 3) {
					m -= 2;
				}
				
				if(m < weightTower3) {
					wizardTowerTier3.generate(world, rand, tex, 128, tez);
				}
				else if (m < weightTower3A+weightTower3) {
					wizardTowerTier3Ancient.generate(world, rand, tex, 128, tez);
				}
				else if (m < weightTower2+weightTower3A+weightTower3) {
					wizardTowerTier2.generate(world, rand, tex, 128, tez);
				}
				else if (m < weightTower2A+weightTower2+weightTower3A+weightTower3) {
					wizardTowerTier2Ancient.generate(world, rand, tex, 128, tez);
				}
				else if (m < weightTower1+weightTower2A+weightTower2+weightTower3A+weightTower3) {
					wizardTowerTier1.generate(world, rand, tex, 128, tez);
				}
				else {
					wizardTowerTier1Ancient.generate(world, rand, tex, 128, tez);
				}
				//TODO: Get Altas API working.
//				if(Loader.isModLoaded("antiqueatlas")) {
//					AtlasAPI.getTileAPI().putCustomTile(world, 0, "wizardtower",
//							chunkX,
//							chunkZ);
//				}
			}
			
			
			if(DragonArtifacts.mystcraftLoaded) {
				Block mystcraftLectern = Block.getBlockFromName("Mystcraft:BlockLectern");
				if(mystcraftLectern != null) {
					for(int vary=130; vary > 0; vary--) {
						tex = chunkX*16+8;
						tez = chunkZ*16+4;
						if(world.getBlock(tex, vary, tez) == mystcraftLectern || world.getBlock(tex, vary, tez+1) == mystcraftLectern) {
							//System.out.println("Found Mystcraft Library v1. Lecturn is at x = " + tex + ", y = " + vary + ", z = " + tez);
							Block b = world.getBlock(tex-2, vary-2, tez);
							if(b == Blocks.cobblestone) {
								found = generateLibraryTrap(rand, world, tex-2, vary-2, tez, ForgeDirection.WEST);
							}
						}
						tex = chunkX*16+4;
						tez = chunkZ*16+8;
						if(!found && (world.getBlock(tex, vary, tez) == mystcraftLectern || world.getBlock(tex+1, vary, tez) == mystcraftLectern)) {
							//System.out.println("Found Mystcraft Library v2. Lecturn is at x = " + tex + ", y = " + vary + ", z = " + tez);
							Block b = world.getBlock(tex, vary-2, tez-2);
							if(b == Blocks.cobblestone) {
								found = generateLibraryTrap(rand, world, tex, vary-2, tez-2, ForgeDirection.NORTH);
							}
						}
					}
				}
			}
		}
	}

	private void generateTempleTrap(Random rand, World world, int tex, int tey, int tez) {
		//System.out.println("Jungle Temple located: " + tex + "," + tez);
		int m = world.getBlockMetadata(tex, tey, tez);
		TileEntityDispenser disp = (TileEntityDispenser)world.getTileEntity(tex, tey, tez);
		for(int d=0; d<9; ++d){
			disp.decrStackSize(d, 64);
		}
		world.setBlock(tex, tey, tez, BlockTrap.instance);
		world.setBlockMetadataWithNotify(tex, tey, tez, m, 3);
		TileEntityTrap dis = (TileEntityTrap)world.getTileEntity(tex, tey, tez);
		if(dis != null) {
			addTrapItem(rand, dis,4);
		}
		
		EnumFacing enumfacing = BlockDispenser.func_149937_b/*getFacing*/(m);
		if(enumfacing.getFrontOffsetZ() != 0) {
			tex+=2;
			tez+=6*enumfacing.getFrontOffsetZ();
			disp = (TileEntityDispenser)world.getTileEntity(tex, tey, tez);
			if(disp != null) {
				for(int d=0; d<9; ++d){
					disp.decrStackSize(d, 64);
				}
				m = world.getBlockMetadata(tex, tey, tez);
				world.setBlock(tex, tey, tez, BlockTrap.instance);
				world.setBlockMetadataWithNotify(tex, tey, tez, m, 3);
				dis = (TileEntityTrap)world.getTileEntity(tex, tey, tez);
				if(dis != null) {
					addTrapItem(rand, dis,4);
				}
			}
			else {
				//System.out.println("Found a temple that does not match schematic A. " + tex + "," + tez);
			}
		}
		else {
			tex+=6*enumfacing.getFrontOffsetX();
			tez+=2;
			disp = (TileEntityDispenser)world.getTileEntity(tex, tey, tez);
			if(disp != null) {
				for(int d=0; d<9; ++d){
					disp.decrStackSize(d, 64);
				}
				m = world.getBlockMetadata(tex, tey, tez);
				world.setBlock(tex, tey, tez, BlockTrap.instance);
				world.setBlockMetadataWithNotify(tex, tey, tez, m, 3);
				dis = (TileEntityTrap)world.getTileEntity(tex, tey, tez);
				if(dis != null) {
					addTrapItem(rand, dis,4);
				}
			}
			else {
				//System.out.println("Found a temple that does not match schematic B. " + tex + "," + tez);
			}
		}
	}

	private void generatePyramidTrap(Random rand, World world, int x, int y, int z) {
		int r = rand.nextInt(4);
		switch(r) {
			case 0:
				pyramidSpikeTrap(rand, world, x, y, z);
				break;
			case 1:
				pyramidArrowTrap(rand, world, x, y, z);
				break;
			case 2:
				pyramidHiddenBoom(rand, world, x, y, z);
				break;
			default:
				break;
		}
	}
	
	private void pyramidHiddenBoom(Random rand, World world, int x, int y, int z) {
		world.setBlockToAir(x, 53, z);
		int ox;
		int oz;
		int l = 3;
		do {
			ox = rand.nextInt(3) - 1;
			oz = rand.nextInt(3) - 1;
			world.setBlock(x+ox, 53, z+oz, BlockArtifactsPressurePlate.invisObsidian);
			--l;
		} while(l >= 0);
	}
	
	private void pyramidArrowTrap(Random rand, World world, int x, int y, int z) {
		for(int ox = -1; ox <= 1; ox++) {
			for(int oz = -1; oz <= 1; oz++) {
				world.setBlock(x+ox, 51, z+oz, Blocks.sandstone);
			}
		}
		for(int ox = -1; ox <= 1; ox++) {
			for(int oz = -1; oz <= 1; oz++) {
				Vec3 v = Vec3.createVectorHelper(x+ox, 52, z+oz);
				simpleTrap(rand, world, v);
			}
		}
	}
	
	private void pyramidSpikeTrap(Random rand, World world, int x, int y, int z) {
		for(int ox = -1; ox <= 1; ox++) {
			for(int oz = -1; oz <= 1; oz++) {
				if(ox != 0 || oz != 0)
					world.setBlock(x+ox, 51, z+oz, Blocks.sandstone);
			}
		}
		for(int ox = -1; ox <= 1; ox++) {
			for(int oz = -1; oz <= 1; oz++) {
				if(ox != 0 || oz != 0)
					world.setBlock(x+ox, 52, z+oz, BlockSpikes.instance);
			}
		}
		world.setBlock(x, 53, z, BlockArtifactsPressurePlate.invisObsidian);
	}

	private Vec3[] locateStrongholdCorridor(World world, int x, int y, int z) {
		if(world.getBlock(x, y+1, z) == Blocks.air) {
			if(world.getBlock(x+1, y+1, z) == Blocks.air && world.getBlock(x-1, y+1, z) == Blocks.air && world.getBlock(x, y+1, z+1) == Blocks.air && world.getBlock(x, y+1, z-1) == Blocks.air) {
				if(world.getBlock(x+1, y+1, z+1) == Blocks.air && world.getBlock(x+1, y+1, z-1) == Blocks.air && world.getBlock(x-1, y+1, z+1) == Blocks.air && world.getBlock(x-1, y+1, z-1) == Blocks.air) {
					return new Vec3[]{Vec3.createVectorHelper(x, y, z),Vec3.createVectorHelper(0, 0, 0)};
				}
				else {
					if(world.getBlock(x+1, y+1, z+1) == Blocks.stonebrick) {
						return locateStrongholdCorridor(world,x+1, y+1, z+1);
					}
					else if(world.getBlock(x+1, y+1, z-1) == Blocks.stonebrick) {
						return locateStrongholdCorridor(world,x+1, y+1, z-1);
					}
					else if(world.getBlock(x-1, y+1, z+1) == Blocks.stonebrick) {
						return locateStrongholdCorridor(world,x-1, y+1, z+1);
					}
					else if(world.getBlock(x-1, y+1, z-1) == Blocks.stonebrick) {
						return locateStrongholdCorridor(world,x-1, y+1, z-1);
					}
				}
			}
			else if(world.getBlock(x+1, y+1, z) == Blocks.air && world.getBlock(x, y+1, z+1) == Blocks.air && world.getBlock(x+2, y+1, z) == Blocks.air && world.getBlock(x, y+1, z+2) == Blocks.air && world.getBlock(x+1, y+1, z+1) == Blocks.air) {
				return new Vec3[]{Vec3.createVectorHelper(x+1, y, z+1),Vec3.createVectorHelper(-1, 0, -1)};
			}
			else if(world.getBlock(x-1, y+1, z) == Blocks.air && world.getBlock(x, y+1, z+1) == Blocks.air && world.getBlock(x-2, y+1, z) == Blocks.air && world.getBlock(x, y+1, z+2) == Blocks.air && world.getBlock(x-1, y+1, z+1) == Blocks.air) {
				return new Vec3[]{Vec3.createVectorHelper(x-1, y, z+1),Vec3.createVectorHelper(1, 0, -1)};
			}
			else if(world.getBlock(x+1, y+1, z) == Blocks.air && world.getBlock(x, y+1, z-1) == Blocks.air && world.getBlock(x+2, y+1, z) == Blocks.air && world.getBlock(x, y+1, z-2) == Blocks.air && world.getBlock(x+1, y+1, z-1) == Blocks.air) {
				return new Vec3[]{Vec3.createVectorHelper(x+1, y, z-1),Vec3.createVectorHelper(-1, 0, 1)};
			}
			else if(world.getBlock(x-1, y+1, z) == Blocks.air && world.getBlock(x, y+1, z-1) == Blocks.air && world.getBlock(x-2, y+1, z) == Blocks.air && world.getBlock(x, y+1, z-2) == Blocks.air && world.getBlock(x-1, y+1, z-1) == Blocks.air) {
				return new Vec3[]{Vec3.createVectorHelper(x-1, y, z-1),Vec3.createVectorHelper(-1, 0, -1)};
			}
		}
		else if(world.getBlock(x, y+1, z) == Blocks.stonebrick) {
			y++;
			if(world.getBlock(x+1, y, z) == Blocks.air) {
				return locateStrongholdCorridor(world, x+1, y-1, z);
			}
			else if(world.getBlock(x-1, y, z) == Blocks.air) {
				return locateStrongholdCorridor(world, x-1, y-1, z);
			}
			else if(world.getBlock(x, y, z+1) == Blocks.air) {
				return locateStrongholdCorridor(world, x, y-1, z+1);
			}
			else if(world.getBlock(x, y, z-1) == Blocks.air) {
				return locateStrongholdCorridor(world, x, y-1, z-1);
			}
			else {
				if(world.getBlock(x+1, y, z+1) == Blocks.air) {
					return locateStrongholdCorridor(world, x+1, y-1, z+1);
				}
				else if(world.getBlock(x-1, y, z+1) == Blocks.air) {
					return locateStrongholdCorridor(world, x-1, y-1, z+1);
				}
				else if(world.getBlock(x-1, y, z-1) == Blocks.air) {
					return locateStrongholdCorridor(world, x-1, y-1, z-1);
				}
				else if(world.getBlock(x+1, y, z-1) == Blocks.air) {
					return locateStrongholdCorridor(world, x+1, y-1, z-1);
				}
			}
		}
		return null;
	}

	private boolean generateLibraryTrap(Random rand, World world, int tex, int vary, int tez, ForgeDirection orientation) {
		boolean ret = false;
		
		//System.out.println("Generating Library Trap.");
		//if(rand.nextInt(4) == 0) {
			//ret = true;
			int r = rand.nextInt(2);
			Vec3[] vec = new Vec3[2];
			vec[0] = Vec3.createVectorHelper(tex, vary, tez);
			vec[1] = Vec3.createVectorHelper(orientation.offsetX, orientation.offsetY, orientation.offsetZ);
			int ox = 0;
			int oz = 0;
			switch(r) {
				case 0:
					ox = rand.nextInt(3)-1;
					oz = rand.nextInt(3)-1;
					vec[0].xCoord += ox;
					vec[0].zCoord += oz;
					ret = simpleTrap(rand, world, vec[0]);
					//System.out.println(" - Simple Trap.");
					break;
				case 1:
					ret = coveredSpikedPit(rand, world, vec[0], Blocks.cobblestone, 0);
					//System.out.println(" - Covered Spikes.");
					break;
//				case 2:
//					ret = forwardTrap(rand, world, vec);
//					break;
//				case 3:
//					ret = sideTrapA(world, vec);
//					break;
//				case 4:
//					ret = sideTrapB(world, vec);
//					break;
				default:
			//}
		}
		return ret;
	}

	private boolean generateStrongholdTrap(Random rand, World world, Vec3[] center) {
		boolean ret = false;
		if(center != null) {
			int x = MathHelper.floor_double(center[0].xCoord);
			int y = MathHelper.floor_double(center[0].yCoord);
			int z = MathHelper.floor_double(center[0].zCoord);
			// = MathHelper.floor_double(center[1].xCoord*2)
			if(MathHelper.floor_double(center[1].xCoord) != 0 && world.getBlock(x+MathHelper.floor_double(center[1].xCoord*2), y+1, z) == Blocks.stonebrick) {
				
			}
			else if(MathHelper.floor_double(center[1].xCoord) != 0 && world.getBlock(x-MathHelper.floor_double(center[1].xCoord*2), y+1, z) == Blocks.stonebrick) {
				center[1].xCoord *= -1;
			}
			else {
				center[1].xCoord = 0;
			}
			if(MathHelper.floor_double(center[1].zCoord) != 0 && world.getBlock(x, y+1, z+MathHelper.floor_double(center[1].zCoord*2)) == Blocks.stonebrick) {
				
			}
			else if(MathHelper.floor_double(center[1].zCoord) != 0 && world.getBlock(x, y+1, z-MathHelper.floor_double(center[1].zCoord*2)) == Blocks.stonebrick) {
				center[1].zCoord *= -1;
			}
			else {
				center[1].zCoord = 0;
			}
			int r = rand.nextInt(6);
			//r = 5;
			if(MathHelper.floor_double(center[1].xCoord) == 0 && MathHelper.floor_double(center[1].zCoord) == 0 && r > 4) {
				r = rand.nextInt(5);
			}
			
			switch(r) {
				case 0:
				case 1:
					ret = simpleTrap(rand, world, center[0]);
					break;
				case 2:
				case 3:
					ret = spikedPit(rand, world, center[0]);
					break;
				case 4:
					ret = coveredSpikedPit(rand, world, center[0], Blocks.stonebrick, -3);
					break;
				case 5:
					ret = trapChest(rand, world, center);
					break;
			}
			//if(ret)
				//System.out.println(r + ":" + MathHelper.floor_double(center[0].xCoord) + " " + MathHelper.floor_double(center[0].yCoord) + " " + MathHelper.floor_double(center[0].zCoord));
		}
		return ret;
	}

	private boolean trapChest(Random rand, World world, Vec3[] c) {
		Vec3 a = Vec3.createVectorHelper(c[0].xCoord, c[0].yCoord, c[0].zCoord);
		
		boolean flag = (world.getBlock(MathHelper.floor_double(a.xCoord+c[1].zCoord*2), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord+c[1].xCoord*2)) != Blocks.air || world.getBlock(MathHelper.floor_double(a.xCoord-c[1].zCoord*2), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord-c[1].xCoord*2)) != Blocks.air);
		if(!flag) {
			if(MathHelper.floor_double(c[1].xCoord) != 0 && MathHelper.floor_double(c[1].zCoord) != 0) {
				if(rand.nextBoolean()) {
					c[1].xCoord = 0;
				}
				else {
					c[1].zCoord = 0;
				}
			}
			
			world.setBlock(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord), Blocks.stone_slab, 5, 3);
			world.setBlock(MathHelper.floor_double(a.xCoord+c[1].zCoord), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord+c[1].xCoord), Blocks.stone_slab, 5, 3);
			world.setBlock(MathHelper.floor_double(a.xCoord-c[1].zCoord), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord-c[1].xCoord), Blocks.stone_slab, 5, 3);
			
			a.xCoord += c[1].xCoord;
			a.zCoord += c[1].zCoord;
			
			world.setBlock(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord), Blocks.stonebrick);
			world.setBlock(MathHelper.floor_double(a.xCoord+c[1].zCoord), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord+c[1].xCoord), Blocks.stonebrick);
			world.setBlock(MathHelper.floor_double(a.xCoord-c[1].zCoord), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord-c[1].xCoord), Blocks.stonebrick);
			world.setBlock(MathHelper.floor_double(a.xCoord+c[1].zCoord*2), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord+c[1].xCoord*2), Blocks.stone_slab, 5, 3);
			world.setBlock(MathHelper.floor_double(a.xCoord-c[1].zCoord*2), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord-c[1].xCoord*2), Blocks.stone_slab, 5, 3);
	
			world.setBlock(MathHelper.floor_double(a.xCoord+c[1].zCoord), MathHelper.floor_double(a.yCoord+1), MathHelper.floor_double(a.zCoord+c[1].xCoord), Blocks.stonebrick);
			world.setBlock(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+1), MathHelper.floor_double(a.zCoord), Blocks.trapped_chest);
			world.setBlock(MathHelper.floor_double(a.xCoord-c[1].zCoord), MathHelper.floor_double(a.yCoord+1), MathHelper.floor_double(a.zCoord-c[1].xCoord), Blocks.stonebrick);
			
			if(MathHelper.floor_double(a.xCoord) == 1) {
				world.setBlockMetadataWithNotify(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+1), MathHelper.floor_double(a.zCoord), 2, 3);
			}
			else if(MathHelper.floor_double(a.xCoord) == -1) {
				world.setBlockMetadataWithNotify(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+1), MathHelper.floor_double(a.zCoord), 3, 3);//not 4
			}
			else if(MathHelper.floor_double(a.zCoord) == 1){
				world.setBlockMetadataWithNotify(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+1), MathHelper.floor_double(a.zCoord), 4, 3);//not 4
			}
			else {
				world.setBlockMetadataWithNotify(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+1), MathHelper.floor_double(a.zCoord), 5, 3);
			}

			a.xCoord += c[1].xCoord;
			a.zCoord += c[1].zCoord;

			world.setBlock(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord), Blocks.redstone_wire);
			world.setBlock(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+2), MathHelper.floor_double(a.zCoord), BlockIllusionary.instance);

			a.xCoord += c[1].xCoord;
			a.zCoord += c[1].zCoord;

			world.setBlock(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+1), MathHelper.floor_double(a.zCoord), Blocks.redstone_torch, 5, 3);
			world.setBlock(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+2), MathHelper.floor_double(a.zCoord), BlockTrap.instance);
			
			if(MathHelper.floor_double(a.xCoord) == 1) {
				world.setBlockMetadataWithNotify(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+2), MathHelper.floor_double(a.zCoord), 2, 3);
			}
			else if(MathHelper.floor_double(a.xCoord) == -1) {
				world.setBlockMetadataWithNotify(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+2), MathHelper.floor_double(a.zCoord), 3, 3);
			}
			else if(MathHelper.floor_double(a.zCoord) == 1){
				world.setBlockMetadataWithNotify(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+2), MathHelper.floor_double(a.zCoord), 4, 3);
			}
			else {
				world.setBlockMetadataWithNotify(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+2), MathHelper.floor_double(a.zCoord), 5, 3);
			}
			
			TileEntityTrap dis = (TileEntityTrap)world.getTileEntity(MathHelper.floor_double(a.xCoord), MathHelper.floor_double(a.yCoord+2), MathHelper.floor_double(a.zCoord));
			if(dis != null) {
				addTrapItem(rand, dis);
			}
			else {
				TileEntityDispenser dis2 = (TileEntityDispenser)world.getTileEntity(MathHelper.floor_double(a.xCoord+2), MathHelper.floor_double(a.yCoord), MathHelper.floor_double(a.zCoord));
				if(dis2 != null) {
					addTrapItem(rand, dis);
				}
			}
			
			return true;
		}
		return false;
	}

	private boolean spikedPit(Random rand, World world, Vec3 c) {
		for(int i=-2; i<=2; i++) {
			for(int j=-2; j<=2; j++) {
				if(i > -2 && i < 2 && j > -2 && j < 2) {
					world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord), MathHelper.floor_double(c.zCoord+j), Blocks.air, 0, 3);
					world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-1), MathHelper.floor_double(c.zCoord+j), BlockSpikes.instance, 0, 3);
					world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-2), MathHelper.floor_double(c.zCoord+j), Blocks.stonebrick, rand.nextInt(3), 3);
				}
				else {
					if(world.getBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-1), MathHelper.floor_double(c.zCoord+j)).isOpaqueCube())
						world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-1), MathHelper.floor_double(c.zCoord+j), Blocks.stonebrick, rand.nextInt(3), 3);
				}
			}
		}
		return true;
	}

	private boolean coveredSpikedPit(Random rand, World world, Vec3 c, Block blockID, int meta) {
		if(meta < 0) {
			meta = rand.nextInt(-1 * meta);
		}
		for(int i=-2; i<=2; i++) {
			for(int j=-2; j<=2; j++) {
				if(i > -2 && i < 2 && j > -2 && j < 2) {
					world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord), MathHelper.floor_double(c.zCoord+j), BlockIllusionary.instance, 0, 3);
					world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-1), MathHelper.floor_double(c.zCoord+j), Blocks.air, 0, 3);
					world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-2), MathHelper.floor_double(c.zCoord+j), Blocks.air, 0, 3);
					world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-3), MathHelper.floor_double(c.zCoord+j), BlockSpikes.instance, 0, 3);
					world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-4), MathHelper.floor_double(c.zCoord+j), blockID, meta, 3);
				}
				else {
					if(world.getBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-1), MathHelper.floor_double(c.zCoord+j)).isOpaqueCube())
						world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-1), MathHelper.floor_double(c.zCoord+j), blockID, meta, 3);
					if(world.getBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-2), MathHelper.floor_double(c.zCoord+j)).isOpaqueCube())
						world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-2), MathHelper.floor_double(c.zCoord+j), blockID, meta, 3);
					if(world.getBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-3), MathHelper.floor_double(c.zCoord+j)).isOpaqueCube())
						world.setBlock(MathHelper.floor_double(c.xCoord+i), MathHelper.floor_double(c.yCoord-3), MathHelper.floor_double(c.zCoord+j), blockID, meta, 3);
				}
			}
		}
		return true;
	}

	private boolean simpleTrap(Random rand, World world, Vec3 c) {
		boolean nn = false; 
		if(world.isAirBlock(MathHelper.floor_double(c.xCoord), MathHelper.floor_double(c.yCoord), MathHelper.floor_double(c.zCoord))) {
			c.yCoord -=1;
			nn = true;
		}
		world.setBlock(MathHelper.floor_double(c.xCoord), MathHelper.floor_double(c.yCoord), MathHelper.floor_double(c.zCoord), BlockTrap.instance, 1, 3);
		world.setBlockMetadataWithNotify(MathHelper.floor_double(c.xCoord), MathHelper.floor_double(c.yCoord), MathHelper.floor_double(c.zCoord), 1, 3);
		world.setBlock(MathHelper.floor_double(c.xCoord), MathHelper.floor_double(c.yCoord+1), MathHelper.floor_double(c.zCoord), rand.nextInt(5) == 0 ? BlockArtifactsPressurePlate.camoObsidian : BlockArtifactsPressurePlate.invisObsidian);
		
		
		TileEntityTrap dis = (TileEntityTrap)world.getTileEntity(MathHelper.floor_double(c.xCoord), MathHelper.floor_double(c.yCoord), MathHelper.floor_double(c.zCoord));
		if(dis != null) {
			addTrapItem(rand, dis);
		}
		else {
			TileEntityDispenser dis2 = (TileEntityDispenser)world.getTileEntity(MathHelper.floor_double(c.xCoord), MathHelper.floor_double(c.yCoord), MathHelper.floor_double(c.zCoord));
			if(dis2 != null) {
				addTrapItem(rand, dis);
			}
		}
		if(nn) {
			c.yCoord +=1;
		}
		return true;
	}

	public static void addTrapItem(Random rand, TileEntityTrap dis) {
		addTrapItem(rand, dis, 0);
	}

	public static void addTrapItem(Random rand, TileEntityTrap dis, int min) {
		int max = 9;
		if(dis.blockMetadata > 1) {
			max += 7;
			if(!(dis instanceof TileEntityTrap)) {
				max--;
			}
		}
		int r = rand.nextInt(max-min)+min;
		switch(r) {
		case 0:
		case 1:
			ItemStack egg = new ItemStack(Items.spawn_egg, 8);
			int e = rand.nextInt(5);
			int s;
			switch(e) {
			case 0:
				s = 50;
				break;
			case 1:
				s = 51;
				break;
			case 2:
				s = 54;
				break;
			case 3:
				s = 59;
				break;
			default:
				s = 60;
				break;
			}
			egg.setItemDamage(s);
			dis.func_146019_a/*addItem*/(egg);
			break;
		case 2:
		case 3:
			dis.func_146019_a/*addItem*/(new ItemStack(Items.flint_and_steel));
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			dis.func_146019_a/*addItem*/(new ItemStack(Items.arrow, rand.nextInt(20)));
			break;
		case 11:
		case 12:
		case 13:
			dis.func_146019_a/*addItem*/(new ItemStack(Items.fire_charge, 4));
			break;
		case 14:
		case 15:
			dis.func_146019_a/*addItem*/(new ItemStack(Items.iron_sword, 1, 245));
			break;
		}
	}

	private boolean forwardTrap(Random rand, World world, Vec3[] c) {
		Vec3 pos = Vec3.createVectorHelper(c[0].xCoord, c[0].yCoord, c[0].zCoord);
		world.setBlock(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord), MathHelper.floor_double(pos.zCoord+1), BlockTrap.instance, 0, 3);
		int m = 0;
		if(c[1].xCoord == 0) {
			if(c[1].zCoord == 1) {
				m = 3;
			}
			else {
				m = 2;
			}
		}
		else {
			if(c[1].xCoord == 1) {
				m = 5;
			}
			else {
				m = 4;
			}
		}
		world.setBlockMetadataWithNotify(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord+1), MathHelper.floor_double(pos.zCoord), m, 3);
		TileEntityTrap dis = (TileEntityTrap)world.getTileEntity(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord+1), MathHelper.floor_double(pos.zCoord));
		if(dis != null) {
			addTrapItem(rand, dis);
		}
		else {
			TileEntityDispenser dis2 = (TileEntityDispenser)world.getTileEntity(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord+1), MathHelper.floor_double(pos.zCoord));
			if(dis2 != null) {
				addTrapItem(rand, dis);
			}
		}
		world.setBlock(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord), MathHelper.floor_double(pos.zCoord), Blocks.unlit_redstone_torch, 5, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord-1), MathHelper.floor_double(pos.zCoord), Blocks.redstone_wire, 15, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord-1), MathHelper.floor_double(pos.zCoord), Blocks.air, 0, 3);
		world.setBlock(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord-2), MathHelper.floor_double(pos.zCoord), Blocks.redstone_wire, 14, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		m = 0;
		if(c[1].xCoord == 0) {
			if(c[1].zCoord == 1) {
				m = 3;
			}
			else {
				m = 4;
			}
		}
		else {
			if(c[1].xCoord == 1) {
				m = 1;
			}
			else {
				m = 2;
			}
		}
		world.setBlock(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord-2), MathHelper.floor_double(pos.zCoord), Blocks.redstone_torch, m, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord-1), MathHelper.floor_double(pos.zCoord), Blocks.redstone_wire, 0, 3);
		world.setBlock(MathHelper.floor_double(pos.xCoord), MathHelper.floor_double(pos.yCoord+1), MathHelper.floor_double(pos.zCoord), BlockArtifactsPressurePlate.invisObsidian, 0, 3);
		return true;
	}

	private boolean sideTrapA(World world, Vec3[] c) {
		Vec3 pos = Vec3.createVectorHelper(c[0].xCoord, c[0].yCoord, c[0].zCoord);
		pos.xCoord += c[1].xCoord*3;
		pos.zCoord += c[1].zCoord*3;
		
		return false;
	}

	private boolean sideTrapB(World world, Vec3[] c) {
		Vec3 pos = Vec3.createVectorHelper(c[0].xCoord, c[0].yCoord, c[0].zCoord);
		pos.xCoord += c[1].xCoord*4;
		pos.zCoord += c[1].zCoord*4;
		return false;
	}
}
