package main.java.edu.eci.arsw.primefinder;

/*
 * Esta clase representa un semaforo para saber en que estado se encuentra el hilo 
*/
public class Semaforo {

    private boolean bandera;

    public synchronized boolean getBandera(){
        return bandera;
    }

    public synchronized void setBandera(boolean bandera){
        this.bandera = bandera;
    }
    
    
}
