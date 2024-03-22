package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;

import java.util.List;
public interface CommentService {
    /**
     * adds a new comment to storage
     * @param comment comment object to be added
     */
    void addComment(Comment comment);

    /**
     * Get 5 last comments from the storage for the game named <code>game</code>
     * @param game name of the game
     * @return list of the (at most) 5 last comments
     */
    List<Comment> getComments(String game);

    /**
     * deletes all comments in the storage (for all games)
     */
    void reset();

}
