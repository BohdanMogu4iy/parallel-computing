import java.util.Arrays;

public class ThreadSingle {
    double[][] matrix;
    int size;
    long workTime = 0;

    public ThreadSingle(double[][] matrix, int size) {
        this.matrix = Arrays.stream(matrix)
                .map((double[] row) -> row.clone())
                .toArray((length) -> new double[length][]);
        this.size = size;
    }

    public long getTime(){
        return workTime;
    }

    public double[][] getMatrix(){
        return matrix;
    }

    public void run(){
        long startTime;
        long finishTime;
        startTime = System.nanoTime();
        int maxIndex;
        for( int i=0; i< this.size; i++){
            maxIndex = i;
            for( int j=0; j< this.size; j++){ // шукаємо індекс максимального елемента
                maxIndex = this.matrix[i][j] > this.matrix[i][maxIndex] ? j : maxIndex;
            }
            if (maxIndex != i){ // якщо максимальний елемент не знаходиться на діагоналі, переміщуємо його туди
                double swap = this.matrix[i][i];
                this.matrix[i][i] = this.matrix[i][maxIndex];
                this.matrix[i][maxIndex] = swap;
            }
        }
        finishTime = System.nanoTime();
        this.workTime = (finishTime - startTime) / 1000;
    }
}
