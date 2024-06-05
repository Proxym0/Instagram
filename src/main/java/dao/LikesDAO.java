package dao;

import entity.Like;

import java.util.List;
import java.util.Optional;

public interface LikesDAO extends DAO<Like> {
    Optional<Like> findLikeByUserId(long userid);

    List<Like> findLikeByPost(long post);
}
