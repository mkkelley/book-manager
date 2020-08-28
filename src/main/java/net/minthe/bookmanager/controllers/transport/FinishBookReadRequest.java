package net.minthe.bookmanager.controllers.transport;

import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FinishBookReadRequest {
  @NotNull @NotEmpty private UUID id;
  @NotNull private Long finished;
}
