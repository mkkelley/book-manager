package net.minthe.bookmanager.controllers.transport;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Some mobile devices default to using ’ instead of ' as the most accessible single quote. Because
 * ’ is technically more correct than ', we normalize all instances of ' to ’ in book titles.
 */
@Schema(type = "string")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookTitle implements JsonSerializable {
  private String title;

  public String getTitle() {
    return title.replace('\'', '’');
  }

  @Override
  public String toString() {
    return getTitle();
  }

  @Override
  public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeString(getTitle());
  }

  @Override
  public void serializeWithType(
      JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
      throws IOException {
    typeSer.writeTypePrefix(gen, typeSer.typeId(this, BookTitle.class, JsonToken.VALUE_STRING));
    this.serialize(gen, serializers);
    typeSer.writeTypeSuffix(gen, typeSer.typeId(this, BookTitle.class, JsonToken.VALUE_STRING));
  }
}
