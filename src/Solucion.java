import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Solucion implements Cloneable {

    public final int nVariables = SetCoveringInstanceFile.getInstance().getCols();
    public int[] x = new int[nVariables];
    public int[] pBest = new int[nVariables];
    private double v;
    public double rank ;
    public	boolean haCambiado = false;
    public int	cambio =0;
    


    public Solucion() {
        for (int j = 0; j < nVariables; j++) {
            x[j] = SetCoveringInstanceFile.getInstance().rnd.nextInt(2);
          
        }
        
        v = SetCoveringInstanceFile.getInstance().rnd.nextDouble(); 
       
    }


    public double fitness() {
        double suma = 0;
        for (int j = 0; j < nVariables; j++) {
            suma += x[j] * SetCoveringInstanceFile.getInstance().getCosts()[j];
        }
        return suma;
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
    
    
    
    public Double distanciaManhattan(Solucion destino) {
	int d = 0;
	for (int i = 0; i < nVariables; i++) {
	    d += Math.abs(x[i] - destino.x[i]);
	}
	
	return Math.sqrt(d);
    }
    
    
    public int distanciaHamming(Solucion destino) {
	int d = 0;
	for (int i = 0; i < nVariables; i++) {
	    if(x[i] !=destino.x[i]) {
	    	d++;
	    }
	}
	
	return d;
    }
    
    public  double cosineSimilarity(Solucion destino) {
	    int dotProduct = 0;
	    int normA = 0;
	    int normB = 0;
	    for (int i = 0; i < nVariables ; i++) {
	        dotProduct += x[i] * destino.x[i];
	        normA += Math.pow(x[i], 2);
	        normB += Math.pow(destino.x[i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
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



    public void set(int i, int x) {
    	 this.x[i]= x;
    }
    public int get(int i) {
    	return x[i];
    }
    
    public Solucion getCopy()
    {
    	Solucion copy = null;

    try
        {
        copy = (Solucion)(this.clone());
        }
    catch (Exception e)
        {
        e.printStackTrace();
        }

    return copy;
    }
   



   
}