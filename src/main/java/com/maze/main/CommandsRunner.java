package com.maze.main;

import com.maze.domain.Maze;
import com.maze.domain.MazePath;
import com.maze.domain.Position;
import com.maze.generator.MazeGenerator;
import com.maze.generator.KruskalGenerator;
import com.maze.renderer.MazeRenderer;
import com.maze.solver.MazeSolver;
import com.maze.solver.CustomMazeSolver;
import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CommandsRunner {
    private static final String DEFAULT_CELL_SIZE = "7";
    private static final String DEFAULT_HEIGHT = "100";
    private static final String DEFAULT_WIDTH = "100";

    private static final String SAVE_OPT = "save";
    private static final String SHOW_OPT = "show";
    private static final String HEIGHT_OPT = "height";
    private static final String WIDTH_OPT = "width";
    private static final String CELL_SIZE_OPT = "cellSize";
    private static final String NAME_OPT = "name";
    private static final String START_OPT = "start";
    private static final String END_OPT = "end";
    public static final String HELP_OPT = "help";

    public void run(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = buildOptions();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption(HELP_OPT)) {
                printHelp(options);
                return;
            }

            boolean save = cmd.hasOption(SAVE_OPT);
            boolean show = cmd.hasOption(SHOW_OPT);
            int height = Integer.parseInt(cmd.getOptionValue(HEIGHT_OPT, DEFAULT_HEIGHT));
            int width = Integer.parseInt(cmd.getOptionValue(WIDTH_OPT, DEFAULT_WIDTH));
            int cellSize = Integer.parseInt(cmd.getOptionValue(CELL_SIZE_OPT, DEFAULT_CELL_SIZE));
            String name = cmd.getOptionValue(NAME_OPT);
            Position startPosition = new Position(0, 0);
            Position endPosition = new Position(width - 1, height - 1);

            if (isEmptySaveAndShowOptions(save, show)) {
                printMessage("At least one output option (--save or --show) must be set.");
                printHelp(options);
                return;
            }
            if (hasInvalidDimensions(height, width)) {
                printMessage("width and height must be greater or equal to 3");
                return;
            }
            if (cellSize < 3) {
                printMessage("cellSize must be greater or equal to 3");
                return;
            }

            if (cmd.hasOption(START_OPT)) {
                String[] startArgs = cmd.getOptionValues(START_OPT);
                startPosition.setX(Integer.parseInt(startArgs[0]));
                startPosition.setY(Integer.parseInt(startArgs[1]));
                if (positionIsOutOfBound(height, width, startPosition)) {
                    printMessage("start coordinates must be within the maze boundary.");
                    return;
                }
            }

            if (cmd.hasOption(END_OPT)) {
                String[] endArgs = cmd.getOptionValues(END_OPT);
                endPosition.setX(Integer.parseInt(endArgs[0]));
                endPosition.setY(Integer.parseInt(endArgs[1]));
                if (positionIsOutOfBound(height, width, endPosition)) {
                    printMessage("end coordinates must be within the maze boundary.");
                    return;
                }
            }

            runWithOptions(height, width, cellSize, name, save, show, startPosition, endPosition);
        } catch (ParseException e) {
            printHelp(options);
        } catch (NumberFormatException e) {
            printMessage("Invalid number format.");
            printHelp(options);
        }
    }

    private boolean hasInvalidDimensions(int height, int width) {
        return height < 3 || width < 3;
    }

    private boolean isEmptySaveAndShowOptions(boolean save, boolean show) {
        return !save && !show;
    }

    private boolean positionIsOutOfBound(int height, int width, Position startPosition) {
        return startPosition.getX() > width - 1 || startPosition.getX() < 0 || startPosition.getY() > height - 1 || startPosition.getY() < 0;
    }

    private void runWithOptions(int height, int width, int cellSize, String name, boolean save, boolean show, Position startPosition, Position endPosition) {
        MazeGenerator mazeGenerator = new KruskalGenerator();
        Maze maze = mazeGenerator.generate(width, height, startPosition, endPosition);
        MazeSolver solver = new CustomMazeSolver();
        MazePath mazePath = solver.solve(maze);

        MazeRenderer renderer = new MazeRenderer(cellSize);
        BufferedImage image = renderer.generateImage(maze, mazePath);

        if (save) {
            saveMazeImage(name, image);
        }

        if (show) {
            showMazeImage(image);
        }
    }

    private void saveMazeImage(String name, BufferedImage image) {
        try {
            File file = new File("%s.png".formatted(name));
            if (ImageIO.write(image, "png", file)) {
                printMessage("Maze was successfully write to \"%s.png.\"", name);
            }
        } catch (IOException e) {
            printMessage("Impossible to write \"%s\" maze file.", name);
        }
    }

    private void printMessage(String formattedMessage, Object... args) {
        String message = formattedMessage.formatted(args);
        System.out.println(message);
    }

    private void showMazeImage(BufferedImage image) {
        JFrame f = new JFrame();

        Container contentPane = f.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new MazePanel(image));

        Dimension dimension = new Dimension(image.getWidth() + 20, image.getHeight() + 40);
        URL resource = this.getClass().getClassLoader().getResource(("maze-generator.png"));
        if (resource != null) {
            f.setIconImage(new ImageIcon(resource.getPath()).getImage());
        }
        f.setTitle("maze-generator");
        f.setSize(dimension);
        f.setResizable(false);
        f.setLocationRelativeTo(null);

        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar maze-generator.jar", options);
    }

    private Options buildOptions() {
        Options options = new Options();

        Option helpOption = Option.builder(HELP_OPT)
                .hasArg(false)
                .required(false)
                .desc("Show help")
                .build();

        Option widthOption = Option.builder("w")
                .longOpt(WIDTH_OPT)
                .hasArg()
                .argName(WIDTH_OPT)
                .required(false)
                .desc("Horizontal size")
                .build();

        Option heightOption = Option.builder("h")
                .longOpt(HEIGHT_OPT)
                .hasArg()
                .argName(HEIGHT_OPT)
                .required(false)
                .desc("Vertical size")
                .build();

        Option nameOption = Option.builder("n")
                .longOpt(NAME_OPT)
                .hasArg()
                .argName(NAME_OPT)
                .required(false)
                .desc("Name of the maze")
                .build();

        Option saveOption = Option.builder(SAVE_OPT)
                .longOpt(SAVE_OPT)
                .hasArg(false)
                .required(false)
                .desc("Save the maze")
                .build();

        Option showOption = Option.builder(SHOW_OPT)
                .longOpt(SHOW_OPT)
                .hasArg(false)
                .required(false)
                .desc("Show the maze")
                .build();

        Option startOption = Option.builder(START_OPT)
                .longOpt(START_OPT)
                .argName("x,y")
                .hasArg(true)
                .numberOfArgs(2)
                .valueSeparator(',')
                .required(false)
                .desc("Set custom start cell.")
                .build();

        Option endOption = Option.builder(END_OPT)
                .longOpt(END_OPT)
                .argName("x,y")
                .hasArg(true)
                .numberOfArgs(2)
                .valueSeparator(',')
                .required(false)
                .desc("Set custom end cell.")
                .build();

        Option cellSizeOption = Option.builder(CELL_SIZE_OPT)
                .longOpt(CELL_SIZE_OPT)
                .hasArg()
                .required(false)
                .desc("Set custom size for cell renderer.")
                .build();


        options.addOption(helpOption);
        options.addOption(heightOption);
        options.addOption(widthOption);
        options.addOption(nameOption);
        options.addOption(saveOption);
        options.addOption(showOption);
        options.addOption(startOption);
        options.addOption(endOption);
        options.addOption(cellSizeOption);
        return options;
    }
}
