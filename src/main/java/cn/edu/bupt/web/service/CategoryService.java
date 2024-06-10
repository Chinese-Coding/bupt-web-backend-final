package cn.edu.bupt.web.service;

import cn.edu.bupt.web.entity.Category;
import cn.edu.bupt.web.mapper.CategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
}
