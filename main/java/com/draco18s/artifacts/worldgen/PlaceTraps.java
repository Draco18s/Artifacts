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

import com.draco18s.artifacts.block.*;
import com.draco18s.artifacts.entity.TileEntityTrap;

public class PlaceTraps implements IWorldGenerator {
	WorldGenerator quicksandPit;
	WorldGenerator wizardTowerA;
	WorldGenerator wizardTowerB;
	WorldGenerator wizardTowerC;
	public final boolean genPyramids;
	public final boolean genTemples;
	public final boolean genStrongholds;
	public final boolean genQuicksand;
	public final boolean genTowers;
	public final boolean whitelistEnabled;
	public final boolean blacklistEnabled;
	public final int[] whitelist;
	public final int[] blacklist;
	public final boolean iDontLikeAntibuilders;

	public PlaceTraps() {
		this(true, true, true, true, true, false, false, new int[] {}, new int[] {}, true);
	}

	public PlaceTraps(boolean pyrm, boolean temp, boolean strn, boolean quik, boolean tow, boolean usewhite, boolean useblack, int[] white, int[] black, boolean antibuild) {
		//if(quik)
		quicksandPit = new WorldGenLakes(BlockQuickSand.instance);
		wizardTowerA = new StructureApprenticeTower();
		wizardTowerB = new StructureJourneymanTower();
		wizardTowerC = new StructureMasterTower();
		genPyramids = pyrm;
		genTemples = temp;
		genStrongholds = strn;
		genQuicksand = quik;
		genTowers = tow;
		whitelistEnabled = usewhite;
		blacklistEnabled = useblack;
		Arrays.sort(white);
		Arrays.sort(black);
		whitelist = white;
		blacklist = black;
		iDontLikeAntibuilders = !antibuild;
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int dim = world.provider.dimensionId;
		boolean found = false;
		int tex;
		int tez;
		Block bID;
		if (dim == 0 && (!whitelistEnabled || Arrays.binarySearch(whitelist, 0) >= 0) && !(blacklistEnabled && Arrays.binarySearch(blacklist, 0) >= 0))
		{
			tex = chunkX*16+10;
			tez = chunkZ*16+10;
			if(genPyramids && world.getBiomeGenForCoords(tex, tez).biomeName.contains("Desert")) {
				if(world.getBlock(tex, 64, tez) == Blocks.wool) {
					generatePyramidTrap(rand, world, tex, 64, tez);
				}
			}
			tex = chunkX*16+9;
			tez = chunkZ*16+11;
			if(genTemples) {
				for(int vary=60;vary < 130; vary++) {
					if(world.getBiomeGenForCoords(tex, tez).biomeName.contains("Jungle")) {
						if(world.getBlock(tex, vary, tez) == Blocks.dispenser) {
							generateTempleTrap(rand, world, tex, vary, tez);
						}
					}
					if(world.getBiomeGenForCoords(tex, tez).biomeName.contains("Jungle")) {
						if(world.getBlock(tex+2, vary, tez-2) == Blocks.dispenser) {
							generateTempleTrap(rand, world, tex+2, vary, tez-2);
						}
					}
				}
			}
			if(genStrongholds) {
				for(int vary=0;vary < 60; vary++) {
					tex = chunkX*16+8;
					tez = chunkZ*16+12;
					bID = world.getBlock(tex, vary, tez);
					if(bID == Blocks.stonebrick) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
					tex = chunkX*16+8;
					tez = chunkZ*16+4;
					bID = world.getBlock(tex, vary, tez);
					if(bID == Blocks.stonebrick) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
				
					tex = chunkX*16+4;
					tez = chunkZ*16+8;
					bID = world.getBlock(tex, vary, tez);
					if(bID == Blocks.stonebrick) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
					tex = chunkX*16+4;
					tez = chunkZ*16+12;
					bID = world.getBlock(tex, vary, tez);
					if(bID == Blocks.stonebrick) {
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
			else if(genQuicksand && !(bid == 2 || bid == 3 || bid == 22 || (bid >= 17 && bid <= 20))) {
				int R = 2;
				if(world.provider.terrainType == WorldType.LARGE_BIOMES) {
					R*=2;
				}
				int mX = chunkX/R;
				int mY = chunkZ/R;
				
				ch = (((mX+1) * mX + mY * mY + (int)Math.pow(1 + Math.abs(mX) * mY, 3)) % 29);
				
				int nx = chunkX % R;
				int ny = chunkZ % R;
				int Z = nx + (ny * R);
				if(mX % 2 == 0 || mY %2 == 0) {
					Z = -1;
				}
				if(ch == Z) {
					tex = chunkX*16+8;
					tez = chunkZ*16+12;
					quicksandPit.generate(world, rand, tex, 128, tez);
				}
			}
			boolean mountain = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MOUNTAIN);
			boolean magical = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MAGICAL);//BiomeGenBase.biomeList[bid].biomeName.toLowerCase().contains("magic");
			boolean frozen = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.FROZEN);
			mountain &= !frozen;
			magical &= !frozen;
			if(genTowers && (bid == 0 || bid == 3 || bid == 14 || magical || mountain)) {
				//int R = 6;
				//int mod = 59;
				//if(bid == 0) {
					int mod = 29;
					int R = 3;
				//}
				if(world.provider.terrainType == WorldType.LARGE_BIOMES) {
					R*=2;
				}
				int mX = chunkX/R;
				int mY = chunkZ/R;
				ch = (((mX+50) * mX + (mY+100) * mY + (int)Math.pow(1 + Math.abs(mX) * mY, 3)) % mod);
				int nx = chunkX % R;
				int ny = chunkZ % R;
				int Z = nx + (ny * R);
				if(mX % 2 == 0 || mY %2 == 0) {
					//Z = -1;
				}
				else if(ch == Z) {
					//System.out.println();
					tex = chunkX*16+8;
					tez = chunkZ*16+8;
					//System.out.println(tex + "," + tez);
					int m = rand.nextInt(12);
					if(magical && m >= 3) {
						m -= 2;
					}
					//System.out.println("Tower rand: " + m);
					if(iDontLikeAntibuilders && m < 3) {
						m += 6;
					}
					switch(m) {
						case 0:
						case 1:
						case 2:
							wizardTowerC.generate(world, rand, tex, 128, tez);
							break;
						case 3:
						case 4:
						case 5:
						case 6:
							wizardTowerB.generate(world, rand, tex, 128, tez);
							break;
						case 7:
						case 8:
						case 9:
						case 10:
						case 11:
							wizardTowerA.generate(world, rand, tex, 128, tez);
							break;
					}
					//TODO: Get Atlas API Working.
//					if(Loader.isModLoaded("antiqueatlas")) {
//						AtlasAPI.getTileAPI().putCustomTile(world, 0, "wizardtower",
//								chunkX,
//								chunkZ);
//					}
				}
			}
		}
		else if(dim != 1 && dim != -1 && (!whitelistEnabled || Arrays.binarySearch(whitelist, dim) >= 0) && !(blacklistEnabled && Arrays.binarySearch(blacklist, dim) >= 0)	){
			int bid = world.getBiomeGenForCoords(chunkX*16, chunkZ*16).biomeID;
			boolean mountain = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MOUNTAIN);
			boolean magical = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.MAGICAL);//BiomeGenBase.biomeList[bid].biomeName.toLowerCase().contains("magic");
			boolean frozen = BiomeDictionary.isBiomeOfType(BiomeGenBase.getBiome(bid), BiomeDictionary.Type.FROZEN);
			mountain &= !frozen;
			magical &= !frozen;
			if(genTowers && (bid == 0 || bid == 3 || bid == 14 || magical || mountain)) {
				//int R = 6;
				//int mod = 59;
				//if(bid == 0) {
					int mod = 29;
					int R = 3;
				//}
				if(world.provider.terrainType == WorldType.LARGE_BIOMES) {
					R*=4;
				}
				int mX = chunkX/R;
				int mY = chunkZ/R;
				int ch = (((mX+50) * mX + (mY+100) * mY + (int)Math.pow(dim + Math.abs(mX) * mY, 3)) % mod);
				
				int nx = chunkX % R;
				int ny = chunkZ % R;
				int Z = nx + (ny * R);
				if(mX % 2 == 0 || mY %2 == 0) {
					//Z = -1;
				}
				else if(ch == Z) {
					tex = chunkX*16+8;
					tez = chunkZ*16+8;
					int m = rand.nextInt(12);
					if(iDontLikeAntibuilders && m < 3) {
						m += 6;
					}
					switch(m) {
						case 0:
						case 1:
						case 2:
							wizardTowerC.generate(world, rand, tex, 128, tez);
							break;
						case 3:
						case 4:
						case 5:
						case 6:
							wizardTowerB.generate(world, rand, tex, 128, tez);
							break;
						case 7:
						case 8:
						case 9:
						case 10:
						case 11:
							wizardTowerA.generate(world, rand, tex, 128, tez);
							break;
					}
					//TODO: Get Altas API working.
//					if(Loader.isModLoaded("antiqueatlas")) {
//						AtlasAPI.getTileAPI().putCustomTile(world, 0, "wizardtower",
//								chunkX,
//								chunkZ);
//					}
				}
			}
			
