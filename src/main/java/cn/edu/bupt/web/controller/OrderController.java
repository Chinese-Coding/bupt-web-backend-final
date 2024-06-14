package cn.edu.bupt.web.controller;

import cn.edu.bupt.web.common.R;
import cn.edu.bupt.web.config.RabbitConfig;
import cn.edu.bupt.web.entity.Order;
import cn.edu.bupt.web.entity.OrderItem;
import cn.edu.bupt.web.entity.Product;
import cn.edu.bupt.web.service.OrderItemService;
import cn.edu.bupt.web.service.OrderService;
import cn.edu.bupt.web.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderService orderService;
    @Resource
    private OrderItemService orderItemService;
    @Resource
    private ProductService productService;
    final RedisTemplate<String, String> redisTemplate;
    public OrderController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Order createOrder(Long userId){
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(BigDecimal.valueOf(0));
        order.setStatus("CREATED");
        order.setCreateTime(LocalDateTime.now());
        orderService.save(order);
        return order;
    }
    public List<OrderItem> getAllItem(Order order){
        var orderId = order.getId();
        LambdaQueryWrapper<OrderItem> lqw = new LambdaQueryWrapper<>();
        lqw.eq(OrderItem::getOrderId,orderId);
        return orderItemService.list(lqw);
    }
    @PostMapping("/addProduct")
    public R<OrderItem> addProduct(@NotNull Long productId,@NotNull Integer num,@RequestHeader("Authorization") String token){
        var userId = redisTemplate.opsForValue().get(token);
        if(userId == null) return R.error("请先登录");
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Order::getUserId, Long.parseLong(userId))
                .eq(Order::getStatus, "CREATED");
        Order createdOrder = orderService.getOne(lqw);
        if(createdOrder == null) createdOrder = createOrder(Long.valueOf(userId));
        Long orderId = createdOrder.getId();
        Product product = productService.getById(productId);
        if(product.getStock() >= num){
            var price = product.getPrice();
            var totalfee = price.multiply(BigDecimal.valueOf(num));
            createdOrder.setTotalAmount(totalfee);
            orderService.updateById(createdOrder);
            OrderItem newOrderItem = new OrderItem();
            newOrderItem.setProductId(productId);
            newOrderItem.setOrderId(orderId);
            newOrderItem.setPrice(price);
            newOrderItem.setQuantity(num);
            orderItemService.save(newOrderItem);
            return R.success(newOrderItem);
        }
        return R.error("添加商品失败");
    }
    @GetMapping("/newOrder")
    public R<Order> newOrder(@RequestHeader("Authorization") String token){
        var userId = redisTemplate.opsForValue().get(token);
        if(userId == null) return R.error("请先登录");
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Order::getUserId, Long.parseLong(userId))
                .eq(Order::getStatus, "CREATED");
        Order createdOrder = orderService.getOne(lqw);
        if(createdOrder == null) return R.error("你的购物车是空的");
        String orderMessage = convertOrderToJson(createdOrder, getAllItem(createdOrder));
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ORDER_QUEUE, orderMessage);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ORDER_CANCEL_QUEUE, orderMessage, message -> {
            message.getMessageProperties().setExpiration("600000");
            return message;
        });
        if(createdOrder.getStatus().equals("CONFIRM")){
            createdOrder.setStatus("PENDING");
            orderService.updateById(createdOrder);
            return R.success(createdOrder);
        }
        return R.error("出错了");
    }
    private String convertOrderToJson(Order order, List<OrderItem> orderItems) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Map<Object, Object> orderMap = new HashMap<>();
        orderMap.put("order", order);
        orderMap.put("orderItems", orderItems);
        try {
            return objectMapper.writeValueAsString(orderMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert order to JSON", e);
        }
    }
    @GetMapping("/searchOrder")
    public R<List<Order>> searchOrder(@RequestHeader("Authorization") String token,@NotNull String status){
        var userId = redisTemplate.opsForValue().get(token);
        if(userId == null) return R.error("请先登录");
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        if (!"All".equals(status)) {
            queryWrapper.eq(Order::getStatus, status);
        }
        List<Order> orders = orderService.list(queryWrapper);
        return R.success(orders);
    }
    @PostMapping("/pay")
    public R<String> payOrder(@RequestHeader("Authorization") String token,Long orderId){
        var userId = redisTemplate.opsForValue().get(token);
        if(userId == null) return R.error("请先登录");
        var order = orderService.getById(orderId);
        if(!order.getStatus().equals("PENDING")){
            return R.error("出错了！请检查你的订单");
        }
        String orderMessage = convertOrderToJson(order, getAllItem(order));
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.PAYMENT_QUEUE, orderMessage);
        return R.success("Order payment initiated.");
    }
}
