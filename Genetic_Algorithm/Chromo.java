/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public String oldChromo;

	public ArrayList <Double> chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;
	public int index;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){

		//  Set gene values to a randum sequence of 1's and 0's
		char geneBit;
		oldChromo = "";
		
		chromo = new ArrayList<>();
		
		//	Sets first move
		randnum = Search.r.nextDouble();
		if (randnum > 0.5) 
			this.chromo.add((double)0);
		else 
			this.chromo.add((double)1);
		
		//	Sets original percentages for strategies
		List<Double> startChances = new ArrayList<Double>();
		double cap = 1;
		for (int j=0; j<Parameters.geneSize; j++){
			randnum = cap * Search.r.nextDouble();
			cap -= randnum;
			startChances.add(randnum);	
		}
		Collections.shuffle(startChances);
		for(double j:startChances){
			this.chromo.add(j);
			//System.out.println(j);
		}
		//System.out.print(this.chromo.get(20));
		//	Sets original number of turns till reevaluation, must be in range 1 - 20
		randnum = Search.r.nextInt(20) + 1;
		this.chromo.add(randnum);
		
		
		
		for (int j=0; j<Parameters.geneSize; j++){
			randnum = Search.r.nextDouble();
			if (randnum > 0.5) geneBit = '0';
			else geneBit = '1';
			this.oldChromo = oldChromo + geneBit;
			
		}

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

	public String getGeneAlpha(int geneID){
		int start = geneID * Parameters.geneSize;
		int end = (geneID+1) * Parameters.geneSize;
		String geneAlpha = this.oldChromo.substring(start, end);
		return (geneAlpha);
	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

	public int getIntGeneValue(int geneID){
		String geneAlpha = "";
		int geneValue;
		char geneSign;
		char geneBit;
		geneValue = 0;
		geneAlpha = getGeneAlpha(geneID);
		for (int i=Parameters.geneSize-1; i>=1; i--){
			geneBit = geneAlpha.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
		}
		geneSign = geneAlpha.charAt(0);
		if (geneSign == '1') geneValue = geneValue - (int)Math.pow(2.0, Parameters.geneSize-1);
		return (geneValue);
	}

	//  Get Integer Value of a Gene (Positive only) ****************************

	public int getPosIntGeneValue(int geneID){
		String geneAlpha = "";
		int geneValue;
		char geneBit;
		geneValue = 0;
		geneAlpha = getGeneAlpha(geneID);
		for (int i=Parameters.geneSize-1; i>=0; i--){
			geneBit = geneAlpha.charAt(i);
			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
		}
		return (geneValue);
	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	
	//Altered for HW3
	public void doMutation(){

		double sum = 0;

		switch (Parameters.mutationType){

		case 1:     //  The only mutation
			
			//Randomly flips whether to co-op or defect
			randnum = Search.r.nextDouble();
			if (randnum < Parameters.mutationRate){
				if (this.chromo.get(0) == 1) this.chromo.set(0, 0.0);
				else this.chromo.set(0, 1.0);
			}
			
			//For each strategy, either adds or subtracts from its chance
			//Then it randomly distributes the difference to the other chances
			for (int j=1; j< (Parameters.geneSize + 1); j++){
				randnum = Search.r.nextDouble();
				if (randnum < Parameters.mutationRate){
					//Randomly add or subtract
					randnum = Search.r.nextDouble();
					if(randnum > .5){
						List<Double> changeChances = new ArrayList<Double>();
						double cap = 1 - this.chromo.get(j);
						randnum = cap * Search.r.nextDouble();
						this.chromo.set(j, this.chromo.get(j) + randnum);
						cap = randnum;
						
						for (int k=0; k<(Parameters.geneSize - 2); k++){
							randnum = cap * Search.r.nextDouble();
							cap -= randnum;
							changeChances.add(randnum);	
						}
						changeChances.add(cap);
						Collections.shuffle(changeChances);
						int counter = 0;
						
						for(int k=0; k<Parameters.geneSize; k++){
							if(k+1 == j)
								continue;
							this.chromo.set(k+1, this.chromo.get(k+1) - changeChances.get(counter));
							counter++;
						}
						for(int k=0; k<Parameters.geneSize; k++){
							if(this.chromo.get(k+1) < 0)
								this.chromo.set(k+1, 0.0);
							sum += this.chromo.get(k+1);
						}
						for(int k=0; k<Parameters.geneSize; k++){
							this.chromo.set(k+1, this.chromo.get(k+1)/sum);
						//	System.out.println(this.chromo.get(k+1));
						}
					}
					else{
						List<Double> changeChances = new ArrayList<Double>();
						double cap = this.chromo.get(j);
						randnum = cap * Search.r.nextDouble();
						this.chromo.set(j, this.chromo.get(j) - randnum);
						cap = randnum;
						
						for (int k=0; k<(Parameters.geneSize - 1); k++){
							randnum = cap * Search.r.nextDouble();
							cap -= randnum;
							changeChances.add(randnum);	
						}
						changeChances.add(cap);
						Collections.shuffle(changeChances);
						int counter = 0;
						
						for(int k=0; k<Parameters.geneSize; k++){
							if(k+1 == j)
								continue;
							this.chromo.set(k+1, this.chromo.get(k+1) + changeChances.get(counter));
							counter++;
						}
						for(int k=0; k<Parameters.geneSize; k++){
							if(this.chromo.get(k+1) < .000001)
								this.chromo.set(k+1, 0.0);
							sum += this.chromo.get(k+1);
						}
						for(int k=0; k<Parameters.geneSize; k++){
							this.chromo.set(k+1, this.chromo.get(k+1)/sum);
							//System.out.println(this.chromo.get(k+1));
						}
					}
					//System.out.println(this.chromo.get(20));
				}
				
				//Randomly changes number of turns a strategy is selected for
				//TODO: Make the number adjusted by a binomial or Poisson distribution centered at 0
				randnum = Search.r.nextDouble();
				if(randnum > .5){
				
					//System.out.println("error " + this.chromo.size());
					int numTurns = this.chromo.get(Parameters.geneSize+1).intValue();
					
					
					randnum = Search.r.nextInt(40) - 20;
					numTurns += randnum;
					if(numTurns < 1) numTurns = 1;
					if(numTurns > 20) numTurns = 20;
					
					this.chromo.set(Parameters.geneSize+1, (double)numTurns);
					
				}
				
			}
			
			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(PriorityQueue<Chromo> list){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection
			
			Chromo winner, loser, gene1, gene2;
			//Tournament selection parameter
			double divide = .75;
			gene1 = Search.sorted[Search.r.nextInt(Parameters.popSize)];
			gene2 = Search.sorted[Search.r.nextInt(Parameters.popSize)];
				
			randnum = Search.r.nextDouble();
			
			if(gene1.rawFitness < gene2.rawFitness)
			{
				winner = gene1;
				loser = gene2;
			}
			else
			{
				winner = gene2;
				loser = gene1;
			}
			
			if(randnum < k)
				return winner.index;
			else
				return loser.index;

			
		case 4: 	//	Rank Selection
			int sum = (Parameters.popSize * (Parameters.popSize + 1))/2;
			int counter = Parameters.popSize;
			randnum = Search.r.nextInt(sum);
			while(randnum >= counter){
				randnum -= counter;
				counter--;
			}

			return Search.sorted[Parameters.popSize - counter].index;

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************
	//	Modified for HW3

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;
		double sum1 = 0;
		double sum2 = 0;

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover

			//  Select crossover point
			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * Parameters.geneSize-1);

			child1.chromo= new ArrayList<Double>(parent1.chromo.subList(0, xoverPoint1));
			child2.chromo= new ArrayList<Double>(parent2.chromo.subList(0, xoverPoint1));
			
			
			//  Create child chromo from parental material
			for(int i = xoverPoint1; i < Parameters.geneSize + 2; i++)
			{
				child1.chromo.add(parent2.chromo.get(i));
				child2.chromo.add(parent1.chromo.get(i));
				
			}
			
			for(int i = 1; i < Parameters.geneSize+1; i++){
				sum1 += child1.chromo.get(i);
				sum2 += child2.chromo.get(i);
			}
			
			for(int i = 1; i < Parameters.geneSize+1; i++){
				child1.chromo.set(i, child1.chromo.get(i)/ sum1);
				child2.chromo.set(i, child2.chromo.get(i)/ sum1);

			}
			
			
			
			//System.out.println(parent1.chromo.size());
			//System.out.println(parent1.chromo.get(20));
			break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

}   // End of Chromo.java ******************************************************
