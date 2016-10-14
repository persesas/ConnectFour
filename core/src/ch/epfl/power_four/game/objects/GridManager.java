package ch.epfl.power_four.game.objects;

public class GridManager {
    private int l, col;
    private int ty;
    public boolean playing = true;
    public int player = 1;
    private int[][] grid;
    public int [] winPos = {-1, -1, -1, -1}; // {(3,4), (5,1),...} => {34, 51, ...}
    private int[] nbElems;

    static final int DIM = 70;

    public GridManager(int l, int c) {
        grid = new int[l][c];
        nbElems = new int[c];
    }

    public boolean hasWon() {
        // System.out.println("("+l+","+col+")");

        // check horizontal
        int counter = 0;
        for (int i = -3; i < 4; i++) {
            if (i + col >= 0 && i + col < grid[0].length) {
                // System.out.println("is cheking horizontal: (" + l + "," +
                // (col+i) + ")");
                if (grid[l][col + i] == player) {
                    winPos[counter] = l*10 + col+i;
                    counter++;
                    if (counter == 4) {
                        System.out.println("horizontal win " + player);
                        return true;
                    }
                } else
                    counter = 0;
            }
        }

        // check vertical
        counter = 0;
        for (int i = -3; i < 4; i++) {
            if (i + l >= 0 && i + l < grid.length) {
                // System.out.println("is cheking vertical: (" + (l+i) + "," +
                // col + ")");
                if (grid[l + i][col] == player) {
                    winPos[counter] = (l+i)*10 + col;
                    counter++;
                    if (counter == 4) {
                        System.out.println("vertical win " + player);
                        return true;
                    }
                } else
                    counter = 0;
            }
        }

        // check diagonal right
        counter = 0;
        for (int i = -3; i < 4; i++) {
            if (l + i >= 0 && l + i < grid.length && col + i >= 0
                    && col + i < grid[0].length) {
                // System.out.println("is cheking diagonal right: DR(" + (l+i) +
                // "," + (col+i) + ")");
                if (grid[l + i][col + i] == player) {
                    winPos[counter] = (l+i)*10 + (col+i);
                    counter++;
                    if (counter == 4) {
                        System.out.println("diagonal right win " + player);
                        return true;
                    }
                } else
                    counter = 0;
            } else
                counter = 0;
        }

        // check diagonal left

        counter = 0;
        for (int i = -3; i < 4; i++) {
            if (l + i >= 0 && l + i < grid.length && col - i >= 0
                    && col - i < grid[0].length) {

                // System.out.println("is cheking diagonal right: DR(" + (l+i) +
                // "," + (col-i) + ")");
                if (grid[l + i][col - i] == player) {
                    winPos[counter] = (l+i)*10 + (col-i);
                    counter++;
                    if (counter == 4) {
                        System.out.println("diagonal left win " + player);
                        return true;
                    }
                } else
                    counter = 0;
            } else
                counter = 0;
        }
        return false;
    }

    public int addElem(int c) {
        int ret = -1;
        if (playing && c < grid[0].length && c >= 0 && nbElems[c] < grid.length) {
            grid[nbElems[c]][c] = player;
            l = nbElems[c];
            col = c;
            nbElems[c]++;
            ret = 0;
        } else
            System.out.println("error input");
        return ret;
    }

    public int addInCol(int y) {
        int ret = (y - DIM) / DIM;
        return (!((ret < 0) || (ret >= grid[0].length))) ? ret : (grid[0].length - 1);
    }

    public void nextPlayer() {
        player = (player == 1) ? 2 : 1;
    }

    public void clearGame() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = 0;
            }
        }

        for (int i = 0; i < grid[0].length; i++) {
            nbElems[i] = 0;
        }
        playing = true;
        player = 1;
    }

    public boolean isTie(){
        boolean tie = true;
        for (int i=0; i<grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                if (grid[i][j] == 0) tie = false;
        return tie;
    }

    public void setTy(int y) {
        ty = y;
    }

    public void displayConsoleGrid() {
        System.out.println("----------------------");
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1)
                    System.out.print("| x ");
                else if (grid[i][j] == 2)
                    System.out.print("| o ");
                else
                    System.out.print("|   ");
            }
            System.out.println("|");
        }
        System.out.println("----------------------");
    }
}
