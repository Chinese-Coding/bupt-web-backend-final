package cn.edu.bupt.web.service;

import cn.edu.bupt.web.entity.Product;
import cn.edu.bupt.web.mapper.ProductMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProductService  extends ServiceImpl<ProductMapper, Product> {
}
