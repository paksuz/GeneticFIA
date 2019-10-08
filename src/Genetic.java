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
	
    private final int T = 2;
    private int poblacionsize = 20;
    private  ArrayList<Solucion> poblacion = new ArrayList<>();
    private Random rnd = new Random();
    public Solucion ganador1;
    public Solucion ganador2;
    public Solucion bestoverall = new Solucion();
    public  ArrayList<Solucion> bestpoblacion = new ArrayList<>();
    public  ArrayList<Solucion> crossoverpoblacion = new ArrayList<>();
    public int tabooRate = 0;
    public double[] probAc = new double[poblacionsize];
    public double [] rankAc ;
    public double elitistrate=0.10;
    public double mutateRate= 0.03;
    public double alpha=0.7;
    
    
    
    
    


    public void execute() {
        initRandom();
        initProbs();
       // System.out.println("primer best mejor:"+getbest().fitness());
        
        
        
        run();
    }
    public void ordenaxrank() {
     	
    	ordenarxfitness();
      	 
       	//Collections.reverse(poblacion);
       	SplitRank();
       	
       /*	Collections.sort(poblacion, new Comparator<Solucion>() {
   		    @Override
   		    public int compare(Solucion c1, Solucion c2) {
   		        return Double.compare(c1.rank, c2.rank);
   		    }
   		});
    	*/
      
    }
    
    public void ordenarxfitness() {
   	 Collections.sort(poblacion, new Comparator<Solucion>() {
		    @Override
		    public int compare(Solucion c1, Solucion c2) {
		        return Double.compare(c1.fitness, c2.fitness);
		    }
		});
    	
    	
    	
    }
    
   
    
    private void run() {
    	//ordena la lista por fintess y luego calcula el ranking
    	//luego la reordena en base a su ranking
    	//ordenaxrank();
    	//ordenarxfitness();
  /*	for(int i=0;i<poblacion.size();i++) {
    		System.out.println(poblacion.get(i).rank);
    	//System.out.println(poblacion.get(i).fitness);
    	}
 	for(int i=0;i<poblacion.size();i++) {
		//System.out.println(poblacion.get(i).rank);
	System.out.println(poblacion.get(i).fitness);
	}
    	*/
  	//	System.out.println("fitness mejor :"+getbest().fitness());
    //	seleccion();
    //	bestOverall();
    //	System.out.println("fitness mejor de Todos los tiempos :"+bestoverall.fitness());
    	
    	int t = 0;

        while (t < T) {
        //	SplitRank();
        		
        	calcularFitness();
        	bestpoblacion.clear();
        	crossoverpoblacion.clear();
        	int numerodeElitistas = (int) (poblacion.size() * elitistrate); //condicion basada en elitismo
        //	Collections.reverse(poblacion);
        	 //poblacion ordenada por fitness de menor a mayor
        	ordenarxfitness();
     	 for(int i=0;i<numerodeElitistas;i++) {
     		 
     		 bestpoblacion.add(poblacion.get(i));
     		 
     		 
     	
     		
     	 }  
     	 
     	 for(int i=0;i<bestpoblacion.size();i++) {
     		 System.out.println("ELitista POB FItness: "+bestpoblacion.get(i).fitness);
     		 
     	 }
     	 //se crea una poblacion nueva solo con elitistas
     	 int numeroCrossover = (int)(poblacionsize-numerodeElitistas)/2;
     	 ordenaxrank();
     	//Collections.reverse(poblacion);
     	 	for(int j =0;j<numeroCrossover;j++) {
     	 		
     	 		ganador1=splitrankruleta() ;
     	 		
     	 		ganador2=splitrankruleta();
     	 		
     	 	
     	 		
     	 		crossover();
     	 		
     	 		crossoverpoblacion.add(ganador1);
     	 		crossoverpoblacion.add(ganador2);
     	 	}
     	 
     	 	//por la cantidad de crossovers que se generaron se muta cada bit de cada solucion
     	 	//y si no es factible, se genera una nueva factible
     	 	
     	 	for(int j=0;j<crossoverpoblacion.size();j++) {
     	 		crossoverpoblacion.get(j).mutate(mutateRate);
     	 		if(!(crossoverpoblacion.get(j).isFeasible())) {
     	 			Solucion p; 
     	 			do {
     	                p = new Solucion();
     	            } while (!p.isFeasible());
     	 			
     	 			
     	 		//	System.out.println("infactible");
     	 			
     	 		}
     	 	}//updateamos nuestra poblacion
     	 	 poblacion.clear();
         	 poblacion = new ArrayList<Solucion>(bestpoblacion); 
        	 poblacion.addAll(crossoverpoblacion);
      	 

        	 calcularFitness();

       /* 	 Collections.sort(poblacion, new Comparator<Solucion>() {
     		    @Override
     		   public int compare(Solucion c1, Solucion c2) {
     		        return Double.compare(c1.fitness, c2.fitness);
     		    }
     		});*/
        	 
     //   	 Collections.reverse(poblacion);
        	 
        	 
        // System.out.println("bestOverall en T " + bestoverall.fitness);
     		 t++;
            
          toConsole(t);
        }

    }
    public void calcularFitness() {
    	for(int i=0;i<poblacion.size();i++) {
    		
    		poblacion.get(i).updateFit();
    		
    	}
    }
    

    public void SplitRank() {
    	double rank = 0;
    	int k = poblacion.size();
    	double beta = 1 - alpha;
    	for(int i=0;i<poblacion.size();i++) {
    	
    		if(i <= (k)/2) {
    			rank = 8 *i;
    			rank = rank /((k*k)-1);
    			rank = beta - rank;
    			
    			
    			poblacion.get(i).rank = rank;
    		//	poblacion.get(i).rank= beta - ((8*i)/((k*k)-1)) ;
    			
    		}else if(i>k/2) {
    			
    			rank = 3*k;
    			rank = rank +1;
    			rank = rank *(k+1);
    			rank = alpha+(8*i)/rank;
    			poblacion.get(i).rank=rank;
    			//poblacion.get(i).rank= alpha + ((8*i)/((k+1)*(3*k+1)));
    			
    		}
    		
    		
    	}
    	
    	
    	
    }
    
    public Solucion splitrankruleta() {
    	double min = 0;
    	double max = poblacion.get(poblacion.size()-1).rank;
    	
    	
    	double random = ThreadLocalRandom.current().nextDouble(min, max);
    	for(int i=0;i<poblacion.size();i++) {
    		
    		if(random < poblacion.get(i).rank) {

    	

    			return poblacion.get(i);
    		}
    		
    	}
    	return null;
    }
    	
    public Solucion getBest() {
    	double min = Double.MAX_VALUE;
    	int minindex=0;
    	for(int i=0;i<poblacion.size();i++) {
    		if(poblacion.get(i).fitness<min) {
    			min = poblacion.get(i).fitness;
    			minindex=i;
    		}
    	}
    	return poblacion.get(minindex);
    	
    }
    	
    	
    
    
    
    
    public Solucion ruleta() {
    	    	
    	initProbs();
    	  System.out.println("---------------------------------------\n");
   System.out.println("Probabilidad Acumulada Total"+ probAc[poblacionsize-1]);
    	
    	double random = ThreadLocalRandom.current().nextDouble(20, 99);
   // 	System.out.println("random porcentaje :"+random);
    	
    	
    	for(int i=poblacion.size()-1;i>0; i--) {
    		
    		if(random > probAc[i-1]&& random <= probAc[i]) {

    		/*	System.out.println("Fitness del Elemento escogido:" + poblacion.get(i).fitness() );
    			System.out.println("Probabilidad del elemento escogido :" + probAc[i]) ;
    			System.out.println("prob siguiente:" + probAc[i+1] +"...\n" );*/

    			return poblacion.get(i);
    		}
    		
    	}
    	return null;
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
    
    
    public void initProbs() {
    	double sumapoblacion =0.0;
    	double pesofitness= 0.0;
    	for(int i=0;i<poblacion.size();i++) {
    		sumapoblacion += poblacion.get(i).fitness;
    		
    	}
		pesofitness =  sumapoblacion-poblacion.get(0).fitness;
		pesofitness = pesofitness/sumapoblacion;
		probAc[0]= pesofitness;
		
    	for(int i=1;i<poblacion.size();i++) {
    		pesofitness =  sumapoblacion-poblacion.get(i).fitness;
    		pesofitness = pesofitness/sumapoblacion;
    		probAc[i]= probAc[i-1] + pesofitness;
    		
    	}    	
    }
    
 

    public void crossover() {
    	
        //Select a random crossover point
    	
        int crossOverPoint = ThreadLocalRandom.current().nextInt(0,poblacion.get(0).nVariables-1 );
        for(int i=0;i<crossOverPoint;i++) {

        //Swap values among parents
            int temp = ganador1.x[i];          
            ganador1.x[i] = ganador2.x[i]; 
            ganador2.x[i] = temp;

        }
        ganador1.updateFit();
        ganador2 .updateFit();
       
        
    }




 private void toConsole(int t) {
    	System.out.println("Generation:  " + t + " Fittest:  " + getBest().fitness);
    	

    }


    public double porcentajeCambio(double nuevo,double viejo) {
    	
    	double valordecremento= viejo-nuevo;
    	return(valordecremento/viejo)*100;
    	
    }
    
  
    
    
}