package us.jcedeno.game.global.utils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import lombok.Getter;

public class Timer implements Runnable {
    private @Getter int time;
    private @Getter final int initialTime;
    private CompletableFuture<Timer> future;
    protected @Getter Thread thread;

    public Timer(int time) {
        this.time = time;
        this.initialTime = time;
        this.future = new CompletableFuture<>();
    }

    public static Timer timer(int time) {
        return new Timer(time);
    }

    public float progress() {
        return (float) time / initialTime;
    }

    public float completion() {
        return 1 - progress();
    }

    protected int tick() {
        if (time > 0) {
            return --time;
        }
        // Complete the future
        future.complete(this);
        return -1;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (tick() >= 0);

    }

    public CompletableFuture<Timer> start() {
        this.thread = new Thread(this, "Timer@" + UUID.randomUUID());
        this.thread.start();
        return future;
    }

    public static BukkitTimer bTimer(int seconds) {
        return new BukkitTimer(seconds);
    }

    public static void main(String[] args) {
        var nTimer = Timer.timer(10).start();

        while (!nTimer.isDone()) {

        }

    }

}
