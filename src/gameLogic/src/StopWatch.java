package gameLogic.src;

public class StopWatch {
    private long beginTime;
    private final long duration;

    public StopWatch(int duration) {
        this.beginTime = System.currentTimeMillis();
        this.duration = duration;
    }

    public long getTime(){
        return System.currentTimeMillis() - beginTime;
    }

    boolean isFinished(){
        return System.currentTimeMillis() >= beginTime + duration; }

}
