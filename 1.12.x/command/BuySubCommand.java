package me.winterguardian.mobracers.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.Core;
import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.stats.CoursePurchase;

import me.winterguardian.mobracers.stats.CourseStats;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuySubCommand extends SubCommand
{
	public BuySubCommand()
	{
		super("buy", Arrays.asList("acheter", "purchase", "achat"), null, null, "§c"+ CourseMessage.COMMAND_USAGE + ": §f/mobracers buy <purchase>");
	}
	
	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			CourseMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(args.length == 0)
		{
			if(CoursePurchase.hasPurchasedEverything((Player) sender))
			{
				CourseMessage.COMMAND_BUY_NOTHINGLEFT.say(sender);
				return true;
			}
			
			CourseMessage.COMMAND_BUY_LIST.say(sender);
			
			int points = CourseStats.get((Player) sender).getPoints();
			
			for(CoursePurchase purchase : CoursePurchase.values())
				if(!purchase.hasPurchased((Player) sender))
					JsonUtil.sendJsonMessage((Player) sender, purchase.getPresentation(points));
			
			return true;
		}
		
		String name = args[0];
		
		for(int i = 1; i < args.length; i++)
			name += "-" + args[i];
		
		CoursePurchase purchase = CoursePurchase.getByName(name);
		
		if(purchase == null)
		{
			CourseMessage.COMMAND_BUY_INVALIDPURCHASE.say(sender);
			return true;
		}
		
		purchase.purchase((Player) sender);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		if(args.length == 1)
		{
			List<String> purchases = new ArrayList<String>();
			
			for(CoursePurchase purchase : CoursePurchase.values())
				if(!purchase.hasPurchased((Player) sender))
					purchases.add(purchase.getName().replaceAll(" ", "-"));
			
			return TextUtil.getStringsThatStartWith(args[0], purchases);
		}
		return null;
	}

}
