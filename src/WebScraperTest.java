import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScraperTest {

    @Test
    @DisplayName("Check that date falls within threshold.")
    void wasPostedWithinThreshold() {
        assertFalse(WebScraper.wasPostedWithinThreshold("2023-03-31T14:37:49Z"));
        assertTrue(WebScraper.wasPostedWithinThreshold("2023-03-31T14:37:49Z", 4, 1));
    }
}