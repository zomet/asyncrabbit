package fr.zomet.tuto.api;

import fr.zomet.tuto.configuration.AsyncExecutor;
import fr.zomet.tuto.dto.UserDto;
import fr.zomet.tuto.rabbit.CorrelationId;
import fr.zomet.tuto.reposity.MockUserRepo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final MockUserRepo userRepo;
    private final AsyncExecutor<UserDto> asyncExecutor;

    public UserController(MockUserRepo userRepo, AsyncExecutor<UserDto> asyncExecutor) {
        this.userRepo = userRepo;
        this.asyncExecutor = asyncExecutor;
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getUserById(@PathVariable("id") int id) {
        return asyncExecutor.execute(() -> userRepo.findById(id)).toString();
    }
}
