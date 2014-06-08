package com.draco18s.artifacts.block;

import com.draco18s.artifacts.api.ArtifactsAPI;
import com.draco18s.artifacts.entity.TileEntitySword;
import com.draco18s.artifacts.item.ItemFakeSwordRenderable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlockSword extends BlockContainer {
	public static Block instance;

	public BlockSword() {
		super(Material.rock);
		this.setBlockBounds(0.4f, 0.0f, 0.4f, 0.6f, 0.5f, 0.6f);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntitySword();
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}

	public int getRenderType()
	{
		return -1;
	}

	public boolean isOpaqueCube()
	{
		return false;
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
		return null;
    }
	
	public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3) {
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerBlockIcons(IIconRegister iconReg)
    {
		blockIcon = ItemFakeSwordRenderable.wood.getIcon(new ItemStack(ItemFakeSwordRenderable.wood), 0);
    }
}
