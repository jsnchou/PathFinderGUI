package Visualizer;

import Grid.*;
import Grid.Cell;
import Heuristic.*;
import SearchAlgos.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * This is the Controller class which controls the GUI functions.
 */
public class Controller {

    // Grid and Cell global variables
    public Grid grid;
    public Cell cell;

    // global heuristic variables
    public ManhattanDistance manhattanDistance;
    public ManhattanDistanceByFour manhattanDistanceByFour;
    public EuclideanDistance euclideanDistance;
    public EuclideanDistanceByFour euclideanDistanceByFour;
    public Chebyshev chebyshev;

    // global Search variables
    public AStarSearch aStarSearch;
    public WeightedAStarSearch weightedAStarSearch;
    public UniformCostSearch uniformCostSearch;
    public SequentialAStarSearch sequentialAStarSearch;

    // other global variables
    public Rectangle[][] displayRect;
    public FileChooser fileChoose;
    public Stage stage;

    @FXML
    public TextArea TextOutput;

    @FXML
    public GridPane gridPane;

    @FXML
    public Button generateNewGridButton;

    @FXML
    public Button loadSavedGridButton;

    @FXML
    public Button saveNewGridButton;

    @FXML
    public TextField saveNewGridName;

    @FXML
    public ToggleGroup HeuristicGroup;

    @FXML
    public Toggle ManhattanDistanceRadioButton;

    @FXML
    public Toggle EuclideanDistanceRadioButton;

    @FXML
    public Toggle ManhattanDistanceByFourRadioButton;

    @FXML
    public Toggle EuclideanDistanceByFourRadioButton;

    @FXML
    public Button runAStar;

    @FXML
    public Button runUniformCostSearch;

    @FXML
    public Button runWeightedAStar;

    @FXML
    public TextField weightedAStarWeight;

    @FXML
    public Button runSequentialHeuristicSearch;

    @FXML
    public TextField sequentialSearchWeight1;

    @FXML
    public TextField sequentialSearchWeight2;


    /**
     * This is the constructor of the Controller class.
     */
    public Controller() {
        this.displayRect = new Rectangle[120][160];
    } // constructor of the Controller class


    /**
     * This is the initializer of the Controller Class.
     */
    @FXML
    public void initialize() {
        initializeGridGUI(gridPane);
    } // ends the initialize() method


    /**
     * This method will initialize the Grid on the GUI.
     * It would make it a giant green grid.
     * @param gridPane the Grid Pane that holds the entire grid
     */
    public void initializeGridGUI(GridPane gridPane) {
        this.gridPane.setPadding(new Insets(2));
        this.gridPane.setHgap(2);
        this.gridPane.setVgap(2);

        this.gridPane = gridPane;
        this.grid = new Grid();
        colorGridBeforePath(grid);
    } // ends the initializeGridGUI() method


    /**
     * This method will be used in order to color the grid before the path is found.
     * At this stage, the cells of the grid are not click-able.
     * @param gridToColor the current grid to color in and display on the GUI
     */
    public void colorGridBeforePath(Grid gridToColor) {
        TextOutput.appendText("\nStart Cell: ("+gridToColor.getStartCell()[0][0]+" , "+gridToColor.getStartCell()[0][1]+")\n");
        TextOutput.appendText("End Cell: ("+gridToColor.getEndCell()[0][0]+" , "+gridToColor.getEndCell()[0][1]+")");
        this.gridPane.getChildren().clear();

        Rectangle rect;
        Color color = Color.BLACK;
        Cell[][] arr = gridToColor.getGrid();

        for (int r = 1; r <= 120; r++) {
            for (int c = 1; c <= 160; c++) {
                cell = arr[r-1][c-1];
                if (cell.getType() == 0) {
                    color = Color.BLACK;
                } else if (cell.getType() == 1) {
                    color = Color.DARKOLIVEGREEN;
                } else if (cell.getType() == 2) {
                    color = Color.BEIGE;
                } else if (cell.getType() == 3) {
                    color = Color.LIGHTBLUE;
                } else {
                    color = Color.DARKBLUE;
                }

                if (cell.getX() == grid.getStartCell()[0][0] && cell.getY() == grid.getStartCell()[0][1]) {
                    color = Color.RED;
                }
                if (cell.getX() == grid.getEndCell()[0][0] && cell.getY() == grid.getEndCell()[0][1]) {
                    color = Color.RED;
                }
                rect = new Rectangle(6, 6, color); // entire grid made up of rectangles
                this.displayRect[r - 1][c - 1] = rect;
                gridPane.add(rect, c, r);
            }
        }
    } // ends the colorGridBeforePath() method


