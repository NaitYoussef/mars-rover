package org.nasa;

import java.util.Objects;

public class Position {

  private final int x;
  private final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Position incX() {
    return new Position(this.x + 1, this.y);
  }

  public Position decX() {
    return new Position(this.x - 1, this.y);
  }

  public Position incY() {
    return new Position(this.x, this.y + 1);
  }

  public Position decY() {
    return new Position(this.x, this.y - 1);
  }

  public int x() {
    return this.x;
  }

  public int y() {
    return this.y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position = (Position) o;
    return x == position.x && y == position.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
