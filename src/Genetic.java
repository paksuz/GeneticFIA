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
	
    private final int T = 10000;
    private int poblacionsize = 100;
    private  ArrayList<Solucion> poblacion = new ArrayList<>();
    public Solucion ganador1;
    public Solucion ganador2;
    public  ArrayList<Solucion> bestpoblacion = new ArrayList<>();
    public  ArrayList<Solucion> crossoverpoblacion = new ArrayList<>();
    public Solucion hijo= new Solucion();
    public double[] probAc = new double[poblacionsize];
    public double elitistrate=0.01;
    public Solucion best;
    public double mutateRate= 0.3;

    public void execute() {
        initRandom();
       // System.out.println("primer best mejor:"+getbest().fitness());
        
        
        
        run();
    }

    
    public void ordenarxfitness(ArrayList<Solucion> pob) {
   	 Collections.sort(pob, new Comparator<Solucion>() {
		    @Override
		    public int compare(Solucion c1, Solucion c2) {
		        return Double.compare(c1.fitness(), c2.fitness());
		    }
		});
   	 
   
    	
    }
    
   
    
    private void run() {
    	
    	
    	int t = 0;
    	ordenarxfitness(poblacion);
    	best = poblacion.get(0);
        while (t < T) {
        
        	int numerodeElitistas = (int) (poblacion.size() * elitistrate); //condicion basada en elitismo
        	 //poblacion ordenada por fitness de menor a mayor
        	bestpoblacion=new ArrayList<>(numerodeElitistas);
        	
        	
     	 for(int i=0;i<numerodeElitistas;i++) {     		 
     		 bestpoblacion.add(poblacion.get(i));     		 
     	 }       	 
    
     	 //se crea una poblacion nueva solo con elitistas
     	 int numeroCrossover = (int)(poblacion.size()-numerodeElitistas);
     	// ordenaxrank(poblacion);
     	crossoverpoblacion= new ArrayList<>(numeroCrossover);
     	
     	//Collections.reverse(poblacion);
     	 	for(int j =0;j<numeroCrossover;j++) {
     	 		do {
     	 		ganador1=(Solucion)ruleta() ; 
     	 		ganador2=(Solucion)ruleta();
     	 	//	System.out.println("padre1 "+ganador1.fitness());
     	 	//	System.out.println("padre2 "+ganador2.fitness());
     	 	//	System.out.println("Ganador1"+ganador1.fitness());
     	 		
     	 		 hijo = crossover(ganador1,ganador2,hijo);
     	 		
     	 			
     	 		hijo.mutate(mutateRate);	
     	 		
     	 		}while((!hijo.isFeasible()));
     	 	//	System.out.println("hijo fitness:"+hijo.fitness());
     	 	//	System.out.println("best fitness:"+best.fitness());	
     	 		
     	 	//	System.out.println("hijo nuevo"+hijo.fitness());
     	 		
     	 		crossoverpoblacion.add(hijo); 		
     	 		
     	 		
     	 	}
     	 	
     	 	
     	 	crossoverpoblacion.addAll(bestpoblacion);
     	 	
     	 	poblacion = crossoverpoblacion;
     	 	ordenarxfitness(poblacion);
     	 	best = poblacion.get(0);
     	 	
     	 	

     		 t++;
            
         toConsole(t);
        }

    }

    
    

    
    
    
    
    public Solucion ruleta() {
    	//System.out.println("---------------------------------------\n");
    	initProbs();
 /* 	for(int i=0;i<probAc.length;i++) {
    		
      	  System.out.println("Probabilidad Acumulada "+ probAc[i]);
      	System.out.println("Probabilidad Acumulada "+ poblacion.get(i).fitness());
    		
    	}
    	System.out.println("---------------------------------------\n");
    	  System.out.println("---------------------------------------\n");*/
    	 
    	
    	double random = ThreadLocalRandom.current().nextDouble(probAc[0],probAc[probAc.length/2]) ;
    //	System.out.println("random porcentaje :"+random);
    	
    	
    	for(int i=poblacion.size()-1;i>0; i--) {
    		
    		if(random > probAc[i-1]&& random <= probAc[i]) {

    		/*	System.out.println("Fitness del Elemento escogido:" + poblacion.get(i).fitness() );
    			System.out.println("Probabilidad del elemento escogido :" + probAc[i]) ;
    			System.out.println("prob siguiente:" + probAc[i+1] +"...\n" );*/
    		//	System.out.println("Fitness ruleta: "+poblacion.get(i).fitness()+"prob:"+probAc[i]);
    			return poblacion.get(i);
    		}else if(i==0) return poblacion.get(0);
    		
    	}
    	System.out.println("---------------------------------------\n");
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
    		sumapoblacion += poblacion.get(i).fitness();
    		
    	}
		pesofitness =  poblacion.get(0).fitness();
		pesofitness = pesofitness/sumapoblacion;
		probAc[0]= pesofitness;
		
    	for(int i=1;i<poblacion.size();i++) {
    		pesofitness =  poblacion.get(i).fitness();
    		pesofitness = pesofitness/sumapoblacion;
    		probAc[i]= probAc[i-1] + pesofitness;
    		
    	}   
    	
    	/*
    	for(int i=0;i<poblacion.size();i++) {
    	System.out.println("prob AC:"+probAc[i]);
    	}*/
    }
    
 

    public Solucion crossover(Solucion padre1,Solucion padre2, Solucion crossresult) {
    //	System.out.println("AAA"+padre1.nVariables);
    	
    	for(int i=0;i<padre1.nVariables;i++) {
    		//si son iguales se mantiene
    		if(padre1.x[i]==padre2.x[i]) {
    			crossresult.x[i]=padre1.x[i];
    		}else {
    			double sumapadres = padre1.fitness()+padre2.fitness();
    			double pesopadre1 = padre2.fitness()/sumapadres;
    			double pesopadre2 = 1-pesopadre1;
    			double random = ThreadLocalRandom.current().nextDouble(0,1) ;
    			if(random <= pesopadre1) {
    				crossresult.x[i]=padre1.x[i];
    			}else {
    				crossresult.x[i]=padre2.x[i];
    				
    			}
    				
    		}
    		
    		
    		
        
    }
    	 return	crossresult;   
    }




 private void toConsole(int t) {
    	System.out.println("Generation:  " + t + " Fittest:  " +best.fitness() );
    	

    }


    public double porcentajeCambio(double nuevo,double viejo) {
    	
    	double valordecremento= viejo-nuevo;
    	return(valordecremento/viejo)*100;
    	
    }
    
  
    
    
}