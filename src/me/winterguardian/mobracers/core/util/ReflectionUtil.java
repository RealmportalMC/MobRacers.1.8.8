package me.winterguardian.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ReflectionUtil
{
	private ReflectionUtil(){}

	public static Object getHandle(Entity entity)
	{
		try
		{
			Method method = entity.getClass().getMethod("getHandle");
			method.setAccessible(true);

			return method.invoke(entity);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Object getHandle(World world)
	{
		try
		{

			Method method = world.getClass().getMethod("getHandle");
			method.setAccessible(true);

			return method.invoke(world);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Class<?> getNMSClass(String name)
	{
		try
		{
			return Class.forName("net.minecraft.server." + getVersion() + "." + name);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Object clone(Object o)
	{
		Object clone = null;

		try
		{
			clone = o.getClass().newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		// Walk up the superclass hierarchy
		for(Class<?> obj = o.getClass(); !obj.equals(Object.class); obj = obj.getSuperclass())
		{
			Field[] fields = obj.getDeclaredFields();
			for(int i = 0; i < fields.length; i++)
			{
				fields[i].setAccessible(true);
				try
				{
					// for each class/suerclass, copy all fields
					// from this object to the clone
					fields[i].set(clone, fields[i].get(o));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return clone;
	}

	/**
	 * Source: http://stackoverflow.com/questions/586363/why-is-super-super-method-not-allowed-in-java
	 */
	public static <Type, ChieldType extends Type> void exec(Class<Type> oneSuperType, ChieldType instance, String methodOfParentToExec)
	{
		try
		{
			Type type = oneSuperType.newInstance();
			shareVars(oneSuperType, instance, type);
			oneSuperType.getMethod(methodOfParentToExec).invoke(type);
			shareVars(oneSuperType, type, instance);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Source: http://stackoverflow.com/questions/586363/why-is-super-super-method-not-allowed-in-java
	 */
	private static <Type, SourceType extends Type, TargetType extends Type> void shareVars(Class<Type> clazz, SourceType source, TargetType target) throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> loop = clazz;
		do
		{
			for(Field f : loop.getDeclaredFields())
			{
				if(!f.isAccessible())
				{
					f.setAccessible(true);
				}
				f.set(target, f.get(source));
			}
			loop = loop.getSuperclass();
		}
		while(loop != Object.class);
	}

	public static Field getFirstFieldByType(Class<?> clazz, Class<?> type)
	{
		for(Field field : clazz.getDeclaredFields())
		{
			field.setAccessible(true);
			if(field.getType() == type)
				return field;
		}
		return null;
	}

	public static String getVersion()
	{
		Class<?> bukkitServerClass = Bukkit.getServer().getClass();
		String[] pas = bukkitServerClass.getName().split("\\.");
		return pas[3];
	}

	public static void sendPacket(Player player, Object packet)
	{
		try
		{
			Object entityPlayer = getHandle(player);

			Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

			playerConnection.getClass().getMethod("sendPacket", new Class<?>[]{ReflectionUtil.getNMSClass("Packet")}).invoke(playerConnection, packet);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static Object getSerialized(String string) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		return Class.forName("net.minecraft.server." + getVersion() + ".IChatBaseComponent$ChatSerializer").getMethod("a", new Class<?>[]{String.class}).invoke(null, string);
	}

	public static boolean isPaper()
	{
		try
		{
			Class.forName("org.github.paperspigot.PaperSpigotConfig");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
