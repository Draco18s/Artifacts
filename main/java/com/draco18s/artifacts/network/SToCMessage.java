package com.draco18s.artifacts.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

public class SToCMessage implements IMessage{
	private byte[] data;
	
	public SToCMessage() 
	{
		this(new byte[0]);
	}
	
	public SToCMessage(ByteBuf dataToSet)
    {
        this(dataToSet.array());
    }

    public SToCMessage(byte[] dataToSet)
    {
        
        if (dataToSet.length > 0x1ffff0)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
        //System.out.println("Creating General Packet!");
        this.data = dataToSet;

    }

    /**
     * Deconstruct your message into the supplied byte buffer
     * @param buf
     */
	@Override
	public void toBytes(ByteBuf buffer) {
		//System.out.println("Encoding General Packet!");
        
		if (data.length > 0x1ffff0)
        {
            throw new IllegalArgumentException("Payload may not be larger than 2097136 (0x1ffff0) bytes");
        }
		
        buffer.writeShort(this.data.length);
        buffer.writeBytes(this.data);
		
	}

	/**
     * Convert from the supplied buffer into your specific message type
     * @param buffer 
     */
	@Override
	public void fromBytes(ByteBuf buffer) {
		//System.out.println("Decoding General Packet!");
        
		this.data = new byte[buffer.readShort()];
        buffer.readBytes(this.data);
	}
	
    public byte[] getData() {
        return this.data;
    }
}
