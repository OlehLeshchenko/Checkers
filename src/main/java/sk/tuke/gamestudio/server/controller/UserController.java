package sk.tuke.gamestudio.server.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;

@Controller
@RequestMapping("/checkers")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private User loggedUser = null;

    @Autowired
    private UserService userService;

    @RequestMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean login(@RequestParam(required = false) String nickname, @RequestParam(required = false) String passwd) {
        if (userService.isUserExist(nickname, passwd)){
            loggedUser = new User(nickname, passwd);
            return true;
        }
        return false;
    }

    @RequestMapping(value="/createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean createUser(@RequestParam(required = false) String nickname, @RequestParam(required = false) String passwd) {
        loggedUser = new User(nickname, passwd);
        return userService.addUser(loggedUser);
    }

    @RequestMapping("/loggedUser")
    @ResponseBody
    public String getLoggedUserNickname() {
        if (loggedUser == null) return "";
        System.out.println(loggedUser.getNickname());
        return loggedUser.getNickname();
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Boolean logout() {
        loggedUser = null;
        return true;
    }

}