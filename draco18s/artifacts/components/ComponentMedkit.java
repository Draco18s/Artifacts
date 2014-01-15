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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import draco18s.artifacts.api.interfaces.IArtifactComponent;

public class ComponentMedkit implements IArtifactComponent {

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
			if(itemStack.stackTagCompound.getInteger("medkitDelay_armor") == 1) {
				ByteArrayOutputStream bt = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bt);
				try
				{
					//System.out.println("Building packet...");
					out.writeInt(1);
					out.writeFloat(1);
					Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
					PacketDispatcher.sendPacketToServer(packet);
					//itemStack.damageItem(1, player);
				}
				catch (IOException ex)
				{
					System.out.println("couldnt send packet!");
				}
			}
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip) {
		//par3List.add("Heals for a half-heart ten seconds after taking damage");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip) {
		par3List.add(StatCollector.translateToLocal("effect.Heals for a half-heart"));
		par3List.add("   10" + StatCollector.translateToLocal("time.seconds") + " " + StatCollector.translateToLocal("tool."+trigger));
	}

	@Override
	public String getPreAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "Healing";
				break;
			case 1:
				str = "Reinvigorating";
				break;
			case 2:
				str = "Staunching";
				break;
		}
		return str;
	}

	@Override
	public String getPostAdj(Random rand) {
		String str = "";
		switch(rand.nextInt(2)) {
			case 0:
				str = "of Healing";
				break;
			case 1:
				str = "of Rejuvenation";
				break;
			case 2:
				str = "of First Aid";
				break;
		}
		return str;
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
			if(itemStack.stackTagCompound.getInteger("medkitDelay_armor") <= 0)
				itemStack.stackTagCompound.setInteger("medkitDelay_armor", 200);
		}
	}

	@Override
	public void onDeath(ItemStack itemStack, LivingDeathEvent event,
			boolean isWornArmor) {
		

	}

}
