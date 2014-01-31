package draco18s.artifacts.block;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import cpw.mods.fml.common.network.PacketDispatcher;

import draco18s.artifacts.DragonArtifacts;
import draco18s.artifacts.entity.TileEntityDisplayPedestal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockPedestal extends BlockContainer {
	public static BlockContainer instance;

	public BlockPedestal(int par1) {
		super(par1, Material.rock);
		setCreativeTab(CreativeTabs.tabDecorations);
		setUnlocalizedName("Display Pedestal");
		setResistance(10F);
		setStepSound(soundGlassFootstep);
		setHardness(5.0F);
        setLightOpacity(0);
		setBlockBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
	}
	
	public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("artifacts:pedestal_icon");
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityDisplayPedestal();
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
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
		if(!world.isRemote) {
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if(te instanceof TileEntityDisplayPedestal && par5EntityLivingBase instanceof EntityPlayer) {
				TileEntityDisplayPedestal ted = (TileEntityDisplayPedestal)te;
				EntityPlayer player = (EntityPlayer)par5EntityLivingBase;
				ted.owner = player.username;
				
				ByteArrayOutputStream bt = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(bt);
				try
				{
					out.writeInt(256);
					out.writeInt(x);
					out.writeInt(y);
					out.writeInt(z);
					out.writeInt(ted.owner.length());
					out.writeChars(ted.owner);
					Packet250CustomPayload packet = new Packet250CustomPayload("Artifacts", bt.toByteArray());
					PacketDispatcher.sendPacketToAllAround(x, y, z, 35, world.provider.dimensionId, packet);
				}
				catch (IOException ex)
				{
					System.out.println("couldnt send packet!");
				}
				
				ted.rotation = ((int) ((MathHelper.floor_double((double)((player.rotationYaw) * 16.0F / 360.0F) + 0.5D) & 15) / 4)) * 90;
			}
		}
    }

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			player.openGui(DragonArtifacts.instance, 0, world, x, y, z);
			return true;
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	private void dropItems(World world, int x, int y, int z){
		Random rand = new Random();

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world,
						x + rx, y + ry, z + rz,
						new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5)
    {
		TileEntity te = par2World.getBlockTileEntity(par3, par4, par5);
		TileEntityDisplayPedestal ted = (TileEntityDisplayPedestal)te;
		/*if(!par2World.isRemote) {
			System.out.println("owner?" + par1EntityPlayer.username.equals(ted.owner));
			System.out.println(" - " + ted.owner);
		}*/
		if(par2World.isRemote) {
			//System.out.println(ted.owner);
		}
		if((par1EntityPlayer.username.equals(ted.owner) || ted.owner.equals(""))) {
	        //if(par2World.isRemote)
				//System.out.println("breaking");
			float f = this.getBlockHardness(par2World, par3, par4, par5);
	        return ForgeHooks.blockStrength(this, par1EntityPlayer, par2World, par3, par4, par5);
		}
		else {
			return 0;
		}
    }
}
