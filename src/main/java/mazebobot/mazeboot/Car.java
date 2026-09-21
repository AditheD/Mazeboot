package mazebobot.mazeboot;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class Car extends Group {
    private Direction direction = Direction.RIGHT;

    public Car() {
        createCar();
    }

    private void createCar() {
        // Purple car body drawn with a polygon.
        Polygon body = new Polygon(-15.0, 4.0, -10.0, -7.0, 3.0, -8.0, 7.0, -2.0, 13.0, -1.0, 15.0, 4.0);
        body.setFill(Color.web("#6426b8"));
        body.setStroke(Color.DARKVIOLET);

        // Green windows drawn with rectangles.
        Rectangle backWindow = new Rectangle(-8, -6, 5, 4);
        backWindow.setFill(Color.web("#80bb18"));

        Rectangle frontWindow = new Rectangle(-1, -6, 7, 4);
        frontWindow.setFill(Color.web("#80bb18"));

        // Black wheels drawn with ovals.
        Ellipse backWheel = new Ellipse(-9, 2, 4, 4.5);
        backWheel.setFill(Color.BLACK);

        Ellipse frontWheel = new Ellipse(7, 2, 4, 4.5);
        frontWheel.setFill(Color.BLACK);

        getChildren().addAll(body, backWindow, frontWindow, backWheel, frontWheel);

        setMouseTransparent(true);
    }

    public void setPosition(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        setRotate(direction.getAngle());
    }

    public Direction getDirection() {
        return direction;
    }

}
