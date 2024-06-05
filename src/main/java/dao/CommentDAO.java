package dao;

import entity.Comment;

import java.util.List;

public interface CommentDAO extends DAO<Comment>{
    List<Comment> findAllCommentsByPostId(long postId);
}
