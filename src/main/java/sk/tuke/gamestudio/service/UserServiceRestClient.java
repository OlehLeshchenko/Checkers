package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.User;

public class UserServiceRestClient implements UserService {
    private final String url = "http://localhost:8080/api/user";

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean addUser(User user) {
        return restTemplate.postForEntity(url, user, Boolean.class).getBody();
    }

    @Override
    public Boolean isUserExist(String nickname, String password) {
        return restTemplate.getForEntity(url + "/userExist", Boolean.class, nickname, password).getBody();
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        throw new UnsupportedOperationException("Not supported via web service");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }

}