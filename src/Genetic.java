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
	
    private final int T = 25000;
    private int poblacionsize = 20;
    private  ArrayList<Solucion> poblacion = new ArrayList<>();
  //  public double mutateRate=0.05;
    public Random rnd = new Random();
    private int tournamentSize=6;
    public int numeromutaciones = 1;
    public double deathrate = 0.01;
    private double[] populationFitness;
    Solucion auxBest = new Solucion();
    public int numeroClusters= 2;//
    public double distEc = 21.5; // 1154
    public double distManhattan = 21.5; // 1030
    public int distanciaHamming = 460; //886
    public double similitudCoseno = 0.49; //scp410 scp65 0,41, scp510 0,47
    public int T_clustering = 500; // 
    SetCoveringInstanceFile scif = new SetCoveringInstanceFile();
    
    public int contador=0; //contador para llevar la cuenta de las generaciones estancadas
    public boolean auxiliar = true;

    

    public void execute() {
        initRandom();
     
       // System.out.println("primer best mejor:"+getbest().fitness());
        
        calculateAllFitness();
        
        run();
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
    	
    	 
    	int t = 0;
        while (t < T) {
        	

        	 auxBest = getBest();
        	double aux = getBestFitness();
        	boolean esUnico = false;
        	Solucion nuevaSolucion = null;


        	while(!esUnico) {
        		Solucion padreUno = getBest();
        		Solucion padreDos = this.tournamentSelection();
        		nuevaSolucion = this.crossover(padreUno, padreDos);
        		for(int i=0;i<=numeromutaciones;i++) {
        		nuevaSolucion = this.mutation(nuevaSolucion);
        		}
        			while(!nuevaSolucion.isFeasible()) {
        				nuevaSolucion = this.mutation(nuevaSolucion);
        				
        		}
        		esUnico = this.isUnique(poblacion,nuevaSolucion);
        	
        	}
        	this.replace(nuevaSolucion);
        
        	
        		
        			 
       		if(t%T_clustering ==0 && t != 0) { // inicio clustering
       // 		System.out.println("clustering...");
        			
        				if(auxiliar) {
        					T_clustering=T_clustering/2;
        					auxiliar = false;
        				}
        			
        		  ArrayList<Solucion> poblacionCluster = new ArrayList<>();
        		  for(int i=0;i<numeroClusters;i++) {
        		 
        			  Solucion nueva = null;
        			Cluster cluster = new Cluster();
        			
        			cluster.setPoblacion(getBest());
        			//this.poblacion.remove(getBest());
        		//	System.out.println("Cluster:"+(i+1)+" Centroide: "+cluster.poblacion.get(0).fitness());
        			for(int j=0;j<poblacionsize/numeroClusters-1;j++) {
        				boolean aux1 = false;
        				
        				while(!aux1) {
        					
        					
        				nueva = creaconSimilitudCoseno(getBest());
        				
        					if(isUnique(cluster.poblacion,nueva)) {
        						aux1=true;
        						
        					}
        				
        				}
       				cluster.setPoblacion(nueva);
        				
        			}
        			clearBest();
        				
        			
        			poblacionCluster.addAll(cluster.poblacion);
        			
        			
        		}
        		
        		this.poblacion = poblacionCluster;
        		calculateAllFitness();
        	
        	}//fin clustering*/
        			
        	//		deathrate();
       	
     		 t++;
            
         toConsole(t);
        }

    }
    public void clearBest() {
    	double aux = getBestFitness();
    	//System.out.println("FitnessBest:"+getBestFitness());
    	for(int i=0;i<populationFitness.length;i++) {
    		if(populationFitness[i]==aux) {
    		//	System.out.println("Fitness:"+populationFitness[i]+" set to Max");
    			populationFitness[i]=Double.MAX_VALUE;
    		}
    		
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
    
    
    public double porcentajeCambio(double nuevo, double viejo) {
    	double valordecremento = viejo-nuevo;
    	return(valordecremento/viejo)*100;
    }
    
    
    public void deathrate() {
    	Solucion  vive= null;
   
    	for(int i=0;i<poblacion.size();i++) {
    		double random = ThreadLocalRandom.current().nextDouble(0, 1);
    			if(random < deathrate) {
    				
    				do {
    				 vive = new Solucion();
    				}while(!vive.isFeasible());
    				this.poblacion.set(i, vive);
    				this.populationFitness[i]=vive.fitness();
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

    
    public Solucion creaconDistanciaHamming(Solucion c) {
 	   boolean aux = false;
 	   Solucion p = c;
 	   while(!aux) {
 		   p = new Solucion();
 		
 		   if(p.distanciaHamming(c)<=distanciaHamming && p.isFeasible()) {
 	//		   System.out.println("true");
 			   aux=true;
 		   }
 	   
 		
 	   }
 //	  System.out.println(c.distanciaHamming(p));
 	  return p;
 	   }
 	   
 	   
    
   public Solucion creaconDistanciaEc(Solucion c) {
	   boolean aux = false;
	   Solucion p = c;
	   while(!aux) {
		   p = new Solucion();
		   if(p.distanciaEuclideana(c)<distEc && p.isFeasible()) {
	//		   System.out.println("true");
			   aux=true;
		   }
	   
		
	   }
		   
	  
	  // System.out.println(c.distanciaEuclideana(p));
	//  
	   return p;
	 
	   
   }
   public Solucion creaconDistanciaManhattan(Solucion c) {
	   boolean aux = false;
	   Solucion p = null;
	   
	   while(!aux) {
		   p = new Solucion();
		
		   if(p.distanciaManhattan(c)<distManhattan && p.isFeasible()) {
			   
			   aux=true;
		   }
	    }
	  // System.out.println(c.distanciaManhattan(p));
	   return p;  
   }
   public Solucion creaconSimilitudCoseno(Solucion c) {
	   boolean aux = false;
	   Solucion p = null;
	   while(!aux) {
		   p = new Solucion();
	//	System.out.println(c.cosineSimilarity(p));

		   if(p.cosineSimilarity(c)<similitudCoseno && p.isFeasible()) {
		
			   aux=true;
		   }
	    }
	   return p;  
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
    private boolean isUnique(ArrayList<Solucion> lista,Solucion solution){
        for(int i=0;i<lista.size();i++) {
        	if(lista.get(i).getCopy().fitness()== solution.fitness()) {
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
    private Solucion getBest(){
        double bestFitness = Double.MAX_VALUE;
        int index=0;
        for (int i = 0;i < this.poblacionsize;i++){
            if (this.populationFitness[i] < bestFitness) {
                bestFitness = this.populationFitness[i];
            index = i;
            
        }
        }
        return poblacion.get(index).getCopy();
    }
    
    
    
 
    public ArrayList<Solucion> clusteringPoblacion(int k){
    	
    	ArrayList<Solucion> poblacionCluster;
    	
    	for(int j=0;j<poblacion.size()/k;j++) {    		
    		 double bestFitness = Integer.MAX_VALUE;
    		 int minindex=0;
    		 for (int i = 0;i < this.poblacionsize;i++){
    	            if (this.populationFitness[i] < bestFitness)
    	                bestFitness = this.populationFitness[i];
    	            	minindex = i;
    	        }
    		 Cluster cluster = new Cluster();
    		 cluster.Clusterinit(poblacion.get(minindex));
    		 this.populationFitness[minindex]=Double.MAX_VALUE;
    		 
    		 
    		 
    		
    	}
    	return poblacion;
    }



 private void toConsole(int t) {
    	System.out.println("Generation:  " + t + " Fittest:  " +getBestFitness() );
    	
    

    }
 /* private void toConsole(int t) {
	
	System.out.println(String.format("%d:%s:%s", t,getBestFitness(),scif.getSeed()));
}

 
    */
    
}