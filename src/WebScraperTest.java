import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScraperTest {

    @Test
    void wasPostedWithinThreshold() {
        assertFalse(WebScraper.wasPostedWithinThreshold("2023-03-31T14:37:49Z"));
    }
}