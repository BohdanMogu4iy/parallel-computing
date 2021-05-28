class CPUProcess extends Thread{

    int generateNumber;
    int pid;
    CPUDispatcher dispatcher;

    CPUProcess(int pid, int gN, CPUDispatcher cD){
        this.pid = pid;
        this.generateNumber = gN;
        this.dispatcher = cD;
    }
    public void run(){
        long generateDelay;
        for (int i = 0; i < generateNumber; i++) {
            int randMin=10;
            int randMax=40;
            generateDelay = randMin + (int) (Math.random() * randMax);
            try {
                Thread.sleep(generateDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Process process = new Process(i, pid);
            System.out.println("Process " + process.getId() + " generated with delay " + generateDelay + "ms");
            dispatcher.dispatchProcess(process);
        }

    }
}
