package org.nasa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nasa.MarsRoverTest.Direction.EAST;
import static org.nasa.MarsRoverTest.Direction.NORTH;
import static org.nasa.MarsRoverTest.Direction.SOUTH;
import static org.nasa.MarsRoverTest.Direction.WEST;

import java.util.Objects;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MarsRoverTest {

  public static class MarsRover {

    private Direction initialDirection;
    private Position position;

    public MarsRover(int x, int y, Direction initialDirection) {
      this.initialDirection = initialDirection;
      this.position = new Position(x, y);
    }

    public void moveBackward() {
      if (initialDirection == NORTH) {
        position.y--;
        return;
      }
      if (initialDirection == WEST) {
        position.x++;
        return;
      }
      if (initialDirection == SOUTH) {
        position.y++;
        return;
      }
      position.x--;
    }

    public void moveForward() {
      if (initialDirection == WEST) {
        position.x--;
        return;
      }
      position.y--;
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

    @Override
    public String toString() {
      return "(" + x + ", " + y + ")";
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

  @Nested
  class ForwardScenarios {

    @Test
    void should_move_south_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH);

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(2, 0));
    }

    @Test
    void should_move_west_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST);

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(1, 1));
    }
  }

  @Nested
  class BackwardScenarios {

    @Test
    public void should_move_north_when_facing_direction_is_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH);

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(2, 2));
    }

    @Test
    public void should_move_east_when_facing_direction_is_west() {
      MarsRover rover = new MarsRover(2, 1, WEST);

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(3, 1));
    }

    @Test
    public void should_move_south_when_facing_direction_is_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH);

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(2, 0));
    }

    @Test
    public void should_move_east_when_facing_direction_is_east() {
      MarsRover rover = new MarsRover(2, 1, EAST);

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(1, 1));
    }
  }

}
