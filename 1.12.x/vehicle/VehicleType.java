package me.winterguardian.mobracers.vehicle;

import java.util.ArrayList;
import java.util.List;

import me.winterguardian.mobracers.vehicle.types.BabyCowVehicle;
import me.winterguardian.mobracers.vehicle.types.BlockVehicle;
import me.winterguardian.mobracers.vehicle.types.CaveSpiderVehicle;
import me.winterguardian.mobracers.vehicle.types.ChickenVehicle;
import me.winterguardian.mobracers.vehicle.types.CowVehicle;
import me.winterguardian.mobracers.vehicle.types.DonkeyVehicle;
import me.winterguardian.mobracers.vehicle.types.ElderGuardianVehicle;
import me.winterguardian.mobracers.vehicle.types.GuardianVehicle;
import me.winterguardian.mobracers.vehicle.types.HorseVehicle;
import me.winterguardian.mobracers.vehicle.types.MagmaCubeVehicle;
import me.winterguardian.mobracers.vehicle.types.MinecartVehicle;
import me.winterguardian.mobracers.vehicle.types.MushroomCowVehicle;
import me.winterguardian.mobracers.vehicle.types.OcelotVehicle;
import me.winterguardian.mobracers.vehicle.types.PigVehicle;
import me.winterguardian.mobracers.vehicle.types.RabbitVehicle;
import me.winterguardian.mobracers.vehicle.types.SheepVehicle;
import me.winterguardian.mobracers.vehicle.types.SilverfishVehicle;
import me.winterguardian.mobracers.vehicle.types.SkeletonHorseVehicle;
import me.winterguardian.mobracers.vehicle.types.SlimeVehicle;
import me.winterguardian.mobracers.vehicle.types.SpiderVehicle;
import me.winterguardian.mobracers.vehicle.types.SquidVehicle;
import me.winterguardian.mobracers.vehicle.types.SuperSheepVehicle;
import me.winterguardian.mobracers.vehicle.types.UndeadHorseVehicle;
import me.winterguardian.mobracers.vehicle.types.WolfVehicle;

import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;

public enum VehicleType
{
	MINECART(MinecartVehicle.class, getConditionByEntityType(EntityType.MINECART)),
	COW(CowVehicle.class, new TypeCondition()
	{
		@Override
		public boolean isType(Entity entity)
		{
			return entity instanceof Cow && ((Cow)entity).isAdult();
		}
	}),
	MUSHROOM_COW(MushroomCowVehicle.class, getConditionByEntityType(EntityType.MUSHROOM_COW)),
	PIG(PigVehicle.class, getConditionByEntityType(EntityType.PIG)), 
	SHEEP(SheepVehicle.class, new TypeCondition()
	{
		@Override
		public boolean isType(Entity entity)
		{
			return entity.getType() == EntityType.SHEEP && entity.getCustomName() != "jeb_";
		}
	}), 
	SUPER_SHEEP(SuperSheepVehicle.class, new TypeCondition()
	{
		@Override
		public boolean isType(Entity entity)
		{
			return entity.getType() == EntityType.SHEEP && entity.getCustomName() == "jeb_";
		}
	}),
	WOLF(WolfVehicle.class, getConditionByEntityType(EntityType.WOLF)),
	CAVE_SPIDER(CaveSpiderVehicle.class, getConditionByEntityType(EntityType.CAVE_SPIDER)),
	DONKEY(DonkeyVehicle.class, getHorseConditionByVariant(Variant.DONKEY)),
	OCELOT(OcelotVehicle.class, getConditionByEntityType(EntityType.OCELOT)), 
	SILVERFISH(SilverfishVehicle.class, getConditionByEntityType(EntityType.SILVERFISH)), 
	
	HORSE(HorseVehicle.class, getHorseConditionByVariant(Variant.HORSE)),
	UNDEAD_HORSE(UndeadHorseVehicle.class, getHorseConditionByVariant(Variant.UNDEAD_HORSE)),
	SKELETON_HORSE(SkeletonHorseVehicle.class, getHorseConditionByVariant(Variant.SKELETON_HORSE)), 
	SLIME(SlimeVehicle.class, getConditionByEntityType(EntityType.SLIME)),
	MAGMA_CUBE(MagmaCubeVehicle.class, getConditionByEntityType(EntityType.MAGMA_CUBE)),
	SPIDER(SpiderVehicle.class, getConditionByEntityType(EntityType.SPIDER)),
	RABBIT(RabbitVehicle.class, getConditionByEntityType(EntityType.RABBIT)), 
	
	GUARDIAN(GuardianVehicle.class, new TypeCondition()
	{
		@Override
		public boolean isType(Entity entity)
		{
			return entity instanceof Guardian && !((Guardian)entity).isElder();
		}
	}),
	ELDER_GUARDIAN(ElderGuardianVehicle.class, new TypeCondition()
	{
		@Override
		public boolean isType(Entity entity)
		{
			return entity instanceof Guardian && ((Guardian)entity).isElder();
		}
	}),
	SQUID(SquidVehicle.class, getConditionByEntityType(EntityType.SQUID)), 
	
	CHICKEN(ChickenVehicle.class, getConditionByEntityType(EntityType.CHICKEN)),
	BLOCK(BlockVehicle.class, getConditionByEntityType(EntityType.FALLING_BLOCK)),
	BABY_COW(BabyCowVehicle.class, new TypeCondition()
	{
		@Override
		public boolean isType(Entity entity)
		{
			return entity instanceof Cow && !((Cow)entity).isAdult();
		}
	}),
	;
	
	private Class<? extends Vehicle> vehicleClass;
	private TypeCondition condition;
	
	VehicleType(Class<? extends Vehicle> vehicleClass, TypeCondition condition)
	{
		this.vehicleClass = vehicleClass;
		this.condition = condition;
	}
	
	public Vehicle createNewVehicle()
	{
		try
		{
			return vehicleClass.getConstructor((Class<Object>[])null).newInstance((Object[])null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String getName()
	{
		return createNewVehicle().getName();
	}
	
	public boolean isType(Entity entity)
	{
		if(condition != null)
			return condition.isType(entity);
		return false;
	}
	
	public boolean canChoose(Player p)
	{
		return this.createNewVehicle().canChoose(p);
	}
	
	public static VehicleType getType(Entity entity)
	{
		for(VehicleType vehicle : values())
			if(vehicle.isType(entity))
				return vehicle;
		return null;
	}
	
	private interface TypeCondition
	{
		boolean isType(Entity entity);
	}
	
	public static TypeCondition getConditionByEntityType(final EntityType type)
	{
		return new TypeCondition()
		{
			@Override
			public boolean isType(Entity entity)
			{
				return entity.getType() == type;
			}
		};
	}
	public static TypeCondition getHorseConditionByVariant(final Variant variant)
	{
		return new TypeCondition()
		{
			@Override
			public boolean isType(Entity entity)
			{
				return entity instanceof Horse && ((Horse)entity).getVariant() == variant;
			}
		};
	}
	
	public static List<VehicleType> getAvailableVehicles(Player p)
	{
		List<VehicleType> availables = new ArrayList<VehicleType>();
		for(VehicleType type : values())
			if(type.createNewVehicle().canChoose(p))
				availables.add(type);
		return availables;
			
	}
}
