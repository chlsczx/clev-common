package io.github.dribble312.common.uid;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * "time + data center id (5bit)
 * + worker id (5bit) + sequence (12 bit) 64-22 = 42
 *
 * @author czx
 */
public class AtomicSnowflakeIdGenerator {

    private final long twepoch;

    private final long workerIdBits = 5L;

    private final long datacenterIdBits = 5L;

    private final long sequenceBits = 12L;

    private final long maxWorkerId = ~(-1L << workerIdBits);

    private final long maxDatacenterId = ~(-1L << datacenterIdBits);

    private final long workerIdShift = sequenceBits;

    private final long datacenterIdShift = sequenceBits + workerIdBits;

    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final long sequenceMask = ~(-1L << sequenceBits);

    private long workerId;

    private long datacenterId;


    private AtomicReference<Pair> lastTsAndSequence = new AtomicReference<>(new Pair(-1L, 0L));

    @Getter
    static class Pair {
        private final long first;
        private final long second;

        public Pair(long first, long second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair{" + "first=" + first + ", second=" + second + '}';
        }
    }

    public AtomicSnowflakeIdGenerator(long twepoch, long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.twepoch = twepoch;
    }

    /**
     * @param workerId     worker id
     * @param datacenterId data center id
     */
    public AtomicSnowflakeIdGenerator(long workerId, long datacenterId) {
        this(1722889372094L, workerId, datacenterId);
    }


    static AtomicReference<BigDecimal> spin = new AtomicReference<>(BigDecimal.valueOf(0));

    public long nextId() {
        int factor = 0;
        while (true) {
            factor++;

            if (factor > 1) {
                Thread.onSpinWait();
            }
            try {
                if (factor > 2) {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Pair pair = lastTsAndSequence.get();
            long timestamp = timeGen();
            long lastTs = pair.getFirst();
            if (timestamp < lastTs) {
                throw new RuntimeException(
                        String.format(
                                "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                                lastTs - timestamp
                        )
                );
            }
            long seq = pair.getSecond();
            Pair nextPair;
            if (lastTs == timestamp) {
                long nextSeq = (seq + 1) & sequenceMask;

                if (nextSeq == 0L) {
                    continue;
                }

                nextPair = new Pair(timestamp, nextSeq);

                if (!lastTsAndSequence.compareAndSet(pair, nextPair)) {
                    continue;
                }
            } else {
                nextPair = new Pair(timestamp, 0);

                if (!lastTsAndSequence.compareAndSet(pair, nextPair)) {
                    continue;
                }
            }


            long l = ((timestamp - twepoch) << timestampLeftShift) |
                    (datacenterId << datacenterIdShift) |
                    (workerId << workerIdShift) |
                    nextPair.getSecond();

            // while (true) {
            //     BigDecimal times = spin.get();
            //     if (spin.compareAndSet(times, times.add(BigDecimal.valueOf(factor)))) {
            //         // System.out.println(factor);
            //         break;
            //     }
            // }

            return l;

        }
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }


    // public static void main(String[] args) {
    //     AtomicSnowflakeIdGenerator snowflakeIdGenerator = new AtomicSnowflakeIdGenerator(0, 0);
    //     Map<Long, Integer> map = new ConcurrentHashMap<>();
    //
    //     ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
    //
    //     // 使用 ScheduledExecutorService 每隔一段时间打印线程池状态
    //     ScheduledExecutorService monitorService = Executors.newScheduledThreadPool(1);
    //
    //
    //     ArrayList<Callable<String>> tasks = new ArrayList<>();
    //     for (int j = 0; j < 10; j++) {
    //         int finalJ = j;
    //         tasks.add(() -> {
    //             final int taskId = finalJ;
    //             long st = System.currentTimeMillis();
    //             for (int i = 0; i < 7_000000; i++) {
    //                 long id = snowflakeIdGenerator.nextId();
    //                 if (map.containsKey(id)) {
    //                     throw new RuntimeException("Duplicate id: " + Long.toBinaryString(id));
    //                 }
    //                 map.put(id, 0);
    //             }
    //             long ed = System.currentTimeMillis();
    //             System.out.println("Cost time: " + (ed - st) + " map size " + map.size());
    //             return "Task " + taskId + " completed.";
    //         });
    //     }
    //
    //     long start = System.currentTimeMillis();
    //
    //     // 定时任务：每隔 2 秒打印一次线程池状态
    //     monitorService.scheduleAtFixedRate(() -> {
    //         System.out.println("==== ThreadPool Status ====");
    //         System.out.println("total: " + map.size());
    //         System.out.println("Average generate " + map.size() / (System.currentTimeMillis() - start) + " id per ms");
    //         System.out.println(
    //                 "Average Spin time " + spin.get().divide(BigDecimal.valueOf(map.size()), 2, RoundingMode.HALF_UP));
    //         System.out.println("Active Threads: " + executorService.getActiveCount());
    //         System.out.println("Total Tasks: " + executorService.getTaskCount());
    //         System.out.println("Completed Tasks: " + executorService.getCompletedTaskCount());
    //         System.out.println("Pool Size: " + executorService.getPoolSize());
    //         System.out.println("Queue Size: " + executorService.getQueue().size());
    //         System.out.println("===========================");
    //     }, 0, 2, TimeUnit.SECONDS);
    //
    //     try {
    //         List<Future<String>> futures = executorService.invokeAll(tasks);
    //
    //         for (Future<String> future : futures) {
    //             System.out.println(future.get());
    //         }
    //
    //         long end = System.currentTimeMillis();
    //
    //         System.out.println("Out Cost time: " + (end - start) + "ms");
    //         System.out.println("Out Cost size: " + map.size());
    //         System.out.println("Average generate " + map.size() / (end - start) + " id per ms");
    //         System.out.println("Average Spin time for each id: " + spin.get()
    //                 .divide(BigDecimal.valueOf(map.size()), 2, RoundingMode.HALF_UP));
    //     } catch (InterruptedException | ExecutionException e) {
    //         throw new RuntimeException(e);
    //     } finally {
    //         executorService.close();
    //     }
    // }

}
