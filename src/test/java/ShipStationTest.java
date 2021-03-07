
import org.junit.jupiter.api.Test;

import java.util.Base64;

class ShipStationTest {
    @Test
    void shipstation_usernamePassword() {
        System.out.println(Base64.getEncoder().encodeToString("9ea9051c16aa443baf1700f48eda34b6:2a28229f1864405b97afb3ace18ddfcf".getBytes()));
    }
}
