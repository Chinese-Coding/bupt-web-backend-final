package cn.edu.bupt.web.service;

import cn.edu.bupt.web.entity.Payment;
import cn.edu.bupt.web.mapper.PaymentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PaymentService extends ServiceImpl<PaymentMapper, Payment> {
}
