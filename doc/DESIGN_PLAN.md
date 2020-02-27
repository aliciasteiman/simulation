# Simulation Design Plan
### 2
### Sanya Kochhar, Alicia Steiman, Jessica Su


## Design Overview
These are the classes we plan to create:
* SimulationModel: Abstract class responsible for the functionality behind a simulation (is in charge of all the actions/updates behind-the-scenes of what is seen on the user interface).
* Simulation-specific subclasses (e.g. GameOfLife, SpreadingFire, etc.): Will each extend SimulationModel and implement the abstract methods from SimulationModel (ex. updateCells) depending on the simulation-specific rules.
* SimulationView: Displays the user interface and allows user to start/stop/step through the simulation. Will allow user to select a simulation from a list of configurations as well as saved configurations.
* Grid: Creates a grid of cells with a specified width and height and cell shapes. Able to count the number of neighbors a Cell has. 
* Cell: Represents a single cell in the Grid. Instantiated with a state read in from configuration file. Responsible for updating its state. Will probably be an abstract superclass with subclasses for the different kinds of Cells between simulations (ex. Cell with alive/dead status, Cell with empty-tree-burning status, etc.)
* Main: Starts/launches the simulation. 


## Design Details
* From Main, SimulationModel and SimulationView objects are created, and a start() method will run the simulation. 
* The SimulationModel is an abstract class that is responsible for handling all the data that will be displayed in the SimulationView (for example, reading in a CSV file and initializing the Grid). 
    * SimulationModel also has abstract methods to update the grid appearance based on the rules of a certain simulation, update the cells’ style, and write a certain configuration to a file. 
    * In other words, SimulationModel provides the functionality behind the buttons that SimulationView creates. 
        * Update grid appearance → step & start buttons 
        * Write configuration to file → save button 
    * SimulationModel will eventually have subclasses that represent all the possible simulations (Game of Life, Spreading Fire, etc.) 
* The Grid is responsible for creating all the cells within a width and height and counting the neighbors of a single cell.
    * A Grid object will be created in the SimulationModel class and used in the SimulationView class. 
    * The Grid class will create instances of Cell objects, and in the abstract updateCells method, each Cell’s state will be updated according to the rules of the Simulation being run. 


## Design Considerations
* Making Cell an abstract superclass with subclasses of different kinds of cells for different simulations v. having one robust Cell class to handle all simulations
    * Alternate Design (One Cell Class): Pros & Cons 
        * Pros: could use an enum to account for different cases in getStatus method; wouldn’t need to create so many subclasses.
        * Cons: enums would get really messy; many different cases to account for. 
    * Decision
        * We are probably going to end up making the Cell class an abstract superclass because there are too many variations among cells to handle in one class.
        * In addition, we already have subclasses for each Simulation that extend the abstract SimulationModel class, so we could probably create the corresponding cell subclass in the SimulationModel subclass (ex. AliveDeadCell subclass would be created in GameOfLife).
* Instantiating a Grid from CSV file in Grid vs. in SimulationModel 
    * Alternate Design (Instantiating from Grid class): 
        * Pros: A Grid is immediately associated with a configuration upon creation, making it easy to access its initial states. 
        * Cons: Different simulations might read from CSV files differently because the configurations will have different inputs for its initial states. Having it be in the Grid class would limit its flexibility. 
    * Decision 
        * We plan to instantiate grid from a CSV file in the SimulationModel class
        * There will also be an abstract method called setCellFromFile that allows different simulations to process inputs in the file differently (ex. GameOfLife will only have 0,1; SpreadingFire might have 0,1,2).
        * In other words, a Grid is only associated with a specific configuration with different states when a SimulationModel is created. 
        * The Grid object itself, upon initialization, is just a bunch of “dead” (depending on simulation) cells. 


## User Interface
* Upon loading the app, the user will be able to choose which simulation to run using a drop-down menu. 
* The user will also be able to control dimensions of the screen and the shapes of the cells through inputs/drop-down menus. 
* The user can pause and play the simulation by hitting a button (or maybe hitting the space-bar). 
* The user can step through the simulation by hitting a button (or maybe hitting the right arrow key to move forward).


## Team Responsibilities

 * Team Member #1 - Jessica
    * Responsible for handling the JavaFX features, specifically regarding the SimulationView.

 * Team Member #2 - Sanya
    * Responsible for implementing the Simulation subclasses; implementing code specific to a given simulation's rules.
    
 * Team Member #3 - Alicia
    * Responsible for the SimulationModel; initializing and updating simulations so that Model can communicate with View. 

