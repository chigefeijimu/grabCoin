package heco.com.hecoTest;

import cn.hutool.core.util.StrUtil;
import org.junit.Test;

import java.io.Serializable;

public class IDGenerate implements Serializable {
    private static final long serialVersionUID = 201610241037L;
    //单例模式
    private volatile static IDGenerate self;
    // 时间戳基数, 此值越大, 生成的ID越小
    private final static long twepoch = 0L;
    // 机器标识位数
    private final static long workerIdBits = 2L;
    // 数据中心标识位数
    private final static long datacenterIdBits = 2L;
    // 机器ID最大值
    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 数据中心ID最大值
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 毫秒内自增位
    private final static long sequenceBits = 8L;
    // 机器ID偏左移12位
    private final static long workerIdShift = sequenceBits;
    // 数据中心ID左移17位
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    // 时间毫秒左移22位
    private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);
    private static long lastTimestamp = -1L;

    //当前序列
    private long sequence = 1000000000000L;
    //当前机器ID
    private long workerId=1L;
    //当前数据中心ID
    private long datacenterId=1L;

    public IDGenerate() {
        String wId = System.getenv("workerId");
        if(!StrUtil.isEmpty(wId)){
            workerId = Long.parseLong(wId);
        }
        String dId = System.getenv("datacenterId");
        if(!StrUtil.isEmpty(dId)){
            datacenterId = Long.parseLong(dId);
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
        }
    }

    /**
     * 实例化方法
     */
    private static IDGenerate getInstance() {
        if (self == null) {
            synchronized (IDGenerate.class) {
                self = new IDGenerate();
            }
        }
        return self;
    }

    /**
     * 供其他业务静态使用
     */
//    public static String NextID() {
//        return String.valueOf(getInstance().nextid());
//    }

    /**
     * 供其他业务静态使用
     */
//    public static long NextIDLong() {
//        return getInstance().nextid();
//    }

    /**
     * 获取下一个序列值
     */
    @Test
    public synchronized void nextid() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (lastTimestamp == timestamp) {
            // 当前毫秒内，则+1
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 当前毫秒内计数满了，则等待下一秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        lastTimestamp = timestamp;
        // ID偏移组合生成最终的ID，并返回ID
        long nextId = ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
        System.out.println(nextId);
    }

    /**
     * 获取下一个毫秒值
     */
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

}
