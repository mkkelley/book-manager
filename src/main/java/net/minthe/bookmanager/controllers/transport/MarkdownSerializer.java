package net.minthe.bookmanager.controllers.transport;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/** Created by Michael Kelley on 9/16/2020 */
public class MarkdownSerializer extends StdSerializer<MarkdownContent> {
  private final Parser parser;
  private final HtmlRenderer renderer;

  public MarkdownSerializer() {
    super(MarkdownContent.class);
    parser = Parser.builder().build();
    renderer = HtmlRenderer.builder().escapeHtml(true).sanitizeUrls(true).build();
  }

  @Override
  public void serialize(MarkdownContent value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    var document = parser.parse(value.getContent());
    var renderedDocument = renderer.render(document);
    gen.writeString(renderedDocument);
  }
}
