package maze.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import maze.logic.GameState;
import maze.logic.Position.Direction;

@SuppressWarnings("serial")
public class GameMaze extends JPanel 
implements MouseListener, MouseMotionListener, KeyListener {

	private JFrame frameGame;
	JButton btnSaveGame;

	// Coordinates of the bounding rectangle for drawing the image
	private int x = 0, y = 0;
	private int withSquare;
	private int heightSquare;

	// in-memory representation of images of elements of game
	private BufferedImage hero;
	private BufferedImage dragon;
	private BufferedImage sword;
	private BufferedImage exit;
	private BufferedImage pathWithNoElements;
	private BufferedImage wall;
	
	private GameState gamest;
	ArrayList<GameState> savedGames = new ArrayList<GameState>();

	public GameMaze(final GameState gamest) {
		setBounds(45, 35, 501, 453);
		frameGame = new JFrame("Maze Game");
		frameGame.setResizable(false);
		frameGame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		frameGame.setPreferredSize(new Dimension(600, 600));		
		frameGame.getContentPane().setLayout(null);
		frameGame.getContentPane().add(this);
		frameGame.pack(); 
		frameGame.setVisible(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		setLayout(null);

		this.gamest = gamest;
		
		readGames();
		
		btnSaveGame = new JButton("Gravar jogo");
		btnSaveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				savedGames.add(gamest);
				ObjectOutputStream os= null;
				try {
					os= new ObjectOutputStream(new FileOutputStream("file.dat"));
					os.writeInt(savedGames.size());
					for(GameState game: savedGames)
						os.writeObject(game);
				}
				catch (IOException e) {
					System.out.println(e.getMessage());
				}
				finally { 
					if (os!= null)
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						} 
				}
				requestFocus();
			}
		});
		btnSaveGame.setBounds(483, 517, 89, 23);
		frameGame.getContentPane().add(btnSaveGame);
		withSquare = this.getWidth()/gamest.getGameBoard().length;
		heightSquare = this.getHeight()/gamest.getGameBoard().length;
		try {
			hero =  ImageIO.read(new File("images/hero.jpg"));
			dragon =  ImageIO.read(new File("images/dragon.jpg"));
			sword =  ImageIO.read(new File("images/sword.jpg"));
			exit =  ImageIO.read(new File("images/exit.jpg"));
			pathWithNoElements =  ImageIO.read(new File("images/pathWithNoElements.jpg"));
			wall =  ImageIO.read(new File("images/wall.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		requestFocus();
	}
	
	private void readGames(){
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream("file.dat"));
			int totalGamesSaved = is.readInt();
			for(int i = 0;i < totalGamesSaved;i++){
				GameState game = (GameState) is.readObject();
				game.initializeElementsOfGame(game.getMaze().getBoard());
				savedGames.add(game);
			}
		}
		catch (IOException | ClassNotFoundException e1) {
			System.out.println("erro ao ler");
		}
		finally { if (is != null)
			try {
				is.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} }
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); 
		char[][] gameBoard = gamest.getGameBoard();
		x = y = 0;
		for(int i = 0;i < gameBoard.length;i++){
			for(int j = 0;j < gameBoard.length;j++){
				char c = gameBoard[i][j];
				if(c == 'H')
					drawElementOfGame(g, hero);
				else if(c == 'A')
					drawElementOfGame(g, hero);
				else if(c == 'D')
					drawElementOfGame(g, dragon);
				else if(c == 'd')
					drawElementOfGame(g, dragon);
				else if(c == 'E')
					drawElementOfGame(g, sword);
				else if(c == 'F')
					drawElementOfGame(g, dragon);
				else if(c == 'S')
					drawElementOfGame(g, exit);
				else if(c == ' ')
					drawElementOfGame(g, pathWithNoElements);
				else if(c == 'X')
					drawElementOfGame(g, wall);
				x += withSquare;

			}
			x = 0;
			y += heightSquare;
		}
	}

	private void drawElementOfGame(Graphics g, BufferedImage elementImage){
		g.drawImage(elementImage, x, y, x + withSquare - 1, y + heightSquare - 1, 0, 0, elementImage.getWidth(), elementImage.getHeight(), null);
	}

	private void playGameRound(Direction dirMovimentHero){		
		if(gamest.heroMoveIsValid(dirMovimentHero)){
			gamest.moveHero(dirMovimentHero);
			gamest.update();
		}
		repaint();
	}

	// ignored mouse events
	public void mousePressed(MouseEvent e) {} 
	public void mouseDragged(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent arg0) { }
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	// Ignored keyboard events
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(gamest.isFinished() == false){
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				playGameRound(Direction.LEFT);
				break;
			case KeyEvent.VK_RIGHT: 
				playGameRound(Direction.RIGHT);
				break;
			case KeyEvent.VK_UP:
				playGameRound(Direction.UP);
				break;
			case KeyEvent.VK_DOWN:
				playGameRound(Direction.DOWN);
				break;
			default:
				break;
			}	
		}
	}
}