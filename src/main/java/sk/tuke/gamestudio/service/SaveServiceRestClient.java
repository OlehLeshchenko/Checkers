package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Save;

import java.util.Arrays;
import java.util.List;

public class SaveServiceRestClient extends SaveService {
    private final String url = "http://localhost:8080/api/save";

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void addSave(Save save) {
        restTemplate.postForEntity(url, save, Save.class);
    }

    @Override
    public List<Save> getSaves(String gameName, String playerName) {
        return Arrays.asList(
            restTemplate.getForEntity(url + "/" + gameName + "/" + playerName, Save[].class).getBody()
        );
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }

}
