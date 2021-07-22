package com.exaltpawarikanda.tutorial.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MessagingRabbitmqApplication {

    static final String topicExchangeName = "spring-boot-exchange";

    static final String queueName = "spring-boot";

    /*Spring AMQP requires that the Queue, the TopicExchange, and the Binding be declared as top-level Spring beans in order to be set up properly.*/

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    /*JMS sends queued messages to only one consumer. While AMQP queues do the same thing,
     AMQP producers do not send messages directly to queues.
     Instead, a message is sent to an exchange, which can go to a single queue or fan out to multiple queues, emulating the concept of JMS topics.*/

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    /*In this case, we use a topic exchange, and the queue is bound with a routing key of foo.bar.#,
    which means that any messages sent with a routing key that begins with foo.bar. are routed to the queue.*/
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }


    /*Because the Receiver class is a POJO, it needs to be wrapped in the MessageListenerAdapter, where you specify that it invokes receiveMessage--the method you have defined.*/
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MessagingRabbitmqApplication.class, args).close();
    }

}
