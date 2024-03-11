package dev.zenqrt.snakeinminecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class SnakeGame {

    private final int size;
    private final Snake snake;
    private Food food;
    public boolean gameActive;
    public int score = 0;


    public SnakeGame(int size) {
        this.size = size;
        this.snake = new Snake(new Position(size / 2, size / 2), Direction.RIGHT);
        this.food = createFoodAtRandomPosition();
        this.gameActive = true;
    }

    public void tick() {
        snake.move();
        handleFoodCollision();

        if (checkWallCollision() || checkSelfCollision()) {
            gameActive = false;
        }
    }

    private void handleFoodCollision() {
        if (snake.headPosition.equals(food.position)) {
            snake.grow();
            food = createFoodAtRandomPosition();
            score++;
        }
    }

    private boolean checkWallCollision() {
        return snake.headPosition.x() < 0 || snake.headPosition.x() >= size || snake.headPosition.y() < 0 || snake.headPosition.y() >= size;
    }

    private boolean checkSelfCollision() {
        return snake.positions().stream().anyMatch(position -> position != snake.headPosition && position.equals(snake.headPosition));
    }

    private Food createFoodAtRandomPosition() {
        Position position;

        do {
            position = new Position(ThreadLocalRandom.current().nextInt(size), ThreadLocalRandom.current().nextInt(size));
        } while (snake.positions().contains(position));

        return new Food(position);
    }

    public int size() {
        return size;
    }

    public Snake snake() {
        return snake;
    }

    public Food food() {
        return food;
    }



    public static class Snake {

        private Position headPosition;
        private List<Position> positions;
        private Direction direction;
        private Direction pendingDirection;

        public Snake(Position headPosition, Direction direction) {
            this.headPosition = headPosition;
            this.positions = new ArrayList<>(List.of(headPosition));
            this.direction = direction;
            this.pendingDirection = direction;
        }

        public void move() {
            if (pendingDirection != null) {
                direction = pendingDirection;
                pendingDirection = null;
            }

            Position newPosition = new Position(headPosition.x() + direction.x(), headPosition.y() + direction.y());
            headPosition = newPosition;

            positions.add(0, newPosition);
            positions.remove(positions.size() - 1);
        }

        public void turn(Direction direction) {
            if (this.direction == Direction.opposite(direction) || this.direction == direction) {
                return;
            }

            pendingDirection = direction;
        }

        public void grow() {
            positions.add(positions.get(positions.size() - 1));
        }

        public int length() {
            return positions.size();
        }

        public List<Position> positions() {
            return positions;
        }

    }

    public static class Food {

            private Position position;

            public Food(Position position) {
                this.position = position;
            }

            public Position position() {
                return position;
            }
    }

    protected record Position(int x, int y) {}

}
