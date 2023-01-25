package org.nasa;

import java.util.Optional;

public class MarsRover {

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
    if (map.outOfMap(nextPosition)) {
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
    if (facingNorth(direction)) {
      return this.position.incY();
    }
    if (facingSouth(direction)) {
      return this.position.decY();
    }
    if (facingWest(direction)) {
      return this.position.decX();
    }
    if (facingEast(direction)) {
      return this.position.incX();
    }
    throw new IllegalArgumentException(direction + " is not handled");
  }

  private static boolean facingEast(Direction direction) {
    return direction == Direction.EAST;
  }

  private static boolean facingWest(Direction direction) {
    return direction == Direction.WEST;
  }

  private static boolean facingSouth(Direction direction) {
    return direction == Direction.SOUTH;
  }

  private static boolean facingNorth(Direction direction) {
    return direction == Direction.NORTH;
  }

  private boolean turnRight() {
    direction = direction.nextOnTheRight();
    return true;
  }

  private boolean turnLeft() {
    direction = direction.nextOnTheLeft();
    return true;
  }

  public void executeCommands(Command... commands) {
    for (Command command : commands) {
      if (hasExecutionFailed(command, this)) {
        break;
      }
    }
  }

  public Position currentPosition() {
    return position;
  }

  public Direction currentDirection() {
    return direction;
  }

  private static boolean hasExecutionFailed(Command command, MarsRover marsRover) {
    return !command.execute(marsRover);
  }

  public Optional<Position> failureReportPosition() {
    return Optional.ofNullable(failurePosition);
  }

  public enum Command {
    F('F', MarsRover::moveForward),
    B('B', MarsRover::moveBackward),
    L('L', MarsRover::turnLeft),
    R('R', MarsRover::turnRight);

    private final char command;
    private final RoverAction action;

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
}
