import java.util.Arrays;

public class ThreadMultiply {
    int numberThreads;
    int size;
    double[][] matrix;
    long workTime;

    public ThreadMultiply(double[][] matrix, int size, int numberThreads) {
        this.matrix = Arrays.stream(matrix)
                .map((double[] row) -> row.clone())
                .toArray((length) -> new double[length][]);
        this.size = size;
        this.numberThreads = numberThreads;
    }

    public long getTime(){
        return workTime;
    }

    public double[][] getMatrix(){
        return matrix;
    }

    public void run() throws InterruptedException {


        long startTime;
        long finishTime;
        ThreadCacl[] TreadArray = new ThreadCacl[numberThreads];
        for(int i = 0; i < size % numberThreads; i++){ //розбиття на потоки
            TreadArray[i] = new ThreadCacl(matrix ,
                    (size/numberThreads + 1) * i,
                    (size/numberThreads + 1));
        }
        for(int i = size % numberThreads; i < numberThreads; i++){ //розбиття на потоки
            TreadArray[i] = new ThreadCacl(matrix ,
                    size/numberThreads * i + size % numberThreads,
                    size/numberThreads);
        }
        startTime = System.nanoTime();
        for(int i = 0; i < numberThreads; i++){ //старт потоків
            TreadArray[i].start();
        }
        for(int i = 0; i < numberThreads; i++){ //очікування завершення усіх потоків
            TreadArray[i].join();
        }
        finishTime = System.nanoTime();
        workTime = (finishTime - startTime) / 1000;
    }
}
