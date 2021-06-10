package functions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class functions {

    private static int getIndex(long[] arr, long elem) {
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == elem)
                    return i;
            }
        }
        return -1;
    }

    public static int maximum(long[] arr) {
        AtomicInteger maxIndex = new AtomicInteger(0); /*створення атомарної змінної (atomic variable)*/
        LongStream.of(arr).parallel().forEach(arrayElement -> {   /*вбудований метод для виконання паралельних обчислень у Java*/
            int oldValue;
            int newValue;
            do { //зміна min з використанням методу compareAndSet
                oldValue = maxIndex.get();
                if (arrayElement > arr[oldValue]) {
                    newValue = getIndex(arr, arrayElement);
                } else break;
            } while (!maxIndex.compareAndSet(oldValue, newValue));
        });
        return maxIndex.get();
    }

    public static int minimum(long[] arr) {
        AtomicInteger minIndex = new AtomicInteger(0); /*створення атомарної змінної (atomic variable)*/
        LongStream.of(arr).parallel().forEach(arrayElement -> {   /*вбудований метод для виконання паралельних обчислень у Java*/
            int oldValue;
            int newValue;
            do { //зміна min з використанням методу compareAndSet
                oldValue = minIndex.get();
                if (arrayElement < arr[oldValue]) {
                    newValue = getIndex(arr, arrayElement);
                } else break;
            } while (!minIndex.compareAndSet(oldValue, newValue));
        });

        return minIndex.get();
    }

    public static int mode(long[] arr) {
        ConcurrentMap<Long, AtomicInteger> mElementsMap = new ConcurrentHashMap<>();
        LongStream.of(arr).parallel().forEach(arrayElement -> {
            AtomicInteger counter;
            int oldValue;
            mElementsMap.putIfAbsent(arrayElement, new AtomicInteger(0));
            counter = mElementsMap.get(arrayElement);
            do {
                oldValue = counter.get();
            } while (!counter.compareAndSet(oldValue, oldValue + 1));
        });
        AtomicInteger modeKeyIndex = new AtomicInteger(0);
        mElementsMap.keySet().stream().parallel().forEach(mapKey -> {
            int oldValue;
            int newValue;
            do {
                oldValue = modeKeyIndex.get();
                if (mElementsMap.get(mapKey).get() > mElementsMap.get(arr[oldValue]).get()) {
                    newValue = getIndex(arr, mapKey);
                } else break;
            } while (!modeKeyIndex.compareAndSet(oldValue, newValue));
        });
        return modeKeyIndex.get();
    }

    public static int checkSum(int[] arr) {
        AtomicInteger atomicXorSum = new AtomicInteger(0);
        IntStream.of(arr).parallel().forEachOrdered(x -> {
            int oldXorSum;
            int newXorSum;
            do {
                oldXorSum = atomicXorSum.get();
                newXorSum = x ^ oldXorSum;
            } while (!atomicXorSum.compareAndSet(oldXorSum, newXorSum));
        });
        return atomicXorSum.get();
    }

    public static int scalarMultiply(int[] vectA, int[] vectB) {
        AtomicInteger result = new AtomicInteger(0);
        int[][] vectors = new int[vectA.length][2];
        for (int i = 0; i < vectors.length; i++) {
            vectors[i][0] = vectA[i];
            vectors[i][1] = vectB[i];
        }
        Stream.of(vectors).parallel().forEach(vectorsEl -> {
            int oldValue;
            int newValue;
            do {
                oldValue = result.get();
                newValue = oldValue + vectorsEl[0] * vectorsEl[1];
            } while (!result.compareAndSet(oldValue, newValue));
        });
        return result.get();
    }

}
