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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInvisiblePressurePlate extends BlockBasePressurePlate {
	public static Block instance;
	public static Block obsidian;
    private BlockPressurePlate.Sensitivity triggerMobType;

	public BlockInvisiblePressurePlate(String name, Material material, BlockPressurePlate.Sensitivity mobType) {
		super(name, material);
        this.triggerMobType = mobType;
        setHardness(0.5F);
        setStepSound(Block.soundTypeStone);
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
            list = world.getEntitiesWithinAABB(EntityLiving.class, this.func_150061_a(x, y, z));
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
		return DragonArtifacts.renderInvis;
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
	
	@Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
    	if(this.triggerMobType == BlockPressurePlate.Sensitivity.players)
    		blockIcon = iconRegister.registerIcon("artifacts:obsinvisible");
    	else
    		blockIcon = iconRegister.registerIcon("artifacts:invisible");
    }
	
	@Override
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
