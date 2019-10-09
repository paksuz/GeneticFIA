import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Genetic {
	
    private final int T = 20000;
    public int iteracionesparacambio = 2;
    private int poblacionsize = 100;
    private  ArrayList<Solucion> poblacion = new ArrayList<>();
  //  public double mutateRate=0.05;
    public Random rnd = new Random();
    private int tournamentSize=10;
    public int numeromutaciones = 10;
    private double[] populationFitness;
    public void execute() {
        initRandom();
     
       // System.out.println("primer best mejor:"+getbest().fitness());
        
        calculateAllFitness();
        
        run();
    }
    
    public void checkCambio() {
    	for(int i=0;i<poblacion.size();i++) {
    		if(!poblacion.get(i).haCambiado) {
    			
    			if(poblacion.get(i).cambio<iteracionesparacambio) {
    				poblacion.get(i).cambio=  poblacion.get(i).cambio + 1;

    			//	System.out.println("Cambio:"+poblacion.get(i).cambio);
    			}else {
    				
    				do{
    					//System.out.println("Mutando las que no cambian");
    					poblacion.set(i, mutation(poblacion.get(i)));
    				}while(!poblacion.get(i).isFeasible());
    				
    			}
    			
    		}
    		
    		
    	}
    	
    	
    	
    }
    public void sethaCambiado() {
    	for (int i=0;i<poblacion.size();i++) {
    		if(poblacion.get(i).haCambiado) {
    		poblacion.get(i).haCambiado=false;
    		}else {
    			poblacion.get(i).cambio++;
    		}
    	}
    }
    private Solucion mutation(Solucion solution){
    	int i = ThreadLocalRandom.current().nextInt(0, solution.nVariables -1);
    	if(solution.x[i]==0) {
			solution.x[i]=1;
		}else  {
			solution.x[i]=0;
			}
        return solution;
    }
    
  
    
   
    
    private void run() {
    	double tfixed = 0.0;
    	 
    	int t = 0;
        while (t < T) {
        	if(t < 5000) {
        		numeromutaciones = 20;
        	}
        	
        	
        	boolean esUnico = false;
        	Solucion nuevaSolucion = null;


        	while(!esUnico) {
        		Solucion padreUno = this.tournamentSelection();
        		Solucion padreDos = this.tournamentSelection();
        		nuevaSolucion = this.crossover(padreUno, padreDos);
        		for(int i=0;i<=numeromutaciones;i++) {
        		nuevaSolucion = this.mutation(nuevaSolucion);
        		}
        			while(!nuevaSolucion.isFeasible()) {
        				nuevaSolucion = this.mutation(nuevaSolucion);
        				
        		}
        		esUnico = this.isUnique(nuevaSolucion);
        		
        		
        	}
        	this.replace(nuevaSolucion);
        	for(int i=0;i<poblacion.size();i++) {
        		if(poblacion.get(i).haCambiado) {
        			
        		}
        	}
        	
        	//checkCambio();
        //	sethaCambiado();
     		 t++;
            
         toConsole(t);
        }

    }

    private void replace(Solucion solution){
        if (solution != null){
            double averageFitness = 0;
            for (int i = 0;i < this.populationFitness.length;i++)
                averageFitness += this.populationFitness[i];
            averageFitness = averageFitness / this.populationFitness.length;
            boolean isReplaced = false;
            while (!isReplaced){
            	int random = ThreadLocalRandom.current().nextInt(0, poblacionsize);
                if (this.populationFitness[random] > averageFitness){
                	solution.cambio=0;
                	solution.haCambiado=true;
                    this.poblacion.set(random, solution);
                    this.populationFitness[random] = solution.fitness();
                    isReplaced = true;
                }
            }
        }
    }
    
    

    
    
    
   
       
    public void initRandom() {
        Solucion p;
        
        for (int i = 1; i <= poblacionsize; i++) {
            do {
                p = new Solucion();
            } while (!p.isFeasible());
            
            p.pBest = p.x;
            poblacion.add(p);
            
        }
        
  
      /*  toConsole(0);
        System.out.println();*/
    }
    
    private Solucion tournamentSelection(){
        Solucion best = null;
        double bestFitness = Double.MAX_VALUE;
        for (int i = 0;i < this.tournamentSize;i++){
        	int random = ThreadLocalRandom.current().nextInt(0, poblacionsize-1);
            
            Solucion individual = this.poblacion.get(random);
            if ((best == null) || (this.populationFitness[random] < bestFitness)) {
                best = individual;
                bestFitness = this.populationFitness[random];
            }
        }
        return best;
    }
    private boolean isUnique(Solucion solution){
        for(int i=0;i<populationFitness.length;i++) {
        	if(this.populationFitness[i]== solution.fitness()) {
        		return false;
        	}
        }
        return true;
  }

    private Solucion crossover(Solucion parentOne, Solucion parentTwo){
    	Solucion newSolution = new Solucion();
        for (int i = 0;i < parentOne.nVariables;i++){
            if (parentOne.x[i] == parentTwo.x[i])
                newSolution.set(i, parentOne.get(i));
            else{
                double prob = parentOne.fitness() / (parentOne.fitness() + parentTwo.fitness());
                if (this.rnd.nextDouble() >= prob)
                    newSolution.set(i, parentOne.get(i));
                else
                    newSolution.set(i, parentTwo.get(i));
            }
        }
        return newSolution;
    }
    	   
    
    private void calculateAllFitness(){
        populationFitness = new double[this.poblacionsize];
        for (int i = 0;i < this.poblacionsize;i++){
            this.populationFitness[i] = this.poblacion.get(i).fitness();
        }
    }
    private double getBestFitness(){
        double bestFitness = Integer.MAX_VALUE;
        for (int i = 0;i < this.poblacionsize;i++){
            if (this.populationFitness[i] < bestFitness)
                bestFitness = this.populationFitness[i];
        }
        return bestFitness;
    }



 private void toConsole(int t) {
    	System.out.println("Generation:  " + t + " Fittest:  " +getBestFitness() );
    	

    }


 
    
    
}