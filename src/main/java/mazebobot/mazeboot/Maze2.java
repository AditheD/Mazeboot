package mazebobot.mazeboot;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Maze2 extends Pane {
    private static final int STEP = 2;

    private final Image mazeImage;
    private final PixelReader pixelReader;
    private final Car car;

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
}