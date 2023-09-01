package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

import main.java.edu.eci.arsw.primefinder.Semaforo;

public class PrimeFinderThread extends Thread{

	
	int a,b;
	
	private List<Integer> primes;

    private Semaforo candado;
	
	public PrimeFinderThread(int a, int b, Semaforo candado) {
		super();
        this.primes = new LinkedList<>();
		this.a = a;
		this.b = b;
        this.candado = candado;
	}

    @Override
	public void run(){
        for (int i= a;i < b;i++){

            // Si la bandera del candado es falsa el hilo debera esperar
            if(!candado.getBandera()){
                try{
                    synchronized(candado){
                        candado.wait();
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }						
            if (isPrime(i)){
                primes.add(i);
                System.out.println(i);
            }
        }
	}
	
	boolean isPrime(int n) {
	    boolean ans;
            if (n > 2) { 
                ans = n%2 != 0;
                for(int i = 3;ans && i*i <= n; i+=2 ) {
                    ans = n % i != 0;
                }
            } else {
                ans = n == 2;
            }
	    return ans;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
}
