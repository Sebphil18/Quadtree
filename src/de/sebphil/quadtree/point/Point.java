package de.sebphil.quadtree.point;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point {

    private static final double radius = 2;
    private Point2D position;

    public Point(double x, double y) {
        position = new Point2D(x, y);
    }

    public void draw(GraphicsContext gc) {

        double x = position.getX() - radius;
        double y = position.getY() - radius;

        gc.setFill(Color.web("#949494"));
        gc.fillOval(x, y, radius * 2, radius * 2);
    }

    public Point2D getPosition() {
        return position;
    }

}
