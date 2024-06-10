package cn.edu.bupt.web.service;

import cn.edu.bupt.web.entity.OrderItem;
import cn.edu.bupt.web.mapper.OrderItemMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService extends ServiceImpl<OrderItemMapper, OrderItem> {
}
