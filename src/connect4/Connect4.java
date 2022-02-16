/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import java.util.Scanner;

/**
 *
 * @author Andrea
 */
public class Connect4 {

    /**
     * @param args the command line arguments
     * @throws connect4.ColumnFullException
     */
    public static void main(String[] args) throws ColumnFullException {
//        playAgainstHuman();
//        playAgainstCPU();
        Window w = new Window();
    }
    
    public static void playAgainstHuman() throws ColumnFullException{
        Grid g = new Grid();
        Scanner in = new Scanner(System.in);
        int player = 1;
        boolean hasWon;
        
        g.printGrid();
        while(true){
            System.out.print("\nPlayer" + player + ", select column: ");
            int col = in.nextInt()-1;
            if(player == 1) {
                hasWon = g.insertAndCheck(Coin.RED, col);
            } else {
                hasWon = g.insertAndCheck(Coin.YELLOW, col);
            }
            System.out.println("\n\n");
            g.printGrid();
            if(hasWon){
                break;
            }
            player = player==1 ? 2 : 1;
        }
        System.out.println("\n\nPlayer" + player + " won");
    }
    
    public static void playAgainstCPU() throws ColumnFullException{
        Grid g = new Grid();
        Scanner in = new Scanner(System.in);
        int player = 1;
        boolean hasWon;
        
        Minimax minimax = new Minimax();
        
        g.printGrid();
        while(true){
            if(player == 1) {
                System.out.print("\nPlayer" + player + ", select column: ");
                int col = in.nextInt()-1;
                hasWon = g.insertAndCheck(Coin.RED, col);
            } else {
                hasWon = g.insertAndCheck(Coin.YELLOW, minimax.executeAlgorithm(g, 7));
            }
            System.out.println("\n\n");
            g.printGrid();
            if(hasWon){
                break;
            }
            player = player==1 ? 2 : 1;
        }
        System.out.println("\n\nPlayer" + player + " won");
    }
}
