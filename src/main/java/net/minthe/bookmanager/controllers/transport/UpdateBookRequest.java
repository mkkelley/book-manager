package net.minthe.bookmanager.controllers.transport;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Created by Michael Kelley on 9/19/2020 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateBookRequest extends AddBookRequest {
  @NotNull @NotEmpty private Long id;
}
