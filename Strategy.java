public class StrategyPercentAll extends Strategy
   {
  /**
   * Encoding for tit-for-tat strategy.
   */

  // 0 = defect, 1 = cooperate

   public StrategyTitForTat()
  	{
  	name = "Tit for Tat";
  	opponentLastMove = 1;
  	}  /* StrategyTitForTat */

   public int nextMove()
  	{
  	return opponentLastMove;
  	}  /* nextMove */



 int numDefects;

  // 0 = defect, 1 = cooperate

   public StrategyTitForTwoTats()
  	{
  	name = "Tit for Two Tats";
  	opponentLastMove = 1;
  	numDefects = 0;
  	}  /* StrategyTitForTwoTats */

   public int nextMove2Tats()
  	{
  	if (opponentLastMove == 0)  numDefects++;

  	if (opponentLastMove == 1)
     	{
     	numDefects = 0;
     	return 1;
     	}
  	else
     	{
     		if (opponentLastMove == 0 && numDefects < 2)
        		return 1;
     		else  
        		{
        		return 0;
        		}
     	}

  	}


public StrategyAlwaysCooperate()
  	{
  	name = "Always cooperate";
  	}  /* StrategyAlwaysCooperate */

   public int nextMoveAlways()
  	{
  	return 1;
  	}


public StrategyRandom()
  	{
  	name = "Random";
  	}  /* StrategyRandom */

   public int nextMoveRandom()
  	{
  	if (Math.random() < 0.5)  return 1;
  	return 0;
  	}


   }  



