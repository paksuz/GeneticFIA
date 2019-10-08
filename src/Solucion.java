import java.util.concurrent.ThreadLocalRandom;

public class Solucion {

    public final int nVariables = SetCoveringInstanceFile.getInstance().getCols();
    public int[] x = new int[nVariables];
    public int[] pBest = new int[nVariables];
    private double v;
    public double fitness;
    public double rank ;
    int[] taboo = new int[nVariables]; //variable taboo
 
    public void updateFit() {
    	this.fitness = fitness();
    }
    
    public void updatepBest() {
    	
    	if(fitness<fitnesspBest()) {
    		pBest = x;
    	}
    	
    }
    

    public void move(Solucion overallBest) {
        for (int j = 0; j < nVariables; j++) {
            /* Update velocity */
            v = SetCoveringInstanceFile.getInstance().rnd.nextDouble() * (overallBest.x[j] - pBest[j]);
                    

            /* Update position */
            x[j] = toBinary(v);
        }
    }
    
    public void decrementarTaboo() {
    	
    	for(int i=0;i<nVariables;i++) {
    		if(taboo[i]>0) {
    			taboo[i]--;
    		}
    	}
    }
    
    public void setTaboo(int tabooRate, int pos) {
    	
    	taboo[pos]=tabooRate;
    }
    
    public int tabooCheck(int pos) {
    	
    	return taboo[pos];
    }

 
    
    public Solucion(int[]x) {
    	
    	this.x=x;
    }
    

    public Solucion() {
        for (int j = 0; j < nVariables; j++) {
            x[j] = SetCoveringInstanceFile.getInstance().rnd.nextInt(2);
          
        }
        
        v = SetCoveringInstanceFile.getInstance().rnd.nextDouble(); 
        fitness= fitness();
    }


    public double fitness() {
        double suma = 0;
        for (int j = 0; j < nVariables; j++) {
            suma += x[j] * SetCoveringInstanceFile.getInstance().getCosts()[j];
        }
        return suma;
    }


 public double fitnesspBest() {
     double suma = 0;
     for (int j = 0; j < nVariables; j++) {
         suma += pBest[j] * SetCoveringInstanceFile.getInstance().getCosts()[j];
     }
     return suma;
 }
	 

 
    public void mutate(double rate) {
    	for(int i=0;i<nVariables;i++) {
        	double random = ThreadLocalRandom.current().nextDouble(0,1);
    		if(random <= rate) {
    			if(x[i]==0) {
    				x[i]=1;
    			}else if(x[i]==1) {
    				x[i]=0;
       			}
    		}
    	} 	
    }

    public boolean isFeasible() {
        int contRestCubiertas = 0;
        for (int[] constraint : SetCoveringInstanceFile.getInstance().getConstraints()) {
            for (int j = 0; j < nVariables; j++) {
                if (x[j] == 1 && x[j] == constraint[j]) {
                    contRestCubiertas++;
                    break;
                }
            }
        }
        return contRestCubiertas == SetCoveringInstanceFile.getInstance().getConstraints().length;
    }

    
    

    private int toBinary(double x) {
        return SetCoveringInstanceFile.getInstance().rnd.nextDouble() < (1 / (1 + Math.pow(Math.E, -1 * x))) ? 1 : 0;
    }
    
    public Double distanciaEuclideana(Solucion destino) {
	int d = 0;
	for (int i = 0; i < nVariables; i++) {
	    d += Math.pow(x[i] - destino.x[i], 2);
	}
	return Math.sqrt(d);
    }
   
    @Override
    public boolean equals(Object obj) {
	Solucion other = (Solucion) obj;
	for (int i = 0; i < x.length; i++) {
	    if (x[i] != other.x[i]) {
		return false;
	    }
	}
	return true;
    }

	public double getFitness() {
		
		return fitness;
	}
   

    public void initTaboo() {
    	
    	for(int i=0;i<nVariables;i++) {
    		
    		taboo[i]=0;
    	}
    	
    }


   
}