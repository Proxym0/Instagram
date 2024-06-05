package service;

import dao.PostDAO;
import dao.impl.JDBCPostDAO;
import entity.Post;

import java.util.List;
import java.util.Optional;

public class PostService {
    private final PostDAO postDAO = JDBCPostDAO.getInstance();
    private static PostService instance;

    private PostService() {

    }

    public static PostService getInstance() {
        if (instance == null) {
            instance = new PostService();
        }
        return instance;
    }

    public Optional<Post> findPostById(long id) {
        return postDAO.findById(id);
    }

    public List<Post> findPostsByUserId(long id){
        return postDAO.findPostsByUserId(id);
    }

    public List<Post> findAllPosts() {
        return postDAO.findAll();
    }

    public void removePost(long id) {
        postDAO.remove(id);
    }

    public List<Post> findPosts(long id) {
        return postDAO.findPostsByUserId(id);
    }

    public void createPost(Post post) {
        postDAO.save(post);
    }

    public Optional<Post> getPostWithComments(long postId){
        return postDAO.findPostWithComments(postId);
    }
}
