package me.winterguardian.mobracers.state.game;

public class CourseRewards
{
	private static int[][] rewards = new int[][]
			{
				new int[]{50}, //2 players
				new int[]{100}, //3 players
				
				new int[]{200, 20}, //4 players
				new int[]{250, 30}, //5 players
				new int[]{300, 50}, //6 players
				new int[]{350, 75}, //7 players
				
				new int[]{500, 100, 20}, //8 players
				new int[]{750, 150, 30} //9 players
				
				//new int[]{1000, 200, 50} example
			};
	
	public static int getReward(int position, int players)
	{
		try
		{
			if(players >= 10)
				switch(position)
				{
				case 1:
					return 100 * players;
				case 2:
					return 200 + 50 * (players - 10);
				case 3:
					return 50 + 5 * (players - 10);
				default:
					return 10;
				}
			else
				return rewards[players - 2][position - 1];
		}
		catch(Exception e)
		{
			return 10;
		}
	}
}
