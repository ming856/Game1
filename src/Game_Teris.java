import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Game_Teris implements ActionListener {
    public static JFrame s4=new JFrame();
    public final int WIDTH = 1500, HEIGHT = 800;//白框
    public static int Player=Game_client_test.how_players;
    public static boss s5= new boss(Player);
    public Game_Teris(){
        s4.setTitle("Tetris Test");  //title
        s4.setSize(WIDTH, HEIGHT);   //size
        s4.setLayout(null);           //取消JFrame位置
        s4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        s5.setBounds(0, 0, 1500, 700);//黑底
        s4.add(s5);
        s4.addKeyListener(s5);
        s4.setVisible(true);
    }
    public Game_Teris(int i) {
        i=Player;
        new Game_Teris();

    }
    public static void all_player_move(int player, String loc, int player_x) throws InterruptedException {
        int player_y=0;
        switch(player){
            case 0:
                player_y=0;
                break;
            case 1:
                player_y=19;
                break;
            case 2:
                player_y=19;
                break;
            case 3:
                player_y=19;
                break;
        }
        if(loc.equals("右")){
            s5.RIGHT(player_x,player,player_y);
        }
        if(loc.equals("左")){
            s5.LEFT(player_x,player,player_y);
        }
        if(loc.equals("空白")){
            s5.space(player_x,player);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
class boss extends JPanel implements KeyListener{
    public static int[][] map = new int [25][20];
    public static int x, y;
    private int flag = 0;
    private Image b1, b2;//存背景
    private Image[] color = new Image[4];//存方塊
    public static boolean player=false;
    public static boolean die=false;
    public static int player_number;
    public static int timenumber=180;

    public boss(int i){
        Game(i);
    }
    public void Game(int player_number){
        this.player_number=player_number;
        if(player_number>0&&player_number<4)
            player=true;
        else
            player=false;
        //玩家位置
        switch(player_number){
            case 0:
                x=13;y=0;
                break;
            case 1:
                x=5;y=19;
                break;
            case 2:
                x=10;y=19;
                break;
            case 3:
                x=15;y=19;
                break;
        }
        //讀取圖片
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        b1 = Toolkit.getDefaultToolkit().getImage("background1.png");
        b2 = Toolkit.getDefaultToolkit().getImage("background2.png");
        color[3] = Toolkit.getDefaultToolkit().getImage("blue.png");
        color[0] = Toolkit.getDefaultToolkit().getImage("green.png");
        color[1] = Toolkit.getDefaultToolkit().getImage("red.png");
        color[2] = Toolkit.getDefaultToolkit().getImage("yellow.png");

        JLabel TIME = new JLabel("時間");
        TIME.setFont(new Font("", Font.BOLD, 50));
        TIME.setBounds(0, 0, 200, 100);
        TIME.setForeground(Color.white);
        add(TIME);

        JLabel timemachine=new JLabel();
        timemachine.setFont(new Font("", Font.BOLD, 50));
        timemachine.setBounds(0, 100, 200, 100);
        timemachine.setForeground(Color.white);
        add(timemachine);

        JLabel P1 = new JLabel("玩家一");
        P1.setFont(new Font("", Font.BOLD, 50));
        P1.setBounds(1100, 0, 200, 100);
        P1.setForeground(Color.white);
        add(P1);

        JLabel P2 = new JLabel("玩家二");
        P2.setFont(new Font("", Font.BOLD, 50));
        P2.setBounds(1100, 200, 200, 100);
        P2.setForeground(Color.white);
        add(P2);
        JLabel P3 = new JLabel("玩家三");
        P3.setFont(new Font("", Font.BOLD, 50));
        P3.setBounds(1100, 400, 200, 100);
        P3.setForeground(Color.white);
        add(P3);
        newMap();
        newBlock(x,y);
        Timer timer = new Timer(50, new TimerListener());
        timer.start();

        Thread check_ti=new Thread(){
            @Override
            public void run() {

                while (true){
                    if(boss.timenumber==-1){
                        break;
                    }
                    if(boss.timenumber==0){
                        JOptionPane.showMessageDialog(null, "players win");
                        System.exit(0);
                        break;
                    }
                    timemachine.setText(Integer.toString(boss.timenumber));
                    boss.timenumber--;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        check_ti.start();

    }

    public void newBlock(int x,int y) {
        map[x][y]=player_number+1;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //選擇(e.getKeyCode())
        switch (e.getKeyCode()) {
            //監聽到右鍵時
            case KeyEvent.VK_RIGHT:
                if(boss.die&&player_number!=0)
                    break;
                try {
                    if(check(x,y)){
                    System.out.println("move|"+ player_number +"|"+x+"|"+0);
                    Output_thread.output_by_Game_Teris("move|"+ player_number +"|"+x+"|"+0);}
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                RIGHT(x,player_number,y);
                break;
            //監聽到左鍵時
            case KeyEvent.VK_LEFT:
                if(boss.die&&player_number!=0)
                    break;
                try {
                    if(check(x,y)){
                    System.out.println("move|"+ player_number +"|"+x+"|"+1);
                    Output_thread.output_by_Game_Teris("move|"+ player_number +"|"+x+"|"+1);}
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                LEFT(x,player_number,y);
                break;
            //監聽到空白鍵時
            case KeyEvent.VK_SPACE:
                if(boss.die&&player_number!=0)
                    break;
                if(player)
                    break;
                try {
                    System.out.println("move|"+ player_number +"|"+x+"|"+2);
                    Output_thread.output_by_Game_Teris("move|"+ player_number +"|"+x+"|"+2);
                    space(x,player_number);
                } catch (InterruptedException | IOException interruptedException) {
                    interruptedException.printStackTrace();
                }
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < 25; i++) {
            for(int j = 0; j < 20; j++) {
                if(map[i][j] == 0) {
                    if((i+j)%2 == 0)
                        g.drawImage(b1, i*30+3*(i+1)+150, j*30+3*(j+1), null);
                    else
                        g.drawImage(b2, i*30+3*(i+1)+150, j*30+3*(j+1), null);
                } else if(map[i][j] == 1){
                    g.drawImage(color[0], i*30+3*(i+1)+150, j*30+3*(j+1), null);
            }
                else if(map[i][j] == 2){
                    g.drawImage(color[1], i*30+3*(i+1)+150, j*30+3*(j+1), null);
                }
                else if(map[i][j] == 3) {
                    g.drawImage(color[2], i * 30 + 3 * (i + 1) + 150, j * 30 + 3 * (j + 1), null);
                }
            else
                g.drawImage(color[3], i * 30 + 3 * (i + 1) + 150, j * 30 + 3 * (j + 1), null);
            }
        }
        g.drawImage(color[1], 1150, 140, null);
        g.drawImage(color[2], 1150, 340, null);
        g.drawImage(color[3], 1150, 540, null);
            }
    public void newMap() {
        int i, j;
        for (i = 0; i < 25; i++) {
            for (j = 0; j < 20; j++) {
                map[i][j] = 0;
            }
        }
    }
    public void RIGHT(int location_x,int player_number,int location_y) {
        if(check(location_x+1,location_y)==true) {
            map[location_x][location_y] = 0;
            map[location_x + 1][location_y] = player_number+1;
            if(player_number==this.player_number)
            x++;
            repaint();
        }
    }
    public void LEFT(int location,int player_number,int location_y) {
        if(check(location-1,location_y)==true){
        map[location][location_y]=0;
        map[location-1][location_y]=player_number+1;
            if(player_number==this.player_number)
            x--;
            repaint();}
    }
    public void space(int location,int player_number) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=1;
                while(check(location,i)){
                    if(i==19) {
                        if(map[location][i]==boss.player_number+1&&boss.player_number!=0){
                            die=true;
                            try {
                                Output_thread.output_by_Game_Teris("DIE|");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Thread.sleep(100);
                            map[location][i-1]=0;
                            map[location][i]=0;
                            JOptionPane.showMessageDialog(null, "you die");
                        }
                        map[location][i]=1;
                        map[location][i-1]=0;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        map[location][i]=0;
                        break;
                    }
                    map[location][i]=player_number+1;
                    if(i==1){
                        repaint();
                        i++;
                        continue;
                    }
                    map[location][i-1]=0;
                    i++;
                    repaint();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                   // System.out.println("子彈 x:" +i);
                }
            }
        }).start();
            }
    public boolean check(int x,int y){
        if(x>24||x<0||y<0||y>20)
            return false;
        else
            return true;
    }
    }
class TimerListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        Game_Teris.s4.repaint();
    }
}






