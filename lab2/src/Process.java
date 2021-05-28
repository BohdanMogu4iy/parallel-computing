import java.util.Arrays;

public class Process{

    int pid;
    int ppid;
    long runTime;

    Process(int pid, int ppid){
        this.pid = pid;
        this.ppid = ppid;
        int randMin=20;
        int randMax=80;
        this.runTime  = randMin + (int) (Math.random() * randMax);
    }
    public long getRunTime(){
        return runTime;
    }
    public void setRunTime(long rT){
        runTime = rT;
    }
    public String getId(){
        int[] id = {ppid, pid};
        return Arrays.toString(id);
    }
    public int getPpid(){
        return ppid;
    }
}
