package com.draco18s.artifacts;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;

public class DamageSourceSword extends DamageSource {
	public static DamageSource instance;

	public DamageSourceSword(String name) {
		super(name);
	}
}
