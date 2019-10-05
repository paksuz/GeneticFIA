import java.util.Random;

public class Solucion {

    public final int nVariables = SetCoveringInstanceFile.getInstance().getCols();
    public final int[] x = new int[nVariables];

    private double v;
    private Random rnd = new Random();

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

 
   
}