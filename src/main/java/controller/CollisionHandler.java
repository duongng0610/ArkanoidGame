package controller;

import game.GameManager;
import javafx.geometry.Bounds;
import objects.Ball;
import objects.Brick;
import objects.Paddle;
import objects.PowerUp;
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
        checkPowerUpCollision(paddle, powerUps);
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
                brick.takeHit();

                if (brick.isDestroyed()) {
                    gameManager.increaseScore(brick.getScore());
                    // spawn powerup in brick
                    gameManager.spawnPowerUp(brick);
                }

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
                    if (fromTop) {
                        double newY = brickBounds.getMinY() - ball.getHeight();
                        ball.setY(newY);
                        ball.bounceY();
                    } else {
                        double newY = brickBounds.getMaxY();
                        ball.setY(newY);
                        ball.bounceY();
                    }

                // left / right
                } else if (fromLeft || fromRight) {
                    if (fromLeft) {
                        double newX = brickBounds.getMinX() - ball.getWidth();
                        ball.setX(newX);
                        ball.bounceX();
                    } else {
                        double newX = brickBounds.getMaxX();
                        ball.setX(newX);
                        ball.bounceX();
                    }

                // corner
                } else {
                    double overlapLeft = ballBounds.getMaxX() - brickBounds.getMinX();
                    double overlapRight = brick.getBounds().getMaxX() - ballBounds.getMinX();
                    double overlapTop = ballBounds.getMaxY() - brickBounds.getMinY();
                    double overlapBottom = brickBounds.getMaxY() - ballBounds.getMinY();

                    double minOverlapX = Math.min(overlapLeft, overlapRight);
                    double minOverlapy = Math.min(overlapBottom, overlapTop);

                    if (minOverlapX < minOverlapy) {
                        if (overlapLeft < overlapRight) {
                            double newX = brickBounds.getMinX() - ball.getWidth();
                            ball.setX(newX);
                            ball.bounceX();
                        } else {
                            double newX = brickBounds.getMaxX();
                            ball.setX(newX);
                            ball.bounceX();
                        }
                    } else {
                        if (overlapTop < overlapBottom) {
                            double newY = brickBounds.getMinY() - ball.getHeight();
                            ball.setY(newY);
                            ball.bounceY();
                        } else {
                            double newY = brickBounds.getMaxY();
                            ball.setY(newY);
                            ball.bounceY();
                        }
                    }
                }

                return;
            }
        }
    }

    // Paddle <-> PowerUp
    private void checkPowerUpCollision(Paddle paddle, List<PowerUp> powerUps) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isCollected()) {
                continue;
            }

            // only top collision
            if(paddle.getBounds().intersects(powerUp.getBounds())) {
                powerUp.applyEffect(paddle);
                powerUp.collect();
            }
        }
    }
}
