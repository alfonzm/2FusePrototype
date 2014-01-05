package prototype2fuse;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Sounds {
	public static Sound press, correct, wrong;
	
	public Sounds() throws SlickException{
		press = new Sound("res/sounds/press.wav");
		correct = new Sound("res/sounds/correct.wav");
		wrong = new Sound("res/sounds/wrong.wav");
	}
}
