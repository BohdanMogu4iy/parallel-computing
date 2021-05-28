class CPU extends Thread{
    Process workProcess;
    CPUQueuesManager queues;

    CPU(CPUQueuesManager queues){
        this.queues = queues;
    }

    public void run(){
        while(true) {
            long workTime = 0;
            if (workProcess == null){
                try{
                    workProcess = queues.getProcess();
                    System.out.println("CPU got a process " + workProcess.getId() + " and i will work " + workProcess.getRunTime() + "\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long startTime = System.currentTimeMillis();
            try {
                Thread.sleep(workProcess.getRunTime());
            } catch (InterruptedException e) {
                workTime = System.currentTimeMillis() - startTime;
                System.out.println("CPU was interrupted while serving process" + workProcess.getId());
                if (workTime < workProcess.getRunTime()){
                    workProcess.setRunTime(workProcess.getRunTime() - workTime);
                    try {
                        queues.setProcess(workProcess, 2);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            } finally {
                System.out.println("I finished a process" + workProcess.getId());
                workProcess = null;
                try {
                    queues.throwWorkProcess();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
