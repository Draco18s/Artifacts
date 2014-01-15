package draco18s.artifacts;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.EnumSet;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Session;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import draco18s.artifacts.item.ItemOrichalcumDust;

public class ArtifactTickHandler implements ITickHandler {
    private World world;
    private EntityPlayer eobj;
    public static ArtifactTickHandler instance;
    private boolean shouldRun = false;
    private int lastLevel = 0;
    private float trigger1 = 0F;
    private float trigger2 = 1F;
    private int trigger3 = 18000;
    private int trigger4 = 2;
    private boolean randomized = false;

	public ArtifactTickHandler() {
		instance = this;
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(!shouldRun)
			return;
		if(eobj.openContainer == null || eobj.openContainer == eobj.inventoryContainer) {
			shouldRun = false;
			float state1 = world.getCurrentMoonPhaseFactor();
			long state2 = world.getWorldTime();
			int calc = (int) Math.abs((state2%24000) - trigger3);
			boolean flag = ((state1 == trigger1 || state1 == trigger2) && calc < 3000);
			if(flag) {
				if(eobj.experienceLevel >= trigger4 && lastLevel > eobj.experienceLevel) {
					int m = Math.min((eobj.experienceLevel+1)/3, 4);
					eobj.addExperienceLevel(-1*m);
					EntityItem ent = new EntityItem(world, eobj.posX, eobj.posY, eobj.posZ, new ItemStack(ItemOrichalcumDust.instance, m));
					world.spawnEntityInWorld(ent);
				}
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return null;
	}
	
	public void readyTickHandler(World w, EntityPlayer pl) {
		world = w;
		eobj = pl;
		shouldRun = true;
		lastLevel = eobj.experienceLevel;
		if(!randomized) {
			Random r = new Random(world.provider.getSeed());
			trigger3 = r.nextInt(4) * 6000;
			trigger1 = r.nextFloat();
			trigger2 = 1 - trigger1;
		}
		randomized = true;
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		
	}
}
