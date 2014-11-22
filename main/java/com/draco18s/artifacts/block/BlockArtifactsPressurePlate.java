package com.draco18s.artifacts.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockArtifactsPressurePlate extends BlockBasePressurePlate {
	public static Block invisStone;
	public static Block invisObsidian;
	public static Block obsidian;
	public static Block camoWood;
	public static Block camoStone;
	public static Block camoObsidian;
	private BlockPressurePlate.Sensitivity triggerMobType;
	private boolean invisible;
	private boolean camouflaged;

	public BlockArtifactsPressurePlate(String name, Material material, BlockPressurePlate.Sensitivity mobType, boolean invis, boolean camo) {
		super(name, material);
		this.invisible = invis;
		this.camouflaged = camo;
		this.triggerMobType = mobType;
		setHardness(0.5F);
		setStepSound(mobType == BlockPressurePlate.Sensitivity.everything ? Block.soundTypeWood : Block.soundTypeStone);
		setCreativeTab(DragonArtifacts.tabGeneral);
	}

	@Override
	//getMetaFromWeight
	protected int func_150066_d(int weight)
	{
		return weight > 0 ? 1 : 0;
	}

	@Override
	//getPowerSupply
	protected int func_150060_c(int meta)
	{
		return meta == 1 ? 15 : 0;
	}

	@Override
	//getPlateState
	protected int func_150065_e(World world, int x, int y, int z)
	{
		List list = null;

		if (this.triggerMobType == BlockPressurePlate.Sensitivity.everything)
		{
			list = world.getEntitiesWithinAABBExcludingEntity((Entity)null, this.func_150061_a/*getSensitiveAABB*/(x, y, z));
		}

		if (this.triggerMobType == BlockPressurePlate.Sensitivity.mobs)
		{
			list = world.getEntitiesWithinAABB(EntityLivingBase.class, this.func_150061_a(x, y, z));
		}

		if (this.triggerMobType == BlockPressurePlate.Sensitivity.players)
		{
			list = world.getEntitiesWithinAABB(EntityPlayer.class, this.func_150061_a(x, y, z));
		}

		if (!list.isEmpty())
		{
			Iterator iterator = list.iterator();

			while (iterator.hasNext())
			{
				Entity entity = (Entity)iterator.next();

				if (!entity.doesEntityNotTriggerPressurePlate())
				{
					return 15;
				}
			}
		}

		return 0;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6)
	{
		Block hiddenBelow = world.getBlock(x, y-2, z);
		if(hiddenBelow == Blocks.redstone_wire)
			world.setBlockMetadataWithNotify(x, y-2, z, 15, 3);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return !this.invisible;
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
	{
		if(DragonArtifacts.boundingInvis) {
			return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
		}
		else {
			return null;
		}
	}

	//If it's a camouflaged block, get it's texture from the neighbors.
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		if(this.camouflaged) {

			if(side != 1) {
				return this == camoStone ? Blocks.stone.getBlockTextureFromSide(0) : this == camoObsidian ? Blocks.obsidian.getBlockTextureFromSide(0) : Blocks.planks.getBlockTextureFromSide(0);
			}

			IIcon icon = this.blockIcon;
			Block block = world.getBlock(x, y-1, z);
			int meta = world.getBlockMetadata(x, y-1, z);

			if(block != null) {
				if(block == BlockTrap.instance) {
					int[] blockToCopyLocation = BlockTrap.getNeighbourBlockPosition(world, x, y-1, z, block, meta);
					Block blockToCopy = Blocks.stonebrick;
					int metaToCopy = 0;
					if(! (blockToCopyLocation[0] == x && blockToCopyLocation[1] == y && blockToCopyLocation[2] == z )) {
						blockToCopy = world.getBlock(blockToCopyLocation[0], blockToCopyLocation[1], blockToCopyLocation[2]);
						metaToCopy = world.getBlockMetadata(blockToCopyLocation[0], blockToCopyLocation[1], blockToCopyLocation[2]);
					}
					return blockToCopy.getIcon(side, metaToCopy);
				}
				else if(block != this && block.isOpaqueCube()) {
					icon = block.getIcon(side, meta);
					if(icon != null) {
						return icon;
					}
				}
				else if(block == BlockIllusionary.instance) {
					return block.getIcon(world, x, y-1, z, side);
					
				}
			}
		}

		//If it fails, return default icon.
				return super.getIcon(world, x, y, z, side);

	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		if(this == invisObsidian)
			blockIcon = iconRegister.registerIcon("artifacts:obsinvisible");
		else if(this == invisStone)
			blockIcon = iconRegister.registerIcon("artifacts:invisible");
		else if(this == camoStone)
			blockIcon = iconRegister.registerIcon("artifacts:plate_camo_stone");
		else if(this == camoObsidian) 
			blockIcon = iconRegister.registerIcon("artifacts:plate_camo_obsidian");
		else if(this == camoWood)
			blockIcon = iconRegister.registerIcon("artifacts:plate_camo_wood");
		else
			blockIcon = Blocks.obsidian.getBlockTextureFromSide(0);
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return this.invisible ? 0 : 1;
	}
}
