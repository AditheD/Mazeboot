package mazebobot.mazeboot;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;

public class MazeApplication extends Application {

    private static final int STEP = 2;

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

        // Print the dimensions of the maze and robot images for debugging purposes.
        System.out.println("Robot width: " + robotImage.getWidth());
        System.out.println("Robot height: " + robotImage.getHeight());

        robotView.setLayoutX(16);
        robotView.setLayoutY(260);

        Pane root = new Pane(mazeView, robotView);
        Maze2 maze2 = new Maze2();

        Tab maze1Tab = new Tab("Maze 1", root);
        Tab maze2Tab = new Tab("Maze 2", maze2);
        maze1Tab.setClosable(false);
        maze2Tab.setClosable(false);

        TabPane tabs = new TabPane(maze1Tab, maze2Tab);
        Scene scene = new Scene(
                tabs,
                Math.max(mazeImage.getWidth(), maze2.getMazeWidth()),
                Math.max(mazeImage.getHeight(), maze2.getMazeHeight()) + 35
        );
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {


            KeyCode code = event.getCode();

            // Start automatic solver on Maze 2
            if (code == KeyCode.S) {

                if (tabs.getSelectionModel().getSelectedItem() == maze2Tab) {
                    maze2.autoSolve();
                }

                event.consume();
                return;
            }

            Direction direction;

            if (code == KeyCode.UP) direction = Direction.UP;
            else if (code == KeyCode.DOWN) direction = Direction.DOWN;
            else if (code == KeyCode.LEFT) direction = Direction.LEFT;
            else if (code == KeyCode.RIGHT) direction = Direction.RIGHT;
            else return;

            if (tabs.getSelectionModel().getSelectedItem() == maze1Tab) {
                move(direction);
            } else {
                maze2.moveCar(direction);
            }
            event.consume();
        });

        //This is for debugging purposes, it will print the coordinates of the mouse click on the maze.
        scene.setOnMouseClicked(event -> {
            System.out.println("X: " + event.getX() + " Y: " + event.getY());
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