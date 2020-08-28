package net.minthe.bookmanager.controllers.transport;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddBookReadRequest {
  @NotNull @NotEmpty private UUID id;
  @NotNull private Long bookId;
  @NotNull private Long started;

  @JsonProperty(defaultValue = "false")
  private boolean audiobook;
}
