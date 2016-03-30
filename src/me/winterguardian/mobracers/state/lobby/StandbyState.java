package me.winterguardian.mobracers.state.lobby;

import me.winterguardian.core.Core;
import me.winterguardian.core.game.state.State;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.message.Message;
import me.winterguardian.core.scoreboard.Board;
import me.winterguardian.core.scoreboard.ScoreboardUtil;
import me.winterguardian.core.scoreboard.UpdatableBoard;
import me.winterguardian.core.util.PlayerUtil;
import me.winterguardian.core.util.Weather;
import me.winterguardian.core.world.Region;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.state.MobRacersState;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class StandbyState extends me.winterguardian.core.game.state.StandbyState implements MobRacersState
{
	public StandbyState(StateGame game)
	{
		super(game);
	}
	
	@Override
	public void join(Player player)
	{
		((MobRacersGame) getGame()).savePlayerState(player);
		super.join(player);
	
		if(getGame().getConfig().isColorInTab())
			player.setPlayerListName("§2" + player.getName());

		if(getGame().getState() == this)
			giveStuff(player);
		
	}
	
	@Override
	public void leave(Player player)
	{
		if(getGame().getConfig().isColorInTab())
			player.setPlayerListName(null);

		super.leave(player);
		
		((MobRacersGame) getGame()).applyPlayerState(player);
	}

	private void giveStuff(Player player)
	{
		PlayerUtil.clearInventory(player);
		player.getInventory().setItem(8, ((MobRacersGame)getGame()).getSetup().getLeaveItem());
	}
	
	@Override
	public void start()
	{
		Core.getBungeeMessager().requestServerName();
		super.start();
		try
		{
			Bukkit.getScheduler().runTaskLater(MobRacersPlugin.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					if(MobRacersPlugin.getGame().getSetup().getRegion() != null)
						for(Entity entity : MobRacersPlugin.getGame().getSetup().getRegion().getMinimum().getWorld().getEntities())
							if(MobRacersPlugin.getGame().getSetup().getRegion().contains(entity.getLocation()))
								if(!(entity instanceof Player) && entity instanceof LivingEntity)
									entity.remove();
				}
			}, 20);
		}
		catch(Exception e){}

		for(Player player : getGame().getPlayers())
			giveStuff(player);
	}
	
	@Override
	public String getTabHeader(Player player)
	{
		return JsonUtil.toJson(CourseMessage.STANDBY_TABHEADER.toString());
	}

	@Override
	public String getTabFooter(Player p)
	{
		return JsonUtil.toJson(CourseMessage.STANDBY_TABFOOTER.toString());
	}

	@Override
	public String getStatus()
	{
		return CourseMessage.STANDBY_STATUS.toString();
	}

	@Override
	public Board getNewScoreboard()
	{
		return new UpdatableBoard()
		{
			@Override
			protected void update(Player player)
			{
				String[] elements = new String[16];
				elements[0] = CourseMessage.STANDBY_BOARD_HEADER.toString();


				String[] board = CourseMessage.STANDBY_BOARD.toString("<players>", getGame().getPlayers().size() + "", "<maxplayers>", getGame().getMaxPlayers() + "", "<server>", Core.getBungeeMessager().getServerName()).split("\\n");

				for(int index = 1; index < Math.max(16, board.length); index++)
				{
					String line = board[index - 1];

					contains:
					while(true)
					{
						for(String element : elements)
						{
							if(element.equals(line))
							{
								line += " ";
								continue contains;
							}
						}

						break;
					}

					elements[index] = line;
				}

				ScoreboardUtil.unrankedSidebarDisplay(player, elements);
			}
			
		};
	}

	

	@Override
	public boolean keepScoreboardAndWeather()
	{
		return true;
	}

	public Weather getPlayerWeather(Player p)
	{
		return ((MobRacersConfig) getGame().getConfig()).getLobbyWeather();
	}
	
	@Override
	protected State getNextState()
	{
		return new ArenaSelectionState(this.getGame());
	}

	@Override
	protected Message getNotEnoughtPlayersToPlayMessage()
	{
		return CourseMessage.STANDBY_NOTENOUGHTPLAYERS;
	}

	@Override
	protected Message getNotEnoughtPlayersToStartMessage()
	{
		return CourseMessage.STANDBY_NOTENOUGHTPLAYERSTOCONTINUE;
	}
	
	@Override
	public Region getRegion()
	{
		return this.getGame().getSetup().getRegion();
	}
}
