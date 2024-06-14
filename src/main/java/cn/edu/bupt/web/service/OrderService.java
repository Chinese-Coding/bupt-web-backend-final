package cn.edu.bupt.web.service;

import cn.edu.bupt.web.entity.Order;
import cn.edu.bupt.web.entity.OrderItem;
import cn.edu.bupt.web.mapper.OrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
    private final Map<Long, List<OrderItem>> userOrderItems = new HashMap<>();
    public void addOrderItem(Long userId, OrderItem orderItem) {
        userOrderItems.computeIfAbsent(userId, k -> new ArrayList<>()).add(orderItem);
    }
    public List<OrderItem> getOrderItems(Long userId) {
        return userOrderItems.get(userId);
    }

    public void clearOrderItems(Long userId) {
        userOrderItems.remove(userId);
    }
}
