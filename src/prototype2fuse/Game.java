package prototype2fuse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class Game extends BasicGame{

	public static int GWIDTH = 600, GHEIGHT = 300;
	public int boxSize = 60;
	
	public Box[] grid;
	Random rand;
	
	int currentPressed;
	Circle press;
	int circleMax = 40;
	
	int score;
	
	ConfigurableEmitter emitterPair;
	ParticleSystem pSystem;
	
	public class Box{
		public Box(Color c, int n){
			color = c;
			num = n;
		}
		
		Color color;
		int num;
	}
	
	public Game() throws SlickException {
		super("2Fuse Prototype");
		rand = new Random();
		
		
	}
	
	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setTargetFrameRate(60);
		gc.setShowFPS(false);
		
		new Sounds();
		try {
			Image image = new Image("res/particles/square.png");
			emitterPair = ParticleIO.loadEmitter("res/particles/pickable.xml");
			emitterPair.addColorPoint(0, Color.white);
			emitterPair.addColorPoint(1, Color.white);

			pSystem = new ParticleSystem(image, 1500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		grid = new Box[12];
		currentPressed = -1;
		
		for(int i = 0 ; i < grid.length; i++){
			grid[i] = new Box(newRandomColor(), 0);
		}
		
		press = new Circle(0,0,0);
		
		score = 0;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		Rectangle r;
		
		int padding = 20;
		
		for(int i = 0, x = 0, y = 0, mod = 0; i < grid.length; i++, x++){			
			if(i == grid.length/2){
				y = boxSize + padding;
				x = 0;
				mod = 0;
			}
			
			if(i == grid.length/4 || i == grid.length/4 + grid.length/2){
				mod = 20;
			}
			
//			if(i == grid.length/2 + grid.length/4){
//				y = boxSize + padding;
//				x = 0;
//				mod = 20;
//			}
			
			int xpos = mod + padding + x * boxSize + x * padding;
			int ypos = y + padding;
			
			r = new Rectangle(xpos, ypos, boxSize, boxSize);
			
			if(i == 0 || i == 2 || i == 3 || i == 5){
				// dont display Q E U and O
			}
			else{
				
				g.setColor(grid[i].color);
				g.fill(r);
				
				g.setColor(Color.white);
				
				g.drawString(grid[i].num + "", xpos + 27, ypos+ 22);
				
				char c = 0;
				switch(i){
				case 0: c = 'Q'; break;
				case 1: c = 'W'; break;
				case 2: c = 'E'; break;
				case 3: c = 'U'; break;
				case 4: c = 'I'; break;
				case 5: c = 'O'; break;
				case 6: c = 'A'; break;
				case 7: c = 'S'; break;
				case 8: c = 'D'; break;
				case 9: c = 'J'; break;
				case 10: c = 'K'; break;
				case 11: c = 'L'; break;
				default: break;
				}
				
//				g.drawString(c + "", xpos + 2, ypos);
				if(currentPressed == i){
					// pressed box
					press.setCenterX(xpos+boxSize/2);
					press.setCenterY(ypos+boxSize/2);
					
					if(press.getRadius() < circleMax)
						press.setRadius(press.getRadius() + (circleMax - press.getRadius())/10);
					
					g.setColor(Color.white);
//					g.fill(new Rectangle(xpos - 5, ypos -5, boxSize + 10, boxSize+10));
					
					g.setLineWidth(4f);
					g.draw(press);
				}
				
			}
		}
				
		g.drawString("Score: " + score, 10, 200);
		
		pSystem.render();
	}


	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		pSystem.update(delta);
		
		Input input = gc.getInput();
		
		if(input.isKeyPressed(Input.KEY_Q)){
			pressBox(0);
		}
		if(input.isKeyPressed(Input.KEY_W)){
			pressBox(1);
		}
		if(input.isKeyPressed(Input.KEY_E)){
			pressBox(2);
		}
		if(input.isKeyPressed(Input.KEY_U)){
			pressBox(3);
		}
		if(input.isKeyPressed(Input.KEY_I)){
			pressBox(4);
		}
		if(input.isKeyPressed(Input.KEY_O)){
			pressBox(5);
		}
		if(input.isKeyPressed(Input.KEY_A)){
			pressBox(6);
		}
		if(input.isKeyPressed(Input.KEY_S)){
			pressBox(7);
		}
		if(input.isKeyPressed(Input.KEY_D)){
			pressBox(8);
		}
		if(input.isKeyPressed(Input.KEY_J)){
			pressBox(9);
		}
		if(input.isKeyPressed(Input.KEY_K)){
			pressBox(10);
		}
		if(input.isKeyPressed(Input.KEY_L)){
			pressBox(11);
		}
		
		if(input.isKeyPressed(Input.KEY_F5)){
			init(gc);
		}
	}
	
	public static void main(String[] args) throws SlickException{
		AppGameContainer appgc = new AppGameContainer(new Game());
		appgc.setDisplayMode(GWIDTH, GHEIGHT, false);
		appgc.start();
	}
	
	private void pressBox(int i){
		press.setRadius(0);
		// a box is already pressed, check if it pairs
		if(currentPressed != -1){
			// unpress
			if(currentPressed == i){
				currentPressed = -1;
				System.out.println("unpress");
				Sounds.press.play();
			}
			// correct pair
			else if(grid[currentPressed].color.equals(grid[i].color) && grid[currentPressed].num == grid[i].num){
				grid[i].num += 1;

				if(grid[i].num > 2){
					grid[i] = new Box(newRandomColor(), 0);
				}
				
				score += grid[i].num;
				
				grid[currentPressed] = new Box(newRandomColor(), 0);
				
				currentPressed = -1;
				
				System.out.println("correct");
				Sounds.correct.play();

				ConfigurableEmitter e = emitterPair.duplicate();
				e.setPosition(100, 100);
				pSystem.addEmitter(e);
			}
			// incorrect pair
			else{
				currentPressed = -1;
				Sounds.wrong.play();
				System.out.println("wrong");
			}
		}
		else{
			currentPressed = i;
			Sounds.press.play();
		}
		
		if(onePairExists()){
			System.out.println("pairs");
		}
		else{
			System.out.println("no pairs");
		}
	}
	
	private boolean onePairExists(){		
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; i < grid.length; j++){
				if(i != j && i!=0 && i!=2 && i!=3 && i!=5 && j!=0 && j!=2 && j!=3 && j!=5){
					if(grid[i].num == grid[j].num && grid[i].color.equals(grid[j].color)){
						System.out.println(i + " " + j);
						return true;
					}
				}
			}
		}
		
		return false;
	}

	private Color newRandomColor() {
		int i = rand.nextInt(3);
		Color c;
		
		switch(i){
		case 0:
			c = new Color(1f, 0.2f, 0.2f);
			break;
			
		case 1:
			c = new Color(0.2f, 0.7f, 0.2f);
			break;
			
		case 2:
			c = new Color(0, 0.4f, 0.8f);
			break;
			
		default:
			c = null;
		}
		
		return c;
	}

}
