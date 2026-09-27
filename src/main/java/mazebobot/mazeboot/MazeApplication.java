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
import javafx.scene.control.Button;


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
        Button maze1SolveButton = new Button("Auto Solve");
        maze1SolveButton.setLayoutX(10);
        maze1SolveButton.setLayoutY(mazeImage.getHeight() + 10);
        maze1SolveButton.setOnAction(event -> autoSolveMaze1());
        root.getChildren().add(maze1SolveButton);
        Maze2 maze2 = new Maze2();
        Button autoSolveButton = new Button("Auto Solve");

        autoSolveButton.setOnAction(event -> {
            maze2.autoSolve();
        });

        autoSolveButton.setLayoutX(10);
        autoSolveButton.setLayoutY(maze2.getMazeHeight() + 10);
        maze2.getChildren().add(autoSolveButton);

        Tab maze1Tab = new Tab("Maze 1", root);
        Tab maze2Tab = new Tab("Maze 2", maze2);
        maze1Tab.setClosable(false);
        maze2Tab.setClosable(false);

        TabPane tabs = new TabPane(maze1Tab, maze2Tab);
        Scene scene = new Scene(
                tabs,
                Math.max(mazeImage.getWidth(), maze2.getMazeWidth()),
                Math.max(mazeImage.getHeight(), maze2.getMazeHeight()) + 80
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

    private void autoSolveMaze1() {
        java.util.List<int[]> path = findMaze1Path();

        if (path.isEmpty()) {
            System.out.println("Maze 1: no path to the exit found.");
            return;
        }

        final int[] index = {0};
        final javafx.animation.Timeline[] animation = {null};

        animation[0] = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        javafx.util.Duration.millis(10),
                        event -> {
                            if (index[0] >= path.size()) {
                                animation[0].stop();
                                System.out.println("Maze 1 solved!");
                                return;
                            }

                            int[] position = path.get(index[0]++);
                            robotView.setLayoutX(position[0]);
                            robotView.setLayoutY(position[1]);
                        }
                )
        );

        animation[0].setCycleCount(javafx.animation.Timeline.INDEFINITE);
        animation[0].play();
    }

    private java.util.List<int[]> findMaze1Path() {

        robotView.setLayoutX(16);
        robotView.setLayoutY(260);

        int width = (int) mazeView.getImage().getWidth();
        int height = (int) mazeView.getImage().getHeight();
        int robotWidth = (int) robotView.getImage().getWidth();

        int startX = (int) robotView.getLayoutX();
        int startY = (int) robotView.getLayoutY();
        int start = startY * width + startX;

        int[] previous = new int[width * height];
        java.util.Arrays.fill(previous, -1);

        java.util.ArrayDeque<Integer> queue = new java.util.ArrayDeque<>();
        queue.add(start);
        previous[start] = start;

        int exit = -1;
        int[][] moves = {{2, 0}, {-2, 0}, {0, 2}, {0, -2}};

        while (!queue.isEmpty()) {
            int current = queue.remove();
            int x = current % width;
            int y = current / width;

            // Maze 1's exit is at the right side of the image.
            if (x + robotWidth >= width - 2) {
                exit = current;
                break;
            }

            for (int[] move : moves) {
                int nextX = x + move[0];
                int nextY = y + move[1];

                if (nextX < 0 || nextY < 0
                        || nextX >= width || nextY >= height
                        || !isPathClear(nextX, nextY)) {
                    continue;
                }

                int next = nextY * width + nextX;
                if (previous[next] != -1) {
                    continue;
                }

                previous[next] = current;
                queue.add(next);
            }
        }

        if (exit == -1) {
            return java.util.Collections.emptyList();
        }

        java.util.List<int[]> path = new java.util.ArrayList<>();
        for (int position = exit; position != start; position = previous[position]) {
            path.add(new int[]{position % width, position / width});
        }
        java.util.Collections.reverse(path);
        return path;
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





