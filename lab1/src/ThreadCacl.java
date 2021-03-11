class ThreadCacl extends Thread{

    double[][] matrix;
    int startIndex;
    int numOfRows;

    public ThreadCacl(double[][] matrix, int startIndex, int numOfRows) {//конструктор класу, приймає дані для обчислень
        this.matrix = matrix;
        this.startIndex = startIndex;
        this.numOfRows = numOfRows;
    }

    @Override
    public void run(){ //обрахунки, що здійснюватимуться в зазначеному потоці
        int maxIndex;
        for( int i=startIndex; i< (numOfRows + startIndex); i++){
            maxIndex = i;
            for( int j=0; j< matrix[i].length; j++){
                maxIndex = matrix[i][j] > matrix[i][maxIndex] ? j : maxIndex;
            }
            if (maxIndex != i){
                double swap = matrix[i][i];
                matrix[i][i] = matrix[i][maxIndex];
                matrix[i][maxIndex] = swap;
            }
        }
    }
}
