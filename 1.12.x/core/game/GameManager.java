package me.winterguardian.core.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class GameManager
{
	private static List<Game> games = new ArrayList<>();
	
	public static void registerGame(Game game)
	{
		games.add(game);
	}
	
	public static void unregisterGame(Game game)
	{
		games.remove(game);
	}
	
	/**
	 * Makes the player leave all the games.
	 * 
	 * @param p
	 */
	public static void leaveAll(Player p)
	{
		for(Game game : games)
			if(game.contains(p))
				game.leave(p);
	}
	
	/**
	 * Get the first Game that contains Player. The player is not supposed to be in more than one Game.
	 * 
	 * @param p
	 * @return the plugin, null if not found
	 */
	
	public static Game getGameContained(Player p)
	{
		for(Game game : games)
			if(game.contains(p))
				return game;
		return null;
	}
	
	public static List<Game> getGames()
	{
		return new ArrayList<>(games);
	}
	
	public static Game getGame(Class<? extends Game> clazz)
	{
		for(Game game : games)
			if(game.getClass().isAssignableFrom(clazz))
				return game;
		return null;
	}
}
