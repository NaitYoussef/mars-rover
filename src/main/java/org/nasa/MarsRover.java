package org.nasa;

import java.util.Optional;

public class MarsRover {

  private Compass compass;
  private final MarsMap map;
  private Position position;
  private Position failurePosition;

  public MarsRover(int x, int y, Compass compass, MarsMap map) {
    this.compass = compass;
    this.map = map;
    this.position = new Position(x, y);
    this.failurePosition = null;
  }

  private boolean moveBackward() {
    return move(compass.opposite());
  }

  private boolean moveForward() {
    return move(compass);
  }

  private boolean move(Compass compass) {
    Position nextPosition = determineNextPosition(compass);
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

  private Position determineNextPosition(Compass compass) {
    return compass.determineNextPosition(this.position);
  }

  private boolean turnRight() {
    compass = compass.nextOnTheRight();
    return true;
  }

  private boolean turnLeft() {
    compass = compass.nextOnTheLeft();
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

  public Compass currentDirection() {
    return compass;
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
