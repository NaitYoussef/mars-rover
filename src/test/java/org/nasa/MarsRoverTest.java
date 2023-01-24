package org.nasa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nasa.MarsRoverTest.Direction.EAST;
import static org.nasa.MarsRoverTest.Direction.NORTH;
import static org.nasa.MarsRoverTest.Direction.SOUTH;
import static org.nasa.MarsRoverTest.Direction.WEST;

import java.util.Objects;
import org.junit.jupiter.api.Test;

public class MarsRoverTest {

  public static class MarsRover {

    private int x;
    private int y;
    private Direction initialDirection;
    private Position position;

    public MarsRover(int x, int y, Direction initialDirection) {
      this.x = x;
      this.y = y;
      this.initialDirection = initialDirection;
      this.position = new Position(x, y);
    }

    public void moveBackward() {
      if (initialDirection == NORTH) {
        y--;
        position.y--;
        return;
      }
      if (initialDirection == WEST) {
        x++;
        position.x++;
        return;
      }
      if (initialDirection == SOUTH) {
        y++;
        position.y++;
        return;
      }
      x--;
      position.x--;
    }
  }

  public static class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
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
  }

  public enum Direction {
    NORTH('N'), SOUTH('S'), WEST('W'), EAST('E');

    private char direction;

    Direction(char direction) {
      this.direction = direction;
    }
  }

  /*  N

    3 * * * *
    2 * * . *
W   1 * . X .   E
    0 * * . *
      0 1 2 3

        S
   */

  @Test
  public void should_move_backward_when_facing_direction_is_south() {
    MarsRover rover = new MarsRover(2, 1, SOUTH);

    rover.moveBackward();

    assertThat(rover.position).isEqualTo(new Position(2, 2));
  }

  @Test
  public void should_move_backward_when_facing_direction_is_west() {
    MarsRover rover = new MarsRover(2, 1, WEST);

    rover.moveBackward();

    assertThat(rover.position).isEqualTo(new Position(3, 1));
  }

  @Test
  public void should_move_backward_when_facing_direction_is_north() {
    MarsRover rover = new MarsRover(2, 1, NORTH);

    rover.moveBackward();

    assertThat(rover.position).isEqualTo(new Position(2, 0));
  }

  @Test
  public void should_move_backward_when_facing_direction_is_east() {
    MarsRover rover = new MarsRover(2, 1, EAST);

    rover.moveBackward();

    assertThat(rover.position).isEqualTo(new Position(1, 1));
  }

}