    /**
     * This method will be used by A*, Weighted A* and Uniform Cost Search once a path has been found on the grid.
     * As the grid is being built and colored in to reflect the changes, this method allows for each rectangle to now
     * be click-able so that it would display some info about that specific Cell.
     * @param rect the rectangle to make click-able
     * @param c the column of the rectangle
     * @param r the row of the rectangle
     * @param path the path generated by the search algorithm
     * @param explored the list of explored cells that the search algorithm generated
     */
    public void addClick(Rectangle rect, int c, int r, List<Cell> path, Set<Cell> explored) {
        rect.setOnMouseClicked(e -> {
            int realc = c-1;
            int realr = r-1;
            Cell curCell = grid.getGrid()[realr][realc];
            if (path.contains(curCell) || explored.contains(curCell)) {
                TextOutput.appendText("\n\nCell ["+realr+"]["+realc+"] clicked.\n");
                if (curCell.getType() == 0) {
                    TextOutput.appendText("Type of Cell: Blocked\n");
                } else if (curCell.getType() == 1) {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse\n");
                } else if (curCell.getType() == 2) {
                    TextOutput.appendText("Type of Cell: Hard-To-Traverse\n");
                } else if (curCell.getType() == 3) {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse River\n");
                } else {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse River\n");
                }
                TextOutput.appendText("G-Cost: " + curCell.getgCost() + "\n");
                TextOutput.appendText("H-Cost: " + curCell.gethCost() + "\n");
                TextOutput.appendText("F-Cost: " + curCell.getfCost());
            } else {
                TextOutput.appendText("\n\nCell ["+realr+"]["+realc+"] clicked.\n");
                if (curCell.getType() == 0) {
                    TextOutput.appendText("Type of Cell: Blocked\n");
                } else if (curCell.getType() == 1) {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse\n");
                } else if (curCell.getType() == 2) {
                    TextOutput.appendText("Type of Cell: Hard-To-Traverse\n");
                } else if (curCell.getType() == 3) {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse River\n");
                } else {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse River\n");
                }
                TextOutput.appendText("CELL HAS NOT BEEN VISITED \nAND IS NOT PART OF THE SHORTEST PATH.");
            }
        });
    } // ends the addClick() method


