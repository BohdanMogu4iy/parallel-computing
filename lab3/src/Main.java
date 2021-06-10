import java.util.*;
import functions.functions;


public class Main {
    static int N = 21;
    static int arraySize = N * 10000;

    public static void main(String[] args) {
//        int[] intArray = {1, 3, 3, 14, 21};
//        long[] longArray = {1, 2, 30, 2, 10, 16};
//        int[] vectA = {1,2,3};
//        int[] vectB = {2,2,2};

        long[] longArray = new Random().longs(arraySize).toArray();
        int[] intArray = new Random().ints(arraySize).toArray();

        int[] vectA = {1, 2, 3};
        int[] vectB = {2, 2, 2};

        int maxIndex = functions.maximum(longArray);
        System.out.println("Atomic max: array[" + maxIndex + "] = " + longArray[maxIndex]);

        long max = Arrays.stream(longArray).max().getAsLong();
        System.out.println("Serial max = " + max);

        int minIndex = functions.minimum(longArray);
        System.out.println("Atomic min: array[" + minIndex + "] = " + longArray[minIndex]);

        long min = Arrays.stream(longArray).min().getAsLong();
        System.out.println("Serial min = " + min);

        int modeIndex = functions.mode(longArray);
        System.out.println("Atomic mode: array[" + modeIndex + "] = " + longArray[modeIndex]);

        int check = functions.checkSum(intArray);
        System.out.println("Xor checkSum format: " + check);

        int vectorsScalar = functions.scalarMultiply(vectA, vectB);
        System.out.println("Scalar product of vectors A:" + Arrays.toString(vectA) + " and B:" + Arrays.toString(vectB) + " = " + vectorsScalar);

        AtomicDouble val = new AtomicDouble();
        val.set(10.5d);
        System.out.println("Initial AtomicDouble value : " + val.get());
        for (int i = 0; i < 10; i++) {
            val.increment();
        }
        System.out.println("Increment x10 AtomicDouble value : " + val.get());
    }
}