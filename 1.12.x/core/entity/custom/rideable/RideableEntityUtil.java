package me.winterguardian.core.entity.custom.rideable;

import me.winterguardian.core.entity.EntityUtil;
import me.winterguardian.core.entity.custom.BlockHolder;
import me.winterguardian.core.util.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import java.lang.reflect.Constructor;

public class RideableEntityUtil
{
	private RideableEntityUtil() {}
	
	public static boolean isRideable(Entity entity)
	{
		return entity != null && ReflectionUtil.getHandle(entity) instanceof RideableEntity;
	}

	public static Entity spawnRideable(EntityType type, Location loc)
	{
		Class<?> customEntityClass;
		String className;
		
		switch(type)
		{
			
		case BOAT:
			className = "RideableBoat";
			break;
			
		case CAVE_SPIDER:
			className = "RideableCaveSpider";
			break;
			
		case CHICKEN:
			className = "RideableChicken";
			break;
			
		case COW:
			className = "RideableCow";
			break;
			
		case FALLING_BLOCK:
			className = "RideableBlock";
			break;
			
		case GUARDIAN:
			className = "RideableGuardian";
			break;
			
		case HORSE:
			className = "RideableHorse";
			break;
			
		case MAGMA_CUBE:
			className = "RideableMagmaCube";
			break;
			
		case MINECART:
			className = "RideableMinecart";
			break;
			
		case MUSHROOM_COW:
			className = "RideableMushroomCow";
			break;
			
		case OCELOT:
			className = "RideableOcelot";
			break;
			
		case PIG:
			className = "RideablePig";
			break;
			
		case RABBIT:
			className = "RideableRabbit";
			break;
			
		case SHEEP:
			className = "RideableSheep";
			break;
			
		case SILVERFISH:
			className = "RideableSilverfish";
			break;
			
		case SLIME:
			className = "RideableSlime";
			break;
			
		case SPIDER:
			className = "RideableSpider";
			break;
			
		case SQUID:
			className = "RideableSquid";
			break;
			
		case WOLF:
			className = "RideableWolf";
			break;
			
		case ZOMBIE:
			className = "RideableZombie";
			break;
			
		default:
			return null;
		
		}

		Object entity = null;

		try
		{
			customEntityClass = Class.forName("me.winterguardian.core.entity.custom.rideable." + ReflectionUtil.getVersion() + "." + className);

			Class<?> entityLivingClass = ReflectionUtil.getNMSClass("EntityLiving");
			Class<?> entityInsentientClass = ReflectionUtil.getNMSClass("EntityInsentient");
			Class<?> blockPositionClass = ReflectionUtil.getNMSClass("BlockPosition");
			Class<?> worldClass = ReflectionUtil.getNMSClass("World");

			try
			{
				Constructor xyzConstructor = customEntityClass.getConstructor(worldClass, double.class, double.class, double.class);
				entity = xyzConstructor.newInstance(ReflectionUtil.getHandle(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
			}
			catch(NoSuchMethodException e)
			{
				Constructor worldConstructor = customEntityClass.getConstructor(worldClass);
				entity = worldConstructor.newInstance(ReflectionUtil.getHandle(loc.getWorld()));
			}

			if(entityLivingClass.isAssignableFrom(customEntityClass))
				ReflectionUtil.getNMSClass("Entity").getMethod("setLocation", double.class, double.class, double.class, float.class, float.class).
						invoke(entity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());


			if(entityInsentientClass.isAssignableFrom(customEntityClass))
			{
				Object blockPosition = blockPositionClass.getConstructor(ReflectionUtil.getNMSClass("Entity")).newInstance(entity);
				Object difficultyDamageScaler = ReflectionUtil.getNMSClass("World").getDeclaredMethod("E", blockPositionClass).invoke(ReflectionUtil.getHandle(loc.getWorld()), blockPosition);

				entityInsentientClass.getDeclaredMethod("prepare", ReflectionUtil.getNMSClass("DifficultyDamageScaler"), ReflectionUtil.getNMSClass("GroupDataEntity")).
						invoke(entity, difficultyDamageScaler, null);
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		EntityUtil.addEntity(loc.getWorld(), EntityUtil.getBukkitEntity(entity), SpawnReason.CUSTOM);
		
		return EntityUtil.getBukkitEntity(entity);
	}
	
	public static float getClimbHeight(Entity entity)
	{
		if(entity == null)
			return -1;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			return ((RideableEntity)ReflectionUtil.getHandle(entity)).getClimbHeight();
		return -1;
	}

	public static void setClimbHeight(Entity entity, float climbHeight)
	{
		if(entity == null)
			return;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			((RideableEntity)ReflectionUtil.getHandle(entity)).setClimbHeight(climbHeight);
	}

	public static float getJumpHeight(Entity entity)
	{
		if(entity == null)
			return -1;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			return ((RideableEntity)ReflectionUtil.getHandle(entity)).getJumpHeight();
		return -1;
	}

	public static void setJumpHeight(Entity entity, float jumpHeight)
	{
		if(entity == null)
			return;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			((RideableEntity)ReflectionUtil.getHandle(entity)).setJumpHeight(jumpHeight);
	}
	
	public static float getJumpThrust(Entity entity)
	{
		if(entity == null)
			return -1;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			return ((RideableEntity)ReflectionUtil.getHandle(entity)).getJumpThrust();
		return -1;
	}

	public static void setJumpThrust(Entity entity, float jumpThrust)
	{
		if(entity == null)
			return;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			((RideableEntity)ReflectionUtil.getHandle(entity)).setJumpThrust(jumpThrust);
	}

	public static float getSpeed(Entity entity)
	{
		if(entity == null)
			return -1;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			return ((RideableEntity)ReflectionUtil.getHandle(entity)).getSpeed();
		return -1;
	}

	public static void setSpeed(Entity entity, float speed)
	{
		if(entity == null)
			return;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			((RideableEntity)ReflectionUtil.getHandle(entity)).setSpeed(speed);
	}

	public static float getBackwardSpeed(Entity entity)
	{
		if(entity == null)
			return -1;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			return ((RideableEntity)ReflectionUtil.getHandle(entity)).getBackwardSpeed();
		return -1;
	}

	public static void setBackwardSpeed(Entity entity, float backwardSpeed)
	{
		if(entity == null)
			return;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			((RideableEntity)ReflectionUtil.getHandle(entity)).setBackwardSpeed(backwardSpeed);
	}

	public static float getSidewaySpeed(Entity entity)
	{
		if(entity == null)
			return -1;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			return ((RideableEntity)ReflectionUtil.getHandle(entity)).getSidewaySpeed();
		return -1;
	}

	public static void setSidewaySpeed(Entity entity, float sidewaySpeed)
	{
		if(entity == null)
			return;
		if(ReflectionUtil.getHandle(entity) instanceof RideableEntity)
			((RideableEntity)ReflectionUtil.getHandle(entity)).setSidewaySpeed(sidewaySpeed);
	}

	public static void setBlock(FallingBlock block, Material material, int data)
	{
		Object entity = ReflectionUtil.getHandle(block);

		if(entity instanceof BlockHolder)
			((BlockHolder)entity).setBlock(material, data);
	}
}