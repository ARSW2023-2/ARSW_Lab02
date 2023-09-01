package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private Semaforo semaforo;

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];

    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        
        
        frame.add(board,BorderLayout.CENTER);
        
        JPanel actionsBPabel=new JPanel();
        actionsBPabel.setLayout(new FlowLayout());
        //actionsBPabel.add(new JButton("Action "));
        JButton bAction = new JButton("Action ");
        JButton bPausar = new JButton("Pausar ");

        bPausar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(semaforo.getBandera()){
                    semaforo.switchBandera();
                    Integer primeraMuerte = Snake.primeraMuerte;
                    JOptionPane.showMessageDialog(null, (primeraMuerte != Integer.MIN_VALUE)? "La primera serpiente que murio fue "+primeraMuerte:"No ha muerto ninguna serpiente");
                }
            }
        });

        JButton bReanudar = new JButton("Reanudar ");

        bReanudar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e ){
                if(!semaforo.getBandera()){
                    semaforo.switchBandera();
                    synchronized(semaforo){
                        semaforo.notifyAll();
                    }
                }
            }
        });        

        actionsBPabel.add(bAction);
        actionsBPabel.add(bPausar);
        actionsBPabel.add(bReanudar);

        frame.add(actionsBPabel,BorderLayout.SOUTH);

    }

    public static void main(String[] args) {
        app = new SnakeApp();
        app.init();
    }

    private void init() {     

        semaforo = new Semaforo();
        semaforo.setBandera(true);

        for (int i = 0; i != MAX_THREADS; i++) {            
            snakes[i] = new Snake(i + 1, spawn[i], i + 1,semaforo);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
            thread[i].start();
        }

        frame.setVisible(true);

            
        /*while (true) {
            int x = 0;
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd() == true) {
                    x++;
                }
            }
            if (x == MAX_THREADS) {
                break;
            }
        }*/

        //Espera a que todos los hilos finalicen su ejecucion
        for(int i=0; i!=MAX_THREADS; i++){
            try{
                //Espera a que todos los hilos terminen para consolidar los resultados
                thread[i].join();

            }catch(InterruptedException e){
                e.printStackTrace();

            }
        }


        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
        

    }

    public static SnakeApp getApp() {
        return app;
    }

}
