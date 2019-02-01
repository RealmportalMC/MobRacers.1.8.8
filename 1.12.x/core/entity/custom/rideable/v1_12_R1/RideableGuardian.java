package me.winterguardian.core.entity.custom.rideable.v1_12_R1;

import me.winterguardian.core.entity.custom.rideable.RideableEntity;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftGuardian;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

public class RideableGuardian extends EntityGuardian implements RideableEntity
{
	private boolean reallyOnGround;
	
	private float climbHeight, jumpHeight, jumpThrust, speed, backwardSpeed, sidewaySpeed;

	public RideableGuardian(org.bukkit.World world)
	{
		this(((CraftWorld)world).getHandle());
	}

	public RideableGuardian(World world)
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
		
		this.goalRandomStroll = new FakePathfinderGoalRandomStroll();
		
		this.moveController = new ControllerMove(this);
		
		this.reallyOnGround = this.onGround;
		this.onGround = false;
	}
	
	@Override
	public void g(float sideMot, float forMot)
	{
		if(this.passengers == null || !(this.passengers instanceof EntityHuman))
		{
			this.P = 0.5f;
			superg(sideMot, forMot);
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
			else if(this.reallyOnGround && this.jumpHeight != 0 && this.jumpThrust != 0)
			{
				this.motY = this.jumpHeight / 2;
				this.motZ = Math.cos(Math.toRadians(-this.yaw)) * this.jumpThrust * forMot; //normal X
				this.motX = Math.sin(Math.toRadians(-this.yaw)) * this.jumpThrust * forMot; //normal Y
			}

		this.k(this.speed / 5);
		superg(sideMot, forMot);
	}
	public void setElder(boolean var1)
	{
		super.setElder(var1);
		this.setSize(0.85F, 0.85F);
		this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
	}

	
	public void superg(float f, float f1)
	{
		if (co())
	    {
	      if ((isInWater()))
	      {
	        double d0 = this.locY;
	        float f3 = 0.8F;
	        float f4 = 0.02F;
	        float f2 = EnchantmentManager.d(this);
	        if (f2 > 3.0F) {
	          f2 = 3.0F;
	        }
	        if(!this.reallyOnGround)
	        	f2 *= 0.5F;

	        if (f2 > 0.0F)
	        {
	          f3 += (0.54600006F - f3) * f2 / 3.0F;
	          f4 += (ck() * 1.0F - f4) * f2 / 3.0F;
	        }
	        a(f, f1, f4);
	        move(this.motX, this.motY, this.motZ);
	        this.motX *= f3;
	        this.motY *= 0.800000011920929D;
	        this.motZ *= f3;
	        this.motY -= 0.02D;
	        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
	          this.motY = 0.30000001192092896D;
	        }
	      }
	      else if ((an()))
	      {
	        double d0 = this.locY;
	        a(f, f1, 0.02F);
	        move(this.motX, this.motY, this.motZ);
	        this.motX *= 0.5D;
	        this.motY *= 0.5D;
	        this.motZ *= 0.5D;
	        this.motY -= 0.02D;
	        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
	          this.motY = 0.30000001192092896D;
	        }
	      }
	      else
	      {
	        float f5 = 0.91F;
	        if (this.reallyOnGround)
	        	f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
	        
	        float f6 = 0.16277136F / (f5 * f5 * f5);
	        float f3;
	        if (this.reallyOnGround)
	          f3 = ck() * f6;
	        else
	          f3 = this.aM;
	        
	        a(f, f1, f3);
	        f5 = 0.91F;
	        if (this.reallyOnGround) {
	          f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
	        }
	        if (n_())
	        {
	          float f4 = 0.15F;
	          this.motX = MathHelper.a(this.motX, -f4, f4);
	          this.motZ = MathHelper.a(this.motZ, -f4, f4);
	          this.fallDistance = 0.0F;
	          if (this.motY < -0.15D) {
	            this.motY = -0.15D;
	          }
	        }
	        move(this.motX, this.motY, this.motZ);
	        if ((this.positionChanged) && (n_())) {
	          this.motY = 0.2D;
	        }
	        if ((this.world.isClientSide) && ((!this.world.isLoaded(new BlockPosition((int)this.locX, 0, (int)this.locZ))) || (!this.world.getChunkAtWorldCoords(new BlockPosition((int)this.locX, 0, (int)this.locZ)).p())))
	        {
	          if (this.locY > 0.0D) {
	            this.motY = -0.1D;
	          } else {
	            this.motY = 0.0D;
	          }
	        }
	        else {
	          this.motY -= 0.08D;
	        }
	        this.motY *= 0.9800000190734863D;
	        this.motX *= f5;
	        this.motZ *= f5;
	      }
	    }
	    this.aE = this.aF;
	    double d0 = this.locX - this.lastX;
	    double d1 = this.locZ - this.lastZ;
	    
	    float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
	    if (f2 > 1.0F) {
	      f2 = 1.0F;
	    }
	    this.aB += (f2 - this.aB) * 0.4F;
	    this.aC += this.aB;
	}
	
	@SuppressWarnings("rawtypes")
	public void move(double d0, double d1, double d2)
	  {
	    if (this.noclip)
	    {
	      a(getBoundingBox().c(d0, d1, d2));
	      recalcPosition();
	    }
	    else
	    {
	      try
	      {
	        checkBlockCollisions();
	      }
	      catch (Throwable throwable)
	      {
	        CrashReport crashreport = CrashReport.a(throwable, "Checking entity block collision");
	        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity being checked for collision");
	        
	        appendEntityCrashDetails(crashreportsystemdetails);
	        throw new ReportedException(crashreport);
	      }
	      if ((d0 == 0.0D) && (d1 == 0.0D) && (d2 == 0.0D) && (this.passengers == null)) {
	        return;
	      }
	      this.world.methodProfiler.a("move");
	      double d3 = this.locX;
	      double d4 = this.locY;
	      double d5 = this.locZ;
	      if (this.E)
	      {
	        this.E = false;
	        d0 *= 0.25D;
	        d1 *= 0.05000000074505806D;
	        d2 *= 0.25D;
	        this.motX = 0.0D;
	        this.motY = 0.0D;
	        this.motZ = 0.0D;
	      }
	      double d6 = d0;
	      double d7 = d1;
	      double d8 = d2;
	      List list = this.world.getCubes(this, getBoundingBox().a(d0, d1, d2));
	      AxisAlignedBB axisalignedbb = getBoundingBox();
	      AxisAlignedBB axisalignedbb1;
	      for (Iterator iterator = list.iterator(); iterator.hasNext(); d1 = axisalignedbb1.b(getBoundingBox(), d1)) {
	        axisalignedbb1 = (AxisAlignedBB)iterator.next();
	      }
	      a(getBoundingBox().c(0.0D, d1, 0.0D));
	      boolean flag1 = (this.reallyOnGround) || ((d7 != d1) && (d7 < 0.0D));
	      AxisAlignedBB axisalignedbb2;
	      Iterator iterator1;
	      for (iterator1 = list.iterator(); iterator1.hasNext(); d0 = axisalignedbb2.a(getBoundingBox(), d0)) {
	        axisalignedbb2 = (AxisAlignedBB)iterator1.next();
	      }
	      a(getBoundingBox().c(d0, 0.0D, 0.0D));
	      for (iterator1 = list.iterator(); iterator1.hasNext(); d2 = axisalignedbb2.c(getBoundingBox(), d2)) {
	        axisalignedbb2 = (AxisAlignedBB)iterator1.next();
	      }
	      a(getBoundingBox().c(0.0D, 0.0D, d2));
	      if ((this.P > 0.0F) && (flag1) && ((d6 != d0) || (d8 != d2)))
	      {
	        double d10 = d0;
	        double d11 = d1;
	        double d12 = d2;
	        AxisAlignedBB axisalignedbb3 = getBoundingBox();
	        
	        a(axisalignedbb);
	        d1 = this.P;
	        List list1 = this.world.getCubes(this, getBoundingBox().a(d6, d1, d8));
	        AxisAlignedBB axisalignedbb4 = getBoundingBox();
	        AxisAlignedBB axisalignedbb5 = axisalignedbb4.a(d6, 0.0D, d8);
	        double d13 = d1;
	        AxisAlignedBB axisalignedbb6;
	        for (Iterator iterator2 = list1.iterator(); iterator2.hasNext(); d13 = axisalignedbb6.b(axisalignedbb5, d13)) {
	          axisalignedbb6 = (AxisAlignedBB)iterator2.next();
	        }
	        axisalignedbb4 = axisalignedbb4.c(0.0D, d13, 0.0D);
	        double d14 = d6;
	        AxisAlignedBB axisalignedbb7;
	        for (Iterator iterator3 = list1.iterator(); iterator3.hasNext(); d14 = axisalignedbb7.a(axisalignedbb4, d14)) {
	          axisalignedbb7 = (AxisAlignedBB)iterator3.next();
	        }
	        axisalignedbb4 = axisalignedbb4.c(d14, 0.0D, 0.0D);
	        double d15 = d8;
	        AxisAlignedBB axisalignedbb8;
	        for (Iterator iterator4 = list1.iterator(); iterator4.hasNext(); d15 = axisalignedbb8.c(axisalignedbb4, d15)) {
	          axisalignedbb8 = (AxisAlignedBB)iterator4.next();
	        }
	        axisalignedbb4 = axisalignedbb4.c(0.0D, 0.0D, d15);
	        AxisAlignedBB axisalignedbb9 = getBoundingBox();
	        double d16 = d1;
	        AxisAlignedBB axisalignedbb10;
	        for (Iterator iterator5 = list1.iterator(); iterator5.hasNext(); d16 = axisalignedbb10.b(axisalignedbb9, d16)) {
	          axisalignedbb10 = (AxisAlignedBB)iterator5.next();
	        }
	        axisalignedbb9 = axisalignedbb9.c(0.0D, d16, 0.0D);
	        double d17 = d6;
	        AxisAlignedBB axisalignedbb11;
	        for (Iterator iterator6 = list1.iterator(); iterator6.hasNext(); d17 = axisalignedbb11.a(axisalignedbb9, d17)) {
	          axisalignedbb11 = (AxisAlignedBB)iterator6.next();
	        }
	        axisalignedbb9 = axisalignedbb9.c(d17, 0.0D, 0.0D);
	        double d18 = d8;
	        AxisAlignedBB axisalignedbb12;
	        for (Iterator iterator7 = list1.iterator(); iterator7.hasNext(); d18 = axisalignedbb12.c(axisalignedbb9, d18)) {
	          axisalignedbb12 = (AxisAlignedBB)iterator7.next();
	        }
	        axisalignedbb9 = axisalignedbb9.c(0.0D, 0.0D, d18);
	        double d19 = d14 * d14 + d15 * d15;
	        double d20 = d17 * d17 + d18 * d18;
	        if (d19 > d20)
	        {
	          d0 = d14;
	          d2 = d15;
	          d1 = -d13;
	          a(axisalignedbb4);
	        }
	        else
	        {
	          d0 = d17;
	          d2 = d18;
	          d1 = -d16;
	          a(axisalignedbb9);
	        }
	        AxisAlignedBB axisalignedbb13;
	        for (Iterator iterator8 = list1.iterator(); iterator8.hasNext(); d1 = axisalignedbb13.b(getBoundingBox(), d1)) {
	          axisalignedbb13 = (AxisAlignedBB)iterator8.next();
	        }
	        a(getBoundingBox().c(0.0D, d1, 0.0D));
	        if (d10 * d10 + d12 * d12 >= d0 * d0 + d2 * d2)
	        {
	          d0 = d10;
	          d1 = d11;
	          d2 = d12;
	          a(axisalignedbb3);
	        }
	      }
	      this.world.methodProfiler.b();
	      this.world.methodProfiler.a("rest");
	      recalcPosition();
	      this.positionChanged = ((d6 != d0) || (d8 != d2));
	      this.E = (d7 != d1);
	      this.reallyOnGround = ((this.E) && (d7 < 0.0D));
	      this.C = ((this.positionChanged) || (this.E));
	      int i = MathHelper.floor(this.locX);
	      int j = MathHelper.floor(this.locY - 0.20000000298023224D);
	      int k = MathHelper.floor(this.locZ);
	      BlockPosition blockposition = new BlockPosition(i, j, k);
	      Block block = this.world.getType(blockposition).getBlock();
	      if (block.getMaterial() == Material.AIR)
	      {
	        Block block1 = this.world.getType(blockposition.down()).getBlock();
	        if (((block1 instanceof BlockFence)) || ((block1 instanceof BlockCobbleWall)) || ((block1 instanceof BlockFenceGate)))
	        {
	          block = block1;
	          blockposition = blockposition.down();
	        }
	      }
	      a(d1, this.reallyOnGround, block, blockposition);
	      if (d6 != d0) {
	        this.motX = 0.0D;
	      }
	      if (d8 != d2) {
	        this.motZ = 0.0D;
	      }
	      if (d7 != d1) {
	        block.a(this.world, this);
	      }
	      if ((this.positionChanged) && ((getBukkitEntity() instanceof Vehicle)))
	      {
	        Vehicle vehicle = (Vehicle)getBukkitEntity();
	        org.bukkit.block.Block bl = this.world.getWorld().getBlockAt(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
	        if (d6 > d0) {
	          bl = bl.getRelative(BlockFace.EAST);
	        } else if (d6 < d0) {
	          bl = bl.getRelative(BlockFace.WEST);
	        } else if (d8 > d2) {
	          bl = bl.getRelative(BlockFace.SOUTH);
	        } else if (d8 < d2) {
	          bl = bl.getRelative(BlockFace.NORTH);
	        }
	        VehicleBlockCollisionEvent event = new VehicleBlockCollisionEvent(vehicle, bl);
	        this.world.getServer().getPluginManager().callEvent(event);
	      }
	      if ((s_()) && (this.vehicle == null))
	      {
	        double d21 = this.locX - d3;
	        double d22 = this.locY - d4;
	        double d23 = this.locZ - d5;
	        if (block != Blocks.LADDER) {
	          d22 = 0.0D;
	        }
	        if (block != null) {}
	        this.M = ((float)(this.M + MathHelper.sqrt(d21 * d21 + d23 * d23) * 0.6D));
	        this.N = ((float)(this.N + MathHelper.sqrt(d21 * d21 + d22 * d22 + d23 * d23) * 0.6D));
	        if ((this.N > this.getEntityh()) && (block.getMaterial() != Material.AIR))
	        {
	          this.setEntityh(((int)this.N + 1));
	          if (V())
	          {
	            float f = MathHelper.sqrt(this.motX * this.motX * 0.20000000298023224D + this.motY * this.motY + this.motZ * this.motZ * 0.20000000298023224D) * 0.35F;
	            if (f > 1.0F) {
	              f = 1.0F;
	            }
	            makeSound(P(), f, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
	          }
	          a(blockposition, block);
	          block.a(this.world, blockposition, this);
	        }
	      }
	      boolean flag2 = U();
	      if (this.world.e(getBoundingBox().shrink(0.001D, 0.001D, 0.001D)))
	      {
	        burn(1.0F);
	        if (!flag2)
	        {
	          this.fireTicks += 1;
	          if (this.fireTicks <= 0)
	          {
	            EntityCombustEvent event = new EntityCombustEvent(getBukkitEntity(), 8);
	            this.world.getServer().getPluginManager().callEvent(event);
	            if (!event.isCancelled()) {
	              setOnFire(event.getDuration());
	            }
	          }
	          else
	          {
	            setOnFire(8);
	          }
	        }
	      }
	      else if (this.fireTicks <= 0)
	      {
	        this.fireTicks = (-this.maxFireTicks);
	      }
	      if ((flag2) && (this.fireTicks > 0))
	      {
	        makeSound("random.fizz", 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
	        this.fireTicks = (-this.maxFireTicks);
	      }
	      this.world.methodProfiler.b();
	    }
	  }
	  
	  private void recalcPosition()
	  {
	    this.locX = ((getBoundingBox().a + getBoundingBox().d) / 2.0D);
	    this.locY = getBoundingBox().b;
	    this.locZ = ((getBoundingBox().c + getBoundingBox().f) / 2.0D);
	  }
	
	@Override
	public void m()
	{	
		if (this.getEntityLivingbn() > 0)
			this.setEntityLivingbn(this.getEntityLivingbn() - 1);

	    if (this.bc > 0)
	    {
	      double d0 = this.locX + (this.bd - this.locX) / this.bc;
	      double d1 = this.locY + (this.be - this.locY) / this.bc;
	      double d2 = this.locZ + (this.bf - this.locZ) / this.bc;
	      double d3 = MathHelper.g(this.bg - this.yaw);
	      
	      this.yaw = ((float)(this.yaw + d3 / this.bc));
	      this.pitch = ((float)(this.pitch + (this.bh - this.pitch) / this.bc));
	      this.bc -= 1;
	      setPosition(d0, d1, d2);
	      setYawPitch(this.yaw, this.pitch);
	    }
	    else if (!bM())
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

	    if (bD())
	    {
	      this.aY = false;
	      this.aZ = 0.0F;
	      this.ba = 0.0F;
	      this.bb = 0.0F;
	    }
	    else if (bM())
	    {
	      this.world.methodProfiler.a("newAi");
	      doTick();
	      this.world.methodProfiler.b();
	    }
	    
	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("jump");
	    if (this.aY)
	    {
	      if (V())
	      {
	        bG();
	      }
	      else if (ab())
	      {
	        bH();
	      }
	      else if ((this.reallyOnGround) && (this.getEntityLivingbn() == 0))
	      {
	        bF();
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
	    this.bb *= 0.9F;

	    g(this.aZ, this.ba);

	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("push");
	    if (!this.world.isClientSide)
	    {
	      bL();
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
	
	public int getEntityh()
	{
		try
		{
			Field h = Entity.class.getDeclaredField("h");
			if(!h.isAccessible())
				h.setAccessible(true);
			return h.getInt(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public void setEntityh(int value)
	{
		try
		{
			Field h = Entity.class.getDeclaredField("h");
			if(!h.isAccessible())
				h.setAccessible(true);
			h.setInt(this, value);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

	protected void E(){}
	
	protected NavigationAbstract b(World world)
	{
		return new Navigation(this, world);
	}
	
	protected String z()
	{
		if (isElder())
			return "mob.guardian.elder.idle";

		return "mob.guardian.idle";
	}

	protected String bo()
	{
		if (isElder())
			return "mob.guardian.elder.hit";
		
		return "mob.guardian.hit";
	}

	protected String bp()
	{
		if (isElder())
			return "mob.guardian.elder.death";

		return "mob.guardian.death";
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
	
	public CraftEntity getBukkitEntity()
	{
		if (this.bukkitEntity == null)
			this.bukkitEntity = new CraftGuardian(this.world.getServer(), this)
			{
				public boolean isOnGround()
				{
					return reallyOnGround;
				}
			};
		
		return this.bukkitEntity;
	}
	
	private static class FakePathfinderGoalRandomStroll extends PathfinderGoalRandomStroll
	{
		public FakePathfinderGoalRandomStroll()
		{
			super(null, 0, 0);
		}
		
		public boolean a()
		{
			return false;
		}

		public boolean b()
		{
			return false;
		}
		public void c() { }
		public void f() { }
		public void setTimeBetweenMovement(int i) { }
		
		public boolean i()
		{
			return true;
		}

		public void d() {}
		
		public void e() {}
		
		public void a(int i) { }

		public int j()
		{ 
			return 0;
		}
	}		
}