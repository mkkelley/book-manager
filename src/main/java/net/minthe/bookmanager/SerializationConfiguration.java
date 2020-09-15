package net.minthe.bookmanager;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.minthe.bookmanager.controllers.transport.BookNoteDto;
import net.minthe.bookmanager.controllers.transport.BookNoteDtoSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Created by Michael Kelley on 9/14/2020 */
@Configuration
public class SerializationConfiguration {
  @Bean
  public Module getBookNoteDtoSerializer() {
    var module = new SimpleModule();
    module.addSerializer(BookNoteDto.class, new BookNoteDtoSerializer());
    return module;
  }
}
