package draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraft.network.packet.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ComponentExplosive implements IArtifactComponent {

	public ComponentExplosive() {
	}
	
	public String getRandomTrigger(Random rand, boolean isArmor) {
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
	public ItemStack attached(ItemStack i, Random rand) {
		i.stackTagCompound.setInteger("droppedDelay", 240);
		return i;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
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
        Vec3 vec3 = world.getWorldVec3Pool().getVecFromPool(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
        MovingObjectPosition movingobjectposition = world.rayTraceBlocks_do_do(vec3, vec31, false, true);
        if (movingobjectposition == null) {
        	return par1ItemStack;
        }
    	ByteArrayOutputStream bt = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bt);
        if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
        {
            int ix = movingobjectposition.blockX;
            int iy = movingobjectposition.blockY;
            int iz = movingobjectposition.blockZ;
            if (!world.isBlockFullCube(ix, iy, iz))
            {
                --iy;
            }
            /*EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, ix, iy, iz);
            world.addWeatherEffect(entityLightningBolt);*/
            //world.newExplosion(player, ix, iy, iz, 4, false, true);
            try
			{
				out.writeInt(7);
				out.writeInt(-1);
				out.writeInt(ix);
				out.writeInt(iy);
				out.writeInt(iz);
				out.writeInt(player.inventory.currentItem);
				Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
				PacketDispatcher.sendPacketToServer(packet);
				//par1ItemStack.damageItem(3, player);
			}
			catch (IOException ex)
			{
				System.out.println("couldnt send packet!");
			}
        }
        if (movingobjectposition.typeOfHit == EnumMovingObjectType.ENTITY) {
        	System.out.println("Hit entity");
        	System.out.println(movingobjectposition.hitVec);
        	double ix = movingobjectposition.hitVec.xCoord;
        	double iy = movingobjectposition.hitVec.yCoord;
        	double iz = movingobjectposition.hitVec.zCoord;
            /*EntityLightningBolt entityLightningBolt = new EntityLightningBolt(world, ix, iy, iz);
            world.addWeatherEffect(entityLightningBolt);*/
            //world.newExplosion(player, ix, iy, iz, 4, false, true);
			try
			{
				out.writeInt(7);
				out.writeInt(movingobjectposition.entityHit.entityId);
				out.writeInt(player.inventory.currentItem);
				Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
				PacketDispatcher.sendPacketToServer(packet);
				par1ItemStack.damageItem(3, player);
			}
			catch (IOException ex)
			{
				System.out.println("couldnt send packet!");
			}
        }
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
		if(par3EntityLivingBase.worldObj.isRemote) {
			if(par2EntityLivingBase.hurtTime == 0) {
				ByteArrayOutputStream bt = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bt);
				try
				{
					out.writeInt(7);
					out.writeInt(par2EntityLivingBase.entityId);
					EntityPlayer par3EntityPlayer = (EntityPlayer) par3EntityLivingBase;
					out.writeInt(par3EntityPlayer.inventory.currentItem);
					Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
					Player player = (Player)par3EntityLivingBase;
					PacketDispatcher.sendPacketToServer(packet);
					par1ItemStack.damageItem(3, par3EntityLivingBase);
				}
				catch (IOException ex)
				{
					System.out.println("couldnt send packet!");
				}
			}
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World world, int block, int x, int y, int z, EntityLivingBase par7EntityLivingBase) {
		world.newExplosion(par7EntityLivingBase, x, y, z, 3F, false, true);
		par1ItemStack.damageItem(3, par7EntityLivingBase);
		/*ByteArrayOutputStream bt = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bt);
		try
		{
			//System.out.println("Building packet...");
			out.writeInt(7);
			out.writeInt(-1);
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(z);
			//out.writeFloat(par3EntityPlayer.getHealth()+1);
			Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
			Player player = (Player)par7EntityLivingBase;
			//System.out.println("Sending packet..." + player);
			PacketDispatcher.sendPacketToServer(packet);
			par1ItemStack.damageItem(3, par7EntityLivingBase);
			return true;
		}
		catch (IOException ex)
		{
			System.out.println("couldnt send packet!");
		}*/
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		ByteArrayOutputStream bt = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bt);
		try
		{
			//System.out.println("Building packet...");
			out.writeInt(7);
			out.writeInt(par2EntityPlayer.entityId);
			out.writeInt(par2EntityPlayer.inventory.currentItem);
			//out.writeFloat(par3EntityPlayer.getHealth()+1);
			Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
			//System.out.println("Sending packet..." + player);
			PacketDispatcher.sendPacketToServer(packet);
			//par1ItemStack.damageItem(1, par2EntityPlayer);
			return true;
		}
		catch (IOException ex)
		{
			System.out.println("couldnt send packet!");
		}
		return false;
	}

	//works great
	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.none;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add("Explodes " + trigger);
		if(trigger == "when dropped") {
			par3List.add(EnumChatFormatting.YELLOW + "  8 second fuse");
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
		return 178;
	}

	@Override
	public int getNegTextureBitflags() {
		return 261;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		return false;
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

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) { }
}
