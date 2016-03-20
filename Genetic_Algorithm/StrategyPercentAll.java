import java.util.ArrayList;

public class StrategyPercentAll extends Strategy
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
	
	ArrayList<Integer> ourMoves = new ArrayList<>();
	ArrayList<Integer> theirMoves = new ArrayList<>();
	ArrayList<Integer> ourScores = new ArrayList<>();
	ArrayList<Integer> theirScores = new ArrayList<>();
	ArrayList<Integer> ordering = new ArrayList<>();
	ArrayList<Double> percent = new ArrayList<>();
 	

	public StrategyPercentAll(ArrayList<Double> strat){
		
		name = "Percent All";
		moveNum = 0;
		numDefects = 0;
		ourScore = 0;
		theirScore = 0;
		currentStrategy = -1;
		
		firstMove = strat.get(0).intValue();
		for(int i = 1; i < Parameters.geneSize + 1; i++){
			percent.add(strat.get(i));
			ordering.add(i-1);
		}
		turnsToWait = strat.get(Parameters.geneSize + 1).intValue();
		
		//Orders percents from lowest to highest while rearanging an indexing 
		//array to match the sequence of the strategies
		dualQuickSort(percent, ordering, 0, Parameters.geneSize-1);
		
		//Converts list of percents into a list of ranges, i.e
		// .3, .3, .4 -> .3, .6, 1
		for(int i = 1; i < Parameters.geneSize; i++){
			percent.set(i, percent.get(i-1) + percent.get(i));
		}

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
		
		if(moveNum % turnsToWait == 0 && moveNum != 0){
		
			selection = Search.r.nextDouble();
			//System.out.println("\nSelection: " + selection);
			
			currentStrategy = Parameters.geneSize - 1;
			for(int j = 0; j < Parameters.geneSize - 1; j++){
				if(selection < percent.get(j)){
					currentStrategy = ordering.get(j);
					break;
				}
			}
			
			
		}
		
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

}  