			//TODO: Get Mystcraft API working.
//			try {
//				Class.forName("com.xcompwiz.mystcraft.api.MystObjects");
//			} catch(ClassNotFoundException e) {
//				return;
//			}
//			tex = chunkX*16+8;
//			tez = chunkZ*16+8;
//			if(MystObjects.book_lectern != null && bid == MystObjects.book_lectern.blockID) {
//				tex = chunkX*16+8;
//				tez = chunkZ*16+4;
//				for(int vary=130;vary > 0; vary--) {
//					Block l = world.getBlock(tex+1, vary, tez);
//					Block r = world.getBlock(tex-1, vary, tez);
//					ForgeDirection o = ForgeDirection.UNKNOWN;
//					if(l == r) {
//						l = world.getBlock(tex, vary, tez+1);
//						r = world.getBlock(tex, vary, tez-1);
//						if(r == Blocks.cobblestone) {
//							//te.x, vary, tez+5
//							o = ForgeDirection.SOUTH;
//						}
//						else {
//							//te.x, vary, tez-5
//							o = ForgeDirection.NORTH;
//						}
//					}
//					else {
//						if(r == Blocks.cobblestone) {
//							//te.x-5, vary, tez
//							o = ForgeDirection.WEST;
//						}
//						else {
//							//te.x+5, vary, tez
//							o = ForgeDirection.EAST;
//						}
//					}
//					found = generateLibraryTrap(rand, world, tex, vary-2, tez, o);
//				}
//			}
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
				System.out.println("Found a temple that does not match schematic A. " + tex + "," + tez);
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
				System.out.println("Found a temple that does not match schematic B. " + tex + "," + tez);
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
			world.setBlock(x+ox, 53, z+oz, BlockInvisiblePressurePlate.obsidian);
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
		world.setBlock(x, 53, z, BlockInvisiblePressurePlate.obsidian);
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

