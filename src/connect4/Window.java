/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Andrea
 */
public class Window extends JFrame{
    
    private class MyJPanel extends JPanel{
        private Color color = Color.white;
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.black);
            g.fillOval(5, 5, 61, 61);
            g.setColor(color);
            g.fillOval(9, 9, 53, 53);
        }
        
        public void setColor(Color c){
            color = c;
            repaint();
        }
    }
    
    private MyJPanel [][] cells;
    private JButton easy, medium, hard, pvp, reset;
    private JPanel primaryPanel, tablePanel, buttonsPanel;
    private JButton [] moveButtons;
    
    private Grid grid;
    private boolean hasWon, againstAI;
    private int player, AISearchDepth;
    private Minimax minimax;
    
    public Window(){
        super();
        
        grid = new Grid();
        player = 1;
        hasWon = false;
        againstAI = false;
        AISearchDepth = 1;
        minimax = new Minimax();
        
        cells = new MyJPanel[6][7];
        moveButtons = new JButton[7];
        primaryPanel = new JPanel(new BorderLayout());
        tablePanel = new JPanel(new GridLayout(7,7));
        buttonsPanel = new JPanel(new GridLayout(1,5));
        easy = new JButton("Easy CPU");
        medium = new JButton("Medium CPU");
        hard = new JButton("Hard CPU");
        pvp = new JButton("PVP");
        reset = new JButton("Reset");
        
        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){
                cells[i][j] = new MyJPanel();
                tablePanel.add(cells[i][j]);
            }
        }
        
        for(int i=0; i<7; i++){
            moveButtons[i] = new JButton("^");
            moveButtons[i].setName(""+i);
            moveButtons[i].setEnabled(false);
            moveButtons[i].addActionListener((ActionEvent ae) -> {
                try{
                    int col = Integer.valueOf(((JButton)ae.getSource()).getName());
                    if(player==1){
                        hasWon = grid.insertAndCheck(Coin.RED, col);
                        displayGrid();
                        if(againstAI){
                            if(hasWon){
                                setMoveButtons(false);
                                JOptionPane.showMessageDialog(null, "Player Won");
                            }else{
                                hasWon = grid.insertAndCheck(Coin.YELLOW, minimax.executeAlgorithm(grid, AISearchDepth));
                                displayGrid();
                                if(hasWon){
                                    setMoveButtons(false);
                                    JOptionPane.showMessageDialog(null, "CPU Won");
                                }
                            }
                        }else{
                            if(hasWon){
                                setMoveButtons(false);
                                JOptionPane.showMessageDialog(null, "Player 1 Won");
                            }else{
                                player = 2;
                            }
                        }
                    }else{
                        hasWon = grid.insertAndCheck(Coin.YELLOW, col);
                        displayGrid();
                        if(hasWon){
                            setMoveButtons(false);
                            JOptionPane.showMessageDialog(null, "Player 2 Won");
                        }else{
                            player = 1;
                        }
                    }
                }catch(ColumnFullException | NumberFormatException e){}
            });
            tablePanel.add(moveButtons[i]);
        }
        
        reset.setEnabled(false);
        
        buttonsPanel.add(easy);
        buttonsPanel.add(medium);
        buttonsPanel.add(hard);
        buttonsPanel.add(pvp);
        buttonsPanel.add(reset);
        
        primaryPanel.add(buttonsPanel, BorderLayout.NORTH);
        primaryPanel.add(tablePanel, BorderLayout.CENTER);
        
        easy.addActionListener((ActionEvent ae) -> {
            AISearchDepth = 3;
            againstAI = true;
            switchButtons();
        });
        
        medium.addActionListener((ActionEvent ae) -> {
            AISearchDepth = 5;
            againstAI = true;
            switchButtons();
        });
        
        hard.addActionListener((ActionEvent ae) -> {
            AISearchDepth = 7;
            againstAI = true;
            switchButtons();
        });
        
        pvp.addActionListener((ActionEvent ae) -> {
            againstAI = false;
            switchButtons();
        });
        
        reset.addActionListener((ActionEvent ae) -> {
            grid = new Grid();
            player = 1;
            hasWon = false;
            againstAI = false;
            AISearchDepth = 1;
            for(int i=0; i<6; i++){
                for(int j=0; j<7; j++){
                    cells[i][j].setColor(Color.white);
                }
            }
            switchButtons();
            setMoveButtons(false);
        });
        
        add(primaryPanel);
        setSize(550, 600);
        setTitle("Connect4");
//        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void switchButtons(){
        setMoveButtons(!moveButtons[0].isEnabled());
        easy.setEnabled(!easy.isEnabled());
        medium.setEnabled(!medium.isEnabled());
        hard.setEnabled(!hard.isEnabled());
        pvp.setEnabled(!pvp.isEnabled());
        reset.setEnabled(!reset.isEnabled());
    }
    
    private void setMoveButtons(boolean b){
        for(int i=0; i<7; i++){
            moveButtons[i].setEnabled(b);
        }
    }
    
    private void displayGrid(){
        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){
                switch (grid.getCoinAt(i, j)) {
                    case RED:
                        cells[5-i][j].setColor(Color.red);
                        break;
                    case YELLOW:
                        cells[5-i][j].setColor(Color.yellow);
                        break;
                    default:
                        cells[5-i][j].setColor(Color.white);
                        break;
                }
            }
        }
    }
    
}
