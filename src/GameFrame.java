import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

public class GameFrame extends Thread implements ActionListener {
    Socket sc;
    public static JFrame s2=new JFrame("mian");;
    public static JButton Gamestart_button=new JButton("Gamestart");
    public GameFrame(){
        Gamestart_function();
    }
    public GameFrame(Socket sc){
        this.sc = sc;
        JButton player1 =new JButton("player1");
        JButton player2 =new JButton();
        JButton player3 =new JButton();
        JButton player4 =new JButton();
        JButton playerCompete =new JButton("repair");
        //JButton Gamestart =new JButton("Gamestart");
        Container cp=s2.getContentPane();
        cp.setLayout(null);  //取消預設之 BorderLayout
        //設定視窗大小
        s2.setSize(500,330);
        player1.setBounds(20,20,100,200);
        cp.add(player1);
        player2.setBounds(140,20,100,200);
        cp.add(player2);
        player3.setBounds(260,20,100,200);
        cp.add(player3);
        player4.setBounds(380,20,100,200);
        cp.add(player4);

       // playerCompete.setBounds(260,240,100,50);
       //cp.add(playerCompete);
        Gamestart_button.setBounds(380,240,100,50);
        cp.add(Gamestart_button);

        playerCompete.addActionListener(this::actionPerformed);
        Gamestart_button.addActionListener(this::actionPerformed);


        Gamestart_button.setEnabled(false);
        //設定視窗位置置中
        s2.setLocationRelativeTo(null);
        //設定視窗可見
        s2.setVisible(true);
        //設定視窗關閉時一同關閉程式
        s2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Thread thread = new Thread(new Runnable() {
                public void run(){

                    while(true){
                        if(Game_client_test.checkZoommaster==true){
                            JLabel master = new JLabel("你是房主");
                            master.setBounds(140,240,100,50);
                            cp.add(master);
                            cp.remove(playerCompete);
                            s2.repaint();
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        switch(Game_client_test.playernumber){
                            case 1:
                                player2.setText("");
                                player3.setText("");
                                player4.setText("");
                                s2.repaint();
                                break;
                            case 2:
                                player2.setText("player2");
                                player3.setText("");
                                player4.setText("");
                                s2.repaint();
                                break;
                            case 3:
                                player2.setText("player2");
                                player3.setText("player3");
                                player4.setText("");
                                s2.repaint();
                                break;
                            case 4:
                                player2.setText("player2");
                                player3.setText("player3");
                                player4.setText("player4");
                                s2.repaint();
                                break;
                        }
                        if(Game_client_test.checkZoommaster==true&&Game_client_test.playernumber>1)
                            Gamestart_button.setEnabled(true);
                        else
                            Gamestart_button.setEnabled(false);

                    }
                }
        });
        thread.setDaemon(true);
        thread.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    if(e.getSource()==Gamestart_button){
        s2.dispose();
        //Game_Teris s3=new Game_Teris();
       Game_client_test.Gamestart=true;
    }
    }
    public void Gamestart_function(){
        s2.dispose();
        Game_Teris s3=new Game_Teris(Game_client_test.how_players);

    }

}
class playernumber_Thread extends JApplet implements  Runnable{

    @Override
    public void run() {
        while(true) {
            // 動畫的狀態改變、緩衝區繪圖
            this.repaint();  // 重繪畫面
            // 執行緒暫停 50 毫秒
            try {
                Thread.sleep(50); // 避免Busy loop
            }
            catch(InterruptedException e) {
                // 例外處理
            }
        }
    }
}

