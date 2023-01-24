package org.nasa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.nasa.MarsRoverTest.Command.B;
import static org.nasa.MarsRoverTest.Command.F;
import static org.nasa.MarsRoverTest.Command.L;
import static org.nasa.MarsRoverTest.Command.R;
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
    private MarsMap map;
    private Position position;

    public MarsRover(int x, int y, Direction direction, MarsMap map) {
      this.direction = direction;
      this.map = map;
      this.position = new Position(x, y);
    }

    private void moveBackward() {
      move(direction.opposite());
    }

    private void moveForward() {
      move(direction);
    }

    private boolean move(Direction direction) {
      Position nextPosition = determineNextPosition(direction);
      if(map.isObstacle(nextPosition)){
        return false;
      }
      this.position = nextPosition;
      return true;
    }

    private Position determineNextPosition(Direction direction) {
      if (direction == NORTH) {
        return new Position(this.position.x, this.position.y + 1);
      }
      if (direction == SOUTH) {
        return new Position(this.position.x, this.position.y - 1);
      }
      if (direction == WEST) {
        return new Position(this.position.x - 1, this.position.y);
      }
      if (direction == EAST) {
        return new Position(this.position.x + 1, this.position.y);
      }
      throw new IllegalArgumentException(direction + " is not handled");
    }

    private void turnRight() {
      direction = direction.nextOnTheRight();
    }

    private void turnLeft() {
      direction = direction.turnLeft();
    }

    public void executeCommands(Command[] commands) {
      for (Command command : commands) {
        command.execute(this);
      }
    }
  }

  public static class MarsMap {

    int[][] land;

    public MarsMap(int[][] land) {
      this.land = land;
    }

    public static MarsMap withoutObstacles() {
      int[][] map = {
          {0, 0, 0, 0},
          {0, 0, 0, 0},
          {0, 0, 0, 0},
          {0, 0, 0, 0}
      };
      return new MarsMap(map);
    }

    public static MarsMap withObstacles() {
      int[][] map = {
          {0, 0, 0, 1},
          {0, 0, 1, 0},
          {0, 1, 0, 0},
          {1, 0, 0, 0}
      };
      return new MarsMap(map);
    }

    public boolean isObstacle(Position position){
      return this.land[position.y][position.x] == 1;
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

  public enum Command {
    F('F', MarsRover::moveForward),
    B('B', MarsRover::moveBackward),
    L('L', MarsRover::turnLeft),
    R('R', MarsRover::turnRight);

    private char command;
    private RoverAction action;

    Command(char command, RoverAction action) {
      this.command = command;
      this.action = action;
    }

    public void execute(MarsRover marsRover) {
      this.action.execute(marsRover);
    }

    interface RoverAction {

      void execute(MarsRover marsRover);
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
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());
      Command[] commands = {R, F, L, B};

      rover.executeCommands(commands);

      assertThat(rover.position).isEqualTo(new Position(3, 2));
    }
  }

  @Nested
  class MoveLeftScenarios {

    @Test
    void should_turn_to_south_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.turnLeft();

      assertThat(rover.direction).isEqualTo(SOUTH);
    }

    @Test
    void should_turn_to_west_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.turnLeft();

      assertThat(rover.direction).isEqualTo(WEST);
    }

    @Test
    void should_turn_to_north_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.turnLeft();

      assertThat(rover.direction).isEqualTo(NORTH);
    }

    @Test
    void should_turn_to_east_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.turnLeft();

      assertThat(rover.direction).isEqualTo(EAST);
    }
  }

  @Nested
  class MoveRightScenarios {

    @Test
    void should_turn_to_north_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.turnRight();

      assertThat(rover.direction).isEqualTo(NORTH);
    }

    @Test
    void should_turn_to_west_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.turnRight();

      assertThat(rover.direction).isEqualTo(WEST);
    }

    @Test
    void should_turn_to_south_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.turnRight();

      assertThat(rover.direction).isEqualTo(SOUTH);
    }

    @Test
    void should_turn_to_east_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.turnRight();

      assertThat(rover.direction).isEqualTo(EAST);
    }
  }

  @Nested
  class ForwardScenarios {

    @Test
    void should_not_move_when_obstacle() {
      MarsRover rover = new MarsRover(2, 0, EAST, MarsMap.withObstacles());

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(2, 0));
    }

    @Test
    void should_move_south_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(2, 0));
    }

    @Test
    void should_move_west_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(1, 1));
    }

    @Test
    void should_move_east_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(3, 1));
    }

    @Test
    void should_move_north_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(2, 2));
    }
  }

  @Nested
  class BackwardScenarios {

    @Test
    void should_not_move_when_obstacle() {
      MarsRover rover = new MarsRover(1, 3, EAST, MarsMap.withObstacles());

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(1, 3));
    }

    @Test
    public void should_move_north_when_facing_direction_is_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(2, 2));
    }

    @Test
    public void should_move_east_when_facing_direction_is_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(3, 1));
    }

    @Test
    public void should_move_south_when_facing_direction_is_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(2, 0));
    }

    @Test
    public void should_move_east_when_facing_direction_is_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(1, 1));
    }
  }
}
