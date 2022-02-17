/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Andrea
 */
public class Minimax {
    
    private class Node {
        private Grid grid;
        private Node father;
        private List<Node> children;
        private Integer value;
        private int level;
        private boolean isMax;
        private Integer move;
        
        public Node(Grid grid){
            this.grid = new Grid(grid);
            father = null;
            children = new ArrayList<>();
            value = null;
            level = 0;
            isMax = true;
            move = null;
        }
        
        public Node(Grid g, Coin c, int col, Node father){
            this.father = father;
            try {
                Grid newGrid = new Grid(g);
                newGrid.insertAndCheck(c, col);
                this.grid = newGrid;
                children = new ArrayList<>();
                father.children.add(this);
                value = null;
                level = father.level+1;
                isMax = c.equals(Coin.RED);
                move = col;
            } catch(ColumnFullException e) {
                this.grid = null;
            }
        }
    }
    
    public int executeAlgorithm(Grid initial, int maxLevel){
        Node initialNode = new Node(initial);
        Deque<Node> deque = new LinkedList<>();
        deque.add(initialNode);
        
        while(!deque.isEmpty()){
            Node actual = deque.poll();
            Coin nextCoin = actual.isMax ? Coin.YELLOW : Coin.RED;
            for(int i=0; i<7; i++){
                Node temp = new Node(actual.grid, nextCoin, i, actual);
                if(temp.grid != null && temp.level < maxLevel){
                    if(temp.grid.hasSomeoneWon()){
                        calculateAndAssignNodeValue(temp);
                        if(calculateValuesForEachNodeFrom(temp)) break;
                    }else{
                        deque.add(temp);
                    }
                }else if(temp.level == maxLevel){
                    calculateAndAssignNodeValue(temp);
                    if(calculateValuesForEachNodeFrom(temp)) break;
                }else if(temp.grid == null){
                    if(temp.father.children.isEmpty()){
                        calculateAndAssignNodeValue(temp.father);
                        calculateValuesForEachNodeFrom(temp.father);
                    }
                }
            }
        }
        
        for(Node c : initialNode.children){
            if(c.value.equals(initialNode.value)){
                int result = c.move;
                cleanRecursive(initialNode);
                return result;
            }
        }
        return 0;
    }
    
    private boolean calculateValuesForEachNodeFrom(Node n){
        //Set values of nodes and fathers
        Node tempFather = n.father;
        while(tempFather != null && tempFather.value == null){
            tempFather.value = n.value;
            tempFather = tempFather.father;
        }

        //Calculate min and max values for each node
        tempFather = n.father;
        while(tempFather != null){
            if(tempFather.isMax){
                int max = -1000;
                for(Node c : tempFather.children){
                    if(c.value != null && c.value > max)
                        max = c.value;
                }
                tempFather.value = max;
            }else{
                int min = 1000;
                for(Node c : tempFather.children){
                    if(c.value != null && c.value < min)
                        min = c.value;
                }
                tempFather.value = min;
            }
            tempFather = tempFather.father;
        }
        
        //Search for pruning
        tempFather = n.father;
        if(tempFather!=null && tempFather.father!=null)
            return (!tempFather.isMax && tempFather.father.isMax && tempFather.value <= tempFather.father.value) || 
                    (tempFather.isMax && !tempFather.father.isMax && tempFather.value >= tempFather.father.value);
        return false;
    }
    
    private void calculateAndAssignNodeValue(Node n){
        if(n.grid.hasSomeoneWon()){
            n.value = utilityFunction(n.isMax);
        }else{
            n.value = evaluationFunction(n.grid);
        }
    }
    
    private int evaluationFunction(Grid g){
        int maxValue = evaluateForSingleAgent(g, Coin.YELLOW);
        int minValue = evaluateForSingleAgent(g, Coin.RED);
        return maxValue - minValue;
    }
    
    private int evaluateForSingleAgent(Grid g, Coin c){
        return evaluateForSingleAgentVertical(g, c)
                + evaluateForSingleAgentHorizontal(g, c)
                + evaluateForSingleAgentRightDiagonal(g, c)
                + evaluateForSingleAgentLeftDiagonal(g, c);
    }
    
    private int evaluateForSingleAgentVertical(Grid g, Coin c){
        int count = 0;
        for(int j=0; j<7; j++){
            for(int i=0; i<3; i++){
                if(g.getCoinAt(i, j)==c
                        && (g.getCoinAt(i+1, j)==c || g.getCoinAt(i+1, j)==Coin.VOID)
                        && (g.getCoinAt(i+2, j)==c || g.getCoinAt(i+2, j)==Coin.VOID)
                        && (g.getCoinAt(i+3, j)==c || g.getCoinAt(i+3, j)==Coin.VOID)
                        ){
                    count++;
                }
            }
        }
        return count;
    }
    
    private int evaluateForSingleAgentHorizontal(Grid g, Coin c){
        int count = 0;
        for(int i=0; i<6; i++){
            for(int j=0; j<4; j++){
                if(g.getCoinAt(i, j)==c
                        && (g.getCoinAt(i, j+1)==c || g.getCoinAt(i, j+1)==Coin.VOID)
                        && (g.getCoinAt(i, j+2)==c || g.getCoinAt(i, j+2)==Coin.VOID)
                        && (g.getCoinAt(i, j+3)==c || g.getCoinAt(i, j+3)==Coin.VOID)
                        ){
                    count++;
                }
            }
        }
        return count;
    }
    
    private int evaluateForSingleAgentRightDiagonal(Grid g, Coin c){
        int count = 0;
        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){
                if(g.getCoinAt(i, j)==c
                        && Grid.isInBounds(i+1, j+1)
                        && Grid.isInBounds(i+2, j+2)
                        && Grid.isInBounds(i+3, j+3)
                        && (g.getCoinAt(i+1, j+1)==c || g.getCoinAt(i+1, j+1)==Coin.VOID)
                        && (g.getCoinAt(i+2, j+2)==c || g.getCoinAt(i+2, j+2)==Coin.VOID)
                        && (g.getCoinAt(i+3, j+3)==c || g.getCoinAt(i+3, j+3)==Coin.VOID)
                        ){
                    count++;
                }
            }
        }
        return count;
    }
    
    private int evaluateForSingleAgentLeftDiagonal(Grid g, Coin c){
        int count = 0;
        for(int i=0; i<6; i++){
            for(int j=0; j<7; j++){
                if(g.getCoinAt(i, j)==c
                        && Grid.isInBounds(i+1, j-1)
                        && Grid.isInBounds(i+2, j-2)
                        && Grid.isInBounds(i+3, j-3)
                        && (g.getCoinAt(i+1, j-1)==c || g.getCoinAt(i+1, j-1)==Coin.VOID)
                        && (g.getCoinAt(i+2, j-2)==c || g.getCoinAt(i+2, j-2)==Coin.VOID)
                        && (g.getCoinAt(i+3, j-3)==c || g.getCoinAt(i+3, j-3)==Coin.VOID)
                        ){
                    count++;
                }
            }
        }
        return count;
    }
    
    private int utilityFunction(boolean isMax){
        return isMax ? -100 : 100;
    }
    
    private void cleanRecursive(Node n){
        if(n.children.isEmpty()){
            n.father = null;
        }else{
            for(Node n2 : n.children)
                cleanRecursive(n2);
            n.children.clear();
        }
    }
}