package me.winterguardian.core.gameplay;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.potion.PotionEffect;

/**
 *
 * Created by Alexander Winter on 2016-01-06.
 */
public class EntityEffectByProjectileEvent extends EntityEvent implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();

	private Projectile projectile;
	private PotionEffect effect;
	private boolean cancel;

	public EntityEffectByProjectileEvent(LivingEntity entity, Projectile projectile, PotionEffect effect)
	{
		super(entity);
		this.projectile = projectile;
		this.effect = effect;
		this.cancel = false;
	}

	@Override
	public boolean isCancelled()
	{
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel)
	{
		this.cancel = cancel;
	}

	public Projectile getProjectile()
	{
		return projectile;
	}

	public PotionEffect getEffect()
	{
		return effect;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
