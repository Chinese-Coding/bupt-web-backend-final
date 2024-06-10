package cn.edu.bupt.web.service;

import cn.edu.bupt.web.entity.User;
import cn.edu.bupt.web.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
}
