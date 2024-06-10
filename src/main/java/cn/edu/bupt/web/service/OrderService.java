package cn.edu.bupt.web.service;

import cn.edu.bupt.web.entity.Order;
import cn.edu.bupt.web.mapper.OrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
}
