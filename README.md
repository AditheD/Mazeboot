# Maze Robot

A JavaFX application that lets users navigate two mazes manually or watch an automatic solving animation.

## Group 2 Members

- Adithe Das
- Olena Fedochynska
- Shahla Meem
- Christian Pivaral
- Sahithi Attada

## Features

- Two mazes displayed in separate tabs.
- A robot image used in Maze 1.
- A car drawn using polygons, rectangles, and ellipses in Maze 2.
- Arrow key controls for movement.
- Pixel-based collision detection to prevent movement through walls.
- Auto Solve buttons to start the animations.
- Car headings that change with the direction of movement.
- Automatic solving that can be replayed by clicking the button again.

## Technologies

- Java
- JavaFX
- Maven
- Git and GitHub

## Main Classes

- `MazeApplication.java`: Creates the application window, tabs, and Maze 1 controls and animation.
- `Maze2.java`: Handles Maze 2 movement, collision detection, pathfinding, and animation.
- `Car.java`: Draws the car and manages its position and heading.
- `Direction.java`: Defines movement directions and rotation angles.

## How to Run

1. Clone the repository:

   git clone https://github.com/AditheD/Mazeboot.git

2. Open the project in IntelliJ IDEA.
3. Allow Maven to load the project dependencies.
4. Run `MazeApplication.java` using the project's JavaFX configuration.

## Controls

| Control | Action |
| ------- | ------ |
| Arrow keys | Move the robot or car in the selected maze |
| Auto Solve | Start or replay the selected maze's solving animation |
| S | Start automatic solving in Maze 2 |
| Maze tabs | Switch between Maze 1 and Maze 2 |

## Image Resources

Images are stored in `src/main/resources/images/`:

- `maze.png`
- `maze2.png`
- `robot.png`

## Repository

https://github.com/AditheD/Mazeboot
