package ticTacTo;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.channels.IllegalBlockingModeException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.Timer;

public class main {

private static Timer timer = new Timer();
private static int sec = 0;
private static JLabel jlbtime;
private static Board board;
private static JButton btnStart;

public static void main(String[] args) {
//	Board board = (choice==0) ? new Board(Cell.OVALUE):new Board(Cell.XVALUE);
	board = new Board();
	board.setEndGameListene(new endGameListener() {
		
		@Override
		public void end(String player, int st) {

			if(st == Board.Win) {
				//System.out.println("nguoi choi "+currentPlayer+"win");
				JOptionPane.showMessageDialog(null, "nguoi choi "+player+"win");

				stopGame();
			}
			if(st == Board.DRAW) {
				JOptionPane.showMessageDialog(null, "Hoa roi");
				stopGame();
			}
		}
	});
    JPanel jPanel = new JPanel();
    BoxLayout boxLayout = new BoxLayout(jPanel,BoxLayout.Y_AXIS);
    jPanel.setLayout(boxLayout);

    board.setPreferredSize(new Dimension(600, 600));

    FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(flowLayout);

     btnStart = new JButton("Start");
    //Timer và TimerTask
    jlbtime = new JLabel("0:0");
    bottomPanel.add(jlbtime);
    bottomPanel.add(btnStart);

    btnStart.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(btnStart.getText().equals("Start")){
                startgame();
            }else{
            	stopGame();
            }
        }
    });

    jPanel.add(board);
    jPanel.add(bottomPanel);

    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

    JFrame jFrame = new JFrame("Game co ca ro 9 o");
    jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jFrame.setResizable(true);
    jFrame.add(jPanel);

    int x =  ((int)dimension.getWidth() / 2 - (jFrame.getWidth() / 2));
    int y = ((int) dimension.getHeight() / 2 - (jFrame.getHeight() / 2));

    jFrame.setLocation(x, y);

    jFrame.pack();
    //show ra frame 
    jFrame.setVisible(true);
	
}
	private static void startgame() {
		
		 int choice = JOptionPane.showConfirmDialog(null, "Người chơi O đi trước đúng không?", "Ai là người đi trước?", JOptionPane.YES_NO_OPTION);

	        board.reset();
	        String currentPlayer = (choice == 0) ? Cell.OVALUE : Cell.XVALUE;

	        board.setCurrentPlayer(currentPlayer);
	        //Đếm ngược
	        sec = 0;
	        
	        //jlbtime.setText("0:0");
	        timer.cancel();
	        timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	            @Override
	           public void run() {
	                sec++;
	                String value = sec / 60 + " : " + sec % 60;
	                jlbtime.setText(value);
	            }
	        }, 1000, 1000);
	        btnStart.setText("Stop");

}
	private static void stopGame() {
		btnStart.setText("Start");
		sec = 0;
		jlbtime.setText("0:0");
		timer.cancel();
		timer = new Timer();
		board.reset();
	}
}
