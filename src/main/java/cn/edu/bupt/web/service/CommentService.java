package cn.edu.bupt.web.service;

import cn.edu.bupt.web.entity.Comment;
import cn.edu.bupt.web.mapper.CommentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> {
}
