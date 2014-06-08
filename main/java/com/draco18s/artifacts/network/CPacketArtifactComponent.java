package com.draco18s.artifacts.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CPacketArtifactComponent extends AbstractPacket {
	private String username;
	private byte[] data;
	
	public CPacketArtifactComponent() 
	{
		this("testName", new byte[]{0});
	}

	public CPacketArtifactComponent(String nameToSet, ByteBuf dataToSet)
    {
        this(nameToSet, dataToSet.array());
    }

    public CPacketArtifactComponent(String nameToSet, byte[] dataToSet)
    {
        
        if (dataToSet.length > 0x1ffff0)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
        
        //System.out.println("Creating Component Packet!");
        
        this.username = nameToSet;
        this.data = dataToSet;

    }
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//System.out.println("Encoding Component Packet!");
		
		if (data.length > 0x1ffff0)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
		
		int stringLength = this.username.length();
		buffer.writeInt(stringLength); 
		for(int i = 0; i < stringLength; i++){
			buffer.writeChar(this.username.charAt(i));
		}
        buffer.writeShort(this.data.length);
        buffer.writeBytes(this.data);
		
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//System.out.println("Reading Component Packet!");
		
		int stringLength = buffer.readInt();
		char[] stringArray = new char[stringLength];
		for(int i = 0; i < stringLength; i++){
			stringArray[i] = buffer.readChar();
		}
		this.username = String.valueOf(stringArray);
		this.data = new byte[buffer.readShort()];
        buffer.readBytes(this.data);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		//Nothing client-side.
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		PacketHandlerServer.handleComponentPacket(this);
	}
	
    public byte[] getData() {
        return this.data;
    }
	
	public String getUsername() {
		return this.username;
	}

}
