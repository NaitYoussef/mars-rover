package org.nasa;

import java.util.Arrays;

public enum Compass {
  // TODO maybe direction can point as to the next position instead of rover calculating it
  NORTH('N', 0, Position::incY),
  EAST('E', Compass.HALF_PI_IN_DEGREES, Position::incX),
  SOUTH('S', Compass.PI_IN_DEGREES, Position::decY),
  WEST('W', Compass.HALF_PI_IN_DEGREES * 3, Position::decX);

  private static final int PI_IN_DEGREES = 180;
  private static final int HALF_PI_IN_DEGREES = 90;
  private final char direction;
  private final int angle;
  private final PositionNavigator positionNavigator;

  Compass(char direction, int angle, PositionNavigator positionNavigator) {
    this.direction = direction;
    this.angle = angle;
    this.positionNavigator = positionNavigator;
  }

  public Compass opposite() {
    int nextAngleOffset = calculateNextAngleOffset(calculateNextAngleOffset(angle));
    return fromAngle(nextAngleOffset);
  }

  public Compass nextOnTheRight() {
    int nextAngleOffset = calculateNextAngleOffset(angle);
    return fromAngle(nextAngleOffset);
  }

  public Compass nextOnTheLeft() {
    int previousAngle = calculatePreviousAngleOffset(angle);
    return fromAngle(previousAngle);
  }

  public Position determineNextPosition(Position position) {
    return this.positionNavigator.calculateNext(position);
  }

  private static Compass fromAngle(int angle) {
    return Arrays.stream(Compass.values())
        .filter(compass -> compass.angle == angle)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("unhandled angle with value " + angle));
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

  interface PositionNavigator {

    Position calculateNext(Position position);
  }
}
