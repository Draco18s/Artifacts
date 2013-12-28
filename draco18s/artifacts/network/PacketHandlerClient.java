package draco18s.artifacts.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import draco18s.artifacts.entity.TileEntityDisplayPedestal;
import draco18s.artifacts.entity.TileEntityTrap;

public class PacketHandlerClient implements IPacketHandler{
	Random rand = new Random();
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
    	//System.out.println("Packet found: " + packet.channel);
        if (packet.channel.equals("Artifacts"))
        {
            handleRandom(packet, player);
        }
    }

    private void handleRandom(Packet250CustomPayload packet, Player player)
    {
        EntityPlayer p = (EntityPlayer) player;
        World world = p.worldObj;
        ByteArrayInputStream stream = new ByteArrayInputStream(packet.data);
        DataInputStream dis = new DataInputStream(stream);
        //System.out.println("Packet get");
        TileEntity te;
        try
        {
            int effectID = dis.readInt();
            switch(effectID) {
	        	case 23:
	        		int x = dis.readInt();
	        		int y = dis.readInt();
	        		int z = dis.readInt();
	        		//if(rand.nextBoolean() && rand.nextBoolean())
	        			drawParticleLine(p.worldObj, x+.5, y+.5, z+.5, p.posX, p.posY+.3, p.posZ);
	        		//System.out.println("Server particles");
	        		break;
            	case 256:
            		te = world.getBlockTileEntity(dis.readInt(), dis.readInt(), dis.readInt());
            		if(te instanceof TileEntityDisplayPedestal) {
            			TileEntityDisplayPedestal ted = (TileEntityDisplayPedestal)te;
            			/*InputStreamReader reader = new InputStreamReader(stream);
            			BufferedReader br = new BufferedReader(reader);
            			String str = br.readLine();*/
            			String str = "";
            			for(int s = dis.readInt()-1; s >= 0; s--) {
            				str += dis.readChar();
            			}
            			//String str = dis.readLine();
            			//System.out.println("New owner: " + str);
            		}
            		break;
            	default:
            		return;
            }
        }
        catch  (IOException e)
        {
            System.out.println("Failed to read packet");
        }
        finally
        {
        }
    }
    
    protected void drawParticleLine(World worldObj, double srcX, double srcY, double srcZ, double destX, double destY, double destZ)
    {
    	int particles = 8;
    	for (int i = 0; i < particles; i++)
    	{
    		double trailFactor = i / (particles - 1.0D);
    		double tx = srcX + (destX - srcX) * trailFactor;// + rand.nextFloat() * 0.005D;
    		double ty = srcY + (destY - srcY) * trailFactor;// + rand.nextFloat() * 0.005D;
    		double tz = srcZ + (destZ - srcZ) * trailFactor;// + rand.nextFloat() * 0.005D;
    		worldObj.spawnParticle("reddust", tx, ty, tz, 1.0D, 1.0D, 1.0D);
    	}
    }
}
