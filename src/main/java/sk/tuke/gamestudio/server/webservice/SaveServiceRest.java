package sk.tuke.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Save;
import sk.tuke.gamestudio.service.SaveService;

import java.util.List;

@RestController
@RequestMapping("/api/save")
public class SaveServiceRest {
    private final SaveService saveService;

    public SaveServiceRest(SaveService saveService) {
        this.saveService = saveService;
    }

    @PostMapping
    public void addSave(@RequestBody Save save) {
        saveService.addSave(save);
    }

    @GetMapping("/{game}/{player}")
    public List<Save> getSaves(@PathVariable String game, @PathVariable String player) {
        return saveService.getSaves(game, player);
    }

}
