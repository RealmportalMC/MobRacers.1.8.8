package me.winterguardian.mobracers.listener;

import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.stats.CoursePurchase;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CommandSignListener implements Listener
{
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if(!(event.getClickedBlock().getState() instanceof Sign))
			return;
		
		Sign sign = (Sign) event.getClickedBlock().getState();
		
		if(!sign.getLine(0).equals(CourseMessage.SIGN_HEADER.toString()))
			return;
		
		if(sign.getLine(1).equals(CourseMessage.SIGN_JOIN.toString()))
		{
			event.getPlayer().performCommand("mobracers join");
			return;
		}

		if(sign.getLine(1).equals(CourseMessage.SIGN_LEAVE.toString()))
		{
			event.getPlayer().performCommand("mobracers leave");
			return;
		}
		
		if(sign.getLine(1).equals(CourseMessage.SIGN_INFO.toString()))
		{
			event.getPlayer().performCommand("mobracers info");
			return;
		}
		
		if(sign.getLine(1).equals(CourseMessage.SIGN_ACHIEVEMENT.toString()))
		{
			event.getPlayer().performCommand("mobracers achievement");
			return;
		}
		
		if(sign.getLine(1).equals(CourseMessage.SIGN_STATS.toString()))
		{
			String arg3 = "";
			
			if(sign.getLine(2) != null)
				arg3 = " " + sign.getLine(2);
			
			event.getPlayer().performCommand("mobracers statistics" + arg3);
			return;
		}
		
		if(sign.getLine(1).equals(CourseMessage.SIGN_VOTE.toString()))
		{
			event.getPlayer().performCommand("mobracers vote " + sign.getLine(2));
			return;
		}
		
		if(sign.getLine(1).equals(CourseMessage.SIGN_BUY.toString()))
		{
			event.getPlayer().performCommand("mobracers buy " + sign.getLine(2));
			return;
		}
		
		if(sign.getLine(1).equals(CourseMessage.SIGN_ARENASTATS.toString()))
		{
			event.getPlayer().performCommand("mobracers ranking " + sign.getLine(2));
			return;
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event)
	{
		if(!event.getPlayer().hasPermission(MobRacersPlugin.STAFF) && event.getPlayer().hasPermission(MobRacersPlugin.ADMIN))
			return;
		
		if(!event.getLine(0).equalsIgnoreCase("[mobracers]"))
			return;
		
		event.setLine(0, CourseMessage.SIGN_HEADER.toString());
		event.setLine(3, CourseMessage.SIGN_CLICK.toString());
		
		if(event.getLine(1).equalsIgnoreCase("join"))
		{
			event.setLine(1, CourseMessage.SIGN_JOIN.toString());
			event.setLine(2, "");
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("leave"))
		{
			event.setLine(1, CourseMessage.SIGN_LEAVE.toString());
			event.setLine(2, "");
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("info"))
		{
			event.setLine(1, CourseMessage.SIGN_INFO.toString());
			event.setLine(2, "");
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("achievement"))
		{
			event.setLine(1, CourseMessage.SIGN_ACHIEVEMENT.toString());
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("stats") || event.getLine(1).equalsIgnoreCase("statistics"))
		{
			event.setLine(1, CourseMessage.SIGN_STATS.toString());
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("vote"))
		{
			event.setLine(1, CourseMessage.SIGN_VOTE.toString());
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("buy"))
		{
			CoursePurchase purchase = CoursePurchase.getByName(event.getLine(2));
			
			if(purchase == null)
			{
				event.setLine(1, "§4§l§mINVALID");
				return;
			}
			
			event.setLine(1, CourseMessage.SIGN_BUY.toString());
			event.setLine(3, "" + purchase.getPrice());
			return;
		}
		
		if(event.getLine(1).equalsIgnoreCase("ranking") || event.getLine(1).equalsIgnoreCase("arenastats"))
		{
			event.setLine(1, CourseMessage.SIGN_ARENASTATS.toString());
			return;
		}
		
		event.setLine(1, "§4§l§mINVALID");
	}
}
