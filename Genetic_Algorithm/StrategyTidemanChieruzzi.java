import java.util.ArrayList;

public class StrategyTidemanChieruzzi extends Strategy
   {
  /**
   * Encoding for tit-for-tat strategy.
   */

  // 0 = defect, 1 = cooperate

	
 	int numDefects;
	int moveNum;
	int ourScore;
	int theirScore;
	int firstMove;
	int turnsToWait;
	int currentStrategy;

	int defects = 0;
	int coop = 0;	
	
	ArrayList<Integer> ourMoves = new ArrayList<>();
	ArrayList<Integer> theirMoves = new ArrayList<>();
	ArrayList<Integer> ourScores = new ArrayList<>();
	ArrayList<Integer> theirScores = new ArrayList<>();
	ArrayList<Integer> ordering = new ArrayList<>();
	ArrayList<Double> percent = new ArrayList<>();
 	

	public StrategyTidemanChieruzzi(){
		
		name = "TidemanChieruzzi";
		moveNum = 0;
		numDefects = 0;
		ourScore = 0;
		theirScore = 0;
		currentStrategy = -1;


	}

	public int nextMove(){

		int move;
		double selection;
		
		
		if(moveNum != 0)
		{
			ourMoves.add(myLastMove);
			theirMoves.add(opponentLastMove);
			ourScores.add(calcScore(myLastMove,opponentLastMove));
			theirScores.add(calcScore(opponentLastMove,myLastMove));
			ourScore += ourScores.get(moveNum-1);
			theirScore += theirScores.get(moveNum-1);

		}

			
		
	
		currentStrategy = 6;
		switch (currentStrategy){
			
		case 0:
			move = TitForTat();
			break;
		case 1:
			move = Adaptive();
			break;
		case 2:
			move = Pavlov();
			break;
		case 3:
			move = TitForTwoTats();
			break;
		case 4:
			move = AlwaysCooperate();
			break;
		case 5:
			move = Random();
			break;
		case 6:
			move = TidemanChieruzziCalc();
			break;
		case 7:
			move = RevisedDowningCalc();
			break;
		default:
			move = firstMove;
		}
		
		moveNum++;
		return move;

	}


   public int TitForTat()
  	{
	   
	   if(moveNum == 0)
		   opponentLastMove = 1;
	   return opponentLastMove;
  	}  /* StrategyTitForTat */





	public int Adaptive(){

		double averageCooperate = 0;
		double averageDefect = 0;
		int numCoop = 0;
		int numDef = 0;
		
		if(moveNum < 5)
			return 1;
		else if(moveNum < 10)
			return 0;
		for(int i=0; i < ourMoves.size(); i++){
			
			if(ourMoves.get(i)==1){
				averageCooperate += ourScores.get(i);
				numCoop++;
			}
			else{
				averageDefect += ourScores.get(i);
				numDef++;
			}	
		}
		averageCooperate /= numCoop;
		averageDefect /= numDef;
		
		
		return averageCooperate >= averageDefect ? 1 : 0;
	}
	
	public int Pavlov(){
		
		if(moveNum == 0)
			return 1;
		
		if(ourScores.get(moveNum-1) == 3 || ourScores.get(moveNum-1) == 5)
			return myLastMove;
		else 
			return (myLastMove + 1) % 2;
		

	}


  // 0 = defect, 1 = cooperate

   public int TitForTwoTats()
  	{
	  	if(moveNum == 0)
	  	{
		   opponentLastMove = 1;
		   numDefects = 0;
	  	}
	  	
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
	
	public int AlwaysCooperate()
	  	{
		return 1;
	  	}  /* StrategyAlwaysCooperate */
	
	
	
	
	public int Random()
  	{
	if (Math.random() < 0.5)  return 1;
  		return 0;
  	}  /* StrategyRandom */
	
	
	public int calcScore(int me, int op){
		
		if(me == 0 && op == 0)
			return 3;
		if(me == 0 && op == 1)
			return 7;
		if(me == 1 && op == 1)
			return 5;
		return 1;
	}

	
	int partition(ArrayList<Double> arr, ArrayList<Integer> arr2, int left, int right)
	{
	      int i = left, j = right;
	      double tmp;
	      int tmp2;
	      double pivot = arr.get((left + right) / 2);
	     
	      while (i <= j) {
	            while (arr.get(i) < pivot)
	                  i++;
	            while (arr.get(j) > pivot)
	                  j--;
	            if (i <= j) {
	                  tmp = arr.get(i);
	                  tmp2 = arr2.get(i);
	                  arr.set(i, arr.get(j));
	                  arr2.set(i, arr2.get(j));
	                  arr.set(j,tmp);
	                  arr2.set(j, tmp2);
	                  i++;
	                  j--;
	            }
	      };
	     
	      return i;
	}
	 
	void dualQuickSort(ArrayList<Double> arr, ArrayList<Integer> arr2, int left, int right) {
	      int index = partition(arr, arr2, left, right);
	      if (left < index - 1)
	            dualQuickSort(arr, arr2, left, index - 1);
	      if (index < right)
	            dualQuickSort(arr, arr2, index, right);
	}

	public int TidemanChieruzziCalc()				//only checks for your score being 10 points above theirs
	{							//as condidtion for start over, so technically truncated TidemanChieruzzi
		int myscore = 0;
		int theirscore = 0;
		if(opponentLastMove == 0)
		{
			for(int i = 0; i < moveNum; i++)
			{
				if(theirMoves.get(i) == 0)
					defects++;
			}
			defects--;
			return 0;
		}
		else if(defects != 0)
		{
			defects--;
			return 0;
		}
		else if(coop != 0)
		{
			coop--;
			return 1;
		}
		else 
		{
			for(int i = 0; i < moveNum; i++)
			{
				myscore += ourScores.get(i);
				theirscore += theirScores.get(i);
			}
			if((myscore - theirscore) > 10)
			{
				coop++;
				return 1;
			}
			return 0;
		}
	}
	
	public int RevisedDowningCalc()
	{
		int	move2 = 2;		
		int TC = 0;			//# of times we cooperated
		int TD = 0;			//# of times we defected
		int CC = 0;			//# of times they cooperated afer we cooperated
		int CD = 0;			//# of times they defected after we cooperated
		int DC = 0;			//# of times they cooperated after we defected
		int DD = 0;			//# of times they defected after we defected
		
		double pcc = 0;			//probability they cooperate if we previously cooperated
		double pdc = 0.5;		//probability they cooperate if we previously defected
		
		if(moveNum == 0 || moveNum == 1)					//if 1st or second turn, cooperate
			move2 = 1;
		else if(moveNum > 1)							//if turn 3+
		{
			for(int i = 0; i < moveNum - 1; i++)				//calculate conditional probabilities
			{
				if(ourMoves.get(i) == 1)				//if we cooperated
				{
					TC++;						//increment total number of times we cooperated
					if(theirMoves.get(i + 1) == 1)			//if they cooperated after we cooperated
					{
						CC++;					//increment count
					}
					else
					{
						CD++;
					}
				}
				else							//we defected
				{
					TD++;						//increment total number of times we defected
					if(theirMoves.get(i + 1) == 1)			//if they cooperated
					{
						DC++;					//increment count
					}
					else
					{
						DD++;			
					}
				}		
			}
			
			pcc = (double) CC / TC;						//probability they cooperate after we cooperate
			if(TD > 0)
			{
				pdc = (double) DC / TD;					//probability they cooperate after we defect
			}
			
			double r = Math.random();					//randomly chose number between 0 - 1
			if(myLastMove == 1)						//if we cooperated last move
			{
				if(r <= pcc)						//if r less than or equal to probability of cooperation
					move2 = 1;					//cooperate
				else
					move2 = 0;					//otherwise defect
			}
			else								//if we defected last move
			{
				if(r <= pdc)						//if r less than or equal to probability they cooperate
					move2 = 1;					//cooperate
				else
					move2 = 0;
			}
		
		}
		return move2;
	}


}  



