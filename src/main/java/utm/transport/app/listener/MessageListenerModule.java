package utm.transport.app.listener;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.DefaultExceptionHandler;
import utm.transport.app.cache.CacheManager;
import utm.transport.app.exceptions.MessageRecieveException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessageListenerModule {

    private final static String QUEUE_NAME = "hello";
    private final static String EXCHANGE_NAME = "telemetry";
    private final static String ROUTING_KEY = "telemetry";
    private static final String UTF_8 = "UTF-8";
    private static Channel CHANNEL;
    private static String QUEUE;

    private MessageListenerModule(Channel channel, String queueName) {
        CHANNEL = channel;
        QUEUE = queueName;
    }

    public static MessageListenerModule init() throws MessageRecieveException {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("185.143.172.238");
            factory.setPort(5672);
            factory.setUsername("hackaton");
            factory.setPassword("QtGcmpPm");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            final ExceptionHandler eh = new DefaultExceptionHandler() {
                @Override
                public void handleConsumerException(Channel channel, Throwable exception, Consumer consumer, String consumerTag, String methodName) {
                    System.out.println(" - Error raised by: " + channel.getChannelNumber());
                }
            };

            factory.setExceptionHandler(eh);

            return new MessageListenerModule(channel, queueName);

        } catch (IOException e) {
            throw new MessageRecieveException("Ошибка получения сообщений");
        } catch (TimeoutException e) {
            throw new MessageRecieveException("Таймаут подключения к очереди");
        }
    }

    public void receiveMessages(String uid, DeliverCallback deliverCallback) throws MessageRecieveException {
        try {
            CHANNEL.basicConsume(QUEUE, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            throw new MessageRecieveException("Ошибка получения сообщений");
        }
    }

    public void abort (String uid) {
        try {
            CHANNEL.close();
            CacheManager.deleteAndEvict();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
