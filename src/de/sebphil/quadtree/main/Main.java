package de.sebphil.quadtree.main;

import de.sebphil.quadtree.point.Point;
import de.sebphil.quadtree.tree.QuadTree;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 800);

        stage.setTitle("Quadtree Demo");
        stage.setScene(scene);
        stage.show();

        Canvas canvas = new Canvas(root.getWidth(), root.getHeight());
        root.setCenter(canvas);

        root.widthProperty().addListener(l -> canvas.setWidth(root.getWidth()) );
        root.heightProperty().addListener(l -> canvas.setHeight(root.getHeight()) );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        List<Point> points = generatePoints(100);

        AnimationTimer timer = getTimer(root, gc, points);
        timer.start();
    }

    private List<Point> generatePoints(int amount) {

        List<Point> points = new ArrayList<>(amount);

        for(int i = 0; i < amount; i++) {
            Point p = getRandomPoint();
            points.add(p);
        }

        return points;
    }

    private Point getRandomPoint() {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        double x = random.nextDouble(0, 800);
        double y = random.nextDouble(0, 800);

        return new Point(x, y);
    }

    private AnimationTimer getTimer(Pane root, GraphicsContext gc, List<Point> points) {

        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                update(root, gc, points);
            }
        };
    }

    private void update(Pane root, GraphicsContext gc, List<Point> points) {

        clearCanvas(gc, root.getWidth(), root.getHeight());

        QuadTree tree = generateTree(points);
        drawPoints(points, gc);
        tree.draw(gc);
    }

    private void clearCanvas(GraphicsContext gc, double width, double height) {

        gc.setFill(Color.web("#1c1c1c"));
        gc.fillRect(0, 0, width, height);
    }

    private QuadTree generateTree(List<Point> points) {

        QuadTree tree = new QuadTree(0, 0, 800, 800);

        for(Point p : points)
            tree.insert(p);

       return tree;
    }

    private void drawPoints(List<Point> points, GraphicsContext gc) {
        for(Point p : points)
            p.draw(gc);
    }

}