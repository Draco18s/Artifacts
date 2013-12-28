package draco18s.artifacts.components;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

public class ComponentAdrenaline implements IArtifactComponent {

	@Override
	public String getRandomTrigger(Random rand, boolean isArmor) {
		if(isArmor)
			return "onTakeDamage";
		return "";
	}

	@Override
	public ItemStack attached(ItemStack i, Random rand, int[] eff) {
		/*NBTTagCompound nbt = i.stackTagCompound;
		int n = nbt.getInteger("onHeld");
		nbt.setInteger("onHeld", 0);
		nbt.removeTag("onHeld");
		if(!nbt.hasKey("onTakeDamage") && !nbt.hasKey("onArmorTickUpdate")) {
			nbt.setInteger("onTakeDamage", n);
			nbt.setInteger("onArmorTickUpdate", n);
		}*/
		return i;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, World par3World, int par4, int par5,
			int par6, int par7, float par8, float par9, float par10) {
		
		return false;
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
		
		return 0;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World,
			EntityPlayer par3EntityPlayer) {
		
		return par1ItemStack;
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack,
			EntityLivingBase par2EntityLivingBase,
			EntityLivingBase par3EntityLivingBase) {
		
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World,
			int par3, int par4, int par5, int par6,
			EntityLivingBase par7EntityLivingBase) {
		
		return false;
	}

	@Override
	public boolean canHarvestBlock(Block par1Block, ItemStack itemStack) {
		
		return false;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase) {
		
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem, String type) {
		
		return false;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World,
			Entity par3Entity, int par4, boolean par5) {
		

	}

	@Override
	public void onHeld(ItemStack par1ItemStack, World par2World,
			Entity par3Entity, int par4, boolean par5) {
		

	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack, boolean worn) {
		if(worn) {
			if(itemStack.stackTagCompound.getInteger("adrenDelay_armor") == 199) {
				try
				{
					sendPotionPacket(4, 200, 1, player);
					sendPotionPacket(17, 200, 0, player);
				}
				catch (IOException ex)
				{
					System.out.println("couldnt send packet!");
				}
			}
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		
		return null;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
		

	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		par3List.add("Activates several potion effects after taking damage");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add("Activates several potion effects " + trigger);
	}

	@Override
	public String getPreAdj(Random rand) {
		return "Brawling";
	}

	@Override
	public String getPostAdj(Random rand) {
		return "of Rage";
	}

	@Override
	public int getTextureBitflags() {
		
		return 1024;
	}

	@Override
	public int getNegTextureBitflags() {
		
		return 6911;
	}

	@Override
	public void onTakeDamage(ItemStack itemStack, LivingHurtEvent event, boolean isWornArmor) {
		if(isWornArmor) {
			if(itemStack.stackTagCompound.getInteger("adrenDelay_armor") <= 0) {
				EntityPlayer p = (EntityPlayer)event.entity;
				if(p.getHealth() <= 4) {
					itemStack.stackTagCompound.setInteger("adrenDelay_armor", 300);
					event.setCanceled(true);
					try
					{
						sendPotionPacket(1, 100, 1, event.entity);
						sendPotionPacket(5, 100, 1, event.entity);
						sendPotionPacket(11, 100, 2, event.entity);
					}
					catch (IOException ex)
					{
						System.out.println("couldnt send packet!");
					}
				}
			}
		}
	}
	
	private void sendPotionPacket(int potionID, int duration, int level, Entity entity) throws IOException {
		ByteArrayOutputStream bt = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bt);
		out.writeInt(10);
		out.writeInt(entity.entityId);
		out.writeInt(potionID);
		out.writeInt(duration);
		out.writeInt(level);
		Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
		PacketDispatcher.sendPacketToServer(packet);
	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event, boolean isWornArmor) {
		
	}
}
