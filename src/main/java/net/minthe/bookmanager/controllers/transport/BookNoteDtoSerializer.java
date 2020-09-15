package net.minthe.bookmanager.controllers.transport;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Created by Michael Kelley on 9/14/2020 */
public class BookNoteDtoSerializer extends StdSerializer<BookNoteDto> {
  private final Logger logger = LoggerFactory.getLogger(BookNoteDtoSerializer.class);
  private final Parser parser;
  private final HtmlRenderer renderer;

  public BookNoteDtoSerializer() {
    super(BookNoteDto.class);
    parser = Parser.builder().build();
    renderer = HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build();
  }

  @Override
  public void serialize(BookNoteDto value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeStartObject();

    var renderedNotes = "";
    try {
      var document = parser.parse(value.getNotes());
      renderedNotes = renderer.render(document);
    } catch (Exception e) {
      renderedNotes = value.getNotes();
      logger.error(e.toString());
    }

    gen.writeObjectField("id", value.getId().toString());
    gen.writeObjectField("username", value.getUsername());
    gen.writeObjectField("bookId", value.getBookId());
    gen.writeObjectField("notes", value.getNotes());
    gen.writeObjectField("renderedNotes", renderedNotes);
    gen.writeObjectField("createdBy", value.getCreatedBy());
    gen.writeObjectField("userCreatedAt", value.getUserCreatedAt().toEpochMilli());

    gen.writeEndObject();
  }
}
