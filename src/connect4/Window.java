/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

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
    private JSpinner difficultySpinner;
    private JLabel difficultyLabel;
    private JButton pve, pvp, reset;
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
        pve = new JButton("CPU");
        pvp = new JButton("PVP");
        difficultyLabel = new JLabel("Difficulty");
        reset = new JButton("Reset");
        difficultySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 8, 1));
        difficultyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ((JSpinner.DefaultEditor)difficultySpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.LEFT);
        
        for(int i=0; i<7; i++){
            moveButtons[i] = new JButton("V");
            moveButtons[i].setFont(new Font(moveButtons[i].getFont().getFamily(),Font.PLAIN,18));
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
                                setMoveButtons(false);
                                reset.setEnabled(false);
                                reset.paintImmediately(0,0,100,100);
                                hasWon = grid.insertAndCheck(Coin.YELLOW, minimax.executeAlgorithm(grid, AISearchDepth));
                                displayGrid();
                                setMoveButtons(true);
                                reset.setEnabled(true);
                                reset.paintImmediately(0,0,200,200);
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
        
        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){
                cells[i][j] = new MyJPanel();
                tablePanel.add(cells[i][j]);
            }
        }
        
        reset.setEnabled(false);
        
        buttonsPanel.add(difficultyLabel);
        buttonsPanel.add(difficultySpinner);
        buttonsPanel.add(pve);
        buttonsPanel.add(pvp);
        buttonsPanel.add(reset);
        
        primaryPanel.add(buttonsPanel, BorderLayout.NORTH);
        primaryPanel.add(tablePanel, BorderLayout.CENTER);
        
        pve.addActionListener((ActionEvent ae) -> {
            AISearchDepth = (Integer)difficultySpinner.getValue();
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
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void switchButtons(){
        setMoveButtons(!moveButtons[0].isEnabled());
        difficultySpinner.setEnabled(!difficultySpinner.isEnabled());
        pve.setEnabled(!pve.isEnabled());
        pvp.setEnabled(!pvp.isEnabled());
        reset.setEnabled(!reset.isEnabled());
    }
    
    private void setMoveButtons(boolean b){
        for(int i=0; i<7; i++){
            moveButtons[i].setEnabled(b);
            moveButtons[i].paintImmediately(0, 0, 100, 100);
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
                cells[5-i][j].paintImmediately(0, 0, 100, 100);
            }
        }
    }
    
}
