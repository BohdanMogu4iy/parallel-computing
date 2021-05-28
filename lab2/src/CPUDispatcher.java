public class CPUDispatcher {

    CPU cpu;
    CPUQueuesManager queues;
    int interruptsCount = 0;

    CPUDispatcher(CPU cpu, CPUQueuesManager CPUQueues){
        this.cpu = cpu;
        this.queues = CPUQueues;
    }

    public void dispatchProcess(Process process){
        Process wP = null;
        try {
            wP = queues.getWorkProcess();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        switch (process.getPpid()){
            case 0:
                if (wP == null){
                    System.out.println("CPU is free");

                    try {
                        queues.setProcess(process, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }else {
                    System.out.println("CPU is serving process " + wP.getPpid());
                    if (wP.getPpid() == 1){
                        System.out.println("--------------INTERRUPT------------------");
                        interruptsCount++;
                        cpu.interrupt();
                    }
                    try {
                        queues.setProcess(process, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 1:
                if (wP == null){
                    System.out.println("CPU is free");

                    try {
                        queues.setProcess(process, 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }else {
                    System.out.println("CPU is serving process " + wP.getPpid());
                    try {
                        queues.setProcess(process, 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public int getInterruptsCount() {
        return interruptsCount;
    }
}
