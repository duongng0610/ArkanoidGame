package controller;

import game.GameManager;
import javafx.geometry.Bounds;
import objects.*;
import util.Constants;

import java.util.Iterator;
import java.util.List;

public class CollisionHandler {
    private GameManager gameManager;

    public CollisionHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void checkCollisions(Ball ball, Paddle paddle, List<Brick> bricks, List<PowerUp> powerUps) {
        checkWallCollision(ball);
        checkPaddleCollision(ball, paddle);
        checkBrickCollisions(ball, bricks);
        checkPowerUpCollision(paddle, ball, powerUps);
    }

    // Wall <-> Ball
    private void checkWallCollision(Ball ball) {
        // Left/Right
        if (ball.getX() <= 0 || ball.getX() + ball.getWidth() >= Constants.SCREEN_WIDTH) {
            ball.bounceX();
        }
        // Up
        if (ball.getY() <= 0) {
            ball.bounceY();
        }
        // Bottom
        if (ball.getY() + ball.getHeight() >= Constants.SCREEN_HEIGHT) {
            gameManager.loseLife();
        }
    }

    // Ball <-> Paddle
    private void checkPaddleCollision(Ball ball, Paddle paddle) {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.setY(paddle.getY() - ball.getHeight());
            ball.bounceY();
        }
    }

    // Brick <-> Ball
    private void checkBrickCollisions(Ball ball, List<Brick> bricks) {
        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();) {
            Brick brick = iterator.next(); // iterator ban dau tro vao ngay truoc first
            if (brick.isDestroyed()) {
                continue;
            }

            if (ball.getBounds().intersects(brick.getBounds())) {

                // ball bounce
                handleBounce(ball, brick);

                if (brick instanceof UnbreakableBrick) {
                    continue;
                } else {
                    brick.takeHit();

                    if (brick.isDestroyed()) {
                        gameManager.increaseScore(brick.getScore());
                        gameManager.spawnPowerUp(brick);
                    }

                    if (brick instanceof ExplosiveBrick) {
                        explode(brick, bricks);
                    }
                }
                return;
            }
        }
    }

    private void handleBounce(Ball ball, Brick brick) {
        Bounds ballBounds = ball.getBounds();
        Bounds brickBounds = brick.getBounds();

        // previous bounds
        double dx = ball.getDx();
        double dy = ball.getDy();

        double prevMaxX = ballBounds.getMaxX() - dx;
        double prevMinX = ballBounds.getMinX() - dx;
        double prevMaxY = ballBounds.getMaxY() - dy;
        double prevMinY = ballBounds.getMinY() - dy;

        // go from top condition
        boolean fromTop = false;
        if (dy > 0 && prevMaxY <= brickBounds.getMinY()) {
            fromTop = true;
        }

        // go from bottom condition
        boolean fromBottom = false;
        if (dy < 0 && prevMinY >= brickBounds.getMaxY()) {
            fromBottom = true;
        }

        // go from left condition
        boolean fromLeft = false;
        if (dx > 0 && prevMaxX <= brickBounds.getMinX()) {
            fromLeft = true;
        }

        // go from right condition
        boolean fromRight = false;
        if (dx < 0 && prevMinX >= brickBounds.getMaxX()) {
            fromRight = true;
        }

        // top / bottom
        if (fromTop || fromBottom) {
            ball.bounceY();

            // left / right
        } else if (fromLeft || fromRight) {
            ball.bounceX();

            // corner
        } else {
            ball.bounceX();
            ball.bounceY();
        }
    }

    private void explode(Brick explosiveBrick, List<Brick> bricks) {
        double explositionRadius = 100.00; // ban kinh vu no
        double explosiveCenterX = explosiveBrick.getX() + explosiveBrick.getWidth() / 2;
        double explosiveCenterY = explosiveBrick.getY() + explosiveBrick.getHeight() / 2;

        for (Brick other : bricks) {
            if (other.isDestroyed() || other == explosiveBrick) {
                continue;
            }

            double otherCenterX = other.getX() + other.getWidth() / 2;
            double otherCenterY = other.getY() + other.getHeight() / 2;

            double distance = Math.hypot(explosiveCenterX - otherCenterX,
                    explosiveCenterY - otherCenterY);

            if (distance <= explositionRadius && !(other instanceof UnbreakableBrick)) {
                if (!other.isDestroyed()) {
                    gameManager.increaseScore(other.getScore());
                }
                other.setDestroyed(true);
            }
        }
    }

    // Paddle <-> PowerUp
    private void checkPowerUpCollision(Paddle paddle,Ball ball, List<PowerUp> powerUps) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isCollected()) {
                continue;
            }

            // only top collision
            if(paddle.getBounds().intersects(powerUp.getBounds())) {
                powerUp.applyEffect(paddle, ball);
                powerUp.collect();
            }
        }
    }
}
