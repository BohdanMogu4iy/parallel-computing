import java.util.Arrays;

public class ThreadTests {
    public static int[] SIZE = {100, 200, 500, 1000, 2000};
    public static int[] NUNMBER_THREADS = {3, 8};
    public static int NUMBER_TESTS = 100;

    public static void main(String [] args ) throws InterruptedException {
        long[] workTime = new long[NUMBER_TESTS];
        for (int size: SIZE){
            double[][] matrixSerial = new double[size][size];
            double[][] matrixParallel = new double[size][size];
            double[][] resultMatrixSerial = new double[size][size];
            double[][] resultMatrixParallel = new double[size][size];
            generateMatrix(matrixSerial, matrixParallel, size);
            System.out.println("Matrix size : " + size + "x" + size);
            for (int i=0; i< NUMBER_TESTS; i++){
                ThreadSingle single = new ThreadSingle(matrixSerial, size);
                single.run();
                workTime[i] = single.getTime();
                if (!Arrays.deepEquals(resultMatrixSerial, single.getMatrix())){
                    if (i != 0) System.out.println(i + " test of single thread program made wrong matrix");
                    resultMatrixSerial = single.getMatrix();
                }
            }
            System.out.println("1 thread work time : " + Arrays.stream(workTime).sum() / NUMBER_TESTS);
            for (int num: NUNMBER_THREADS){
                for (int i=0; i< NUMBER_TESTS; i++) {
                    ThreadMultiply multiply = new ThreadMultiply(matrixParallel, size, num);
                    multiply.run();
                    workTime[i] = multiply.getTime();
                    if (!Arrays.deepEquals(resultMatrixParallel, multiply.getMatrix())){
                        if (i != 0) System.out.println(i + " test of " + num + " threads program made wrong matrix");
                        resultMatrixParallel = multiply.getMatrix();
                    }
                }
                if (!Arrays.deepEquals(resultMatrixParallel, resultMatrixSerial)){
                    System.out.println(num + " threads test program made wrong matrix");
                }
                System.out.println(num + " threads work time : " + Arrays.stream(workTime).sum() / NUMBER_TESTS);
            }
        }
    }

    static void generateMatrix(double[][] matrixA, double[][] matrixB, int size){
        int rand_min=1;
        int rand_max=42;
        for(int i =0; i<size; i++){//початкове заповнення матриць випадковими величинами з зазначеного проміжку
            for(int j =0; j<size; j++){
                matrixA[i][j] = rand_min + (int) (Math.random() * rand_max);
                matrixB[i][j] = matrixA[i][j];
            }
        }
    }
}
