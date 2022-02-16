/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

/**
 *
 * @author Andrea
 */
public class Grid {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    
    private final Coin grid[][];
    private boolean someoneWon = false;
    
    public Grid(){
        grid = new Coin[ROWS][COLS];
        for(int i=0; i<ROWS; i++)
            for(int j=0; j<COLS; j++)
                grid[i][j] = Coin.VOID;
    }
    
    public Grid(Grid g){
        grid = new Coin[ROWS][COLS];
        for(int i=0; i<ROWS; i++)
            for(int j=0; j<COLS; j++)
                grid[i][j] = g.grid[i][j];
    }
    
    public boolean hasSomeoneWon(){
        return someoneWon;
    }
    
    public void printGrid(){
        for(int i=ROWS-1; i>=0; i--){
            System.out.print("|");
            for(int j=0; j<COLS; j++){
                if(grid[i][j] == Coin.VOID) System.out.print(" ");
                if(grid[i][j] == Coin.RED) System.out.print("O");
                if(grid[i][j] == Coin.YELLOW) System.out.print("X");
                System.out.print("|");
            }
            //System.out.println("\n---------------");
            System.out.println("");
        }
        System.out.println(" 1 2 3 4 5 6 7 ");
    }
    
    public boolean insertAndCheck(Coin c, int col) throws ColumnFullException{
        CoinPos pos = insertCoin(c, col);
        return victoryCheck(pos, c);
    }
    
    public CoinPos insertCoin(Coin c, int col) throws ColumnFullException{
        for(int actualRow=0; actualRow<ROWS; actualRow++){
            if(grid[actualRow][col] == Coin.VOID){
                grid[actualRow][col] = c;
                return new CoinPos(actualRow, col);
            }
        }
        
        throw new ColumnFullException();
    }
    
    public Grid insertCoinAndReturnGrid(Coin c, int col) throws ColumnFullException{
        insertCoin(c, col);
        return new Grid(this);
    }
    
    public boolean victoryCheck(CoinPos pos, Coin c){
        if(victoryCheckVertical(pos, c)
                || victoryCheckHorizontal(pos, c)
                || victoryCheckDiagonal(pos, c))
        {
            someoneWon = true;
            return true;
        }
        
        return false;
    }

    private boolean victoryCheckVertical(CoinPos pos, Coin c) {
        return isInBounds(pos.getX()-1, pos.getY())
                && isInBounds(pos.getX()-2, pos.getY())
                && isInBounds(pos.getX()-3, pos.getY())
                && grid[pos.getX()-1][pos.getY()] == c
                && grid[pos.getX()-2][pos.getY()] == c
                && grid[pos.getX()-3][pos.getY()] == c;
    }

