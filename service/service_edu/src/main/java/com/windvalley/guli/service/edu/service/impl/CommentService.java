package com.windvalley.guli.service.edu.service.impl;

import com.windvalley.guli.service.edu.entity.Comment;
import com.windvalley.guli.service.edu.mapper.CommentMapper;
import com.windvalley.guli.service.edu.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author icewind4096
 * @since 2021-01-23
 */
@Service
public class CommentService extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
