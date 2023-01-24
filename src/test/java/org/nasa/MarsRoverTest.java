package org.nasa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nasa.MarsRoverTest.Direction.EAST;
import static org.nasa.MarsRoverTest.Direction.NORTH;
import static org.nasa.MarsRoverTest.Direction.SOUTH;
import static org.nasa.MarsRoverTest.Direction.WEST;

import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MarsRoverTest {

  public static class MarsRover {

    private Direction direction;
    private Position position;

    public MarsRover(int x, int y, Direction direction) {
      this.direction = direction;
      this.position = new Position(x, y);
    }

    public void moveBackward() {
      move(direction.opposite());
    }

    public void moveForward() {
      move(direction);
    }

    private void move(Direction direction) {
      if (direction == NORTH) {
        this.position.y++;
      }
      if (direction == SOUTH) {
        this.position.y--;
      }
      if (direction == WEST) {
        this.position.x--;
      }
      if (direction == EAST) {
        this.position.x++;
      }
    }

    public void turnRight() {
      direction = direction.nextOnTheRight();
    }

    public void turnLeft() {
      direction = direction.turnLeft();
    }

    public void executeCommands(char[] commands) {

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
    NORTH('N', 0), EAST('E', 25), SOUTH('S', 50), WEST('W', 75);

    private char direction;
    private int angle;

    Direction(char direction, int angle) {
      this.direction = direction;
      this.angle = angle;
    }

    public Direction opposite() {
      int nextAngleOffset = calculateNextAngleOffset(calculateNextAngleOffset(angle));
      return fromAngle(nextAngleOffset);
    }

    public Direction nextOnTheRight() {
      int nextAngleOffset = calculateNextAngleOffset(angle);
      return fromAngle(nextAngleOffset);
    }

    public Direction turnLeft() {
      int previousAngle = calculatePreviousAngleOffset(angle);
      return fromAngle(previousAngle);
    }

    private int calculatePreviousAngleOffset(int angle) {
      return (angle + 75) % 100;
    }

    private int calculateNextAngleOffset(int angle) {
      return (angle + 25) % 100;
    }

    private static Direction fromAngle(int angle) {
      return Arrays.stream(Direction.values())
          .filter(direction -> direction.angle == angle)
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("unhandled angle with value " + angle));
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
  class CommandsScenarios {

    @Test
    void should_execute_sequence_without_failure() {
      MarsRover rover = new MarsRover(2, 1, WEST);
      char[] commands = {'R', 'F', 'L', 'B'};

      rover.executeCommands(commands);

      assertThat(rover.position).isEqualTo(new Position(3, 2));
    }
  }

  @Nested
  class MoveLeftScenarios {

    @Test
    void should_turn_to_south_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST);

      rover.turnLeft();

      assertThat(rover.direction).isEqualTo(SOUTH);
    }

    @Test
    void should_turn_to_west_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH);

      rover.turnLeft();

      assertThat(rover.direction).isEqualTo(WEST);
    }

    @Test
    void should_turn_to_north_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST);

      rover.turnLeft();

      assertThat(rover.direction).isEqualTo(NORTH);
    }

    @Test
    void should_turn_to_east_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH);

      rover.turnLeft();

      assertThat(rover.direction).isEqualTo(EAST);
    }
  }

  @Nested
  class MoveRightScenarios {

    @Test
    void should_turn_to_north_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST);

      rover.turnRight();

      assertThat(rover.direction).isEqualTo(NORTH);
    }

    @Test
    void should_turn_to_west_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH);

      rover.turnRight();

      assertThat(rover.direction).isEqualTo(WEST);
    }

    @Test
    void should_turn_to_south_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST);

      rover.turnRight();

      assertThat(rover.direction).isEqualTo(SOUTH);
    }

    @Test
    void should_turn_to_east_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH);

      rover.turnRight();

      assertThat(rover.direction).isEqualTo(EAST);
    }
  }

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

    @Test
    void should_move_east_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST);

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(3, 1));
    }

    @Test
    void should_move_north_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH);

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(2, 2));
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
