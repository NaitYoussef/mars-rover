package org.nasa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nasa.MarsRover.Command.B;
import static org.nasa.MarsRover.Command.F;
import static org.nasa.MarsRover.Command.L;
import static org.nasa.MarsRover.Command.R;
import static org.nasa.Direction.EAST;
import static org.nasa.Direction.NORTH;
import static org.nasa.Direction.SOUTH;
import static org.nasa.Direction.WEST;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MarsRoverTest {

  /*  N

    3 * * * *
    2 * * . *
W   1 * . X .   E
    0 * * . *
      0 1 2 3

        S
   */

  @Nested
  class CommandsScenarios {

    @Test
    void should_execute_sequence_without_failure() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.executeCommands(R, F, L, B);

      assertThat(rover.currentPosition()).isEqualTo(new Position(3, 2));
    }

    @Test
    void should_execute_sequence_with_failure() {
      MarsRover rover = new MarsRover(1, 0, EAST, MarsMap.withObstacles());

      rover.executeCommands(F, R, B, B);

      assertThat(rover.currentPosition()).isEqualTo(new Position(2, 1));
      assertThat(rover.failureReportPosition()).contains(new Position(2, 2));
    }

    @Test
    void should_stop_on_edge() {
      MarsRover rover = new MarsRover(3, 0, EAST, MarsMap.withoutObstacles());

      rover.executeCommands(F, F, B);

      assertThat(rover.currentPosition()).isEqualTo(new Position(2, 0));
      assertThat(rover.failureReportPosition()).isEmpty();
    }

    @Test
    void should_spot_executing_sequence_on_obstacle() {
      MarsRover rover = new MarsRover(1, 0, EAST, MarsMap.withObstacles());

      rover.executeCommands(B, F, F, F, F, F, F);

      assertThat(rover.currentPosition()).isEqualTo(new Position(1, 0));
      assertThat(rover.failureReportPosition()).contains(new Position(0, 0));
    }
  }

  @Nested
  class MoveLeftScenarios {

    @Test
    void should_turn_to_south_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.executeCommands(L);

      assertThat(rover.currentDirection()).isEqualTo(SOUTH);
    }

    @Test
    void should_turn_to_west_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.executeCommands(L);

      assertThat(rover.currentDirection()).isEqualTo(WEST);
    }

    @Test
    void should_turn_to_north_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.executeCommands(L);

      assertThat(rover.currentDirection()).isEqualTo(NORTH);
    }

    @Test
    void should_turn_to_east_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.executeCommands(L);

      assertThat(rover.currentDirection()).isEqualTo(EAST);
    }
  }

  @Nested
  class MoveRightScenarios {

    @Test
    void should_turn_to_north_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.executeCommands(R);

      assertThat(rover.currentDirection()).isEqualTo(NORTH);
    }

    @Test
    void should_turn_to_west_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.executeCommands(R);

      assertThat(rover.currentDirection()).isEqualTo(WEST);
    }

    @Test
    void should_turn_to_south_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.executeCommands(R);

      assertThat(rover.currentDirection()).isEqualTo(SOUTH);
    }

    @Test
    void should_turn_to_east_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.executeCommands(R);

      assertThat(rover.currentDirection()).isEqualTo(EAST);
    }
  }

  @Nested
  class ForwardScenarios {

    @Test
    void should_not_move_when_obstacle() {
      MarsRover rover = new MarsRover(1, 0, WEST, MarsMap.withObstacles());

      rover.executeCommands(F);

      assertThat(rover.currentPosition()).isEqualTo(new Position(1, 0));
      assertThat(rover.failureReportPosition()).contains(new Position(0, 0));
    }

    @Test
    void should_move_south_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.executeCommands(F);

      assertThat(rover.currentPosition()).isEqualTo(new Position(2, 0));
    }

    @Test
    void should_move_west_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.executeCommands(F);

      assertThat(rover.currentPosition()).isEqualTo(new Position(1, 1));
    }

    @Test
    void should_move_east_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.executeCommands(F);

      assertThat(rover.currentPosition()).isEqualTo(new Position(3, 1));
    }

    @Test
    void should_move_north_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.executeCommands(F);

      assertThat(rover.currentPosition()).isEqualTo(new Position(2, 2));
    }
  }

  @Nested
  class BackwardScenarios {

    @Test
    void should_not_move_when_obstacle() {
      MarsRover rover = new MarsRover(3, 2, EAST, MarsMap.withObstacles());

      rover.executeCommands(B);

      assertThat(rover.currentPosition()).isEqualTo(new Position(3, 2));
      assertThat(rover.failureReportPosition()).contains(new Position(2, 2));
    }

    @Test
    public void should_move_north_when_facing_direction_is_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.executeCommands(B);

      assertThat(rover.currentPosition()).isEqualTo(new Position(2, 2));
    }

    @Test
    public void should_move_east_when_facing_direction_is_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.executeCommands(B);

      assertThat(rover.currentPosition()).isEqualTo(new Position(3, 1));
    }

    @Test
    public void should_move_south_when_facing_direction_is_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.executeCommands(B);

      assertThat(rover.currentPosition()).isEqualTo(new Position(2, 0));
    }

    @Test
    public void should_move_east_when_facing_direction_is_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.executeCommands(B);

      assertThat(rover.currentPosition()).isEqualTo(new Position(1, 1));
    }
  }
}
