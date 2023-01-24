package org.nasa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nasa.MarsRoverTest.Direction.SOUTH;
import static org.nasa.MarsRoverTest.Direction.WEST;

import org.junit.jupiter.api.Test;

public class MarsRoverTest {

  public static class MarsRover {

    private int x;
    private int y;
    private char initialDirection;
    private Direction direction;

    public MarsRover(int x, int y, char initialDirection, Direction direction) {
      this.x = x;
      this.y = y;
      this.initialDirection = initialDirection;
      this.direction = direction;
    }

    public void moveBackward() {
      if (initialDirection == 'N') {
        y--;
        return;
      }
      if (direction == WEST) {
        x++;
        return;
      }
      if (direction == SOUTH) {
        y++;
        return;
      }
      x--;
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
  public void should_move_back_ward_when_facing_direction_is_south() {
    MarsRover rover = new MarsRover(2, 1, 'S', SOUTH);

    rover.moveBackward();

    assertThat(rover.x).isEqualTo(2);
    assertThat(rover.y).isEqualTo(2);
  }

  @Test
  public void should_move_back_ward_when_facing_direction_is_west() {
    MarsRover rover = new MarsRover(2, 1, 'W', WEST);

    rover.moveBackward();

    assertThat(rover.x).isEqualTo(3);
    assertThat(rover.y).isEqualTo(1);
  }

  @Test
  public void should_move_back_ward_when_facing_direction_is_north() {
    MarsRover rover = new MarsRover(2, 1, 'N', null);

    rover.moveBackward();

    assertThat(rover.x).isEqualTo(2);
    assertThat(rover.y).isEqualTo(0);
  }

  @Test
  public void should_move_back_ward_when_facing_direction_is_east() {
    MarsRover rover = new MarsRover(2, 1, 'E', null);

    rover.moveBackward();

    assertThat(rover.x).isEqualTo(1);
    assertThat(rover.y).isEqualTo(1);
  }

}
