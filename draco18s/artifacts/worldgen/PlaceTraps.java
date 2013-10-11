package draco18s.artifacts.worldgen;

import java.util.Random;

import com.xcompwiz.mystcraft.api.MystObjects;

//import com.xcompwiz.mystcraft.api.MystObjects;

import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.IWorldGenerator;
import draco18s.artifacts.block.*;
import draco18s.artifacts.entity.TileEntityTrap;

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

	public PlaceTraps() {
		this(true, true, true, true, true);
	}

	public PlaceTraps(boolean pyrm, boolean temp, boolean strn, boolean quik, boolean tow) {
		//if(quik)
		quicksandPit = new WorldGenLakes(BlockQuickSand.instance.blockID);
		wizardTowerA = new StructureApprenticeTower();
		wizardTowerB = new StructureJourneymanTower();
		wizardTowerC = new StructureMasterTower();
		genPyramids = pyrm;
		genTemples = temp;
		genStrongholds = strn;
		genQuicksand = quik;
		genTowers = tow;
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		int dim = world.provider.dimensionId;
		boolean found = false;
		int tex;
		int tez;
		int bID;
		if (dim == 0)
		{
			tex = chunkX*16+10;
			tez = chunkZ*16+10;
			if(genPyramids && world.getBiomeGenForCoords(tex, tez).biomeName.contains("Desert")) {
				if(world.getBlockId(tex, 64, tez) == Block.cloth.blockID) {
					generatePyramidTrap(rand, world, tex, 64, tez);
				}
			}
			tex = chunkX*16+9;
			tez = chunkZ*16+11;
			if(genTemples) {
				for(int vary=60;vary < 130; vary++) {
					if(world.getBiomeGenForCoords(tex, tez).biomeName.contains("Jungle")) {
						if(world.getBlockId(tex, vary, tez) == Block.dispenser.blockID) {
							generateTempleTrap(rand, world, tex, vary, tez);
						}
					}
					if(world.getBiomeGenForCoords(tex, tez).biomeName.contains("Jungle")) {
						if(world.getBlockId(tex+2, vary, tez-2) == Block.dispenser.blockID) {
							generateTempleTrap(rand, world, tex+2, vary, tez-2);
						}
					}
				}
			}
			if(genStrongholds) {
				for(int vary=0;vary < 130; vary++) {
					tex = chunkX*16+8;
					tez = chunkZ*16+12;
					bID = world.getBlockId(tex, vary, tez);
					if(bID == Block.stoneBrick.blockID) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
					tex = chunkX*16+8;
					tez = chunkZ*16+4;
					bID = world.getBlockId(tex, vary, tez);
					if(bID == Block.stoneBrick.blockID) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
				
					tex = chunkX*16+4;
					tez = chunkZ*16+8;
					bID = world.getBlockId(tex, vary, tez);
					if(bID == Block.stoneBrick.blockID) {
						//locateStrongholdCorridor(world, tex, vary, tez);
						found = generateStrongholdTrap(rand, world, locateStrongholdCorridor(world, tex, vary, tez));
						//found = true;
					}
					tex = chunkX*16+4;
					tez = chunkZ*16+12;
					bID = world.getBlockId(tex, vary, tez);
					if(bID == Block.stoneBrick.blockID) {
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
				int mX = chunkX/R;
				int mY = chunkZ/R;
				
				ch = (((mX+1) * mX + mY * mY + (int)Math.pow(1 + mX * mY, 3)) % 29);
				
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
			if(genTowers && (bid == 0 || bid == 3 || bid == 14)) {
				int R = 6;
				int mod = 57;
				if(bid == 0) {
					mod = 29;
					R = 3;
				}
				int mX = chunkX/R;
				int mY = chunkZ/R;
				ch = (((mX+1) * mX + mY * mY + (int)Math.pow(1 + mX * mY, 3)) % mod);
				int nx = chunkX % R;
				int ny = chunkZ % R;
				int Z = nx + (ny * R);
				if(mX % 2 == 0 || mY %2 == 0) {
					Z = -1;
				}
				if(ch == Z) {
					tex = chunkX*16+8;
					tez = chunkZ*16+8;
					int m = rand.nextInt(12);
					//System.out.println("Tower rand: " + m);
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
							wizardTowerC.generate(world, rand, tex, 128, tez);
							break;
						case 7:
						case 8:
						case 9:
						case 10:
						case 11:
							wizardTowerC.generate(world, rand, tex, 128, tez);
							break;
					}
				}
			}
		}
		else if(dim != 1 && dim != -1){
			int bid = world.getBiomeGenForCoords(chunkX*16, chunkZ*16).biomeID;
			if(genTowers && (bid == 0 || bid == 3 || bid == 14)) {
				int R = 6;
				int mod = 57;
				if(bid == 0) {
					mod = 29;
					R = 3;
				}
				int mX = chunkX/R;
				int mY = chunkZ/R;
				int ch = (((mX+1) * mX + mY * mY + (int)Math.pow(dim + mX * mY, 3)) % mod);
				
				int nx = chunkX % R;
				int ny = chunkZ % R;
				int Z = nx + (ny * R);
				if(mX % 2 == 0 || mY %2 == 0) {
					Z = -1;
				}
				if(ch == Z) {
					tex = chunkX*16+8;
					tez = chunkZ*16+8;
					switch(rand.nextInt(12)) {
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
				}
			}
			try {
				Class.forName("com.xcompwiz.mystcraft.api.MystObjects");
			} catch(ClassNotFoundException e) {
				return;
			}
			tex = chunkX*16+8;
			tez = chunkZ*16+8;
			if(MystObjects.book_lectern != null && bid == MystObjects.book_lectern.blockID) {
				tex = chunkX*16+8;
				tez = chunkZ*16+4;
				for(int vary=130;vary > 0; vary--) {
					int l = world.getBlockId(tex+1, vary, tez);
					int r = world.getBlockId(tex-1, vary, tez);
					ForgeDirection o = ForgeDirection.UNKNOWN;
					if(l == r) {
						l = world.getBlockId(tex, vary, tez+1);
						r = world.getBlockId(tex, vary, tez-1);
						if(r == Block.cobblestone.blockID) {
							//te.x, vary, tez+5
							o = ForgeDirection.SOUTH;
						}
						else {
							//te.x, vary, tez-5
							o = ForgeDirection.NORTH;
						}
					}
					else {
						if(r == Block.cobblestone.blockID) {
							//te.x-5, vary, tez
							o = ForgeDirection.WEST;
						}
						else {
							//te.x+5, vary, tez
							o = ForgeDirection.EAST;
						}
					}
					found = generateLibraryTrap(rand, world, tex, vary-2, tez, o);
				}
			}
		}
	}

	private void generateTempleTrap(Random rand, World world, int tex, int tey, int tez) {
		//System.out.println("Jungle Temple located: " + tex + "," + tez);
		int m = world.getBlockMetadata(tex, tey, tez);
		TileEntityDispenser disp = (TileEntityDispenser)world.getBlockTileEntity(tex, tey, tez);
		for(int d=0; d<9; ++d){
			disp.decrStackSize(d, 64);
		}
		world.setBlock(tex, tey, tez, BlockTrap.instance.blockID);
		world.setBlockMetadataWithNotify(tex, tey, tez, m, 3);
		TileEntityTrap dis = (TileEntityTrap)world.getBlockTileEntity(tex, tey, tez);
		if(dis != null) {
			addTrapItem(rand, dis,4);
		}
		
		EnumFacing enumfacing = BlockDispenser.getFacing(m);
		if(enumfacing.getFrontOffsetZ() != 0) {
			tex+=2;
			tez+=6*enumfacing.getFrontOffsetZ();
			disp = (TileEntityDispenser)world.getBlockTileEntity(tex, tey, tez);
			if(disp != null) {
				for(int d=0; d<9; ++d){
					disp.decrStackSize(d, 64);
				}
				m = world.getBlockMetadata(tex, tey, tez);
				world.setBlock(tex, tey, tez, BlockTrap.instance.blockID);
				world.setBlockMetadataWithNotify(tex, tey, tez, m, 3);
				dis = (TileEntityTrap)world.getBlockTileEntity(tex, tey, tez);
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
			disp = (TileEntityDispenser)world.getBlockTileEntity(tex, tey, tez);
			if(disp != null) {
				for(int d=0; d<9; ++d){
					disp.decrStackSize(d, 64);
				}
				m = world.getBlockMetadata(tex, tey, tez);
				world.setBlock(tex, tey, tez, BlockTrap.instance.blockID);
				world.setBlockMetadataWithNotify(tex, tey, tez, m, 3);
				dis = (TileEntityTrap)world.getBlockTileEntity(tex, tey, tez);
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
			world.setBlock(x+ox, 53, z+oz, BlockInvisiblePressurePlate.obsidian.blockID);
			--l;
		} while(l >= 0);
	}
	
	private void pyramidArrowTrap(Random rand, World world, int x, int y, int z) {
		for(int ox = -1; ox <= 1; ox++) {
			for(int oz = -1; oz <= 1; oz++) {
				world.setBlock(x+ox, 51, z+oz, Block.sandStone.blockID);
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
					world.setBlock(x+ox, 51, z+oz, Block.sandStone.blockID);
			}
		}
		for(int ox = -1; ox <= 1; ox++) {
			for(int oz = -1; oz <= 1; oz++) {
				if(ox != 0 || oz != 0)
					world.setBlock(x+ox, 52, z+oz, BlockSpikes.instance.blockID);
			}
		}
		world.setBlock(x, 53, z, BlockInvisiblePressurePlate.obsidian.blockID);
	}

	private Vec3[] locateStrongholdCorridor(World world, int x, int y, int z) {
		if(world.getBlockId(x, y+1, z) == 0) {
			if(world.getBlockId(x+1, y+1, z) == 0 && world.getBlockId(x-1, y+1, z) == 0 && world.getBlockId(x, y+1, z+1) == 0 && world.getBlockId(x, y+1, z-1) == 0) {
				if(world.getBlockId(x+1, y+1, z+1) == 0 && world.getBlockId(x+1, y+1, z-1) == 0 && world.getBlockId(x-1, y+1, z+1) == 0 && world.getBlockId(x-1, y+1, z-1) == 0) {
					return new Vec3[]{Vec3.createVectorHelper(x, y, z),Vec3.createVectorHelper(0, 0, 0)};
				}
				else {
					if(world.getBlockId(x+1, y+1, z+1) == Block.stoneBrick.blockID) {
						return locateStrongholdCorridor(world,x+1, y+1, z+1);
					}
					else if(world.getBlockId(x+1, y+1, z-1) == Block.stoneBrick.blockID) {
						return locateStrongholdCorridor(world,x+1, y+1, z-1);
					}
					else if(world.getBlockId(x-1, y+1, z+1) == Block.stoneBrick.blockID) {
						return locateStrongholdCorridor(world,x-1, y+1, z+1);
					}
					else if(world.getBlockId(x-1, y+1, z-1) == Block.stoneBrick.blockID) {
						return locateStrongholdCorridor(world,x-1, y+1, z-1);
					}
				}
			}
			else if(world.getBlockId(x+1, y+1, z) == 0 && world.getBlockId(x, y+1, z+1) == 0 && world.getBlockId(x+2, y+1, z) == 0 && world.getBlockId(x, y+1, z+2) == 0 && world.getBlockId(x+1, y+1, z+1) == 0) {
				return new Vec3[]{Vec3.createVectorHelper(x+1, y, z+1),Vec3.createVectorHelper(-1, 0, -1)};
			}
			else if(world.getBlockId(x-1, y+1, z) == 0 && world.getBlockId(x, y+1, z+1) == 0 && world.getBlockId(x-2, y+1, z) == 0 && world.getBlockId(x, y+1, z+2) == 0 && world.getBlockId(x-1, y+1, z+1) == 0) {
				return new Vec3[]{Vec3.createVectorHelper(x-1, y, z+1),Vec3.createVectorHelper(1, 0, -1)};
			}
			else if(world.getBlockId(x+1, y+1, z) == 0 && world.getBlockId(x, y+1, z-1) == 0 && world.getBlockId(x+2, y+1, z) == 0 && world.getBlockId(x, y+1, z-2) == 0 && world.getBlockId(x+1, y+1, z-1) == 0) {
				return new Vec3[]{Vec3.createVectorHelper(x+1, y, z-1),Vec3.createVectorHelper(-1, 0, 1)};
			}
			else if(world.getBlockId(x-1, y+1, z) == 0 && world.getBlockId(x, y+1, z-1) == 0 && world.getBlockId(x-2, y+1, z) == 0 && world.getBlockId(x, y+1, z-2) == 0 && world.getBlockId(x-1, y+1, z-1) == 0) {
				return new Vec3[]{Vec3.createVectorHelper(x-1, y, z-1),Vec3.createVectorHelper(-1, 0, -1)};
			}
		}
		else if(world.getBlockId(x, y+1, z) == Block.stoneBrick.blockID) {
			y++;
			if(world.getBlockId(x+1, y, z) == 0) {
				return locateStrongholdCorridor(world, x+1, y-1, z);
			}
			else if(world.getBlockId(x-1, y, z) == 0) {
				return locateStrongholdCorridor(world, x-1, y-1, z);
			}
			else if(world.getBlockId(x, y, z+1) == 0) {
				return locateStrongholdCorridor(world, x, y-1, z+1);
			}
			else if(world.getBlockId(x, y, z-1) == 0) {
				return locateStrongholdCorridor(world, x, y-1, z-1);
			}
			else {
				if(world.getBlockId(x+1, y, z+1) == 0) {
					return locateStrongholdCorridor(world, x+1, y-1, z+1);
				}
				else if(world.getBlockId(x-1, y, z+1) == 0) {
					return locateStrongholdCorridor(world, x-1, y-1, z+1);
				}
				else if(world.getBlockId(x-1, y, z-1) == 0) {
					return locateStrongholdCorridor(world, x-1, y-1, z-1);
				}
				else if(world.getBlockId(x+1, y, z-1) == 0) {
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
					ret = coveredSpikedPit(rand, world, vec[0], Block.cobblestone.blockID, 0);
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
			if((int)(center[1].xCoord) != 0 && world.getBlockId(x+(int)(center[1].xCoord*2), y+1, z) == Block.stoneBrick.blockID) {
				
			}
			else if((int)(center[1].xCoord) != 0 && world.getBlockId(x-(int)(center[1].xCoord*2), y+1, z) == Block.stoneBrick.blockID) {
				center[1].xCoord *= -1;
			}
			else {
				center[1].xCoord = 0;
			}
			if((int)(center[1].zCoord) != 0 && world.getBlockId(x, y+1, z+(int)(center[1].zCoord*2)) == Block.stoneBrick.blockID) {
				
			}
			else if((int)(center[1].zCoord) != 0 && world.getBlockId(x, y+1, z-(int)(center[1].zCoord*2)) == Block.stoneBrick.blockID) {
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
					ret = coveredSpikedPit(rand, world, center[0], Block.stoneBrick.blockID, -3);
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
		
		boolean flag = (world.getBlockId((int)(a.xCoord+c[1].zCoord*2), (int)a.yCoord, (int)(a.zCoord+c[1].xCoord*2)) != 0 || world.getBlockId((int)(a.xCoord-c[1].zCoord*2), (int)a.yCoord, (int)(a.zCoord-c[1].xCoord*2)) != 0);
		if(!flag) {
			if((int)c[1].xCoord != 0 && (int)c[1].zCoord != 0) {
				if(rand.nextBoolean()) {
					c[1].xCoord = 0;
				}
				else {
					c[1].zCoord = 0;
				}
			}
			
			world.setBlock((int)a.xCoord, (int)a.yCoord, (int)a.zCoord, Block.stoneSingleSlab.blockID, 5, 3);
			world.setBlock((int)(a.xCoord+c[1].zCoord), (int)a.yCoord, (int)(a.zCoord+c[1].xCoord), Block.stoneSingleSlab.blockID, 5, 3);
			world.setBlock((int)(a.xCoord-c[1].zCoord), (int)a.yCoord, (int)(a.zCoord-c[1].xCoord), Block.stoneSingleSlab.blockID, 5, 3);
			
			a.xCoord += c[1].xCoord;
			a.zCoord += c[1].zCoord;
			
			world.setBlock((int)a.xCoord, (int)a.yCoord, (int)a.zCoord, Block.stoneBrick.blockID);
			world.setBlock((int)(a.xCoord+c[1].zCoord), (int)a.yCoord, (int)(a.zCoord+c[1].xCoord), Block.stoneBrick.blockID);
			world.setBlock((int)(a.xCoord-c[1].zCoord), (int)a.yCoord, (int)(a.zCoord-c[1].xCoord), Block.stoneBrick.blockID);
			world.setBlock((int)(a.xCoord+c[1].zCoord*2), (int)a.yCoord, (int)(a.zCoord+c[1].xCoord*2), Block.stoneSingleSlab.blockID, 5, 3);
			world.setBlock((int)(a.xCoord-c[1].zCoord*2), (int)a.yCoord, (int)(a.zCoord-c[1].xCoord*2), Block.stoneSingleSlab.blockID, 5, 3);
	
			world.setBlock((int)(a.xCoord+c[1].zCoord), (int)a.yCoord+1, (int)(a.zCoord+c[1].xCoord), Block.stoneBrick.blockID);
			world.setBlock((int)a.xCoord, (int)a.yCoord+1, (int)a.zCoord, Block.chestTrapped.blockID);
			world.setBlock((int)(a.xCoord-c[1].zCoord), (int)a.yCoord+1, (int)(a.zCoord-c[1].xCoord), Block.stoneBrick.blockID);
			
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

			world.setBlock((int)a.xCoord, (int)a.yCoord, (int)a.zCoord, Block.redstoneWire.blockID);
			world.setBlock((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord, BlockIllusionary.instance.blockID);

			a.xCoord += c[1].xCoord;
			a.zCoord += c[1].zCoord;

			world.setBlock((int)a.xCoord, (int)a.yCoord+1, (int)a.zCoord, Block.torchRedstoneActive.blockID, 5, 3);
			world.setBlock((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord, BlockTrap.instance.blockID);
			
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
			
			TileEntityTrap dis = (TileEntityTrap)world.getBlockTileEntity((int)a.xCoord, (int)a.yCoord+2, (int)a.zCoord);
			if(dis != null) {
				addTrapItem(rand, dis);
			}
			else {
				TileEntityDispenser dis2 = (TileEntityDispenser)world.getBlockTileEntity((int)a.xCoord+2, (int)a.yCoord, (int)a.zCoord);
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
					world.setBlock((int)c.xCoord+i, (int)c.yCoord, (int)c.zCoord+j, 0, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j, BlockSpikes.instance.blockID, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-2, (int)c.zCoord+j, Block.stoneBrick.blockID, rand.nextInt(3), 3);
				}
				else {
					if(world.isBlockOpaqueCube((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j))
						world.setBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j, Block.stoneBrick.blockID, rand.nextInt(3), 3);
				}
			}
		}
		return true;
	}

	private boolean coveredSpikedPit(Random rand, World world, Vec3 c, int blockID, int meta) {
		if(meta < 0) {
			meta = rand.nextInt(-1 * meta);
		}
		for(int i=-2; i<=2; i++) {
			for(int j=-2; j<=2; j++) {
				if(i > -2 && i < 2 && j > -2 && j < 2) {
					world.setBlock((int)c.xCoord+i, (int)c.yCoord, (int)c.zCoord+j, BlockIllusionary.instance.blockID, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j, 0, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-2, (int)c.zCoord+j, 0, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-3, (int)c.zCoord+j, BlockSpikes.instance.blockID, 0, 3);
					world.setBlock((int)c.xCoord+i, (int)c.yCoord-4, (int)c.zCoord+j, blockID, meta, 3);
				}
				else {
					if(world.isBlockOpaqueCube((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j))
						world.setBlock((int)c.xCoord+i, (int)c.yCoord-1, (int)c.zCoord+j, blockID, meta, 3);
					if(world.isBlockOpaqueCube((int)c.xCoord+i, (int)c.yCoord-2, (int)c.zCoord+j))
						world.setBlock((int)c.xCoord+i, (int)c.yCoord-2, (int)c.zCoord+j, blockID, meta, 3);
					if(world.isBlockOpaqueCube((int)c.xCoord+i, (int)c.yCoord-3, (int)c.zCoord+j))
						world.setBlock((int)c.xCoord+i, (int)c.yCoord-3, (int)c.zCoord+j, blockID, meta, 3);
				}
			}
		}
		return true;
	}

	private boolean simpleTrap(Random rand, World world, Vec3 c) {
		boolean nn = false; 
		if(world.getBlockId((int)c.xCoord, (int)c.yCoord, (int)c.zCoord) == 0) {
			c.yCoord -=1;
			nn = true;
		}
		world.setBlock((int)c.xCoord, (int)c.yCoord, (int)c.zCoord, BlockTrap.instance.blockID, 1, 3);
		world.setBlockMetadataWithNotify((int)c.xCoord, (int)c.yCoord, (int)c.zCoord, 1, 3);
		world.setBlock((int)c.xCoord, (int)c.yCoord+1, (int)c.zCoord, BlockInvisiblePressurePlate.obsidian.blockID);
		
		
		TileEntityTrap dis = (TileEntityTrap)world.getBlockTileEntity((int)c.xCoord, (int)c.yCoord, (int)c.zCoord);
		if(dis != null) {
			addTrapItem(rand, dis);
		}
		else {
			TileEntityDispenser dis2 = (TileEntityDispenser)world.getBlockTileEntity((int)c.xCoord, (int)c.yCoord, (int)c.zCoord);
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
			ItemStack egg = new ItemStack(Item.monsterPlacer, 8);
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
			dis.addItem(egg);
			break;
		case 2:
		case 3:
			dis.addItem(new ItemStack(Item.flintAndSteel));
			break;
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			dis.addItem(new ItemStack(Item.arrow, 32));
			break;
		case 11:
		case 12:
		case 13:
			dis.addItem(new ItemStack(Item.fireballCharge, 4));
			break;
		case 14:
		case 15:
			dis.addItem(new ItemStack(Item.swordIron));
			break;
		}
	}

	private boolean forwardTrap(Random rand, World world, Vec3[] c) {
		Vec3 pos = Vec3.createVectorHelper(c[0].xCoord, c[0].yCoord, c[0].zCoord);
		world.setBlock((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord+1, BlockTrap.instance.blockID, 0, 3);
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
		TileEntityTrap dis = (TileEntityTrap)world.getBlockTileEntity((int)pos.xCoord, (int)pos.yCoord+1, (int)pos.zCoord);
		if(dis != null) {
			addTrapItem(rand, dis);
		}
		else {
			TileEntityDispenser dis2 = (TileEntityDispenser)world.getBlockTileEntity((int)pos.xCoord, (int)pos.yCoord+1, (int)pos.zCoord);
			if(dis2 != null) {
				addTrapItem(rand, dis);
			}
		}
		world.setBlock((int)pos.xCoord, (int)pos.yCoord, (int)pos.zCoord, Block.torchRedstoneIdle.blockID, 5, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-1, (int)pos.zCoord, Block.redstoneWire.blockID, 15, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-1, (int)pos.zCoord, 0, 0, 3);
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-2, (int)pos.zCoord, Block.redstoneWire.blockID, 14, 3);
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
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-2, (int)pos.zCoord, Block.torchRedstoneActive.blockID, m, 3);
		pos.xCoord += c[1].xCoord;
		pos.zCoord += c[1].zCoord;
		world.setBlock((int)pos.xCoord, (int)pos.yCoord-1, (int)pos.zCoord, Block.redstoneWire.blockID, 0, 3);
		world.setBlock((int)pos.xCoord, (int)pos.yCoord+1, (int)pos.zCoord, BlockInvisiblePressurePlate.obsidian.blockID, 0, 3);
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
