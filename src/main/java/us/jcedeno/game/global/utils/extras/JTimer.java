package us.jcedeno.game.global.utils.extras;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import lombok.Getter;
import lombok.Setter;

public class JTimer implements Runnable {
    private @Setter int time;
    private @Getter final int initialTime;
    private final CompletableFuture<JTimer> future;
    protected @Getter Thread thread;

    public JTimer(int time) {
        this.time = time;
        this.initialTime = time;
        this.future = new CompletableFuture<>();
    }

    protected int tick() {
        if (time >= 0) {
            return time--;
        }
        onComplete();
        return 0;
    }

    protected void onComplete() {
        // Complete the future
        future.complete(this);
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

    public float progress() {
        return (float) time / initialTime;
    }

    public float completion() {
        return 1 - progress();
    }
    protected int time(){
        return this.time;
    }

    public CompletableFuture<JTimer> start() {
        this.thread = new Thread(this, "Timer@" + UUID.randomUUID());
        this.thread.start();
        return future;
    }

    public void end() {
        time = 0;
    }
    

    public static BukkitTimer bTimer(int seconds) {
        return new BukkitTimer(seconds);
    }

}