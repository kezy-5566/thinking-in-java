package xyz.vimtool.chapter21.section7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 无界区块队列
 *
 * @author  zhangzheng
 * @version 1.0.0
 * @since   jdk1.8
 * @date    2018/7/18
 */
public class DelayQueueDemo {

    public static void main(String[] args) {
        Random random = new Random(47);

        ExecutorService executorService = Executors.newCachedThreadPool();

        DelayQueue<DelayedTask> queue = new DelayQueue<>();

        for (int i = 0; i < 20; i++) {
            queue.put(new DelayedTask(random.nextInt(5000)));
        }

        queue.add(new DelayedTask.EndSentinel(5000, executorService));
        executorService.execute(new DelayedTaskConsumer(queue));
    }
}

class DelayedTask implements Runnable, Delayed {

    private static int counter = 0;

    private final int id = counter++;

    private final int delta;

    private final long trigger;

    protected static List<DelayedTask> sequence = new ArrayList<>();

    public DelayedTask(int delayInMilliseconds) {
        delta = delayInMilliseconds;
        trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);
        sequence.add(this);
    }

    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed arg) {
        DelayedTask that = (DelayedTask) arg;

        if (trigger < that.trigger) {
            return -1;
        }

        if (trigger > that.trigger) {
            return 1;
        }

        return 0;
    }

    @Override
    public void run() {
        System.out.println(this + " ");
    }

    @Override
    public String toString() {
        return String.format("[%1$-34d]", delta) + " Task " + id;
    }

    public String summary() {
        return "(" + id + ":" + delta + ")";
    }

    public static class EndSentinel extends DelayedTask {

        private ExecutorService executorService;

        public EndSentinel(int delay, ExecutorService executorService) {
            super(delay);
            this.executorService = executorService;
        }

        @Override
        public void run() {
            for (DelayedTask delayedTask : sequence) {
                System.out.println(delayedTask.summary() + " ");
            }
            System.out.println(this + " Calling shutdownNow()");
            executorService.shutdownNow();
        }
    }
}

class DelayedTaskConsumer implements Runnable {

    private DelayQueue<DelayedTask> queue;

    public DelayedTaskConsumer(DelayQueue<DelayedTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                queue.take().run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished DelayedTaskConsumer");
    }
}
