package me.winterguardian.core.listener;

import java.lang.reflect.Method;

import me.winterguardian.core.world.SerializableRegion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPhysicsEvent;

@SuppressWarnings("unused")
public abstract class RegionProtectionListener
{
	public abstract SerializableRegion getRegion();
	
	private boolean cancelPhysics;
	private boolean cancelBlockForm;
	private boolean cancelBlockFormTo;
	private boolean cancelBlockFade;
	private boolean cancelBlockGrow;
	private boolean cancelBlockBurn;
	private boolean cancelBlockSpread;
	private boolean cancelBlockBreak;
	private boolean cancelBlockPlace;
	private boolean cancelBlockExplode;
	private boolean cancelPistonPush;
	private boolean cancelPistonRetract;
	
	public RegionProtectionListener()
	{
		this(EventPriority.NORMAL);
	}
	
	public RegionProtectionListener(EventPriority priority)
	{
		this.setPriority(priority);
		this.cancelPhysics = false;
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		
	}
	
	public void setPriority(EventPriority priority)
	{
		try
		{
			for(Method method : this.getClass().getDeclaredMethods())
			{
				EventHandler handler;
				if((handler = method.getAnnotation(EventHandler.class)) == null)
					continue;
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public EventPriority getPriority()
	{
		try
		{
			for(Method method : this.getClass().getDeclaredMethods())
			{
				EventHandler handler;
				if((handler = method.getAnnotation(EventHandler.class)) == null)
					continue;
				
				return handler.priority();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
