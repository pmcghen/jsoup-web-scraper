import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class WebScraperTest {

    @Test
    @DisplayName("Check that date falls within threshold.")
    void wasPostedWithinThreshold() {
        ZoneId zoneId = ZoneId.of("America/New_York");
        LocalDate today = LocalDate.now(zoneId);

        assertTrue(WebScraper.wasPostedWithinThreshold(String.valueOf(today)));
        assertFalse(WebScraper.wasPostedWithinThreshold("2023-03-31T14:37:49Z"));
        assertTrue(WebScraper.wasPostedWithinThreshold("2023-03-31T14:37:49Z", 4, 1));
    }
}