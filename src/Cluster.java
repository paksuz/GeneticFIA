import java.util.ArrayList;

public class Cluster {
	
    public  ArrayList<Solucion> poblacion = new ArrayList<>();
    public Solucion centroide =null;
    
    
    public void Clusterinit(Solucion centroide) {
    	
    this.centroide = centroide;
    
    	
    	
    	
    }
    
    public void setPoblacion(Solucion sol) {
    	this.poblacion.add(sol);
    }
    
    public ArrayList<Solucion> getPoblacion() {
    	return poblacion;
    }
    
    
	

}
