package com.draco18s.artifacts.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySpikes extends TileEntity {
	public int isBloody;
	public int bloodTime;
	public String modelTexture1;
	public String modelTexture2;
	public String modelTexture3;
	
	public TileEntitySpikes() {
		isBloody = 0;
		bloodTime = 0;
		modelTexture1 = "artifacts:textures/entities/spike.png";
		modelTexture2 = "artifacts:textures/entities/spike2.png";
		modelTexture3 = "artifacts:textures/entities/spike3.png";
	}
	
	public void setBloody(int t) {
		if(t > 0) {
			bloodTime = 3000*t;
		}
		isBloody = t;
	}
	
	@Override
	public void updateEntity() {
		if(isBloody > 0) {
			bloodTime--;
			if(bloodTime % 3000 == 0) {
				isBloody--;
			}
		}
	}

	public String getModelTexture() {
		if(isBloody == 0) {
			return modelTexture1;
		}
		else if(isBloody == 1) {
			return modelTexture2;
		}
		else
			return modelTexture3;
	}
}
