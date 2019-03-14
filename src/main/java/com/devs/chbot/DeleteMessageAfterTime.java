package com.devs.chbot;

import java.util.concurrent.ExecutionException;
import org.javacord.api.entity.message.Message;

public class DeleteMessageAfterTime implements Runnable {
    private long time;
    private Message msg;

    public DeleteMessageAfterTime(long time, Message msg) {
        this.time = time;
        this.msg = msg;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time);
            msg.delete("auto-deleted command").get();
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
