package sk.tuke.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {
    private final UserService userService;

    public UserServiceRest(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Boolean addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping
    @RequestMapping("/exist")
    public Boolean isUserExist(String nickname, String password) {
        return userService.isUserExist(nickname, password);
    }

}
