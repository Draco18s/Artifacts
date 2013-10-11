package draco18s.artifacts.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import draco18s.artifacts.DragonArtifacts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInvisiblePressurePlate extends BlockBasePressurePlate {
	public static Block instance;
	public static Block obsidian;
    private EnumMobType triggerMobType;

	public BlockInvisiblePressurePlate(int par1, String par2Str, Material par2Material, EnumMobType par4EnumMobType) {
		super(par1, par2Str, par2Material);
        this.triggerMobType = par4EnumMobType;
        setHardness(0.5F);
        setStepSound(soundStoneFootstep);
        setCreativeTab(DragonArtifacts.tabTraps);
	}

	protected int getMetaFromWeight(int par1)
    {
        return par1 > 0 ? 1 : 0;
    }

    protected int getPowerSupply(int par1)
    {
        return par1 == 1 ? 15 : 0;
    }

    protected int getPlateState(World par1World, int par2, int par3, int par4)
    {
        List list = null;

        if (this.triggerMobType == EnumMobType.everything)
        {
            list = par1World.getEntitiesWithinAABBExcludingEntity((Entity)null, this.getSensitiveAABB(par2, par3, par4));
        }

        if (this.triggerMobType == EnumMobType.mobs)
        {
            list = par1World.getEntitiesWithinAABB(EntityLiving.class, this.getSensitiveAABB(par2, par3, par4));
        }

        if (this.triggerMobType == EnumMobType.players)
        {
            list = par1World.getEntitiesWithinAABB(EntityPlayer.class, this.getSensitiveAABB(par2, par3, par4));
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
    
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	if(par1World.getBlockId(par2, par3-2, par4) == Block.redstoneWire.blockID)
    		par1World.setBlockMetadataWithNotify(par2, par3-2, par4, 15, 3);
    }
    
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
    public void registerIcons(IconRegister iconRegister)
    {
    	if(this.triggerMobType == EnumMobType.players)
    		blockIcon = iconRegister.registerIcon("artifacts:obsinvisible");
    	else
    		blockIcon = iconRegister.registerIcon("artifacts:invisible");
    }
	
	public int quantityDropped(Random par1Random)
    {
        return 0;
    }
}
