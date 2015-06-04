package com.draco18s.artifacts.components;

import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;
import com.draco18s.artifacts.DragonArtifacts;
import com.draco18s.artifacts.api.ArtifactsAPI;
import com.draco18s.artifacts.api.interfaces.IArtifactComponent;
import com.draco18s.artifacts.components.UtilsForComponents.Flags;
import com.draco18s.artifacts.network.CToSMessage;
import com.draco18s.artifacts.network.PacketHandlerServer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ComponentExplosive extends BaseComponent {

	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor) return "";
		String str = "";
		switch(rand.nextInt(5)) {
			case 0:
				str = "onBlockDestroyed";
				break;
			case 1:
				str = "hitEntity";
				break;
			case 2:
				str = "itemInteractionForEntity";
				break;
			case 3:
				str = "onItemRightClick";
				break;
			case 4:
				str = "onDropped";
				break;
		}
		return str;
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		int e = i.stackTagCompound.getInteger("onDropped");
		if(eff.length == 1 && e > 0 && ArtifactsAPI.artifacts.getComponent(e) == this) {
			i.stackSize = 10;
		}
		i.stackTagCompound.setInteger("droppedDelay", 240);
		return i;
	}
	
	@Override
	public float getDigSpeed(ItemStack par1ItemStack, Block par2Block, int meta) {
		if(par1ItemStack.stackTagCompound.getInteger("onBlockDestroyed") != 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world,	EntityPlayer player) {
		float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)f + 1.62D - (double)player.yOffset;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        MovingObjectPosition movingobjectposition = world.func_147447_a/*rayTraceBlocks_do_do*/(vec3, vec31, false, true, false);
        if (movingobjectposition == null) {
        	return par1ItemStack;
        }
    	PacketBuffer out =  new PacketBuffer(Unpooled.buffer());
        if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK)
        {
            int ix = movingobjectposition.blockX;
            int iy = movingobjectposition.blockY;
            int iz = movingobjectposition.blockZ;
            if (!world.getBlock(ix, iy, iz).isBlockNormalCube())
            {
                --iy;
            }
            /*EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, ix, iy, iz);
            world.addWeatherEffect(entityLightningBolt);*/
            //world.newExplosion(player, ix, iy, iz, 4, false, true);
			out.writeInt(PacketHandlerServer.EXPLOSIONS);
			out.writeInt(-1);
			out.writeInt(ix);
			out.writeInt(iy);
			out.writeInt(iz);
			out.writeInt(player.inventory.currentItem);
			CToSMessage packet = new CToSMessage(out);
			DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
			UtilsForComponents.sendItemDamagePacket(player, player.inventory.currentItem, 3);
        }
        if (movingobjectposition.typeOfHit == MovingObjectType.ENTITY) {
        	System.out.println("Hit entity");
        	System.out.println(movingobjectposition.hitVec);
        	double ix = movingobjectposition.hitVec.xCoord;
        	double iy = movingobjectposition.hitVec.yCoord;
        	double iz = movingobjectposition.hitVec.zCoord;
            /*EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, ix, iy, iz);
            world.addWeatherEffect(entityLightningBolt);*/
            //world.newExplosion(player, ix, iy, iz, 4, false, true);
			out.writeInt(PacketHandlerServer.EXPLOSIONS);
			out.writeInt(movingobjectposition.entityHit.getEntityId());
			out.writeInt(player.inventory.currentItem);
			CToSMessage packet = new CToSMessage(out);
			DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
			UtilsForComponents.sendItemDamagePacket(player, player.inventory.currentItem, 3);
        }
		par1ItemStack.stackTagCompound.setInteger("onItemRightClickDelay", 20);
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLivingHit, EntityLivingBase entityLivingPlayer) {
		if(!entityLivingPlayer.worldObj.isRemote && entityLivingPlayer instanceof EntityPlayer) {
			entityLivingHit.worldObj.newExplosion(entityLivingPlayer, entityLivingHit.posX, entityLivingHit.posY, entityLivingHit.posZ, 3F, false, true);	
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World world, Block block, int x, int y, int z, EntityLivingBase par7EntityLivingBase) {
		Block i = world.getBlock(x, y, z);
		int m = world.getBlockMetadata(x, y, z);
		i.dropBlockAsItem(world, x, y, z, m, 0);
		world.setBlockToAir(x, y, z);
		world.newExplosion(par7EntityLivingBase, x, y, z, 3F, false, true);
		par1ItemStack.damageItem(3, par7EntityLivingBase);
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entityLiving) {
		if(player.worldObj.isRemote) {
			PacketBuffer out =  new PacketBuffer(Unpooled.buffer());
			
			//System.out.println("Building packet...");
			out.writeInt(PacketHandlerServer.EXPLOSIONS);
			out.writeInt(player.getEntityId());
			out.writeInt(player.inventory.currentItem);
			//out.writeFloat(par3EntityPlayer.getHealth()+1);
			CToSMessage packet = new CToSMessage(out);
			//System.out.println("Sending packet..." + player);
			DragonArtifacts.artifactNetworkWrapper.sendToServer(packet);
			//par1ItemStack.damageItem(1, par2EntityPlayer);
			
			return true;

		}
		return false;
	}

	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Explodes") + " " + StatCollector.translateToLocal("tool."+trigger));
		if(trigger == "when dropped.") {
			par3List.add(EnumChatFormatting.YELLOW + "  8 " + StatCollector.translateToLocal("time.second") + " " + StatCollector.translateToLocal("time.fuse"));
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Explodes!");
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Fragmenting";
	}

	@Override
	public String getPostAdj(Random rand) {
		return "of Explosions";
	}

	@Override
	public int getTextureBitflags() {
		return Flags.DAGGER | Flags.STAFF | Flags.SWORD | Flags.WAND;
	}

	@Override
	public int getNegTextureBitflags() {
		return Flags.AMULET | Flags.FIGURINE | Flags.BELT | Flags.ARMOR;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		if(type == "onDropped") {
			entityItem.worldObj.newExplosion(entityItem, entityItem.posX, entityItem.posY, entityItem.posZ, 3F, false, true);
			//entityItem.setEntityItemStack(new ItemStack(Block.dirt));
			entityItem.getEntityItem().stackSize--;
			//entityItem.setEntityItemStack();
		}
		return false;
	}
}
