package org.nasa;

public class MarsMap {

  int[][] land;
  private final int width;
  private final int height;

  public MarsMap(int[][] land, int width, int height) {
    this.land = land;
    this.width = width;
    this.height = height;
  }
  /*
3  0 0 0 0
2  0 0 0 0
1  0 0 0 0
0  0 0 0 0
   0 1 2 3
   */
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
    return this.land[position.y()][position.x()] == 1;
  }

  public boolean outOfMap(Position position) {
    return this.height < position.y() + 1 || this.width < position.x() + 1;
  }
}
