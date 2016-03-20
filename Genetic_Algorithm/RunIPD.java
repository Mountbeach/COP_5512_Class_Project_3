import java.util.ArrayList;

/**
 * General class containing main program to run the
 * iterated Prisoner's Dilemma (IPD).
 * @author	081028AW
 */
public class RunIPD
   {
  /**
   * Main program to start IPD program.
   */

   public static void ipd(ArrayList<Double> percent)
      {
      int i;
      int maxSteps = 10;
      
      Strategy player1, player2;
      IteratedPD ipd;

      
	

      player1 = new StrategyPercentAll(percent);
      player2 = new StrategyTitForTat();
      ipd = new IteratedPD(player1, player2);

      ipd.runSteps(maxSteps);

      System.out.printf(" Player 1 score = %d\n", ipd.player1Score());
      System.out.printf(" Player 2 score = %d\n", ipd.player2Score());

      }  /* main */
   }  /* class RunIPD */

