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

  boolean moveBackward() {
    return move(direction.opposite());
  }

  boolean moveForward() {
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
    if (direction == Direction.NORTH) {
      return new Position(this.position.x, this.position.y + 1);
    }
    if (direction == Direction.SOUTH) {
      return new Position(this.position.x, this.position.y - 1);
    }
    if (direction == Direction.WEST) {
      return new Position(this.position.x - 1, this.position.y);
    }
    if (direction == Direction.EAST) {
      return new Position(this.position.x + 1, this.position.y);
    }
    throw new IllegalArgumentException(direction + " is not handled");
  }

  boolean turnRight() {
    direction = direction.nextOnTheRight();
    return true;
  }

  boolean turnLeft() {
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

}
