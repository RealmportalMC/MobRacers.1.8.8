package me.winterguardian.core.entity.custom.npc;

import me.winterguardian.core.entity.EntityUtil;
import me.winterguardian.core.util.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Constructor;

/**
 *
 * Created by Alexander Winter on 2016-01-05.
 */
public class NPCUtil
{
	private NPCUtil(){}

	public static boolean isNPC(Entity entity)
	{
		return entity != null && ReflectionUtil.getHandle(entity) instanceof NPCEntity;
	}

	public static Entity spawnNPC(EntityType type, Location loc)
	{
		Class<?> customEntityClass;
		String className;

		switch(type)
		{

			case VILLAGER:
				className = "NPCVillager";
				break;

			case PLAYER:
				className = "NPCPlayer";
				break;
			default:
				return null;

		}

		Object entity = null;

		try
		{
			customEntityClass = Class.forName("me.winterguardian.core.entity.custom.npc." + ReflectionUtil.getVersion() + "." + className);

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

		EntityUtil.addEntity(loc.getWorld(), EntityUtil.getBukkitEntity(entity), CreatureSpawnEvent.SpawnReason.CUSTOM);

		return EntityUtil.getBukkitEntity(entity);
	}
}
