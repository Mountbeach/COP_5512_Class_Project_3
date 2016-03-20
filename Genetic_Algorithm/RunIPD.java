import java.io.FileWriter;
import java.util.ArrayList;

/**
 * General class containing main program to run the
 * iterated Prisoner's Dilemma (IPD).
 * @author	081028AW
 */
public class RunIPD extends FitnessFunction
   {
  /**
   * Main program to start IPD program.
   */

	public RunIPD(){
		name = "IPD Tournament";
	}
	
	
	public void doRawFitness(Chromo x, int rounds)
      {
      int i;
      double score = 0;
      int maxSteps = 10;
      
      Strategy player1;
      Strategy [] players = new Strategy[5];

      
	

      player1 = new StrategyPercentAll(x.chromo);
      players[0] = new StrategyAlwaysCooperate();
      players[1] = new StrategyAlwaysDefect();
      players[2] = new StrategyRandom();
      players[3] = new StrategyTitForTat();
      players[4] = new StrategyTitForTwoTats();
     
      for(i=0; i<5; i++){
    	  IteratedPD ipd = new IteratedPD(player1, players[i]); 
    	  ipd.runSteps(maxSteps);
    	  score += ipd.p1Score;
      }
      
      score = score / (5 * maxSteps);
     
      x.rawFitness = score;

      } 
 

	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException
	{
		Hwrite.left("First move: " + X.chromo.get(0).intValue(), 14, output);
		output.write("\nPercent chance: ");
		for (int i=1; i<Parameters.geneSize + 1; i++){
			output.write(X.chromo.get(i) + " ");
		}
		output.write("\n");
		output.write("   RawFitness: ");
		
		Hwrite.right(X.rawFitness,13,output);
		output.write("\n\n");
		return;
	}

	
	
 }