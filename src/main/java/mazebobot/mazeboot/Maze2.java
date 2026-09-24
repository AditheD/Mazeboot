package mazebobot.mazeboot;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.*;

public class Maze2 extends Pane {
    private static final int STEP = 2;

    private final Image mazeImage;
    private final PixelReader pixelReader;
    private final Car car;

    private Timeline solverAnimation;

    public Maze2() {
        mazeImage = new Image(
                getClass().getResourceAsStream("/images/maze2.png")
        );
        pixelReader = mazeImage.getPixelReader();

        ImageView mazeView = new ImageView(mazeImage);
        car = new Car();
        car.setPosition(35, 34);

        getChildren().addAll(mazeView, car);
    }

    public double getMazeWidth() {
        return mazeImage.getWidth();
    }

    public double getMazeHeight() {
        return mazeImage.getHeight();
    }

    public void moveCar(Direction direction) {
        double oldX = car.getLayoutX();
        double oldY = car.getLayoutY();
        Direction oldDirection = car.getDirection();

        car.setDirection(direction);
        car.setPosition(
                oldX + direction.getChangeX() * STEP,
                oldY + direction.getChangeY() * STEP
        );

        if (!isPathClear(car.getBoundsInParent())) {
            car.setPosition(oldX, oldY);
            car.setDirection(oldDirection);
        }
    }

    private boolean isPathClear(Bounds bounds) {
        // Check the area occupied by the car against the maze pixels.
        for (int y = (int) Math.floor(bounds.getMinY());
             y <= (int) Math.ceil(bounds.getMaxY()); y++) {
            for (int x = (int) Math.floor(bounds.getMinX());
                 x <= (int) Math.ceil(bounds.getMaxX()); x++) {

                if (x < 0 || y < 0
                        || x >= mazeImage.getWidth()
                        || y >= mazeImage.getHeight()) {
                    return false;
                }

                Color color = pixelReader.getColor(x, y);
                boolean blueWall = color.getBlue() > 0.7
                        && color.getRed() < 0.2
                        && color.getGreen() < 0.6;

                if (blueWall) {
                    return false;
                }
            }
        }

        return true;
    }

    //automation
    public void autoSolve() {

        if (solverAnimation != null) {
            solverAnimation.stop();
        }

        List<Direction> solution = findSolution();

        if (solution == null) {
            System.out.println("No solution found");
            return;
        }

        System.out.println(
                "Solution found! Number of moves: " + solution.size()
        );

        playSolution(solution);
    }


    private List<Direction> findSolution() {

        int startX = (int) car.getLayoutX();
        int startY = (int) car.getLayoutY();

        // Destination in maze2.png
        int goalX = 446;
        int goalY = 329;

        Point start = new Point(startX, startY);
        Point goal = new Point(goalX, goalY);

        Queue<Point> queue = new LinkedList<>();

        Map<Point, Point> previous = new HashMap<>();
        Map<Point, Direction> directionUsed = new HashMap<>();

        queue.add(start);
        previous.put(start, null);

        Direction[] directions = {
                Direction.UP,
                Direction.DOWN,
                Direction.LEFT,
                Direction.RIGHT
        };

        Point finalPoint = null;


        // =========================
        // SEARCH THE MAZE
        // =========================

        while (!queue.isEmpty()) {

            Point current = queue.remove();

            // Check if we reached the goal
            if (Math.abs(current.x - goal.x) <= STEP
                    && Math.abs(current.y - goal.y) <= STEP) {

                finalPoint = current;
                break;
            }


            // Try all four directions
            for (Direction direction : directions) {

                Point next = new Point(
                        current.x + direction.getChangeX() * STEP,
                        current.y + direction.getChangeY() * STEP
                );

                // Already visited?
                if (previous.containsKey(next)) {
                    continue;
                }

                // Is there a wall?
                if (!canMoveTo(next.x, next.y)) {
                    continue;
                }

                // Remember where we came from
                previous.put(next, current);
                directionUsed.put(next, direction);

                queue.add(next);
            }
        }




        // if no solution
        if (finalPoint == null) {
            return null;
        }



        LinkedList<Direction> path = new LinkedList<>();

        Point current = finalPoint;

        while (!current.equals(start)) {

            Direction direction = directionUsed.get(current);

            path.addFirst(direction);

            current = previous.get(current);
        }

        return path;
    }


    private boolean canMoveTo(int x, int y) {

        Bounds currentBounds = car.getBoundsInParent();

        double currentX = car.getLayoutX();
        double currentY = car.getLayoutY();

        double offsetX = x - currentX;
        double offsetY = y - currentY;

        Bounds testBounds = new javafx.geometry.BoundingBox(
                currentBounds.getMinX() + offsetX,
                currentBounds.getMinY() + offsetY,
                currentBounds.getWidth(),
                currentBounds.getHeight()
        );

        return isPathClear(testBounds);
    }




    //play Solution
    private void playSolution(List<Direction> solution) {

        final int[] index = {0};

        solverAnimation = new Timeline(
                new KeyFrame(
                        Duration.millis(10),
                        event -> {

                            if (index[0] >= solution.size()) {

                                solverAnimation.stop();

                                System.out.println("Maze solved!");

                                return;
                            }

                            Direction direction =
                                    solution.get(index[0]);

                            moveCar(direction);

                            index[0]++;
                        }
                )
        );

        solverAnimation.setCycleCount(
                Timeline.INDEFINITE
        );

        solverAnimation.play();
    }


    private static class Point {

        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Point)) {
                return false;
            }

            Point other = (Point) obj;

            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}