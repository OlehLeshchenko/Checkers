package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

public class RatingServiceRestClient implements RatingService{
    private final String url = "http://localhost:8080/api/rating";

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getRating(String gameName, String playerName) {
        return restTemplate.getForEntity(url + "/" + gameName + "/" + playerName, Integer.class).getBody();
    }

    @Override
    public int getAverageRating(String gameName) {
        return restTemplate.getForEntity(url + "/" + gameName, Integer.class).getBody();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }

}
