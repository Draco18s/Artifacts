package com.draco18s.artifacts.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

public class SPacketGeneral extends AbstractPacket{
	private String type;
	private byte[] data;
	
	public SPacketGeneral() 
	{
		this("testName", new byte[]{0});
	}
	
	public SPacketGeneral(String nameToSet, ByteBuf dataToSet)
    {
        this(nameToSet, dataToSet.array());
    }

    public SPacketGeneral(String nameToSet, byte[] dataToSet)
    {
        
        if (dataToSet.length > 0x1ffff0)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
        //System.out.println("Creating General Packet!");
        this.type = nameToSet;
        this.data = dataToSet;

    }

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//System.out.println("Encoding General Packet!");
        
		if (data.length > 0x1ffff0)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
		
		int stringLength = this.type.length();
		buffer.writeInt(stringLength); 
		for(int i = 0; i < stringLength; i++){
			buffer.writeChar(this.type.charAt(i));
		}
        buffer.writeShort(this.data.length);
        buffer.writeBytes(this.data);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//System.out.println("Decoding General Packet!");
        
		int stringLength = buffer.readInt();
		char[] stringArray = new char[stringLength];
		for(int i = 0; i < stringLength; i++){
			stringArray[i] = buffer.readChar();
		}
		this.type = String.valueOf(stringArray);
		this.data = new byte[buffer.readShort()];
        buffer.readBytes(this.data);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		PacketHandlerClient.handleGeneralPacket(this);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		
	}
	
    public byte[] getData() {
        return this.data;
    }
	
    public String getInfo() {
        return this.type;
    }
}
