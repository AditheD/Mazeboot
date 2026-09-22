package mazebobot.mazeboot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MazeApplication extends Application {

    private static final int STEP = 5;

    private ImageView mazeView;
    private ImageView robotView;
    private PixelReader mazeReader;

    @Override
    public void start(Stage stage) {
        Image mazeImage = new Image(getClass().getResourceAsStream("/images/maze.png"));
        mazeView = new ImageView(mazeImage);
        mazeReader = mazeImage.getPixelReader();

        Image robotImage = new Image(getClass().getResourceAsStream("/images/robot.png"));
        robotView = new ImageView(robotImage);
        robotView.setLayoutX(16);
        robotView.setLayoutY(260);

        Pane root = new Pane(mazeView, robotView);
        Scene scene = new Scene(root, mazeImage.getWidth(), mazeImage.getHeight());

        //This is for debugging purposes, it will print the coordinates of the mouse click on the maze.
        scene.setOnMouseClicked(event -> {
            System.out.println("X: " + event.getX() + " Y: " + event.getY());
        });

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.UP) move(Direction.UP);
            else if (code == KeyCode.DOWN) move(Direction.DOWN);
            else if (code == KeyCode.LEFT) move(Direction.LEFT);
            else if (code == KeyCode.RIGHT) move(Direction.RIGHT);
        });

        stage.setScene(scene);
        stage.setTitle("Maze Robot");
        stage.show();
        root.requestFocus();
    }

    private boolean isPathClear(double x, double y) {
        double width = robotView.getImage().getWidth();
        double height = robotView.getImage().getHeight();

        double[][] corners = {
                {x, y},
                {x + width - 1, y},
                {x, y + height - 1},
                {x + width - 1, y + height - 1}
        };

        for (double[] corner : corners) {
            int px = (int) corner[0];
            int py = (int) corner[1];

            if (px < 0 || py < 0 || px >= mazeView.getImage().getWidth() || py >= mazeView.getImage().getHeight()) {
                return false;
            }
            if (!isPath(mazeReader.getColor(px, py))) {
                return false;
            }
        }
        return true;
    }

    private boolean isPath(javafx.scene.paint.Color c) {
        return c.getRed() > 0.9 && c.getGreen() > 0.9 && c.getBlue() > 0.9;
    }

    private void move(Direction direction) {
        double newX = robotView.getLayoutX() + direction.getChangeX() * STEP;
        double newY = robotView.getLayoutY() + direction.getChangeY() * STEP;

        if (isPathClear(newX, newY)) {
            robotView.setLayoutX(newX);
            robotView.setLayoutY(newY);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}