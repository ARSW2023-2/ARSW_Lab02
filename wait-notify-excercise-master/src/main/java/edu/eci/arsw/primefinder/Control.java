/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

import main.java.edu.eci.arsw.primefinder.Semaforo;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;
    private Semaforo objetoAccesible;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        this.objetoAccesible = new Semaforo();

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, objetoAccesible);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1,objetoAccesible);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        objetoAccesible.setBandera(true);
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }

        while(true){
            try{
                //Esperar el tiempo indicado en la variable TMILISECONDS
                Thread.sleep(TMILISECONDS);

                // Pausar los hilos
                pausarHilos();
                System.out.println("Hilos pausados");

                //Conteo  de los numeros primos encontrados hasta el momento
                contarPrimos();

                // Despausar los hilos
                despausarHilos();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /*
     * Pausa los hilos cuando se le indica
     */
    private void pausarHilos(){
        objetoAccesible.setBandera(false);
    }

    /*
     * Cuenta los numeros primos encontrados hasta el momento
     */
    private void contarPrimos(){
        int cont = 0;
        for(PrimeFinderThread hilo: pft){
            cont += hilo.getPrimes().size();
        }
        System.out.println("Cantidad numero de primos encontrados: " + cont);
    }

    /*
     * Despausa los hilos cuando se le indica
     */
    private void despausarHilos(){
        // Lee la entrada del usuario desde la consola
        Scanner sc = new Scanner(System.in);


        while(true){
            // Lee una linea de entrada del usuario, espera a que el usuario presione enter
            String enterKey = sc.nextLine();

            // comprueba si el usuario presiono enter sin escribir nada, si es asi sale del ciclo
            if(enterKey.isEmpty()){
                break;
            }
        }

        // Se cambia el estado de la bandera para que los hilos puedan continuar
        objetoAccesible.setBandera(true);

        // Se le notifica a los hilos que pueden continuar
        synchronized(objetoAccesible){
            objetoAccesible.notifyAll();
        }
    }
}
