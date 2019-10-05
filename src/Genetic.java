import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Genetic {
	
    private final int T = 1000;
    private final int poblacionsize = 100;
    private final ArrayList<Solucion> poblacion = new ArrayList<>();
    private double[] poblacionFitness = new double[poblacionsize];
    private Random rnd = new Random();
    private Solucion best;
    private Solucion best2;
/*
    Random rn = new Random();

    Solucion fittest;
    Solucion secondFittest;
    int generationCount = 0;
    int T = 30;
    */

    public void execute() {
        initRandom();

        
        
        
        run();
    }
    
    public void initRandom() {
        Solucion p;
        for (int i = 1; i <= poblacionsize; i++) {
            do {
                p = new Solucion();
            } while (!p.isFeasible());
          
            poblacion.add(p);
        }
        
      /*  toConsole(0);
        System.out.println();*/
    }
    

  

    
    private void run() {
        int t = 0;
       
        
        while (t < T) {
        	
        	seleccion();
        	crossover();
        	double random = rnd.nextInt(100);
            if (random < 1) {{
            }
                mutation();
            }
          //  System.out.println(poblacion.get(getLeastFittestIndex()).fitness()  );
            addFittestOffspring();
            calculateFitness();

            t++;
            
           toConsole(t);
        }
    }
    
    
    private void seleccion() {
    	this.best = getbest();
    	this.best2 = getbest2();
    	
    	
    }
    
    
    public Solucion getbest() {
        double minFit = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < poblacionsize; i++) {
            if (poblacion.get(i).fitness()<= minFit) {
                minFit = poblacion.get(i).fitness();
                minFitIndex = i;
            }
        }
        
        return poblacion.get(minFitIndex);
    }
    
    public Solucion getbest2() {
    	double promedio=0;
    	for(int i=0; i< poblacionsize; i++) {
    		promedio += poblacion.get(i).fitness();
    		
    	}
    	promedio = promedio/poblacionsize;
    	
    	boolean x = false;
    	int pos = rnd.nextInt(poblacionsize);;
    	while(!x) {
    		 
    		if(poblacion.get(pos).fitness()<=promedio) {
    			
    			x=true;    			
    		}else pos = rnd.nextInt(poblacionsize);	
    		
    	}
    	
    	return poblacion.get(pos);
    	
   
    }
    
    void crossover() {
        Random rn = new Random();

        //Select a random crossover point
        int crossOverPoint = rn.nextInt(poblacion.get(0).nVariables);

        //Swap values among parents
        for (int i = 0; i < crossOverPoint; i++) {
            int temp = best.x[i];
            best.x[i] = best2.x[i];
            best2.x[i] = temp;

        }

    }
    
    void mutation() {
        Random rn = new Random();

        //Select a random mutation point
        int mutationPoint = rn.nextInt(poblacion.get(0).nVariables);

        //Flip values at the mutation point
        if (best.x[mutationPoint] == 0) {
            this.best.x[mutationPoint] = 1;
        } else {
            this.best.x[mutationPoint] = 0;
        }

        mutationPoint = rn.nextInt(poblacion.get(0).nVariables);

        if (best2.x[mutationPoint] == 0) {
            this.best2.x[mutationPoint] = 1;
        } else {
            this.best2.x[mutationPoint] = 0;
        }
    }
    
    
    void addFittestOffspring() {


        //Get index of least fit individual
        int leastFittestIndex = getLeastFittestIndex();
        

        //Replace least fittest individual from most fittest offspring
        poblacion.set(leastFittestIndex, getFittestOffspring());
    }

    public int getLeastFittestIndex() {
        double maxFitVal = Double.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < poblacionsize; i++) {
            if (poblacion.get(i).fitness() > maxFitVal) {
            	maxFitVal = poblacion.get(i).fitness();
            	maxFitIndex = i;
            }
        }
        return maxFitIndex;
    }

    Solucion getFittestOffspring() {
        if (best.fitness() < best2.fitness()) {
            return this.best;
        }
        return this.best2;
    }
    
    public void calculateFitness() {

        for (int i = 0; i < poblacionsize; i++) {
            poblacion.get(i).fitness();
        }
        getbest();
    }





 private void toConsole(int t) {
	 	System.out.println("Best1:"+ best.fitness() + "Best2:"+best2.fitness());
    	System.out.println("Generation:  " + t + " Fittest:  " + best.fitness());
    }


    
   

    

    private void calculateAllFitness() {
    	for(int i=0;i<poblacionsize;i++) {    		
    		poblacionFitness[i]= poblacion.get(i).fitness();    		
    		
    
    }
    }
    	


    
  

    
    
}