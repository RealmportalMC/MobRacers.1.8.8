package me.winterguardian.core.entity.custom.rideable.v1_9_R1;

import me.winterguardian.core.entity.custom.rideable.RideableEntity;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.lang.reflect.Field;

public class RideableChicken extends EntityChicken implements RideableEntity
{
	private float climbHeight, jumpHeight, jumpThrust, speed, backwardSpeed, sidewaySpeed;

	public RideableChicken(org.bukkit.World world)
	{
		this(((CraftWorld)world).getHandle());
	}

	public RideableChicken(World world)
	{
		super(world);
		this.climbHeight = 1f;
		this.jumpHeight = 1f;
		this.jumpThrust = 1f;
		this.speed = 1f;
		this.backwardSpeed = 0.25f;
		this.sidewaySpeed = 0.4f;
		
		this.goalSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
		this.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);

		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
	}
	
	@Override
	public void g(float sideMot, float forMot)
	{
		if(this.passengers == null || !(this.passengers instanceof EntityHuman))
		{
			this.P = 0.5f;
			super.g(sideMot, forMot);
			return;
		}
		
		this.lastYaw = this.yaw = ((EntityHuman) this.passengers).yaw;
		this.pitch = ((EntityHuman) this.passengers).pitch * 0.75f;
		if(this.pitch > 0)
			this.pitch = 0;
		this.setYawPitch(this.yaw, this.pitch);
		this.aK = this.aI = this.yaw;
	
		this.P = this.climbHeight;
	
		boolean jump = false;
		
		try
		{
			Field field = EntityLiving.class.getDeclaredField("aY");
			field.setAccessible(true);
			jump = (boolean) field.get(this.passengers);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		sideMot = ((EntityLiving) this.passengers).bd;
		forMot = ((EntityLiving) this.passengers).be;

		if (forMot < 0.0F)
			forMot *= this.backwardSpeed;
	
		sideMot *= this.sidewaySpeed;
	 
		if(jump)
			if(this.inWater)
				this.ci();
			else if(this.onGround && this.jumpHeight != 0 && this.jumpThrust != 0)
			{
				this.motY = this.jumpHeight / 2;
				this.motZ = Math.cos(Math.toRadians(-this.yaw)) * this.jumpThrust * forMot; //normal X
				this.motX = Math.sin(Math.toRadians(-this.yaw)) * this.jumpThrust * forMot; //normal Y
			}

		this.k(this.speed / 5);
		super.g(sideMot, forMot);
	}
	
	@Override
	public void m()
	{	
		if (this.getEntityLivingbn() > 0)
			this.setEntityLivingbn(this.getEntityLivingbn() - 1);

	    if (this.bg > 0)
	    {
	      double d0 = this.locX + (this.bd - this.locX) / this.bg;
	      double d1 = this.locY + (this.be - this.locY) / this.bg;
	      double d2 = this.locZ + (this.bf - this.locZ) / this.bg;
	      double d3 = MathHelper.g(this.bg - this.yaw);
	      
	      this.yaw = ((float)(this.yaw + d3 / this.bg));
	      this.pitch = this.pitch + (this.bh - this.pitch) / this.bg;
	      this.bg -= 1;
	      setPosition(d0, d1, d2);
	      setYawPitch(this.yaw, this.pitch);
	    }
	    else if (!co())
	    {
	      this.motX *= 0.98D;
	      this.motY *= 0.98D;
	      this.motZ *= 0.98D;
	    }
	    if (Math.abs(this.motX) < 0.005D) {
	      this.motX = 0.0D;
	    }
	    if (Math.abs(this.motY) < 0.005D) {
	      this.motY = 0.0D;
	    }
	    if (Math.abs(this.motZ) < 0.005D) {
	      this.motZ = 0.0D;
	    }
	    this.world.methodProfiler.a("ai");
	    
	    if (cf())
	    {
	      this.bc = false;
	      this.aZ = 0.0F;
	      this.be = 0.0F;
	      this.bf = 0.0F;
	    }
	    else if (co())
	    {
	      this.world.methodProfiler.a("newAi");
	      doTick();
	      this.world.methodProfiler.b();
	    }
	    
	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("jump");
	    if (this.bc)
	    {
	      if (isInWater())
	      {
	        ci();
	      }
	      else if (an())
	      {
	        bH();
	      }
	      else if ((this.onGround) && (this.getEntityLivingbn() == 0))
	      {
	        ch();
	        this.setEntityLivingbn(10);
	      }
	    }
	    else
	    {
	    	this.setEntityLivingbn(0);
	    }
	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("travel");
	    this.aZ *= 0.98F;
	    this.ba *= 0.98F;
	    this.bf *= 0.9F;

	    g(this.aZ, this.ba);

	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("push");
	    if (!this.world.isClientSide)
	    {
	      cn();
	    }
	    this.world.methodProfiler.b();
	}
	
	public int getEntityLivingbn()
	{
		try
		{
			Field bn = EntityLiving.class.getDeclaredField("bn");
			if(!bn.isAccessible())
				bn.setAccessible(true);
			return bn.getInt(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public void setEntityLivingbn(int value)
	{
		try
		{
			Field bn = EntityLiving.class.getDeclaredField("bn");
			if(!bn.isAccessible())
				bn.setAccessible(true);
			bn.setInt(this, value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}


	@Override
	public float getClimbHeight()
	{
		return this.climbHeight;
	}

	@Override
	public void setClimbHeight(float climbHeight)
	{
		this.climbHeight = climbHeight;
	}

	@Override
	public float getJumpHeight()
	{
		return this.jumpHeight;
	}

	@Override
	public void setJumpHeight(float jumpHeight)
	{
		this.jumpHeight = jumpHeight;
	}
	
	@Override
	public float getJumpThrust()
	{
		return this.jumpThrust;
	}

	@Override
	public void setJumpThrust(float jumpThrust)
	{
		this.jumpThrust = jumpThrust;
	}

	@Override
	public float getSpeed()
	{
		return this.speed;
	}

	@Override
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	@Override
	public float getBackwardSpeed()
	{
		return this.backwardSpeed;
	}

	@Override
	public void setBackwardSpeed(float backwardSpeed)
	{
		this.backwardSpeed = backwardSpeed;
	}

	@Override
	public float getSidewaySpeed()
	{
		return this.sidewaySpeed;
	}

	@Override
	public void setSidewaySpeed(float sidewaySpeed)
	{
		this.sidewaySpeed = sidewaySpeed;
	}

}