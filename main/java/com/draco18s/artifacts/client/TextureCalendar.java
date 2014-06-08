package com.draco18s.artifacts.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;

@SideOnly(Side.CLIENT)
public class TextureCalendar extends TextureAtlasSprite
{
    //private double field_94239_h;
    //private double field_94240_i;
	private int netherAnim = 0;

    public TextureCalendar(String par1Str)
    {
        super(par1Str);
    }

    public void updateAnimation()
    {
        if (!this.framesTextureData.isEmpty())
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            
            int i = frameCounter;
            if (minecraft.theWorld != null && minecraft.thePlayer != null)
            {
            	if (!minecraft.theWorld.provider.isSurfaceWorld())
                {
            		netherAnim++;
            		if(netherAnim > 8) {
            			netherAnim = 0;
	                    i += (Math.random()*3)-1;
	                    if(i < 0)
	                    	i += 8;
	                    if(i > 7)
	                    	i -= 8;
            		}
                }
            	else {
            		i = minecraft.theWorld.getMoonPhase();
            	}
	            //System.out.println("Moon: " + minecraft.theWorld.getMoonPhase());
            }
            if (i != this.frameCounter)
            {
	            //System.out.println("Moon: " + i + "," + minecraft.theWorld.getCurrentMoonPhaseFactor());
                this.frameCounter = i;
                TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
                //TextureUtil.uploadTextureSub((int[])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}
