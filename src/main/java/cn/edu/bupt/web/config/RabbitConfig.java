package cn.edu.bupt.web.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String ORDER_QUEUE = "orderQueue";
    public static final String PAYMENT_QUEUE = "paymentQueue";
    public static final String SHIPPED_QUEUE = "shippedQueue";
    public static final String ORDER_CANCEL_QUEUE = "orderCancelQueue";
    public static final String EXCHANGE = "exchange";

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public Queue orderCancelQueue() {
        return new Queue(ORDER_CANCEL_QUEUE, true);
    }
    @Bean
    public Queue shippedQueue() {
        return new Queue(SHIPPED_QUEUE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange exchange) {
        return BindingBuilder.bind(orderQueue).to(exchange).with(ORDER_QUEUE);
    }

    @Bean
    public Binding paymentBinding(Queue paymentQueue, DirectExchange exchange) {
        return BindingBuilder.bind(paymentQueue).to(exchange).with(PAYMENT_QUEUE);
    }

    @Bean
    public Binding orderCancelBinding(Queue orderCancelQueue, DirectExchange exchange) {
        return BindingBuilder.bind(orderCancelQueue).to(exchange).with(ORDER_CANCEL_QUEUE);
    }

    @Bean
    public Binding shippedBinding(Queue shippedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(shippedQueue).to(exchange).with("order.shipped");
    }
}
