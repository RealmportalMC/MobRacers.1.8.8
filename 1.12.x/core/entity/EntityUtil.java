package me.winterguardian.core.entity;

import me.winterguardian.core.entity.custom.CustomNoAI;
import me.winterguardian.core.util.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class EntityUtil
{
	private EntityUtil(){ }

	public static void addEntity(World bukkitWorld, Entity bukkitEntity, CreatureSpawnEvent.SpawnReason reason)
	{
		try
		{
			Object world = ReflectionUtil.getHandle(bukkitWorld);
			Object entity = ReflectionUtil.getHandle(bukkitEntity);

			Class<?> worldClass = ReflectionUtil.getNMSClass("World");

			worldClass.getDeclaredMethod("addEntity", ReflectionUtil.getNMSClass("Entity"), reason.getClass()).invoke(world, entity, reason);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Entity getBukkitEntity(Object nmsEntity)
	{
		try
		{
			return (Entity)ReflectionUtil.getNMSClass("Entity").getDeclaredMethod("getBukkitEntity").invoke(nmsEntity);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static double getSpeed(LivingEntity entity)
	{
		try
		{
			Class<?> livingEntityClass = ReflectionUtil.getNMSClass("LivingEntity");
			Class<?> genericAttributesClass = ReflectionUtil.getNMSClass("GenericAttributes");
			Class<?> attributeInstanceClass = ReflectionUtil.getNMSClass("AttributeInstance");
			Class<?> iAttributeClass = ReflectionUtil.getNMSClass("IAttribute");
			Object movementSpeed = genericAttributesClass.getField("MOVEMENT_SPEED").get(null);

			Object attributeInstance = livingEntityClass.getDeclaredMethod("getAttributeInstance", iAttributeClass).invoke(ReflectionUtil.getHandle(entity), movementSpeed);

			return (int)attributeInstanceClass.getDeclaredMethod("getValue").invoke(attributeInstance);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void setSpeed(LivingEntity entity, double speed)
	{
		try
		{
			Class<?> livingEntityClass = ReflectionUtil.getNMSClass("LivingEntity");
			Class<?> genericAttributesClass = ReflectionUtil.getNMSClass("GenericAttributes");
			Class<?> attributeInstanceClass = ReflectionUtil.getNMSClass("AttributeInstance");
			Class<?> iAttributeClass = ReflectionUtil.getNMSClass("IAttribute");
			Object movementSpeed = genericAttributesClass.getField("MOVEMENT_SPEED").get(null);

			Object attributeInstance = livingEntityClass.getDeclaredMethod("getAttributeInstance", iAttributeClass).invoke(ReflectionUtil.getHandle(entity), movementSpeed);

			attributeInstanceClass.getDeclaredMethod("setValue", double.class).invoke(attributeInstance, speed);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setNoAI(Entity entity, boolean noAI)
	{
		if(entity == null)
			return;

		try
		{
			Class<?> entityInsentientClass = ReflectionUtil.getNMSClass("EntityInsentient");

			if(ReflectionUtil.getHandle(entity) instanceof CustomNoAI)
				((CustomNoAI)ReflectionUtil.getHandle(entity)).setNoAI(noAI);
			else if(entityInsentientClass.isAssignableFrom(ReflectionUtil.getHandle(entity).getClass()))
				entityInsentientClass.getDeclaredMethod("k", boolean.class).invoke(ReflectionUtil.getHandle(entity), noAI);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static boolean getNoAI(Entity entity)
	{
		if(entity == null)
			return false;

		try
		{
			Class<?> entityInsentientClass = ReflectionUtil.getNMSClass("EntityInsentient");

			if(ReflectionUtil.getHandle(entity) instanceof CustomNoAI)
				return ((CustomNoAI)ReflectionUtil.getHandle(entity)).getNoAI();

			if(entityInsentientClass.isAssignableFrom(ReflectionUtil.getHandle(entity).getClass()))
				return (boolean)entityInsentientClass.getDeclaredMethod("ce").invoke(ReflectionUtil.getHandle(entity));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}

	public static void setNoclip(Entity bukkitEntity, boolean noclip)
	{
		try
		{
			Class<?> entityClass = ReflectionUtil.getNMSClass("Entity");

			Object entity = bukkitEntity.getClass().getDeclaredMethod("getHandle").invoke(bukkitEntity);
			entityClass.getDeclaredField("noclip").set(entity, noclip);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isNoClip(Entity bukkitEntity)
	{
		try
		{
			Class<?> entityClass = ReflectionUtil.getNMSClass("Entity");

			Object entity = ReflectionUtil.getHandle(bukkitEntity);
			return entityClass.getDeclaredField("noclip").getBoolean(entity);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static int getId(Entity entity)
	{
		return getId(ReflectionUtil.getHandle(entity));
	}

	public static int getId(Object entity)
	{
		try
		{
			return (int)ReflectionUtil.getNMSClass("Entity").getDeclaredMethod("getId").invoke(entity);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public static void setYawPitch(Player player, Entity entity, float yaw, float pitch)
	{
		try
		{
			Constructor playOutEntityLookConstructor = ReflectionUtil.getNMSClass("PacketPlayOutEntity$PacketPlayOutEntityLook").getConstructor(int.class, byte.class, byte.class, boolean.class);
			Constructor playOutEntityHeadRotation = ReflectionUtil.getNMSClass("PacketPlayOutEntityHeadRotation").getConstructor(ReflectionUtil.getNMSClass("Entity"), byte.class);

			ReflectionUtil.sendPacket(player, playOutEntityLookConstructor.newInstance(getId(entity), (byte) 0, (byte) ((int) pitch * 256.0F / 360.0F), entity.isOnGround()));
			ReflectionUtil.sendPacket(player, playOutEntityHeadRotation.newInstance(ReflectionUtil.getHandle(entity), (byte) ((int) yaw * 256.0F / 360.0F)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setYawPitch(Collection<Player> players, Entity entity, float yaw, float pitch)
	{
		for(Player p : players)
			setYawPitch(p, entity, yaw, pitch);
	}

	public static Object getBoundingBox(Entity entity)
	{
		try
		{
			return ReflectionUtil.getNMSClass("Entity").getDeclaredMethod("getBoundingBox").invoke(entity);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean willBeOnGround(Entity bukkitEntity, Location loc)
	{
		if(isNoClip(bukkitEntity))
			return false;

		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();

		try
		{
			Class<?> entityClass = ReflectionUtil.getNMSClass("Entity");
			Class<?> worldClass = ReflectionUtil.getNMSClass("World");
			Class<?> axisAlignedBBClass = ReflectionUtil.getNMSClass("AxisAlignedBB");
			Method aMethodFromEntityClass = entityClass.getDeclaredMethod("a", axisAlignedBBClass);
			Method aMethodFromBoundingBoxClass = axisAlignedBBClass.getDeclaredMethod("a", entityClass, double.class, double.class, double.class);
			Method cMethodFromBoundingBoxClass = axisAlignedBBClass.getDeclaredMethod("c", entityClass, double.class, double.class, double.class);
			Method getCubesFromWorldClass = worldClass.getMethod("getCubes", entityClass, axisAlignedBBClass);

			net.minecraft.server.v1_12_R1.Entity entity = (net.minecraft.server.v1_12_R1.Entity)ReflectionUtil.getHandle(bukkitEntity);
			Object world = ReflectionUtil.getHandle(bukkitEntity.getWorld());

			double originalY = y;

			List<?> list = (List<?>)getCubesFromWorldClass.invoke(world, entity, aMethodFromBoundingBoxClass.invoke(getBoundingBox(bukkitEntity), entity, x, y, z));

			for(Object box : list)
				x = (double)axisAlignedBBClass.getMethod("a", axisAlignedBBClass, double.class).invoke(box, getBoundingBox(bukkitEntity), x);

			aMethodFromEntityClass.invoke(cMethodFromBoundingBoxClass.invoke(entity, x, 0, 0));

			for(Object box : list)
				y = (double)axisAlignedBBClass.getMethod("b", axisAlignedBBClass, double.class).invoke(box, getBoundingBox(bukkitEntity), y);

			aMethodFromEntityClass.invoke(cMethodFromBoundingBoxClass.invoke(entity, 0, y, 0));

			for(Object box : list)
				z = (double)axisAlignedBBClass.getMethod("c", axisAlignedBBClass, double.class).invoke(box, getBoundingBox(bukkitEntity), z);

			aMethodFromEntityClass.invoke(cMethodFromBoundingBoxClass.invoke(entity, 0, 0, z));

			return originalY != y && originalY < 0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static void setTNTPrimedSource(TNTPrimed tnt, LivingEntity entitySource)
	{
		Object handle = ReflectionUtil.getHandle(tnt);

		try
		{
			Field source = handle.getClass().getDeclaredField("source");

			source.setAccessible(true);
			source.set(handle, ReflectionUtil.getHandle(entitySource));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}
