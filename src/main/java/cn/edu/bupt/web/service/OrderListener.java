package cn.edu.bupt.web.service;
import cn.edu.bupt.web.config.RabbitConfig;
import cn.edu.bupt.web.entity.Order;
import cn.edu.bupt.web.entity.OrderItem;
import cn.edu.bupt.web.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OrderListener {
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private OrderService orderService;
    @Resource
    private OrderItemService orderItemService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private ProductService productService;
    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE)
    public void handleOrder(String orderMessage) {
        try {
            Map<String, Object> orderMap = objectMapper.readValue(orderMessage, Map.class);
            Order order = objectMapper.convertValue(orderMap.get("order"), Order.class);
            List<OrderItem> orderItems = objectMapper.convertValue(orderMap.get("orderItems"), objectMapper.getTypeFactory().constructCollectionType(List.class, OrderItem.class));
            for (OrderItem orderItem : orderItems) {
                var productId = orderItem.getProductId();
                Product curProduct = productService.getById(productId);
                if(curProduct.getStock() < orderItem.getQuantity()){
                    order.setStatus("CREATE");
                    orderService.updateById(order);
                    throw new RuntimeException("库存不足，订单取消");
                }
            }
            for(OrderItem orderItem : orderItems){
                var productId = orderItem.getProductId();
                Product curProduct = productService.getById(productId);
                curProduct.setStock(curProduct.getStock()-orderItem.getQuantity());
                productService.updateById(curProduct);
            }
            order.setStatus("PENDING");
            orderService.updateById(order);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE)
    public void handlePayment(String orderMessage) {
        log.info("Received message: {}", orderMessage);
        try {
            Map<String, Object> orderMap = objectMapper.readValue(orderMessage, Map.class);
            Order order = objectMapper.convertValue(orderMap.get("order"), Order.class);
            log.info("Received order: {}", order);

            // 更新订单状态为PAID
            order.setStatus("PAID");
            boolean isUpdated = orderService.updateById(order);
            if (isUpdated) {
                log.info("Order status updated to PAID for order ID: {}", order.getId());

                // 发送发货消息
                rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.SHIPPED_QUEUE, orderMessage, message -> {
                    message.getMessageProperties().setExpiration("300000"); // 5分钟后过期
                    return message;
                });
            } else {
                log.error("Failed to update order status for order ID: {}", order.getId());
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing order message: {}", orderMessage, e);
        }
    }

    @RabbitListener(queues = RabbitConfig.ORDER_CANCEL_QUEUE)
    public void handleCancel(String orderMessage){
        try {
            Map<String, Object> orderMap = objectMapper.readValue(orderMessage, Map.class);
            Order order = objectMapper.convertValue(orderMap.get("order"), Order.class);
            List<OrderItem> orderItems = objectMapper.convertValue(orderMap.get("orderItems"), objectMapper.getTypeFactory().constructCollectionType(List.class, OrderItem.class));
            if ("PENDING".equals(order.getStatus())) {
                for(OrderItem orderItem : orderItems){
                    var productId = orderItem.getProductId();
                    Product curProduct = productService.getById(productId);
                    curProduct.setStock(curProduct.getStock()+orderItem.getQuantity());
                    productService.updateById(curProduct);
                }
                order.setStatus("CANCELLED");
                orderService.updateById(order);

            }
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitConfig.SHIPPED_QUEUE)
    public void handleShip(String orderMessage){
        try {
            Map<String, Object> orderMap = objectMapper.readValue(orderMessage, Map.class);
            Order order = objectMapper.convertValue(orderMap.get("order"), Order.class);
            order.setStatus("SHIPPED");
            orderService.updateById(order);
            log.info(("订单：")+order.getId()+(",发货咯!"));
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
