package fr.zomet.tuto.reposity;

import fr.zomet.tuto.dto.UserDto;
import org.springframework.stereotype.Repository;

@Repository
public class MockUserRepo {

    public UserDto findById(int id) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return createMockedUser(id);
    }

    private UserDto createMockedUser(int id) {
        UserDto dto = new UserDto();
        dto.id = id;
        dto.fullName = "foo bar";
        return dto;
    }
}
