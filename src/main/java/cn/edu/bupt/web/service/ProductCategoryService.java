package cn.edu.bupt.web.service;

import cn.edu.bupt.web.mapper.ProductCategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {
    @Resource
    private ProductCategoryMapper productCategoryMapper;
}