    private boolean victoryCheckHorizontal(CoinPos pos, Coin c) {
        if (isInBounds(pos.getX(), pos.getY()-1)
                && isInBounds(pos.getX(), pos.getY()-2)
                && isInBounds(pos.getX(), pos.getY()-3)
                && grid[pos.getX()][pos.getY()-1] == c
                && grid[pos.getX()][pos.getY()-2] == c
                && grid[pos.getX()][pos.getY()-3] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX(), pos.getY()+1)
                && isInBounds(pos.getX(), pos.getY()-1)
                && isInBounds(pos.getX(), pos.getY()-2)
                && grid[pos.getX()][pos.getY()+1] == c
                && grid[pos.getX()][pos.getY()-1] == c
                && grid[pos.getX()][pos.getY()-2] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX(), pos.getY()+2)
                && isInBounds(pos.getX(), pos.getY()+1)
                && isInBounds(pos.getX(), pos.getY()-1)
                && grid[pos.getX()][pos.getY()+2] == c
                && grid[pos.getX()][pos.getY()+1] == c
                && grid[pos.getX()][pos.getY()-1] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX(), pos.getY()+3)
                && isInBounds(pos.getX(), pos.getY()+2)
                && isInBounds(pos.getX(), pos.getY()+1)
                && grid[pos.getX()][pos.getY()+3] == c
                && grid[pos.getX()][pos.getY()+2] == c
                && grid[pos.getX()][pos.getY()+1] == c)
        {
            return true;
        }
        return false;
    }

    private boolean victoryCheckDiagonal(CoinPos pos, Coin c) {
        return victoryCheckDiagonalLeft(pos, c)
                || victoryCheckDiagonalRight(pos, c);
    }

    private boolean victoryCheckDiagonalRight(CoinPos pos, Coin c) {
        if (isInBounds(pos.getX()-1, pos.getY()-1)
                && isInBounds(pos.getX()-2, pos.getY()-2)
                && isInBounds(pos.getX()-3, pos.getY()-3)
                && grid[pos.getX()-1][pos.getY()-1] == c
                && grid[pos.getX()-2][pos.getY()-2] == c
                && grid[pos.getX()-3][pos.getY()-3] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX()+1, pos.getY()+1)
                && isInBounds(pos.getX()-1, pos.getY()-1)
                && isInBounds(pos.getX()-2, pos.getY()-2)
                && grid[pos.getX()+1][pos.getY()+1] == c
                && grid[pos.getX()-1][pos.getY()-1] == c
                && grid[pos.getX()-2][pos.getY()-2] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX()+2, pos.getY()+2)
                && isInBounds(pos.getX()+1, pos.getY()+1)
                && isInBounds(pos.getX()-1, pos.getY()-1)
                && grid[pos.getX()+2][pos.getY()+2] == c
                && grid[pos.getX()+1][pos.getY()+1] == c
                && grid[pos.getX()-1][pos.getY()-1] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX()+3, pos.getY()+3)
                && isInBounds(pos.getX()+2, pos.getY()+2)
                && isInBounds(pos.getX()+1, pos.getY()+1)
                && grid[pos.getX()+3][pos.getY()+3] == c
                && grid[pos.getX()+2][pos.getY()+2] == c
                && grid[pos.getX()+1][pos.getY()+1] == c)
        {
            return true;
        }
        return false;
    }

    private boolean victoryCheckDiagonalLeft(CoinPos pos, Coin c) {
        if (isInBounds(pos.getX()+1, pos.getY()-1)
                && isInBounds(pos.getX()+2, pos.getY()-2)
                && isInBounds(pos.getX()+3, pos.getY()-3)
                && grid[pos.getX()+1][pos.getY()-1] == c
                && grid[pos.getX()+2][pos.getY()-2] == c
                && grid[pos.getX()+3][pos.getY()-3] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX()-1, pos.getY()+1)
                && isInBounds(pos.getX()+1, pos.getY()-1)
                && isInBounds(pos.getX()+2, pos.getY()-2)
                && grid[pos.getX()-1][pos.getY()+1] == c
                && grid[pos.getX()+1][pos.getY()-1] == c
                && grid[pos.getX()+2][pos.getY()-2] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX()-2, pos.getY()+2)
                && isInBounds(pos.getX()-1, pos.getY()+1)
                && isInBounds(pos.getX()+1, pos.getY()-1)
                && grid[pos.getX()-2][pos.getY()+2] == c
                && grid[pos.getX()-1][pos.getY()+1] == c
                && grid[pos.getX()+1][pos.getY()-1] == c)
        {
            return true;
        }
        if (isInBounds(pos.getX()-3, pos.getY()+3)
                && isInBounds(pos.getX()-2, pos.getY()+2)
                && isInBounds(pos.getX()-1, pos.getY()+1)
                && grid[pos.getX()-3][pos.getY()+3] == c
                && grid[pos.getX()-2][pos.getY()+2] == c
                && grid[pos.getX()-1][pos.getY()+1] == c)
        {
            return true;
        }
        return false;
    }
    
    public static boolean isInBounds(int x, int y){
        return x>=0 && x<ROWS && y>=0 && y<COLS;
    }
    
    public Coin getCoinAt(int x, int y){
        return grid[x][y];
    }
}
