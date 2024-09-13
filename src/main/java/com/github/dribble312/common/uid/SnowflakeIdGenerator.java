package com.github.dribble312.common.uid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;

/**
 * "time + rollback tolerance sequence(3bit) + data center id (5bit)
 * + worker id (5bit) + sequence (12 bit) 64-25 = 39
 *
 * @author czx
 */
public class SnowflakeIdGenerator {

    private final long twepoch;

    private final long workerIdBits = 5L;

    private final long datacenterIdBits = 5L;

    private final long sequenceBits = 12L;

    private final long maxWorkerId = ~(-1L << workerIdBits);

    private final long maxDatacenterId = ~(-1L << datacenterIdBits);

    private final long workerIdShift = sequenceBits;

    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private final long sequenceMask = ~(-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095) 2^12
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;


    public SnowflakeIdGenerator(long twepoch, long workerId, long datacenterId) {
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
    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        this(1722889372094L, workerId, datacenterId);
    }


    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format(
                            "Clock moved backwards.  Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp
                    )
            );
        }

        long thisSequence = 0L;

        if (lastTimestamp == timestamp) {
            thisSequence = (++sequence) & sequenceMask;

            if (thisSequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                thisSequence;

    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            Thread.onSpinWait();
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        AtomicSnowflakeIdGenerator snowflakeIdGenerator = new AtomicSnowflakeIdGenerator(0, 0);
        Map<Long, Integer> map = new ConcurrentHashMap<>();

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);

        // 使用 ScheduledExecutorService 每隔一段时间打印线程池状态
        ScheduledExecutorService monitorService = Executors.newScheduledThreadPool(1);


        ArrayList<Callable<String>> tasks = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            int finalJ = j;
            tasks.add(() -> {
                final int taskId = finalJ;
                long st = System.currentTimeMillis();
                for (int i = 0; i < 5_000_000; i++) {
                    long id = snowflakeIdGenerator.nextId();
                    if (map.containsKey(id)) {
                        throw new RuntimeException("Duplicate id: " + Long.toBinaryString(id));
                    }
                    map.put(id, 0);
                }
                long ed = System.currentTimeMillis();
                System.out.println("Cost time: " + (ed - st) + " map size " + map.size());
                return "Task " + taskId + " completed.";
            });
        }

        long start = System.currentTimeMillis();

        // 定时任务：每隔 2 秒打印一次线程池状态
        monitorService.scheduleAtFixedRate(() -> {
            System.out.println("==== ThreadPool Status ====");
            System.out.println("total: " + map.size());
            System.out.println("Average generate " + map.size() / (System.currentTimeMillis() - start) + " id per ms");
            // System.out.println(
            //         "Average Spin time " + spin.get().divide(BigDecimal.valueOf(map.size()), 2, RoundingMode.HALF_UP));
            System.out.println("Active Threads: " + executorService.getActiveCount());
            System.out.println("Total Tasks: " + executorService.getTaskCount());
            System.out.println("Completed Tasks: " + executorService.getCompletedTaskCount());
            System.out.println("Pool Size: " + executorService.getPoolSize());
            System.out.println("Queue Size: " + executorService.getQueue().size());
            System.out.println("===========================");
        }, 0, 2, TimeUnit.SECONDS);

        try {
            List<Future<String>> futures = executorService.invokeAll(tasks);

            for (Future<String> future : futures) {
                System.out.println(future.get());
            }

            long end = System.currentTimeMillis();

            System.out.println("Out Cost time: " + (end - start) + "ms");
            System.out.println("Out Cost size: " + map.size());
            System.out.println("Average generate " + map.size() / (end - start) + " id per ms");
            // System.out.println("Average Spin time for each id: " + spin.get()
            //         .divide(BigDecimal.valueOf(map.size()), 2, RoundingMode.HALF_UP));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.close();
        }
    }

}
