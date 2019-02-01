package me.winterguardian.core.entity.custom.npc.v1_12_R1;

import me.winterguardian.core.entity.custom.npc.NPCEntity;
import me.winterguardian.core.util.ReflectionUtil;
import net.minecraft.server.v1_12_R1.EntityVillager;
import net.minecraft.server.v1_12_R1.World;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * Created by Alexander Winter on 2016-01-05.
 */
public class NPCVillager extends EntityVillager implements NPCEntity
{

	public NPCVillager(World world)
	{
		this(world, 0);
	}

	public NPCVillager(World world, int i)
	{
		super(world, i);

		try
		{
			Class<?> lookAtPlayerClass = ReflectionUtil.getNMSClass("PathfinderGoalLookAtPlayer");
			Class<?> entityInsentientClass = ReflectionUtil.getNMSClass("EntityInsentient");
			Class<?> pathfinderClass = ReflectionUtil.getNMSClass("PathfinderGoal");
			Class<?> playerClass = ReflectionUtil.getNMSClass("EntityHuman");

			Object goalSelector = entityInsentientClass.getDeclaredField("goalSelector").get(this);
			Field b = goalSelector.getClass().getDeclaredField("b");
			b.setAccessible(true);
			b.set(goalSelector, new ArrayList<>());

			Field c = goalSelector.getClass().getDeclaredField("c");
			c.setAccessible(true);
			c.set(goalSelector, new ArrayList<>());

			Object pathFinder = lookAtPlayerClass.getConstructor(entityInsentientClass, Class.class, float.class, float.class).newInstance(this, playerClass, 8.0F, 1.0F);

			Method a = goalSelector.getClass().getDeclaredMethod("a", int.class, pathfinderClass);

			a.invoke(goalSelector, 1, pathFinder);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void move(double x, double y, double z)
	{

	}
}
