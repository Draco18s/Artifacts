package draco18s.artifacts.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

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
            			System.out.println("New owner: " + str);
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
}
