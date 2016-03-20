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
	//	int theirMovesDefect_Index = 0; // Getting first defect index (= 0)
	int co_OpCount = 0; // The cooperation count (maximum value 2) after a defect is detected
	int numCoop = 0;
	int t_NumDef = 0;
	int defCount = 0;


	ArrayList<Integer> ourMoves = new ArrayList<>();
	ArrayList<Integer> theirMoves = new ArrayList<>();
	ArrayList<Integer> ourScores = new ArrayList<>();
	ArrayList<Integer> theirScores = new ArrayList<>();	
	//ArrayList<Integer> temp_theirMoves = new ArrayList<>(); //Copying a temporary ArrayList for evaluation and manipulation


	public StrategyPercentAll(){

		name = "Percent All";
		moveNum = 0;
		numDefects = 0;
		ourScore = 0;
		theirScore = 0;

	}

	public int nextMove(){

		int move;
		double selection = Math.random();

		if(moveNum != 0)
		{
			ourMoves.add(myLastMove);
			theirMoves.add(opponentLastMove);
			ourScores.add(calcScore(myLastMove,opponentLastMove));
			theirScores.add(calcScore(opponentLastMove,myLastMove));
			ourScore += ourScores.get(moveNum-1);
			theirScore += theirScores.get(moveNum-1);

		}

		if (selection < .1)
			move = Pavlov();
		else if(selection < .2)
			move = Adaptive();
		else if (selection < .3)
			move = SoftGurder();
		else if(selection < .4)
			move = Gradual();
		else if (selection < .5)
			move = Friedmen();
		else if(selection < .6)
			move = AlwaysCooperate();
		else
			move = TitForTat();

		moveNum++;
		return move;

	}



	public int TitForTat()
	{
		//name = "Tit for Tat";

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
		//name = "Tit for Two Tats";
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
		//name = "Always cooperate";
		return 1;
	}  /* StrategyAlwaysCooperate */




	public int Random()
	{
		//name = "Random";
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


	public int Gradual() {


		for(int i=0; i < theirMoves.size(); i++){

			if(theirMoves.get(i)==1){
				numCoop++;
			}
			else{

				t_NumDef++;
			}

		}

		if(t_NumDef > 0) {
			co_OpCount=2;
			t_NumDef--;
			return 0;
		}

		if (co_OpCount > 0){
			co_OpCount--;
			return 1;
		}
		return 1 ;
	}

	public int SoftGurder(){

		for(int i=0; i < theirMoves.size(); i++){

			if(theirMoves.get(i)==0)
				t_NumDef = 4;



			if(t_NumDef > 0) {
				co_OpCount=2;
				t_NumDef--;
				return 0;
			}

			if (co_OpCount > 0){
				co_OpCount--;
				return 1;
			}



		}
		return 1;
	}

	public int Friedmen(){

		for(int i=0; i < theirMoves.size(); i++){

			if(theirMoves.get(i)==0)
				return 0;

		}
		return 1 ;
	}



}







