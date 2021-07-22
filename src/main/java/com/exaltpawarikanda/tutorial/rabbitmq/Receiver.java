package com.exaltpawarikanda.tutorial.rabbitmq;
import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

/*	For convenience, this POJO also has a CountDownLatch. This lets it signal that the message has been received.
 This is something you are not likely to implement in a production application.*/
    public CountDownLatch getLatch() {
        return latch;
    }

}
