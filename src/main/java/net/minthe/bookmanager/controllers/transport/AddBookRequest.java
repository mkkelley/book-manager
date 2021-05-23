package net.minthe.bookmanager.controllers.transport;

import java.util.Optional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddBookRequest {
  @NotNull @NotEmpty private BookTitle title;
  @NotNull @NotEmpty private String authorName;
  private Long published;

  public Optional<Long> getPublished() {
    return Optional.ofNullable(published);
  }
}
