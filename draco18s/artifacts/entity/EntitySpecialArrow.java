package draco18s.artifacts.entity;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentThorns;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityOwnable;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet70GameEvent;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntitySpecialArrow extends EntityArrow {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private int inData = 0;
	private boolean inGround = false;
	private int ticksInGround = 0;
	private int ticksInAir = 0;
	private float knockbackStrength = 0;
	public ArrowEffect effect = ArrowEffect.DEFAULT;

	public EntitySpecialArrow(World world) {
		super(world);
	}

	public EntitySpecialArrow(World world, double x, double y, double z) {
		super(world, x, y, z);
	}
	public EntitySpecialArrow(World world, double x, double y, double z, double damage) {
		super(world, x, y, z);
		setDamage(damage);
	}

	public EntitySpecialArrow(World world, EntityLivingBase shooter, EntityLivingBase target, float power, float variance) {
		super(world, shooter, target, power, variance);
	}

	public EntitySpecialArrow(World world, EntityLivingBase shooter, EntityLivingBase target, float power, float variance, double damage) {
		super(world, shooter, target, power, variance);
		setDamage(damage);
	}

	public EntitySpecialArrow(World world, EntityLivingBase shooter, float power) {
		super(world, shooter, power);
	}

	public EntitySpecialArrow(World world, EntityLivingBase shooter, float power, double damage) {
		super(world, shooter, power);
		setDamage(damage);
	}

	public EntitySpecialArrow(World world, EntityLivingBase shooter, EntityLivingBase target, float power, float variance, ArrowEffect fx) {
		super(world, shooter, target, power, variance);
		this.effect = fx;
		setStatsByEffect();
	}
	public EntitySpecialArrow(World world, EntityLivingBase shooter, EntityLivingBase target, float power, float variance, double damage, ArrowEffect fx) {
		super(world, shooter, target, power, variance);
		setDamage(damage);
		this.effect = fx;
		setStatsByEffect();
	}

	public EntitySpecialArrow(World world, EntityLivingBase shooter, float power, ArrowEffect fx) {
		super(world, shooter, power);
		this.effect = fx;
		setStatsByEffect();
	}
	public EntitySpecialArrow(World world, EntityLivingBase shooter, float power, double damage, ArrowEffect fx) {
		super(world, shooter, power);
		setDamage(damage);
		this.effect = fx;
		setStatsByEffect();
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(17, "");
	}

	public void onUpdate()
	{
		onEntityUpdate();
		if ((this.prevRotationPitch == 0.0F) && (this.prevRotationYaw == 0.0F)) {
			float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
			this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(this.motionY, var1) * 180.0D / 3.141592653589793D));
		}
		int var16 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
		if (var16 > 0) {
			Block.blocksList[var16].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
			AxisAlignedBB var2 = Block.blocksList[var16].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);
			if ((var2 != null) && (var2.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ))))
				this.inGround = true;
		}
		if (this.arrowShake > 0)
			this.arrowShake -= 1;
		if (this.inGround) {
			int var18 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
			int var19 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
			if ((var18 == this.inTile) && (var19 == this.inData)) {
				this.ticksInGround += 1;
				if (this.ticksInGround == 1200)
					setDead();
			}
			else {
				this.inGround = false;
				this.motionX *= this.rand.nextFloat() * 0.2F;
				this.motionY *= this.rand.nextFloat() * 0.2F;
				this.motionZ *= this.rand.nextFloat() * 0.2F;
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}
		}
		else {
			this.ticksInAir += 1;
			Vec3 var17 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			Vec3 var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition var4 = this.worldObj.rayTraceBlocks_do_do(var17, var3, false, true);
			var17 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			if (var4 != null)
				var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
			Entity var5 = null;
			List var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double var7 = 0.0D;

			for (int var9 = 0; var9 < var6.size(); var9++) {
				Entity var10 = (Entity)var6.get(var9);
				if ((var10.canBeCollidedWith()) && ((var10 != this.shootingEntity) || (this.ticksInAir >= 5))) {
					float var11 = 0.3F;
					AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
					MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);
					if (var13 != null) {
						double var14 = var17.distanceTo(var13.hitVec);
						if ((var14 < var7) || (var7 == 0.0D)) {
							var5 = var10;
							var7 = var14;
						}
					}
				}
			}
			if (var5 != null) {
				var4 = new MovingObjectPosition(var5);
			}

			if (var4 != null) {
				if (var4.entityHit != null) {
					float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					int var23 = MathHelper.ceiling_double_int(var20 * getDamage() * 1.5);
					if (getIsCritical())
						var23 += this.rand.nextInt(var23 / 2 + 2);
					DamageSource var21 = null;
					if (this.shootingEntity == null)
						var21 = DamageSource.causeArrowDamage(this, this);
					else
						var21 = DamageSource.causeArrowDamage(this, this.shootingEntity);
					if ((isBurning()) && (!(var4.entityHit instanceof EntityEnderman)))
						var4.entityHit.setFire(5);
					if((shouldDamage(var4.entityHit))) {
						var4.entityHit.hurtResistantTime = 0;
						if ((var4.entityHit.attackEntityFrom(var21, var23))) {
							hitEffect(var4.entityHit);
							if ((var4.entityHit instanceof EntityLivingBase)) {
								EntityLivingBase var24 = (EntityLivingBase)var4.entityHit;
								if (!this.worldObj.isRemote)
									var24.setArrowCountInEntity(var24.getArrowCountInEntity() + 1);
								if (this.knockbackStrength > 0) {
									float var26 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
									if (var26 > 0.0F) {
										var4.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6D / var26, 0.1D, this.motionZ * this.knockbackStrength * 0.6D / var26);
									}
								}
								if (this.shootingEntity != null)
									EnchantmentThorns.func_92096_a(this.shootingEntity, var24, this.rand);
								if ((this.shootingEntity != null) && (var4.entityHit != this.shootingEntity) && ((var4.entityHit instanceof EntityPlayer)) && ((this.shootingEntity instanceof EntityPlayerMP)))
									((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
							}
							playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
							if (!(var4.entityHit instanceof EntityEnderman))
								setDead();
						}
					}
					else {
						this.motionX *= -0.1D;
						this.motionY *= -0.1D;
						this.motionZ *= -0.1D;
						this.rotationYaw += 180.0F;
						this.prevRotationYaw += 180.0F;
						this.ticksInAir = 0;
					}
				}
				else {
					this.xTile = var4.blockX;
					this.yTile = var4.blockY;
					this.zTile = var4.blockZ;
					this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
					this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
					missEffect(this.xTile, this.yTile, this.zTile);
					this.motionX = ((float)(var4.hitVec.xCoord - this.posX));
					this.motionY = ((float)(var4.hitVec.yCoord - this.posY));
					this.motionZ = ((float)(var4.hitVec.zCoord - this.posZ));
					float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / var20 * 0.05D;
					this.posY -= this.motionY / var20 * 0.05D;
					this.posZ -= this.motionZ / var20 * 0.05D;
					playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.inGround = true;
					this.arrowShake = 7;
					setIsCritical(false);
					if (this.inTile != 0)
						Block.blocksList[this.inTile].onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
				}
			}
			if (getIsCritical()) {
				for (int var9 = 0; var9 < 4; var9++)
					this.worldObj.spawnParticle("crit", this.posX + this.motionX * var9 / 4.0D, this.posY + this.motionY * var9 / 4.0D, this.posZ + this.motionZ * var9 / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
			}
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = ((float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
			for (this.rotationPitch = ((float)(Math.atan2(this.motionY, var20) * 180.0D / 3.141592653589793D)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
			while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
				this.prevRotationPitch += 360.0F;
			while (this.rotationYaw - this.prevRotationYaw < -180.0F)
				this.prevRotationYaw -= 360.0F;
			while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
				this.prevRotationYaw += 360.0F;
			this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
			this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
			float var22 = 0.99F;
			float var11 = 0.05F;
			if (isInWater()) {
				for (int var25 = 0; var25 < 4; var25++) {
					float var26 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * var26, this.posY - this.motionY * var26, this.posZ - this.motionZ * var26, this.motionX, this.motionY, this.motionZ);
				}
				var22 = 0.8F;
			}
			this.motionX *= var22;
			this.motionY *= var22;
			this.motionZ *= var22;
			this.motionY -= var11;
			setPosition(this.posX, this.posY, this.posZ);
			doBlockCollisions();
		}
	}

	public boolean shouldDamage(Entity entity)
	{
		return (entity != this.shootingEntity);
	}

	public void writeEntityToNBT(NBTTagCompound tag)
	{
		super.writeEntityToNBT(tag);
		tag.setShort("xTile", (short)this.xTile);
		tag.setShort("yTile", (short)this.yTile);
		tag.setShort("zTile", (short)this.zTile);
		tag.setByte("inTile", (byte)this.inTile);
		tag.setByte("inData", (byte)this.inData);
		tag.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		tag.setByte("effect", this.effect.ID);
	}

	public void readEntityFromNBT(NBTTagCompound tag)
	{
		super.readEntityFromNBT(tag);
		this.xTile = tag.getShort("xTile");
		this.yTile = tag.getShort("yTile");
		this.zTile = tag.getShort("zTile");
		this.inTile = (tag.getByte("inTile") & 0xFF);
		this.inData = (tag.getByte("inData") & 0xFF);
		this.inGround = (tag.getByte("inGround") == 1);
		this.effect = ArrowEffect.get(tag.getByte("effect"));
	}

	public void setKnockbackStrength(float strength)
	{
		this.knockbackStrength = strength;
	}

	public void setStatsByEffect() {
		switch (this.effect.ID) {
			case 0:
				setKnockbackStrength(1);
				break;
			case 1:
				setKnockbackStrength(0);
				break;
		}
	}

	public void hitEffect(Entity entityHit) {
		switch (this.effect.ID) {
			case 1:
				Explosion explosion = new Explosion(this.worldObj, this, this.posX, this.posY, this.posZ, 2F);
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				break;
		}
	}

	public void missEffect(int x, int y, int z) {
		switch (this.effect.ID) {
			case 1:
				Explosion explosion = new Explosion(this.worldObj, this, this.posX, this.posY, this.posZ, 2F);
				explosion.doExplosionA();
				explosion.doExplosionB(true);
				setDead();
				break;
		}
	}
	
	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Item.arrow, 1)))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }
}