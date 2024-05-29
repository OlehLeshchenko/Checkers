package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.User;

public interface UserService {
    /**
     * adds a new user to storage
     * @param user user object to be added
     * @return true false depending on the success of the execution
     */
    Boolean addUser(User user);

    Boolean isUserExist(String nickname, String password);
    /**
     * update all users in the storage
     */
    void updatePassword(User user, String newPassword);


    /**
     * deletes all users in the storage
     */
    void reset();

}
