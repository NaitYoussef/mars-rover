package org.nasa;

import java.util.Arrays;

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
