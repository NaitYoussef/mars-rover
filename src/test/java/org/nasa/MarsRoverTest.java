package org.nasa;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MarsRoverTest {

  public static class MarsRover {

    private int x;
    private int y;
    private char initialDirection;

    public MarsRover(int x, int y, char initialDirection) {
      this.x = x;
      this.y = y;
      this.initialDirection = initialDirection;
    }

    public void moveBackward() {
      x--;
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
  public void should_move_back_ward_when_facing_direction_is_north() {
    MarsRover rover = new MarsRover(2, 1, 'N');

    rover.moveBackward();

    Assertions.assertThat(rover.x).isEqualTo(2);
    Assertions.assertThat(rover.y).isEqualTo(0);
  }

  @Test
  public void should_move_back_ward_when_facing_direction_is_east() {
    MarsRover rover = new MarsRover(2, 1, 'E');

    rover.moveBackward();

    Assertions.assertThat(rover.x).isEqualTo(1);
    Assertions.assertThat(rover.y).isEqualTo(1);
  }

}
