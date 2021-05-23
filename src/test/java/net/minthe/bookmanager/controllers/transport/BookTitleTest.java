package net.minthe.bookmanager.controllers.transport;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

public class BookTitleTest {

  @Test
  public void quote_becomesApostrophe() {
    var unescapedTitle = "a's's'e'''s";
    var title = new BookTitle(unescapedTitle);
    var escapedTitle = title.getTitle();

    assertFalse(escapedTitle.contains("'"));
    assertEquals("a’s’s’e’’’s", escapedTitle);
  }

  @Test
  public void deserializes_fromString() throws JsonProcessingException {
    var jsonString = "{\"a\": \"b\"}";
    var mapper = new ObjectMapper();
    assertDoesNotThrow(
        () -> {
          mapper.readValue(jsonString, BookTitleTestContainer.class);
        });
    assertEquals("b", mapper.readValue(jsonString, BookTitleTestContainer.class).getA().getTitle());
  }

  @Test
  public void deserializes_toString() throws JsonProcessingException {
    var title = new BookTitle("b");
    var container = new BookTitleTestContainer(title);
    var mapper = new ObjectMapper();
    assertDoesNotThrow(() -> mapper.writeValueAsString(container));
    assertEquals("{\"a\":\"b\"}", mapper.writeValueAsString(container));
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  private static class BookTitleTestContainer {
    private BookTitle a;
  }
}
