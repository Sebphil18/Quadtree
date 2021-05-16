package de.sebphil.quadtree.tree;

import de.sebphil.quadtree.point.Point;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {

    private static final long maxCapacity = 1;

    private final BoundingBox bounds;
    public QuadTree[] children;
    private final List<Point> points;

    public QuadTree(double x1, double y1, double x2, double y2) {

        children = new QuadTree[4];
        points = new ArrayList<>();

        double width = x2 - x1;
        double height = y2 - y1;

        bounds = new BoundingBox(x1, y1, width, height);
    }

    public boolean insert(Point p) {

        if(!pointIsInside(p))
            return false;

        if(points.size() < maxCapacity && isLeaf()) {
            points.add(p);
            return true;
        }

        if(isLeaf())
            subdivide();

        return insertIntoChildren(p);
    }

    public boolean pointIsInside(Point point) {
        return bounds.contains(point.getPosition());
    }

    public void subdivide() {

        Point2D upperLeft = getUpperLeft();
        Point2D lowerRight = getLowerRight();
        Point2D middlePoint = getMiddlePoint();

        // upper left, right; lower left, right
        children[0] = new QuadTree(upperLeft.getX(), upperLeft.getY(), middlePoint.getX(), middlePoint.getY());
        children[1] = new QuadTree(middlePoint.getX(), upperLeft.getY(), lowerRight.getX(), middlePoint.getY());
        children[2] = new QuadTree(upperLeft.getX(), middlePoint.getY(), middlePoint.getX(), lowerRight.getY());
        children[3] = new QuadTree(middlePoint.getX(), middlePoint.getY(), lowerRight.getX(), lowerRight.getY());

        copyPointsToChildren();
    }

    private Point2D getUpperLeft() {
        return new Point2D(
                bounds.getMinX(),
                bounds.getMinY()
        );
    }

    private Point2D getLowerRight() {
        return new Point2D(
                bounds.getMaxX(),
                bounds.getMaxY()
        );
    }

    private Point2D getMiddlePoint() {
        return new Point2D(
                bounds.getMinX() + bounds.getWidth() / 2,
                bounds.getMinY() + bounds.getHeight() / 2
        );
    }

    private void copyPointsToChildren() {

        for(Point p : points) {
            children[0].insert(p);
            children[1].insert(p);
            children[2].insert(p);
            children[3].insert(p);
        }
    }

    private boolean insertIntoChildren(Point p ) {

        if(children[0].insert(p)) return true;
        if (children[1].insert(p)) return true;
        if (children[2].insert(p)) return true;
        if (children[3].insert(p)) return true;

        return false;
    }

    public void draw(GraphicsContext gc) {

        if(isLeaf())
            drawCell(gc);
        else
            drawChildren(gc);
    }

    public boolean isLeaf() {
        return children[0] == null;
    }

    private void drawCell(GraphicsContext gc) {

        double width = bounds.getWidth();
        double height = bounds.getHeight();

        gc.setStroke(Color.web("#d99b00"));
        gc.strokeRect(bounds.getMinX(), bounds.getMinY(), width, height);
    }

    private void drawChildren(GraphicsContext gc) {

        for(QuadTree child : children)
            child.draw(gc);
    }

}