    /**
     * This method is called once the A Star Search button on the GUI is clicked
     */
    public void runAStarClicked() {
        if (ManhattanDistanceRadioButton.isSelected()) {
            TextOutput.appendText("\n\nRunning A Star Search \nw/ Manhattan Distance Heuristic");
            manhattanDistance = new ManhattanDistance(this.grid);
            aStarSearch = new AStarSearch(grid,manhattanDistance);
            long startTime = System.currentTimeMillis();
            aStarSearch.run();
            long totalTime = System.currentTimeMillis() - startTime;
            List<Cell> path = aStarSearch.getPath();
            Set<Cell> explored = aStarSearch.getExploredCells();
            if (path == null) {
                TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("NO PATH FOUND");
                alert.setHeaderText("Reasons:");
                alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                alert.showAndWait();
            } else {
                TextOutput.appendText("\nPATH FOUND!");
                TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                TextOutput.appendText("\nCost of the path: " + aStarSearch.getPathCost());
                colorGridAfterPath(grid,path, explored);
            }
        } else if (EuclideanDistanceRadioButton.isSelected()) {
            TextOutput.appendText("\n\nRunning A Star Search \nw/ Euclidean Distance Heuristic");
            euclideanDistance = new EuclideanDistance(this.grid);
            aStarSearch = new AStarSearch(grid,euclideanDistance);
            long startTime = System.currentTimeMillis();
            aStarSearch.run();
            long totalTime = System.currentTimeMillis() - startTime;
            List<Cell> path = aStarSearch.getPath();
            Set<Cell> explored = aStarSearch.getExploredCells();
            if (path == null) {
                TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("NO PATH FOUND");
                alert.setHeaderText("Reasons:");
                alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                alert.showAndWait();
            } else {
                TextOutput.appendText("\nPATH FOUND!");
                TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                TextOutput.appendText("\nCost of the path: " + aStarSearch.getPathCost());
                colorGridAfterPath(grid,path, explored);
            }
        } else if (ManhattanDistanceByFourRadioButton.isSelected()) {
            TextOutput.appendText("\n\nRunning A Star Search \nw/ Manhattan Distance By Four Heuristic");
            manhattanDistanceByFour = new ManhattanDistanceByFour(this.grid);
            aStarSearch = new AStarSearch(grid,manhattanDistanceByFour);
            long startTime = System.currentTimeMillis();
            aStarSearch.run();
            long totalTime = System.currentTimeMillis() - startTime;
            List<Cell> path = aStarSearch.getPath();
            Set<Cell> explored = aStarSearch.getExploredCells();
            if (path == null) {
                TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("NO PATH FOUND");
                alert.setHeaderText("Reasons:");
                alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                alert.showAndWait();
            } else {
                TextOutput.appendText("\nPATH FOUND!");
                TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                TextOutput.appendText("\nCost of the path: " + aStarSearch.getPathCost());
                colorGridAfterPath(grid,path, explored);
            }
        } else if (EuclideanDistanceByFourRadioButton.isSelected()) {
            TextOutput.appendText("\n\nRunning A Star Search \nw/ Euclidean Distance By Four Heuristic");
            euclideanDistanceByFour = new EuclideanDistanceByFour(this.grid);
            aStarSearch = new AStarSearch(grid,euclideanDistanceByFour);
            long startTime = System.currentTimeMillis();
            aStarSearch.run();
            long totalTime = System.currentTimeMillis() - startTime;
            List<Cell> path = aStarSearch.getPath();
            Set<Cell> explored = aStarSearch.getExploredCells();
            if (path == null) {
                TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("NO PATH FOUND");
                alert.setHeaderText("Reasons:");
                alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                alert.showAndWait();
            } else {
                TextOutput.appendText("\nPATH FOUND!");
                TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                TextOutput.appendText("\nCost of the path: " + aStarSearch.getPathCost());
                colorGridAfterPath(grid,path, explored);
            }
        } else {
            TextOutput.appendText("\n\nRunning A Star Search \nw/ Chebyshev Distance Heuristic");
            chebyshev = new Chebyshev(this.grid);
            aStarSearch = new AStarSearch(grid,chebyshev);
            long startTime = System.currentTimeMillis();
            aStarSearch.run();
            long totalTime = System.currentTimeMillis() - startTime;
            List<Cell> path = aStarSearch.getPath();
            Set<Cell> explored = aStarSearch.getExploredCells();
            if (path == null) {
                TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("NO PATH FOUND");
                alert.setHeaderText("Reasons:");
                alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                alert.showAndWait();
            } else {
                TextOutput.appendText("\nPATH FOUND!");
                TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                TextOutput.appendText("\nCost of the path: " + aStarSearch.getPathCost());
                colorGridAfterPath(grid,path, explored);
            }
        }
    } // ends the runAStarClicked() method


    /**
     * This method is called once the Uniform Cost Search button is clicked
     */
    public void runUniformCostSearchButtonClicked() {
        TextOutput.appendText("\n\nRunning Uniform Cost Search");
        uniformCostSearch = new UniformCostSearch(this.grid);
        long startTime = System.currentTimeMillis();
        uniformCostSearch.run();
        long totalTime = System.currentTimeMillis() - startTime;
        List<Cell> path = uniformCostSearch.getPath();
        Set<Cell> explored = uniformCostSearch.getExploredCells();
        if (path == null) {
            TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("NO PATH FOUND");
            alert.setHeaderText("Reasons:");
            alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
            alert.showAndWait();
        } else {
            TextOutput.appendText("\nPATH FOUND!");
            TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
            TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
            TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
            TextOutput.appendText("\nCost of the path: " + uniformCostSearch.getPathCost());
            colorGridAfterPath(grid,path, explored);
        }
    } // ends the runUniformCostSearchButtonClicked()


