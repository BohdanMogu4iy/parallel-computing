import java.util.concurrent.atomic.AtomicReference;

class AtomicDouble{
    private final AtomicReference<Double> value = new AtomicReference<Double>();

    public AtomicDouble(){
        value.set(0d);
    }

    public AtomicDouble(double val){
        value.set(val);
    }

    public double get(){
        return value.get();
    }

    public boolean set(double val){
        return value.compareAndSet(value.get(), val);
    }

    public void increment(){
        Double oldVal, newVal;
        do{
            oldVal = value.get();
            newVal = oldVal + 1;
        }while (!value.compareAndSet(oldVal, newVal));
    }
}

