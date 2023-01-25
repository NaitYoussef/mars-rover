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
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MarsRoverTest {

  // TODO move these classes to a their packages
  public static class MarsRover {

    private Direction direction;
    private final MarsMap map;
    private Position position;
    private Position failurePosition;

    public MarsRover(int x, int y, Direction direction, MarsMap map) {
      this.direction = direction;
      this.map = map;
      this.position = new Position(x, y);
      this.failurePosition = null;
    }

    private boolean moveBackward() {
      return move(direction.opposite());
    }

    private boolean moveForward() {
      return move(direction);
    }

    private boolean move(Direction direction) {
      Position nextPosition = determineNextPosition(direction);
      if (map.unReachable(nextPosition)) {
        return true;
      }
      if (map.isObstacle(nextPosition)) {
        this.failurePosition = nextPosition;
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

    private boolean turnRight() {
      direction = direction.nextOnTheRight();
      return true;
    }

    private boolean turnLeft() {
      direction = direction.nextOnTheLeft();
      return true;
    }

    public void executeCommands(Command[] commands) {
      for (Command command : commands) {
        if (hasExecutionFailed(command, this)) {
          break;
        }
      }
    }

    private static boolean hasExecutionFailed(Command command, MarsRover marsRover) {
      return !command.execute(marsRover);
    }

    public Optional<Position> failureReportPosition() {
      return Optional.ofNullable(failurePosition);
    }

  }

  public static class MarsMap {

    int[][] land;
    private final int width;
    private final int height;

    public MarsMap(int[][] land, int width, int height) {
      this.land = land;
      this.width = width;
      this.height = height;
    }

    public static MarsMap withoutObstacles() {
      int[][] map = {
          {0, 0, 0, 0},
          {0, 0, 0, 0},
          {0, 0, 0, 0},
          {0, 0, 0, 0}
      };
      return new MarsMap(map, 4, 4);
    }

    /*
  3  0 0 0 1
  2  0 0 1 0
  1  0 1 0 0
  0  1 0 0 0
     0 1 2 3
     */
    public static MarsMap withObstacles() {
      int[][] map = {
          {1, 0, 0, 0},
          {0, 1, 0, 0},
          {0, 0, 1, 0},
          {0, 0, 0, 1}
      };
      return new MarsMap(map, 4, 4);
    }

    public boolean isObstacle(Position position) {
      return this.land[position.y][position.x] == 1;
    }

    public boolean unReachable(Position position) {
      return this.height < position.y + 1 || this.width < position.x + 1;
    }
  }

  public static class Position {

    private final int x;
    private final int y;

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
    NORTH('N', 0),
    EAST('E', Direction.HALF_PI_IN_DEGREES),
    SOUTH('S', Direction.PI_IN_DEGREES),
    WEST('W', Direction.HALF_PI_IN_DEGREES * 3);

    private static final int PI_IN_DEGREES = 180;
    private static final int HALF_PI_IN_DEGREES = 90;
    private final char direction;
    private final int angle;

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

    public Direction nextOnTheLeft() {
      int previousAngle = calculatePreviousAngleOffset(angle);
      return fromAngle(previousAngle);
    }

    private int calculatePreviousAngleOffset(int angle) {
      int treeHalvesOfPie = HALF_PI_IN_DEGREES * 3;
      int doublePi = 2 * PI_IN_DEGREES;
      return (angle + treeHalvesOfPie) % doublePi;
    }

    private int calculateNextAngleOffset(int angle) {
      int doublePi = 2 * PI_IN_DEGREES;
      return (angle + HALF_PI_IN_DEGREES) % doublePi;
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

    public boolean execute(MarsRover marsRover) {
      return this.action.execute(marsRover);
    }

    interface RoverAction {

      boolean execute(MarsRover marsRover);
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

    @Test
    void should_execute_sequence_with_failure() {
      MarsRover rover = new MarsRover(1, 0, EAST, MarsMap.withObstacles());
      Command[] commands = {F, R, B, B};

      rover.executeCommands(commands);

      assertThat(rover.position).isEqualTo(new Position(2, 1));
      assertThat(rover.failureReportPosition()).contains(new Position(2, 2));
    }

    @Test
    void should_stop_on_edge() {
      MarsRover rover = new MarsRover(3, 0, EAST, MarsMap.withoutObstacles());
      Command[] commands = {F, F, B};

      rover.executeCommands(commands);

      assertThat(rover.position).isEqualTo(new Position(2, 0));
      assertThat(rover.failureReportPosition()).isEmpty();
    }

    @Test
    void should_spot_executing_sequence_on_obstacle() {
      MarsRover rover = new MarsRover(1, 0, EAST, MarsMap.withObstacles());
      Command[] commands = {B, F, F, F, F, F, F};

      rover.executeCommands(commands);

      assertThat(rover.position).isEqualTo(new Position(1, 0));
      assertThat(rover.failureReportPosition()).contains(new Position(0, 0));
    }
  }

  @Nested
  class MoveLeftScenarios {

    private final Command[] moveLeft = {L};

    @Test
    void should_turn_to_south_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.executeCommands(moveLeft);

      assertThat(rover.direction).isEqualTo(SOUTH);
    }

    @Test
    void should_turn_to_west_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.executeCommands(moveLeft);

      assertThat(rover.direction).isEqualTo(WEST);
    }

    @Test
    void should_turn_to_north_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.executeCommands(moveLeft);

      assertThat(rover.direction).isEqualTo(NORTH);
    }

    @Test
    void should_turn_to_east_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.executeCommands(moveLeft);

      assertThat(rover.direction).isEqualTo(EAST);
    }
  }

  @Nested
  class MoveRightScenarios {

    private final Command[] moveRight = {R};

    @Test
    void should_turn_to_north_when_facing_west() {
      MarsRover rover = new MarsRover(2, 1, WEST, MarsMap.withoutObstacles());

      rover.executeCommands(moveRight);

      assertThat(rover.direction).isEqualTo(NORTH);
    }

    @Test
    void should_turn_to_west_when_facing_south() {
      MarsRover rover = new MarsRover(2, 1, SOUTH, MarsMap.withoutObstacles());

      rover.executeCommands(moveRight);

      assertThat(rover.direction).isEqualTo(WEST);
    }

    @Test
    void should_turn_to_south_when_facing_east() {
      MarsRover rover = new MarsRover(2, 1, EAST, MarsMap.withoutObstacles());

      rover.executeCommands(moveRight);

      assertThat(rover.direction).isEqualTo(SOUTH);
    }

    @Test
    void should_turn_to_east_when_facing_north() {
      MarsRover rover = new MarsRover(2, 1, NORTH, MarsMap.withoutObstacles());

      rover.executeCommands(moveRight);

      assertThat(rover.direction).isEqualTo(EAST);
    }
  }

  @Nested
  class ForwardScenarios {

    @Test
    void should_not_move_when_obstacle() {
      MarsRover rover = new MarsRover(1, 0, WEST, MarsMap.withObstacles());

      rover.moveForward();

      assertThat(rover.position).isEqualTo(new Position(1, 0));
      assertThat(rover.failureReportPosition()).contains(new Position(0, 0));
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
      MarsRover rover = new MarsRover(3, 2, EAST, MarsMap.withObstacles());

      rover.moveBackward();

      assertThat(rover.position).isEqualTo(new Position(3, 2));
      assertThat(rover.failureReportPosition()).contains(new Position(2, 2));
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
