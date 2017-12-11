public class GameOfLife {
    private int numOfRows;

    GameOfLife(int numOfRows) {
        this.numOfRows = numOfRows;
    }


    public Cell[][] generateRandomLife(int density, Cell[][] grid) {
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfRows; j++) {
                if (Math.random() < (density*0.01)) {
                    grid[i][j].setAlive();
                } else {
                    grid[i][j].setDead();
                }
            }
        }
        return grid;
    }



    public Cell[][] getNextGeneration(Cell[][] grid) {
        Cell[][] tmpBoard = new Cell[numOfRows][numOfRows];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                tmpBoard[i][j] = new Cell();
            }
        }

        for(int i=1; i<(grid.length-1); i++ ){
            for(int j=1; j<(grid[0].length-1); j++ ){
                if(getNextGenOfCell(grid,i,j)){
                    tmpBoard[i][j].setAlive();
                }else{
                    tmpBoard[i][j].setDead();
                }
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if(tmpBoard[i][j].getStatus()){
                    grid[i][j].setAlive();
                }else
                    grid[i][j].setDead();
            }
        }
        return grid;
    }


    private boolean getNextGenOfCell(Cell[][] grid, int i, int j){
        boolean newCell;

        if(grid[i][j].getStatus()){
            if((getNeighboursSum(grid,i,j)-1)<2){
                newCell=false;
            }else if((getNeighboursSum(grid,i,j)-1)>3){
                newCell=false;
            }else{// sum = 2 or 3
                newCell=true;
            }
        }else{
            if(getNeighboursSum(grid,i,j)==3){
                newCell=true;
            }else{
                newCell=false;
            }
        }

        return newCell;
    }


    private int getNeighboursSum(Cell[][] grid,int i, int j){
        int neighboursSum=0;
        for(int x=i-1;x<i+2;x++){
            for(int y=j-1;y<j+2;y++){
                if(grid[x][y].getStatus()){neighboursSum++;}
            }
        }
        return neighboursSum;
    }

}