package org.nasa;

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
