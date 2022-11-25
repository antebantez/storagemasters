package storage_masters.demo.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import storage_masters.demo.data.User;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class UserPayload {

    private final UUID id;

    private String username;
    private boolean admin;

    public static UserPayload fromUser(User user) {
        var payload = new UserPayload(user.getUserId());
        payload.setAdmin(user.isAdmin());
        payload.setUsername(user.getName());

        return payload;
    }

}
