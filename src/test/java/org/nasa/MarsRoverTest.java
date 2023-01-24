package org.nasa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nasa.MarsRoverTest.Direction.EAST;
import static org.nasa.MarsRoverTest.Direction.NORTH;
import static org.nasa.MarsRoverTest.Direction.SOUTH;
import static org.nasa.MarsRoverTest.Direction.WEST;

import org.junit.jupiter.api.Test;

public class MarsRoverTest {

  public static class MarsRover {

    private int x;
    private int y;
    private Direction initialDirection;

    public MarsRover(int x, int y, Direction initialDirection, Position position) {
      this.x = x;
      this.y = y;
      this.initialDirection = initialDirection;
    }

    public void moveBackward() {
      if (initialDirection == NORTH) {
        y--;
        return;
      }
      if (initialDirection == WEST) {
        x++;
        return;
      }
      if (initialDirection == SOUTH) {
        y++;
        return;
      }
      x--;
    }
  }

  public static class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
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
    MarsRover rover = new MarsRover(2, 1, SOUTH, null);

    rover.moveBackward();

    assertThat(rover.x).isEqualTo(2);
    assertThat(rover.y).isEqualTo(2);
  }

  @Test
  public void should_move_backward_when_facing_direction_is_west() {
    MarsRover rover = new MarsRover(2, 1, WEST, null);

    rover.moveBackward();

    assertThat(rover.x).isEqualTo(3);
    assertThat(rover.y).isEqualTo(1);
  }

  @Test
  public void should_move_backward_when_facing_direction_is_north() {
    MarsRover rover = new MarsRover(2, 1, NORTH, null);

    rover.moveBackward();

    assertThat(rover.x).isEqualTo(2);
    assertThat(rover.y).isEqualTo(0);
  }

  @Test
  public void should_move_backward_when_facing_direction_is_east() {
    MarsRover rover = new MarsRover(2, 1, EAST, null);

    rover.moveBackward();

    assertThat(rover.x).isEqualTo(1);
    assertThat(rover.y).isEqualTo(1);
  }

}
