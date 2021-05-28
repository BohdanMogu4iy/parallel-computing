import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int[] processToGenerate = {10, 20};
        int[] CPUQueuesOrder = {0, 2, 1};

        CPUQueue[] q = new CPUQueue[CPUQueuesOrder.length];
        for (int i = 0; i < CPUQueuesOrder.length; i++) {
            q[i] = new CPUQueue(i);
        }

        CPUQueuesManager M = new CPUQueuesManager(q, CPUQueuesOrder);
        CPU C = new CPU(M);
        CPUDispatcher dispatcher = new CPUDispatcher(C, M);

        CPUProcess[] processes = new CPUProcess[processToGenerate.length];
        for (int i = 0; i < processToGenerate.length; i++) {
            processes[i] = new CPUProcess(i, processToGenerate[i], dispatcher);
        }
        C.start();
        for (CPUProcess p: processes){
            p.start();
        }
        for (CPUProcess p: processes){
            p.join();
        }
        synchronized (System.out){
            System.out.println("-------------------------------------------------");
            System.out.println("CPU was interrupted " + dispatcher.getInterruptsCount() + " times");
            System.out.println("CPUQueues MAX sizes " + Arrays.toString(M.getMaxSizes()));
            System.out.println("-------------------------------------------------");
        }
    }
}
