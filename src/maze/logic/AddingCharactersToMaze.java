package maze.logic;

import java.util.Random;
import java.util.ArrayList;

/**
 * The class is responsible for adding characters to the maze.
 */
public class AddingCharactersToMaze {
	char[][] maze;
	Position heroPos;
	ArrayList<Position> dragonsPos;
	Position swordPos;
	
	/**
	 * Put in maze 1 hero, 1 sword and 1 dragon
	 * @param maze
	 * @return maze with the characters
	 */
	public char[][] getMazeWithCharacters(char[][] maze){
		this.maze = maze;
		dragonsPos = new ArrayList<Position>();
		heroPos = creatPosition(); 
		this.maze[heroPos.getX()][heroPos.getY()] = 'H';

		swordPos = creatPosition();
		this.maze[swordPos.getX()][swordPos.getY()] = 'E';
		
		Position dragonPos = null;
		do{
			dragonPos = creatPosition();
		}while(heroPos.positionsAreNearOfeachOther(dragonPos));
		this.maze[dragonPos.getX()][dragonPos.getY()] = 'D';

		return maze;
	}
	
	/**
	 * Add dragons in maze until numDragons. Remember: the is initialized with 1 dragon.
	 * @param maze.
	 * @param numDragons number of dragons.
	 * @return maze with the number of dragons intended.
	 */
	public char[][] addDragonsInMazeUntilNumDragons(char[][] maze, int numDragons){
		this.maze = maze;
		findCharactersThatExistsInMaze();
		int numDragonsToAdd = numDragons - dragonsPos.size();
		for(int i = 0;i < numDragonsToAdd;i++){
			Position dragonPos = null;
			boolean positionValid = false;
			
			do{
				positionValid = true;
				dragonPos = creatPosition();
				for(Position pos: dragonsPos)
					if(pos.positionsAreNearOfeachOther(dragonPos)){
						positionValid = false;
						break;
					}

				if(heroPos.positionsAreNearOfeachOther(dragonPos))
					positionValid = false;

			}while(positionValid == false);
			
			this.maze[dragonPos.getX()][dragonPos.getY()] = 'D';
		}
		return maze;
	}
	
	/**
	 * Gets the position of the elements of the game.
	 */
	private void findCharactersThatExistsInMaze(){
		dragonsPos = new ArrayList<Position>();
		for(int i = 1;i < maze.length-1;i++)
			for(int j = 1;j < maze.length-1;j++)
				if(maze[i][j] == 'H')
					heroPos = new Position(i, j);
				else if(maze[i][j] == 'E')
					swordPos = new Position(i, j);
				else if(maze[i][j] == 'D' || maze[i][j] == 'd')
					dragonsPos.add(new Position(i, j));
	}
	
	/**
	 * 
	 * @return a random position
	 */
	private Position creatPosition(){
		Position pos = null;
		do{
			pos = generateRandomPosition();
		}while(pos == null || positionIsInvalid(pos));
		return pos;
	}
	
	/**
	 * 
	 * @param pos
	 * @return true if in that position is some element,or false if the cell is empty
	 */
	private boolean positionIsInvalid(Position pos){
		if(maze[pos.getX()][pos.getY()] != ' ') return true;
		else return false;
	}
	
	/**
	 * 
	 * @return a random position
	 */
	private Position generateRandomPosition(){
		Random r = new Random();
		int row = r.nextInt(maze.length-2)+1;
		int column = r.nextInt(maze.length-2)+1;
		return new Position(row, column);
	}
}