    /**
     * This method is called once the Weighted A Star Search button is clicked
     */
    public void runWeightedAStarButtonClicked() {
        float weight = 1;
        try {
            weight = Float.parseFloat(weightedAStarWeight.getText());
        } catch (Exception e) {
            TextOutput.appendText("\n\nPLEASE MAKE SURE THE WEIGHT IS A DECIMAL NUMBER.");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("IMPROPER WEIGHT");
            alert.setHeaderText("Reason:");
            alert.setContentText("Make sure to use a weight that is a decimal number >= 1.0");
            alert.showAndWait();
            return;
        }
        if (weight >= 1) {
            if (ManhattanDistanceRadioButton.isSelected()) {
                TextOutput.appendText("\n\nRunning Weighted A Star Search \nw/ Manhattan Distance Heuristic \nw/ weight: " + weight);
                manhattanDistance = new ManhattanDistance(this.grid);
                weightedAStarSearch = new WeightedAStarSearch(grid,manhattanDistance,weight);
                long startTime = System.currentTimeMillis();
                weightedAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = weightedAStarSearch.getPath();
                Set<Cell> explored = weightedAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + weightedAStarSearch.getPathCost());
                    colorGridAfterPath(grid,path,explored);
                }
            } else if (EuclideanDistanceRadioButton.isSelected()) {
                TextOutput.appendText("\n\nRunning Weighted A Star Search \nw/ Euclidean Distance Heuristic \nw/ weight: " + weight);
                euclideanDistance = new EuclideanDistance(this.grid);
                weightedAStarSearch = new WeightedAStarSearch(grid,euclideanDistance,weight);
                long startTime = System.currentTimeMillis();
                weightedAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = weightedAStarSearch.getPath();
                Set<Cell> explored = weightedAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + weightedAStarSearch.getPathCost());
                    colorGridAfterPath(grid,path,explored);
                }
            } else if (ManhattanDistanceByFourRadioButton.isSelected()) {
                TextOutput.appendText("\n\nRunning Weighted A Star Search \nw/ Manhattan Distance By Four Heuristic \nw/ weight: " + weight);
                manhattanDistanceByFour = new ManhattanDistanceByFour(this.grid);
                weightedAStarSearch = new WeightedAStarSearch(grid,manhattanDistanceByFour,weight);
                long startTime = System.currentTimeMillis();
                weightedAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = weightedAStarSearch.getPath();
                Set<Cell> explored = weightedAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + weightedAStarSearch.getPathCost());
                    colorGridAfterPath(grid,path,explored);
                }
            } else if (EuclideanDistanceByFourRadioButton.isSelected()) {
                TextOutput.appendText("\n\nRunning Weighted A Star Search \nw/ Euclidean Distance By Four Heuristic \nw/ weight: " + weight);
                euclideanDistanceByFour = new EuclideanDistanceByFour(this.grid);
                weightedAStarSearch = new WeightedAStarSearch(grid,euclideanDistanceByFour,weight);
                long startTime = System.currentTimeMillis();
                weightedAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = weightedAStarSearch.getPath();
                Set<Cell> explored = weightedAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + weightedAStarSearch.getPathCost());
                    colorGridAfterPath(grid,path,explored);
                }
            } else {
                TextOutput.appendText("\n\nRunning Weighted A Star Search \nw/ Chebyshev Distance Heuristic \nw/ weight: " + weight);
                chebyshev = new Chebyshev(this.grid);
                weightedAStarSearch = new WeightedAStarSearch(grid,chebyshev,weight);
                long startTime = System.currentTimeMillis();
                weightedAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = weightedAStarSearch.getPath();
                Set<Cell> explored = weightedAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + explored.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + weightedAStarSearch.getPathCost());
                    colorGridAfterPath(grid,path,explored);
                }
            }
        } else {
            TextOutput.appendText("\n\nPLEASE MAKE SURE THE WEIGHT IS >= 1");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("IMPROPER WEIGHT");
            alert.setHeaderText("Reason:");
            alert.setContentText("Make sure to use a weight that is a decimal number >= 1.0");
            alert.showAndWait();
        }
    } // ends the runWeightedAStarButtonClicked() method


    /**
     * This method is called once the Sequential Heuristic Search button is clicked
     */
    public void runSequentialHeuristicSearchButtonClicked() {
        float w1 = 0;
        float w2 = 0;
        try {
            w1 = Float.parseFloat(sequentialSearchWeight1.getText());
            w2 = Float.parseFloat(sequentialSearchWeight2.getText());
        } catch (Exception e) {
            TextOutput.appendText("\n\nPLEASE MAKE SURE THE WEIGHTS ARE DECIMAL NUMBERS.");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("IMPROPER WEIGHTS");
            alert.setHeaderText("Reason:");
            alert.setContentText("Make sure to use weights that are decimal numbers >= 1.0");
            alert.showAndWait();
            return;
        }
        if (w1 >= 1 && w2 >= 1) {
            Heuristic[] arr = new Heuristic[5];
            if (ManhattanDistanceRadioButton.isSelected()) {
                TextOutput.appendText("\n\nRunning Sequential Heuristic Search \nw/ Manhattan Distance Heuristic as the anchor \nw/ weight 1 = " + w1 + " , weight 2 = " + w2);
                arr[0] = new ManhattanDistance(this.grid);
                arr[1] = new ManhattanDistanceByFour(this.grid);
                arr[2] = new EuclideanDistance(this.grid);
                arr[3] = new EuclideanDistanceByFour(this.grid);
                arr[4] = new Chebyshev(this.grid);
                sequentialAStarSearch = new SequentialAStarSearch(this.grid,w1,w2,arr);
                long startTime = System.currentTimeMillis();
                sequentialAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = sequentialAStarSearch.getPath();
                HashMap<Cell, Cell[]> exploredCells = sequentialAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + exploredCells.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + sequentialAStarSearch.getPathCost());
                    colorSequentialGridAfterPath(grid,path,exploredCells);
                }
            } else if (ManhattanDistanceByFourRadioButton.isSelected()) {
                TextOutput.appendText("\n\nRunning Sequential Heuristic Search \nw/ Manhattan Distance By Four Heuristic as the anchor \nw/ weight 1 = " + w1 + " , weight 2 = " + w2);
                arr[0] = new ManhattanDistanceByFour(this.grid);
                arr[1] = new ManhattanDistance(this.grid);
                arr[2] = new EuclideanDistance(this.grid);
                arr[3] = new EuclideanDistanceByFour(this.grid);
                arr[4] = new Chebyshev(this.grid);
                sequentialAStarSearch = new SequentialAStarSearch(this.grid,w1,w2,arr);
                long startTime = System.currentTimeMillis();
                sequentialAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = sequentialAStarSearch.getPath();
                HashMap<Cell, Cell[]> exploredCells = sequentialAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + exploredCells.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + sequentialAStarSearch.getPathCost());
                    colorSequentialGridAfterPath(grid,path,exploredCells);
                }
            } else if (EuclideanDistanceRadioButton.isSelected()) {
                TextOutput.appendText("\n\nRunning Sequential Heuristic Search \nw/ Euclidean Distance Heuristic as the anchor \nw/ weight 1 = " + w1 + " , weight 2 = " + w2);
                arr[0] = new EuclideanDistance(this.grid);
                arr[1] = new ManhattanDistance(this.grid);
                arr[2] = new ManhattanDistanceByFour(this.grid);
                arr[3] = new EuclideanDistanceByFour(this.grid);
                arr[4] = new Chebyshev(this.grid);
                sequentialAStarSearch = new SequentialAStarSearch(this.grid,w1,w2,arr);
                long startTime = System.currentTimeMillis();
                sequentialAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = sequentialAStarSearch.getPath();
                HashMap<Cell, Cell[]> exploredCells = sequentialAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + exploredCells.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + sequentialAStarSearch.getPathCost());
                    colorSequentialGridAfterPath(grid,path,exploredCells);
                }
            } else if (EuclideanDistanceByFourRadioButton.isSelected()) {
                TextOutput.appendText("\n\nRunning Sequential Heuristic Search \nw/ Euclidean Distance By Four Heuristic as the anchor \nw/ weight 1 = " + w1 + " , weight 2 = " + w2);
                arr[0] = new EuclideanDistanceByFour(this.grid);
                arr[1] = new ManhattanDistance(this.grid);
                arr[2] = new ManhattanDistanceByFour(this.grid);
                arr[3] = new EuclideanDistance(this.grid);
                arr[4] = new Chebyshev(this.grid);
                sequentialAStarSearch = new SequentialAStarSearch(this.grid,w1,w2,arr);
                long startTime = System.currentTimeMillis();
                sequentialAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = sequentialAStarSearch.getPath();
                HashMap<Cell, Cell[]> exploredCells = sequentialAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + exploredCells.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + sequentialAStarSearch.getPathCost());
                    colorSequentialGridAfterPath(grid,path,exploredCells);
                }
            } else {
                TextOutput.appendText("\n\nRunning Sequential Heuristic Search \nw/ Chebyshev Distance Heuristic as the anchor \nw/ weight 1 = " + w1 + " , weight 2 = " + w2);
                arr[0] = new Chebyshev(this.grid);
                arr[1] = new ManhattanDistance(this.grid);
                arr[2] = new ManhattanDistanceByFour(this.grid);
                arr[3] = new EuclideanDistance(this.grid);
                arr[4] = new EuclideanDistanceByFour(this.grid);
                sequentialAStarSearch = new SequentialAStarSearch(this.grid,w1,w2,arr);
                long startTime = System.currentTimeMillis();
                sequentialAStarSearch.run();
                long totalTime = System.currentTimeMillis() - startTime;
                List<Cell> path = sequentialAStarSearch.getPath();
                HashMap<Cell, Cell[]> exploredCells = sequentialAStarSearch.getExploredCells();
                if (path == null) {
                    TextOutput.appendText("\nNO PATH FOUND or SEARCH WAS DONE ON THIS GRID");
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("NO PATH FOUND");
                    alert.setHeaderText("Reasons:");
                    alert.setContentText("1) A path could not be formed given the placement of the start and goal points in this specific grid \n2) A search was already completed on this grid and it must be cleared out and run again");
                    alert.showAndWait();
                } else {
                    TextOutput.appendText("\nPATH FOUND!");
                    TextOutput.appendText("\nRun time: " + totalTime + " milliseconds");
                    TextOutput.appendText("\nLength of the path: " + path.size() + " cells.");
                    TextOutput.appendText("\nNumber of cells visited: " + exploredCells.size() + " cells");
                    TextOutput.appendText("\nCost of the path: " + sequentialAStarSearch.getPathCost());
                    colorSequentialGridAfterPath(grid,path,exploredCells);
                }
            }
        } else {
            TextOutput.appendText("\n\nPLEASE MAKE SURE WEIGHTS ARE >= 1");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("IMPROPER WEIGHTS");
            alert.setHeaderText("Reason:");
            alert.setContentText("Make sure to use weights that are decimal numbers >= 1.0");
            alert.showAndWait();
        }
    } // ends the runSequentialHeuristicSearchButtonClicked() method


    /**
     * This method is called after Sequential Heuristic Search has been done on the current grid.
     * @param gridToColor the current grid that needs to be updated on the GUI
     * @param path the shortest path generated by the Sequential Heuristic Search
     * @param exploredCells a map of all the explored cells generated by Sequential Heuristic Search
     */
    public void colorSequentialGridAfterPath(Grid gridToColor, List<Cell> path, HashMap<Cell, Cell[]> exploredCells) {
        gridPane.getChildren().clear();

        Rectangle rect;
        Color color = Color.BLACK;
        Cell[][] arr = gridToColor.getGrid();

        for (int r = 1; r <= 120; r++) {
            for (int c = 1; c <= 160; c++) {
                cell = arr[r-1][c-1];
                if (path.contains(arr[r-1][c-1])) {
                    color = Color.RED;
                } else if(exploredCells.containsKey(arr[r-1][c-1])) {
                    color = Color.ORANGE;
                } else if (cell.getType() == 0) {
                    color = Color.BLACK;
                } else if (cell.getType() == 1) {
                    color = Color.DARKOLIVEGREEN;
                } else if (cell.getType() == 2) {
                    color = Color.BEIGE;
                } else if (cell.getType() == 3) {
                    color = Color.LIGHTBLUE;
                } else {
                    color = Color.DARKBLUE;
                }
                rect = new Rectangle(6, 6, color);
                addClickSequential(rect,c,r,path,exploredCells);
                this.displayRect[r - 1][c - 1] = rect;
                gridPane.add(rect, c, r);
            }
        }
    } // ends the colorSequentialGridAfterPath()


    /**
     * This method is called once the GUI has been updated once a Sequential Heuristic Search has been done.
     * As the grid is being built and colored in to reflect the changes, this method allows for each rectangle to now
     * be click-able so that it would display some info about that specific Cell.
     * @param rect the current rectangle to make click-able
     * @param c the column of the cell
     * @param r the row of the cell
     * @param path the shortest path generated by the Sequential Heuristic Search
     * @param exploredCells the map of explored cells generated by the Sequential Heuristic Search
     */
    public void addClickSequential(Rectangle rect, int c, int r, List<Cell> path, HashMap<Cell, Cell[]> exploredCells) {
        rect.setOnMouseClicked(e -> {
            int realc = c-1;
            int realr = r-1;
            Cell curCell = grid.getGrid()[realr][realc];
            if (path.contains(curCell) || exploredCells.containsKey(curCell)) {
                TextOutput.appendText("\n\nCell ["+realr+"]["+realc+"] clicked.\n");
                if (curCell.getType() == 0) {
                    TextOutput.appendText("Type of Cell: Blocked\n");
                } else if (curCell.getType() == 1) {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse\n");
                } else if (curCell.getType() == 2) {
                    TextOutput.appendText("Type of Cell: Hard-To-Traverse\n");
                } else if (curCell.getType() == 3) {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse River\n");
                } else {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse River\n");
                }
                TextOutput.appendText("G-Cost: " + curCell.getgCost() + "\n");
                TextOutput.appendText("H-Cost: " + curCell.gethCost() + "\n");
                TextOutput.appendText("F-Cost: " + curCell.getfCost());
            } else {
                TextOutput.appendText("\n\nCell ["+realr+"]["+realc+"] clicked.\n");
                if (curCell.getType() == 0) {
                    TextOutput.appendText("Type of Cell: Blocked\n");
                } else if (curCell.getType() == 1) {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse\n");
                } else if (curCell.getType() == 2) {
                    TextOutput.appendText("Type of Cell: Hard-To-Traverse\n");
                } else if (curCell.getType() == 3) {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse River\n");
                } else {
                    TextOutput.appendText("Type of Cell: Easy-To-Traverse River\n");
                }
                TextOutput.appendText("CELL HAS NOT BEEN VISITED \nAND IS NOT PART OF THE SHORTEST PATH.");
            }
        });
    } // ends the addClickSequential() method


    /**
     * This method is called after the A Star, Weighted A Star and Uniform Cost Search algorithms have been done on the GUI.
     * This method will color in the changes.
     * @param gridToColor the current grid to color in
     * @param path the path generated by the algorithm
     * @param explored the list of explored cells generated by the algorithm
     */
    public void colorGridAfterPath(Grid gridToColor, List<Cell> path, Set<Cell> explored) {
        gridPane.getChildren().clear();

        Rectangle rect;
        Color color = Color.BLACK;
        Cell[][] arr = gridToColor.getGrid();

        for (int r = 1; r <= 120; r++) {
            for (int c = 1; c <= 160; c++) {
                cell = arr[r-1][c-1];
                if (path.contains(arr[r-1][c-1])) {
                    color = Color.RED;
                } else if(explored.contains(arr[r-1][c-1])) {
                    color = Color.ORANGE;
                } else if (cell.getType() == 0) {
                    color = Color.BLACK;
                } else if (cell.getType() == 1) {
                    color = Color.DARKOLIVEGREEN;
                } else if (cell.getType() == 2) {
                    color = Color.BEIGE;
                } else if (cell.getType() == 3) {
                    color = Color.LIGHTBLUE;
                } else {
                    color = Color.DARKBLUE;
                }
                rect = new Rectangle(6, 6, color);
                addClick(rect, c, r,path,explored);
                this.displayRect[r - 1][c - 1] = rect;
                gridPane.add(rect, c, r);
            }
        }
    } // ends the colorGridAfterPath() method


    /**
     * This method is called when the Generate New Grid button is clicked.
     */
    public void generateNewGridButtonClicked() {
        grid = new Grid();
        grid.generateEntireGrid();
        TextOutput.appendText("\n\nGenerating new grid ... ");
        colorGridBeforePath(grid);
    } // ends the generateNewGridButtonClicked() method


    /**
     * This method is called once the Load Saved Grid button is clicked
     */
    public void loadSavedGridButtonClicked() {
        fileChoose = new FileChooser();
        fileChoose.setInitialDirectory(new File("./src/SavedGrids/"));
        File file = fileChoose.showOpenDialog(stage);
        if(file != null) {
            TextOutput.appendText("\n\nLoading grid: " + file.getName() + " ...");
            grid = new Grid();
            grid.importGrid(file);
            colorGridBeforePath(grid);
        }
    } // ends the loadSavedGridButtonClicked() method


    /**
     * This method is called once the Save New Grid button is called
     */
    public void saveNewGridButtonClicked() {
        String pathToDirectory = "./src/SavedGrids/";
        String newFileName = saveNewGridName.getText();
        pathToDirectory += newFileName;
        System.out.println(pathToDirectory);
        File file = new File(pathToDirectory);
        if(file != null) {
            grid.saveGrid(file);
        }
        TextOutput.appendText("\n\nSaving grid: " + newFileName + " ...");
    } // ends the saveNewGridButtonClicked() method
} // ends the Controller Class