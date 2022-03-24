package us.jcedeno.game.global.utils.extras;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import lombok.Getter;

public class JTimer implements Runnable {
    private @Getter int time;
    private @Getter final int initialTime;
    private final CompletableFuture<JTimer> future;
    protected @Getter Thread thread;

    public JTimer(int time) {
        this.time = time;
        this.initialTime = time;
        this.future = new CompletableFuture<>();
    }

    protected int tick() {
        if (time > 0) {
            return time--;
        }
        // Complete the future
        future.complete(this);
        return 0;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (tick() > 0);

    }

    public float progress() {
        return (float) time / initialTime;
    }

    public float completion() {
        return 1 - progress();
    }

    public CompletableFuture<JTimer> start() {
        this.thread = new Thread(this, "Timer@" + UUID.randomUUID());
        this.thread.start();
        return future;
    }

    public static BukkitTimer bTimer(int seconds) {
        return new BukkitTimer(seconds);
    }

}