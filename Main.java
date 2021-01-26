
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main 
{

	public static void main(String[] args) 
	{
		
		JFrame frame = new JFrame();
		Gameplay game = new Gameplay();
		
		frame.setBounds(10, 10, 700, 600);
		frame.setTitle("Brick Break");
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(game);
		frame.setVisible(true);
	
	}
}
