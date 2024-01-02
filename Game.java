/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room Lobby, key, control, powerUp, guard, out, safe, exit, empty1, empty2, empty3, empty4, empty5, empty6, empty7, empty8, empty9, underControl;
      
        // create the rooms
        Lobby = new Room("you enter the lobby of the escape room");
        key = new Room("you enter a room and obtain a key");
        control = new Room("you enter the control room and see the camera system");
        underControl = new Room("you enter a secret room under the control room");
        powerUp = new Room("you enter a room full of rations and gain newfound strength");
        guard = new Room("you enter a room with a bouncer blocking your path. Maybe some rations could help.");
        safe = new Room("you enter a room with a safe. It looks like you need a key from somewhere...");
        exit = new Room("you enter a room with a exit door. It looks like it needs a pin to unlock...");
        out = new Room("you've escaped and won!");
        empty9 = new Room("you enter a empty room");
        empty1 = new Room("you enter a empty room");
        empty2 = new Room("you enter a empty room");
        empty3 = new Room("you enter a empty room");
        empty4 = new Room("you enter a empty room");
        empty5 = new Room("you enter a empty room");
        empty6 = new Room("you enter a empty room");
        empty7 = new Room("you enter a empty room");
        empty8 = new Room("you enter a empty room");

        // initialise room exits (north, east, south, west)
        Lobby.setExits(empty5,safe,null,null);
        empty5.setExits(key,empty6,Lobby,null);
        key.setExits(empty1,powerUp,empty5,null);
        empty1.setExits(null,empty2,key,null);
        safe.setExits(empty5,empty8,null,Lobby);
        empty6.setExits(powerUp,guard,safe,empty5);
        powerUp.setExits(empty2,empty4,empty6,key);
        empty2.setExits(null,empty3,powerUp,empty1);
        empty3.setExits(null,control,empty4,empty2);
        empty4.setExits(empty3,exit,guard,powerUp);
        guard.setExits(empty4,empty7,empty8,empty6);
        empty8.setExits(guard,empty9,null,safe);
        empty9.setExits(empty7,null,null,empty8);
        empty7.setExits(exit,null,empty9,guard);
        exit.setExits(control,null,empty7,empty4);
        control.setExits(null,null,exit,empty4);
        control.setExits("downstairs", underControl);
        underControl.setExits("upstairs",control);
        exit.setExits("out",out);
        currentRoom = Lobby;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);

        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getDescription());
        System.out.print("You can go: ");
        locationInfo();
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = currentRoom.getExit("north");
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.getExit("east");
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.getExit("south");
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.getExit("west");
        }
        if(direction.equals("downstairs")){
            nextRoom = currentRoom.getExit("downstairs");
        }
        if(direction.equals("upstairs")){
            nextRoom = currentRoom.getExit("upstairs");
        }
        if(direction.equals("out")){
            nextRoom = currentRoom.getExit("out");
        }
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println("You are " + currentRoom.getDescription());
            System.out.print("Exits: ");
            locationInfo();
            System.out.println();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * helper method that returns location info.
     *
     * @return string of directions you can go.
     */
    private void locationInfo(){
        System.out.println("Your are " + currentRoom.getDescription());
        System.out.print("You can go: ");
        System.out.print(currentRoom.getExitString());
        System.out.println();
    }
}
