import java.util.LinkedList;
import java.util.Queue;

class CPUQueue {

    private Queue<Process> queue = new LinkedList<>();
    private final int id;
    private int maxSize = 0;

    public CPUQueue(int id) {
        this.id = id;
    }
    public synchronized void put(Process p) throws InterruptedException {
        queue.add(p);
        if(queue.size()>maxSize){
            maxSize=queue.size();
        }
        System.out.println("Process " + p.getId() + " added to queue[" + id + "], queue size = [" + queue.size() + "]");
    }

    public synchronized Process get() throws InterruptedException {
        if (queue.isEmpty()){
            return null;
        }else {
            Process item = queue.remove();
            System.out.println("Process " + item.getId() + " removed from queue [" + id + "], queue size = [" + queue.size() + "]");
            return item;
        }
    }

    public synchronized  int getId() {
        return id;
    }

    public synchronized int getMaxSize() {
        return maxSize;
    }
}
