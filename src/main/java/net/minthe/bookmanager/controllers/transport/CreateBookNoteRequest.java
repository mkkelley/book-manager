package net.minthe.bookmanager.controllers.transport;

import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Created by Michael Kelley on 8/29/2020 */
@Getter
@Setter
@NoArgsConstructor
public class CreateBookNoteRequest {
  private UUID id;
  @NotNull @NotEmpty private String username;
  @NotNull private Long bookId;
  @NotNull @NotEmpty private String notes;
}
