public class CPUQueuesManager {
    private final CPUQueue[] queue;
    private final int[] order;
    Process workProcess;
    Boolean isCpuFree = true;

    CPUQueuesManager(CPUQueue[] queue, int[] order){
        this.queue = queue;
        this.order = order;
    }
    public synchronized void setProcess(Process process, int queueID) throws InterruptedException{
        queue[queueID].put(process);
    }
    public Process getProcess() throws InterruptedException{
        Process p = null;
        while (p == null){
            for(int i: order){
                p = queue[i].get();
                if (p != null){
                    break;
                }
            }
        }
        synchronized (isCpuFree){
            workProcess = p;
            isCpuFree = false;
        }
        return p;
    }
    public Process getWorkProcess() throws InterruptedException {
        synchronized (isCpuFree){
            return workProcess;
        }
    }
    public void throwWorkProcess() throws InterruptedException {
        synchronized (isCpuFree){
            workProcess = null;
            isCpuFree = true;
        }
    }
    public int[] getMaxSizes(){
        int[] size = new int[queue.length];
        for (int i = 0; i < queue.length; i++) {
            size[i] = queue[i].getMaxSize();
        }
        return size;
    }
}