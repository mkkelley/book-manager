package net.minthe.bookmanager.exceptions;

/** Created by Michael Kelley on 9/19/2020 */
public class NotFoundException extends RuntimeException {
  public <T, K> NotFoundException(Class<T> clazz, K id) {
    super("Unable to find " + clazz.getSimpleName() + " with id \"" + id.toString() + "\"");
  }
}
