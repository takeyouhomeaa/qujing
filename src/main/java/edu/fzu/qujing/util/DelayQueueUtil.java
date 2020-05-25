package edu.fzu.qujing.util;

import edu.fzu.qujing.bean.DelayTask;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;

public class DelayQueueUtil {
    private static DelayQueue<DelayTask> delayQueueToCancel = new DelayQueue<>();

    private static DelayQueue<DelayTask> delayQueueToConfirm = new DelayQueue<>();

    public static DelayQueue<DelayTask> getDelayQueueToCancel() {
        return delayQueueToCancel;
    }

    public static DelayQueue<DelayTask> getDelayQueueToConfirm() {
        return delayQueueToConfirm;
    }

    private static void remove(DelayTask delayTask, DelayQueue<DelayTask> delayQueueToCancel) {
        if (delayTask != null && delayTask.getId() != null) {
            for (Iterator<DelayTask> iterator = delayQueueToCancel.iterator(); iterator.hasNext(); ) {
                DelayTask temp = (DelayTask) iterator.next();
                if (temp.getId().equals(delayTask.getId())) {
                    delayQueueToCancel.remove(temp);
                }
            }
        }
    }


    public static void removeDelayTaskToCancel(DelayTask delayTask) {
        remove(delayTask, delayQueueToCancel);
    }

    public static void removeDelayTaskToConfirm(DelayTask delayTask) {
        remove(delayTask, delayQueueToConfirm);
    }

    public static void addDelayTaskToCancel(DelayTask delayTask) {
        delayQueueToCancel.add(delayTask);
    }

    public static void addDelayTaskToConfirm(DelayTask delayTask) {
        delayQueueToConfirm.add(delayTask);
    }

    public static DelayTask getDelayTaskToCancel() {
        try {
            return delayQueueToCancel.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DelayTask getDelayTaskToConfirm() {
        try {
            return delayQueueToConfirm.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
