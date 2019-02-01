package me.winterguardian.core.util;

import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * Created by Alexander Winter on 2015-11-23.
 */
public class CombatUtil
{
	private CombatUtil() {}

	public static boolean isHeadshot(EntityDamageByEntityEvent event)
	{
		if(event.getDamager() != null && event.getEntity() != null)
			if(event.getDamager() instanceof Projectile)
                return event.getDamager().getLocation().getY() - event.getEntity().getLocation().getY() > 1.35D;
		return false;
	}

	public static boolean isFriendly(Potion potion)
	{
		for(PotionEffect effect : potion.getEffects())
			if(!isFriendly(effect.getType()))
				return false;
		return true;
	}

	public static boolean isFriendly(ThrownPotion potion)
	{
		for(PotionEffect effect : potion.getEffects())
			if(!isFriendly(effect.getType()))
				return false;
		return true;
	}

	public static boolean isFriendly(PotionEffectType type)
	{
		if(PotionEffectType.BLINDNESS.equals(type))
			return false;

		if(PotionEffectType.CONFUSION.equals(type))
			return false;

		if(PotionEffectType.WITHER.equals(type))
			return false;

		if(PotionEffectType.POISON.equals(type))
			return false;

		if(PotionEffectType.HUNGER.equals(type))
			return false;

		if(PotionEffectType.INCREASE_DAMAGE.equals(type))
			return false;

		if(PotionEffectType.SLOW.equals(type))
			return false;

		if(PotionEffectType.SLOW_DIGGING.equals(type))
			return false;

        return !PotionEffectType.WEAKNESS.equals(type);
    }

	public static boolean isMedical(Potion potion)
	{
		boolean atLeastOne = false;
		for(PotionEffect effect : potion.getEffects())
		{
			if(!isFriendly(effect.getType()))
				return false;

			if(isMedical(effect.getType()))
				atLeastOne = true;
		}
		return atLeastOne;
	}

	public static boolean isMedical(ThrownPotion potion)
	{
		boolean atLeastOne = false;
		for(PotionEffect effect : potion.getEffects())
		{
			if(!isFriendly(effect.getType()))
				return false;

			if(isMedical(effect.getType()))
				atLeastOne = true;
		}
		return atLeastOne;
	}

	public static boolean isMedical(PotionEffectType type)
	{
		if(PotionEffectType.HEAL.equals(type))
			return true;

		if(PotionEffectType.HEALTH_BOOST.equals(type))
			return true;

        return PotionEffectType.REGENERATION.equals(type);

    }
}
