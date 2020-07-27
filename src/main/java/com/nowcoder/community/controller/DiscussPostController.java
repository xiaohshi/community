package com.nowcoder.community.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {

        User user = hostHolder.getUser();

        if (user == null) {
            return CommunityUtil.getJsonString(403, "你好没有登录");
        }

        DiscussPost post = DiscussPost.builder().userId(user.getId()).content(content)
                .title(title).createTime(new Date()).build();
        discussPostService.addDiscussPost(post);

        // 计算帖子分数
        String redisKey = RedisKeyUtil.getPostScorKey();
        redisTemplate.opsForSet().add(redisKey, post.getId());

        return CommunityUtil.getJsonString(0, "发布成功");
    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        DiscussPost discussPost = discussPostService.findDiscussPost(discussPostId);
        model.addAttribute("post", discussPost);
        //作者
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);
        //点赞数量
        long count = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", count);
        //点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        //评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(discussPost.getCommentCount());

        //评论：给帖子的评论
        //回复：给评论的评论
        //评论列表
        List<Comment> commentsList = commentService.findCommentsByEntity(ENTITY_TYPE_POST,
                discussPost.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentViewList = new ArrayList<>();

        commentsList.forEach(comment -> {
            // 评论集合vo
            Map<String, Object> commentVo = new HashMap<>();
            // 评论
            commentVo.put("comment", comment);
            // 作者
            commentVo.put("user", userService.findUserById(comment.getUserId()));
            //点赞数量
            long commentLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
            commentVo.put("likeCount", commentLikeCount);
            //点赞状态
            int commentLikeStatus = hostHolder.getUser() == null ? 0 :
                    likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
            commentVo.put("likeStatus", commentLikeStatus);
            // 回复列表
            List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,
                    comment.getId(), 0, Integer.MAX_VALUE);
            // 回复vo列表
            List<Map<String, Object>> replyViewList = new ArrayList<>();
            replyList.forEach(reply -> {
                Map<String, Object> replyVo = new HashMap<>();
                // 回复
                replyVo.put("reply", reply);
                // 作者
                replyVo.put("user", userService.findUserById(reply.getUserId()));
                // 回复的目标
                User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                //点赞数量
                long replyLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                replyVo.put("likeCount", replyLikeCount);
                //点赞状态
                int replyLikeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                replyVo.put("likeStatus", replyLikeStatus);
                replyVo.put("target", target);
                replyViewList.add(replyVo);
            });
            commentVo.put("replys", replyViewList);
            //回复数量
            int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
            commentVo.put("replyCount", replyCount);

            commentViewList.add(commentVo);
        });
        model.addAttribute("comments", commentViewList);
        return "/site/discuss-detail";
    }

    //置顶
    @RequestMapping(path = "/top", method = RequestMethod.POST)
    @ResponseBody
    public String setTop(int id) {
        discussPostService.updateType(id, 1);
        return CommunityUtil.getJsonString(0);
    }

    //加精
    @RequestMapping(path = "/wonderful", method = RequestMethod.POST)
    @ResponseBody
    public String setWonderful(int id) {
        discussPostService.updateStatus(id, 1);
        return CommunityUtil.getJsonString(0);
    }

    //加精
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int id) {
        discussPostService.updateStatus(id, 2);
        return CommunityUtil.getJsonString(0);
    }
}