	private boolean generateLibraryTrap(Random rand, World world, int tex, int vary, int tez, ForgeDirection orrientation) {
		boolean ret = false;
		if(rand.nextInt(4) == 0) {
			//ret = true;
			int r = rand.nextInt(4);
			Vec3[] vec = new Vec3[2];
			vec[0] = Vec3.createVectorHelper(tex, vary, tez);
			vec[1] = Vec3.createVectorHelper(orrientation.offsetX, orrientation.offsetY, orrientation.offsetZ);
			int ox = 0;
			int oz = 0;
			switch(r) {
				case 0:
					ox = rand.nextInt(5)-2;
					oz = rand.nextInt(5)-2;
					vec[0].xCoord += ox;
					vec[0].zCoord += oz;
					ret = simpleTrap(rand, world, vec[0]);
					break;
				case 1:
					ret = coveredSpikedPit(rand, world, vec[0], Blocks.cobblestone, 0);
					break;
				case 2:
					ret = forwardTrap(rand, world, vec);
					break;
				case 3:
					ret = sideTrapA(world, vec);
					break;
				case 4:
					ret = sideTrapB(world, vec);
					break;
			}
		}
		return ret;
	}

	private boolean generateStrongholdTrap(Random rand, World world, Vec3[] center) {
		boolean ret = false;
		if(center != null) {
			int x = (int)(center[0].xCoord);
			int y = (int)(center[0].yCoord);
			int z = (int)(center[0].zCoord);
			// = (int)(center[1].xCoord*2)
			if((int)(center[1].xCoord) != 0 && world.getBlock(x+(int)(center[1].xCoord*2), y+1, z) == Blocks.stonebrick) {
				
			}
			else if((int)(center[1].xCoord) != 0 && world.getBlock(x-(int)(center[1].xCoord*2), y+1, z) == Blocks.stonebrick) {
				center[1].xCoord *= -1;
			}
			else {
				center[1].xCoord = 0;
			}
			if((int)(center[1].zCoord) != 0 && world.getBlock(x, y+1, z+(int)(center[1].zCoord*2)) == Blocks.stonebrick) {
				
			}
			else if((int)(center[1].zCoord) != 0 && world.getBlock(x, y+1, z-(int)(center[1].zCoord*2)) == Blocks.stonebrick) {
				center[1].zCoord *= -1;
			}
			else {
				center[1].zCoord = 0;
			}
			int r = rand.nextInt(6);
			//r = 5;
			if((int)(center[1].xCoord) == 0 && (int)(center[1].zCoord) == 0 && r > 4) {
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
				//System.out.println(r + ":" + ((int)center[0].xCoord) + " " + ((int)center[0].yCoord) + " " + ((int)center[0].zCoord));
		}
		return ret;
	}

	private boolean trapChest(Random rand, World world, Vec3[] c) {
		Vec3 a = Vec3.createVectorHelper(c[0].xCoord, c[0].yCoord, c[0].zCoord);
		
		boolean flag = (world.getBlock((int)(a.xCoord+c[1].zCoord*2), (int)a.yCoord, (int)(a.zCoord+c[1].xCoord*2)) != Blocks.air || world.getBlock((int)(a.xCoord-c[1].zCoord*2), (int)a.yCoord, (int)(a.zCoord-c[1].xCoord*2)) != Blocks.air);
		if(!flag) {
			if((int)c[1].xCoord != 0 && (int)c[1].zCoord != 0) {
				if(rand.nextBoolean()) {
					c[1].xCoord = 0;
				}
				else {
					c[1].zCoord = 0;
				}
			}
			
			world.setBlock((int)a.xCoord, (int)a.yCoord, (int)a.zCoord, Blocks.stone_slab, 5, 3);
			world.setBlock((int)(a.xCoord+c[1].zCoord), (int)a.yCoord, (int)(a.zCoord+c[1].xCoord), Blocks.stone_slab, 5, 3);
			world.setBlock((int)(a.xCoord-c[1].zCoord), (int)a.yCoord, (int)(a.zCoord-c[1].xCoord), Blocks.stone_slab, 5, 3);
			
			a.xCoord += c[1].xCoord;
			a.zCoord += c[1].zCoord;
			
			world.setBlock((int)a.xCoord, (int)a.yCoord, (int)a.zCoord, Blocks.stonebrick);
			world.setBlock((int)(a.xCoord+c[1].zCoord), (int)a.yCoord, (int)(a.zCoord+c[1].xCoord), Blocks.stonebrick);
			world.setBlock((int)(a.xCoord-c[1].zCoord), (int)a.yCoord, (int)(a.zCoord-c[1].xCoord), Blocks.stonebrick);
			world.setBlock((int)(a.xCoord+c[1].zCoord*2), (int)a.yCoord, (int)(a.zCoord+c[1].xCoord*2), Blocks.stone_slab, 5, 3);
			world.setBlock((int)(a.xCoord-c[1].zCoord*2), (int)a.yCoord, (int)(a.zCoord-c[1].xCoord*2), Blocks.stone_slab, 5, 3);
	
			world.setBlock((int)(a.xCoord+c[1].zCoord), (int)a.yCoord+1, (int)(a.zCoord+c[1].xCoord), Blocks.stonebrick);
			world.setBlock((int)a.xCoord, (int)a.yCoord+1, (int)a.zCoord, Blocks.trapped_chest);
			world.setBlock((int)(a.xCoord-c[1].zCoord), (int)a.yCoord+1, (int)(a.zCoord-c[1].xCoord), Blocks.stonebrick);
			
			if((int)a.xCoord == 1) {
				world.setBlockMetadataWithNotify((int)a.xCoord, (int)a.yCoord+1, (int)a.zCoord, 2, 3);
			}
			else if((int)a.xCoord == -1) {
				world.setBlockMetadataWithNotify((int)a.xCoord, (int)a.yCoord+1, (int)a.zCoord, 3, 3);//not 4
			}
			else if((int)a.zCoord == 1){
				world.setBlockMetadataWithNotify((int)a.xCoord, (int)a.yCoord+1, (int)a.zCoord, 4, 3);//not 4
			}
			else {
				world.setBlockMetadataWithNotify((int)a.xCoord, (int)a.yCoord+1, (int)a.zCoord, 5, 3);
			}

			a.xCoord += c[1].xCoord;
			a.zCoord += c[1].zCoord;

			world.setBlock((int)a.xCoord, (int)a.yCoord, (int)a.zCoord, Blocks.redstone_wire);
			world.setBlock((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord, BlockIllusionary.instance);

			a.xCoord += c[1].xCoord;
			a.zCoord += c[1].zCoord;

			world.setBlock((int)a.xCoord, (int)a.yCoord+1, (int)a.zCoord, Blocks.redstone_torch, 5, 3);
			world.setBlock((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord, BlockTrap.instance);
			
			if((int)a.xCoord == 1) {
				world.setBlockMetadataWithNotify((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord, 2, 3);
			}
			else if((int)a.xCoord == -1) {
				world.setBlockMetadataWithNotify((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord, 3, 3);
			}
			else if((int)a.zCoord == 1){
				world.setBlockMetadataWithNotify((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord, 4, 3);
			}
			else {
				world.setBlockMetadataWithNotify((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord, 5, 3);
			}
			
			TileEntityTrap dis = (TileEntityTrap)world.getTileEntity((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord);
			if(dis != null) {
				addTrapItem(rand, dis);
			}
			else {
				TileEntityDispenser dis2 = (TileEntityDispenser)world.getTileEntity((int)a.xCoord+2, (int)a.yCoord, (int)a.zCoord);
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
					world.setBlock((int)c.xCoord+i, (int)c.yCoord, (int)c.zCoord+j, Blocks.air, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j, BlockSpikes.instance, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-2, (int)c.zCoord+j, Blocks.stonebrick, rand.nextInt(3), 3);
				}
				else {
					if(world.getBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j).isOpaqueCube())
						world.setBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j, Blocks.stonebrick, rand.nextInt(3), 3);
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
					world.setBlock((int)c.xCoord+i, (int)c.yCoord, (int)c.zCoord+j, BlockIllusionary.instance, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j, Blocks.air, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-2, (int)c.zCoord+j, Blocks.air, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-3, (int)c.zCoord+j, BlockSpikes.instance, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-4, (int)c.zCoord+j, blockID, meta, 3);
				}
				else {
					if(world.getBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j).isOpaqueCube())
						world.setBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j, blockID, meta, 3);
					if(world.getBlock((int)c.xCoord+i, (int)c.yCoord-2, (int)c.zCoord+j).isOpaqueCube())
						world.setBlock((int)c.xCoord+i, (int)c.yCoord-2, (int)c.zCoord+j, blockID, meta, 3);
					if(world.getBlock((int)c.xCoord+i, (int)c.yCoord-3, (int)c.zCoord+j).isOpaqueCube())
						world.setBlock((int)c.xCoord+i, (int)c.yCoord-3, (int)c.zCoord+j, blockID, meta, 3);
				}
			}
		}
		return true;
	}

	private boolean simpleTrap(Random rand, World world, Vec3 c) {
		boolean nn = false; 
		if(world.isAirBlock((int)c.xCoord, (int)c.yCoord, (int)c.zCoord)) {
			c.yCoord -=1;
			nn = true;
		}
		world.setBlock((int)c.xCoord, (int)c.yCoord, (int)c.zCoord, BlockTrap.instance, 1, 3);
		world.setBlockMetadataWithNotify((int)c.xCoord, (int)c.yCoord, (int)c.zCoord, 1, 3);
		world.setBlock((int)c.xCoord, (int)c.yCoord+1, (int)c.zCoord, BlockInvisiblePressurePlate.obsidian);
		
		
		TileEntityTrap dis = (TileEntityTrap)world.getTileEntity((int)c.xCoord, (int)c.yCoord, (int)c.zCoord);
		if(dis != null) {
			addTrapItem(rand, dis);
		}
		else {
			TileEntityDispenser dis2 = (TileEntityDispenser)world.getTileEntity((int)c.xCoord, (int)c.yCoord, (int)c.zCoord);
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
			dis.func_146019_a/*addItem*/(new ItemStack(Items.arrow, 32));
			break;
		case 11:
		case 12:
		case 13:
			dis.func_146019_a/*addItem*/(new ItemStack(Items.fire_charge, 4));
			break;
		case 14:
		case 15:
			dis.func_146019_a/*addItem*/(new ItemStack(Items.iron_sword));
			break;
		}
	}

	private boolean forwardTrap(Random rand, World world, Vec3[] c) {
		Vec3 pos = Vec3.createVectorHelper(c[0].xCoord, c[0].yCoord, c[0].zCoord);
		world.setBlock((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord+1, BlockTrap.instance, 0, 3);
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
		world.setBlockMetadataWithNotify((int)pos.xCoord, (int)pos.yCoord+1, (int)pos.zCoord, m, 3);
		TileEntityTrap dis = (TileEntityTrap)world.getTileEntity((int)pos.xCoord, (int)pos.yCoord+1, (int)pos.zCoord);
		if(dis != null) {
			addTrapItem(rand, dis);
		}
		else {
			TileEntityDispenser dis2 = (TileEntityDispenser)world.getTileEntity((int)pos.xCoord, (int)pos.yCoord+1, (int)pos.zCoord);
			if(dis2 != null) {
				addTrapItem(rand, dis);
			}
		}
		world.setBlock((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord, Blocks.unlit_redstone_torch, 5, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-1, (int)pos.zCoord, Blocks.redstone_wire, 15, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-1, (int)pos.zCoord, Blocks.air, 0, 3);
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-2, (int)pos.zCoord, Blocks.redstone_wire, 14, 3);
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
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-2, (int)pos.zCoord, Blocks.redstone_torch, m, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-1, (int)pos.zCoord, Blocks.redstone_wire, 0, 3);
		world.setBlock((int)pos.xCoord, (int)pos.yCoord+1, (int)pos.zCoord, BlockInvisiblePressurePlate.obsidian, 0, 3);
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
