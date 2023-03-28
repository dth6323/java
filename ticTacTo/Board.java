package ticTacTo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.DrbgParameters.Capability;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.sound.sampled.*;
public class Board extends JPanel{
	private static final int N=24;
	private static final int M=24;
	public static final int DRAW = 0;
	public static final int Win = 1;
	public static final int LOSE = 2;
	
	private endGameListener endGameListene;
	private Image imgX;
	private Image imgO;
	
	private Cell[][] matrix = new Cell[N][M];
	
	private String currentPlayer = Cell.EMPTY_VALUE;
	
	public void setEndGameListene(endGameListener endGameListene) {
		this.endGameListene = endGameListene;
	}
	public Board(String player) {
		this();
		this.currentPlayer = player;
	}
	public Board() {

		this.initMatrix();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
				if(currentPlayer.equals(Cell.EMPTY_VALUE)) {
					return;
				}
				int x = e.getX();
				int y = e.getY();
				soundClick();
				for(int i=0;i<N;i++){
					 for(int j=0;j<M;j++) {
						Cell cell = matrix[i][j];
						
						int cXstart = cell.getX();
						int cYstart = cell.getY();
						
						int cXEnd = cXstart + cell.getW();
						int cYEnd = cYstart + cell.getH();
						
				 		if(x >= cXstart && x<= cXEnd && y>=cYstart &&y<=cYEnd) {
							if(cell.getValue().equals(Cell.EMPTY_VALUE)) {
								cell.setValue(currentPlayer);
								repaint(); 
							 	int result = checkWin(currentPlayer,i,j);
								if(endGameListene!=null)
								endGameListene.end(currentPlayer, result);
								if(result == LOSE) {
									currentPlayer = currentPlayer.equals(Cell.OVALUE) ? Cell.XVALUE:Cell.OVALUE;
								}
							}
						}
					}
					}
			}
		});
		try {
			imgX= ImageIO.read(getClass().getResource("x.png"));
			imgO= ImageIO.read(getClass().getResource("o.png"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private synchronized void soundClick() {
		Thread thread = new Thread(new  Runnable() {
			
			@Override
			public void run() {
				try {
					Clip clip  = AudioSystem.getClip();
					
					AudioInputStream adAudioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("click.wav"));
					
					clip.open(adAudioInputStream);
					clip.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}
	
	private void initMatrix() {
		for(int i=0;i<N;i++){
			for(int j=0;j<M;j++) {
				Cell cell = new Cell();
				matrix[i][j]=cell;
				
			}
			} 
	}
	public void reset() {
		this.initMatrix();
		this.setCurrentPlayer(Cell.EMPTY_VALUE);
		repaint();
	}
	public int checkWin(String playern,int i, int j) {
		//Chiều ngang
        int count = 0;
        for(int col = 0; col < M; col++){
            Cell cell = matrix[i][col];
            if (cell.getValue().equals(currentPlayer)) {
                count++;
                if(count == 5){
                    System.out.println("Ngang");
                    return Win;
                }
            }else {
                count = 0;
            }
        }


        //Chiều dọc
        count = 0;
        for(int row = 0; row < N; row++){
            Cell cell = matrix[row][j];
            if (cell.getValue().equals(currentPlayer)) {
                count++;
                if(count == 5){
                    System.out.println("Dọc");
                    return Win;
                }
            }else {
                count = 0;
            }
        }

        //Chéo trái
        int min = Math.min(i, j);
        int TopI = i - min;
        int TopJ = j - min;
        count = 0;

        for(;TopI < N && TopJ < M; TopI++, TopJ++){
            Cell cell = matrix[TopI][TopJ];
            if (cell.getValue().equals(currentPlayer)) {
                count++;
                if(count == 5){
                    System.out.println("Chéo trái");
                    return Win;
                }
            }else {
                count = 0;
            }
        }


        //Chéo phải
        min = Math.min(i, j);
        TopI = i - min;
        TopJ = j + min;
        count = 0;

        if(TopJ >= M){
            int du = TopJ - (M - 1);
            TopI = TopI + du;
            TopJ = M - 1;
        }

        for(;TopI < N && TopJ >= 0; TopI++, TopJ--){
            Cell cell = matrix[TopI][TopJ];
            if (cell.getValue().equals(currentPlayer)) {
                count++;
                if(count == 5){
                    System.out.println("Chéo phải");
                    return Win;
                }
            }else {
                count = 0;
            }
        }

		if(this.isfull())
			return DRAW;
		
		return LOSE;
	}
	private boolean isfull() {
		int k=0;
		int Number = N*M;
		for(int i=0;i<N;i++){
			for(int j=0;j<M;j++) {
				Cell cell = matrix[i][j];
				if(!cell.getValue().equals(Cell.EMPTY_VALUE)) {
					k++;
				}
				
			}
			} 
		if(k==Number) 
			return true;
		
		return false;
	}
	public String getCurrentPlayer() {
		return currentPlayer;
}
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
@Override
public void paint(Graphics g) {
	// TODO Auto-generated method stub
	Graphics2D graphic2d = (Graphics2D) g;
	int w = getWidth() / M;
	int h = getHeight() /N;
	graphic2d.setColor(Color.blue);

	graphic2d.fillRect(0, 0, w, h);
	for(int i=0;i<N;i++){

		int k=i;
		for(int j=0;j<M;j++){
			
		int x= j * w;
		int y= i * h;
		
		//cap nhat lai ma tran
		Cell cell = matrix[i][j];
		cell.setY(y);
		cell.setX(x);
		cell.setH(h);
		cell.setW(w);
		Color color= k%  2==0 ? Color.white : Color.gray;
		graphic2d.setColor(color);
		k++;
		graphic2d.fillRect(x, y, w, h);
		if(cell.getValue().equals(Cell.XVALUE)) {
			Image imgImage = imgX;
			graphic2d.drawImage(imgImage, x, y, w, h, this);
			
		}else if(cell.getValue().equals(Cell.OVALUE)) {

			Image imgImage = imgO;
			graphic2d.drawImage(imgImage, x, y, w, h, this);
		}
		//Image img = k%  2==0 ? imgX : imgO;
		//graphic2d.drawImage(img, x, y,w,h,this);
	}
		}
}

}
