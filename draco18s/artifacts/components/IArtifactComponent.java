package draco18s.artifacts.components;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Multimap;

import draco18s.artifacts.item.FactoryArtifact;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public interface IArtifactComponent {
	public String getRandomTrigger(Random rand);
	
	public ItemStack attached(ItemStack i, Random rand);
	
	public Icon getIcon(ItemStack stack, int pass);
	/**
     * Called when a player drops the item into the world,
     * returning false from this will prevent the item from
     * being removed from the players inventory and spawning
     * in the world
     *
     * @param player The player that dropped the item
     * @param item The item stack, before the item is removed.
     */
    public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player);
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10);
	/**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block);
	/**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);
	/**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase);
	/**
     * Called when a block is broken with the ItemStack active
     */
    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase);
    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canHarvestBlock(Block par1Block, ItemStack itemStack);
    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, EntityLivingBase par3EntityLivingBase);
    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public boolean onEntityItemUpdate(EntityItem entityItem);
    /**
     * Called by the default implemetation of EntityItem's onUpdate method, allowing for cleaner 
     * control over the update of the item without having to write a subclass.
     * 
     * @param entityItem The entity Item
     * @param type onUpdate vs. onEntityItemUpdate
     * @return Return true to skip any further update code.
     */
    public boolean onEntityItemUpdate(EntityItem entityItem, String type);
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5);
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onHeld(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5);
    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack);
    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4);
    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean advTooltip);
    /**
     * allows items to add custom lines of information to the mouseover description, takes a String for the trigger type
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, String trigger, boolean advTooltip);

	public String getName();
	
	public String getPreAdj(Random rand);
	
	public String getPostAdj(Random rand);
	
	public int getTextureBitflags();
	
	public int getNegTextureBitflags();
}
