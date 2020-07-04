import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Game_client_test extends JFrame implements ActionListener {
    public static int playernumber;
    JPanel contentPane;//總面板設定
    JPanel panel1 = new JPanel();//面板設定
    JLabel title1 = new JLabel("小東出品，必出精品");//小標題標籤
    JButton login_button = new JButton("Login");//登入按鈕

    JLabel ID_Name = new JLabel("UserName");//使用者名稱標籤
    JTextField PlayerName_JTextField = new JTextField("User");//Name(使用者輸入之視窗)
    JLabel IP = new JLabel("IP :");//名稱標籤_IP
    JTextField ip_connect_JTextField = new JTextField("127.0.0.1");//IP地址框

    JLabel port_Label = new JLabel("port");//端口號標籤
    JTextField port_JTextField = new JTextField("5200");//port標籤

    Socket socket = null; //網路連線IO之客戶端用

    public static boolean Gamestart=false;
    //String message = "";
    public static boolean checkZoommaster =false;
    public static int how_players=0;
    public Game_client_test() {
        login();
    }

    private void login() {
        contentPane = (JPanel) this.getContentPane();

        title1.setFont(new Font("宋體", 0, 14));
        title1.setBounds(new Rectangle(0, 0, 150, 28));

        ID_Name.setFont(new Font("宋體", 0, 14));
        ID_Name.setBounds(new Rectangle(0, 23, 150, 28));
        PlayerName_JTextField.setBounds(new Rectangle(80, 26, 100, 24));//ipName 輸入

        IP.setFont(new Font("宋體", 0, 14));//標籤
        IP.setBounds(new Rectangle(0, 50, 150, 28));
        ip_connect_JTextField.setBounds(new Rectangle(80, 52, 100, 24));//地址

        port_Label.setFont(new Font("宋體", 0, 14));//標籤
        port_Label.setBounds(new Rectangle(0, 70, 150, 28));
        port_JTextField.setBounds(new Rectangle(80, 78, 100, 24));//地址

        login_button.setBounds(new Rectangle(0, 110, 180, 25));
        login_button.setFont(new Font("Dialog", 0, 14));
        login_button.setBorder(BorderFactory.createEtchedBorder());

        login_button.setActionCommand("login");

        this.setSize(new Dimension(210, 180));
        this.setLocationRelativeTo(null);

        panel1.setLayout(null);
        panel1.setBounds(10, 0, 540, 160);
        panel1.add(title1);//小東出品，必出精品
        panel1.add(PlayerName_JTextField);
        panel1.add(ID_Name);
        panel1.add(IP);
        panel1.add(ip_connect_JTextField);
        panel1.add(port_Label);
        panel1.add(port_JTextField);
        panel1.add(login_button);

        contentPane.setLayout(null);
        this.setTitle("login");
        contentPane.add(panel1);

        login_button.addActionListener(this);

    }



    public static void main(String args[]) throws Exception {
        JFrame.setDefaultLookAndFeelDecorated(true);
        Game_client_test s1 = new Game_client_test();
        System.out.println("程式已啟動");
        s1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//設定文件關閉動作時，關閉進程
        s1.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login_button) {//連接按钮
            JOptionPane.showMessageDialog(null, "Loading&connect....");
            String PlayerName = PlayerName_JTextField.getText().trim();
            String ip = ip_connect_JTextField.getText().trim();
            int port = Integer.parseInt(port_JTextField.getText().trim());
            if(ConnectGameServer(PlayerName,ip,port)==true){
                JOptionPane.showMessageDialog(null, "success connect");
                System.out.println("login success");

                //連線後將Socket給主要遊戲視窗
                GameFrame game=new GameFrame(socket);

                //test05152 game=new test05152();
                //game.setVisible(true);
                this.dispose();


            }
            else
                JOptionPane.showMessageDialog(null, "connect error");


        }
    }

    private boolean ConnectGameServer(String playerName, String ip, int port) {
        boolean checkconnect=true;
        if(login_button.getText().trim().equals("Login")){//判斷依據可能必須更改
            login_button.setEnabled(false);
                login_button.setText("Connect");
                try{
                    socket=new Socket(ip,port);
                    panel1.repaint();
                    Input_thread t=new Input_thread(socket);//INput_thread
                    Output_thread t1=new Output_thread(socket,playerName);//Output_thread
                    t.start();t1.start();
                    checkconnect=true;
                    return checkconnect;
                    //contentPane.close();
                }catch(UnknownHostException e1){
                    System.err.println(e1);
                    System.err.println("UnknownHostException");
                        checkconnect=false;
                    login_button.setText("Login");
                    login_button.setEnabled(true);
                }catch(IOException e2){
                    System.err.println(e2);
                    System.err.println("輸入輸出異常");
                    checkconnect=false;
                    login_button.setText("Login");
                    login_button.setEnabled(true);
                }

        }else {
            checkconnect = false;
            login_button.setEnabled(true);
        }
        return checkconnect;
        }
}
class Output_thread extends Thread implements Runnable{
    public static Socket socket;
    public static BufferedReader reader;
    public static PrintWriter out;
    String  line;
    String Name;

    public Output_thread(Socket socket,String Name){ this.socket = socket;this.Name=Name;
    }
    public static void output_by_Game_Teris(String s) throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(socket.getOutputStream());
        out.println(s);
        out.flush();
    }
    @Override
    public void run() {

        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(socket.getOutputStream());
            out.println("playerProfile |" + Name );
            out.flush();

            Thread gamestart_thread = new Thread(new Runnable() {
                public void run(){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while(true){
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(Game_client_test.Gamestart==true){

                            out.println("Gamestart|");
                            out.flush();
                            Game_client_test.Gamestart=false;
                            break;
                        }
                    }}
            });
            gamestart_thread.start();
            line = reader.readLine();
            while (!"end".equalsIgnoreCase(line)) {
               // if(Gamestart=true){}
                //將從鍵盤獲取的資訊給到伺服器
                out.println("chat|"+Name+"|"+line);
               out.flush();;
                //顯示輸入的資訊
                line = reader.readLine();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


}}
class Input_thread extends Thread implements Runnable {//取得資料資訊
    Socket socket;
    BufferedReader in ;
    public static String ss[]=new String[10];

    public Input_thread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {

                String md=in.readLine();
                ss=md.split("\\|");
                if(md.startsWith("playernumber")) {
                    Game_client_test.playernumber = Integer.parseInt(ss[1]);
                    continue;
                }
                if(md.startsWith("RoomMaster")){
                    Game_client_test.checkZoommaster=true;
                    continue;
                }
                if(md.startsWith("boss")){
                    System.out.println("You are the boss");
                    Game_client_test.how_players=0;
                    new Game_Teris(Game_client_test.how_players);
                    GameFrame.s2.dispose();
                    continue;
                }
                if(md.startsWith("player1")){
                    System.out.println("You are the player1");
                    Game_client_test.how_players=1;
                    new Game_Teris(Game_client_test.how_players);
                    GameFrame.s2.dispose();
                    continue;
                }
                if(md.startsWith("player2")){
                    System.out.println("You are the player2");
                    Game_client_test.how_players=2;
                    new Game_Teris(Game_client_test.how_players);
                    GameFrame.s2.dispose();
                    continue;
                }
                if(md.startsWith("player3")){
                    System.out.println("You are the player3");
                    Game_client_test.how_players=3;
                    new Game_Teris(Game_client_test.how_players);
                    GameFrame.s2.dispose();
                    continue;
                }
               if(md.startsWith("move")){
                    //move | 玩家 | 原本位置 |移動方向
                   if(Game_client_test.how_players==Integer.parseInt(ss[1]))
                       continue;
                   if (Integer.parseInt(ss[3])==0){//方向 0.右 1.左 2.space
                        Game_Teris.all_player_move(Integer.parseInt(ss[1]),"右",Integer.parseInt(ss[2]));
                       continue;
                   }
                   else if(Integer.parseInt(ss[3])==1){
                        Game_Teris.all_player_move(Integer.parseInt(ss[1]),"左",Integer.parseInt(ss[2]));
                        continue;
                    }
                   else {
                       Game_Teris.all_player_move(Integer.parseInt(ss[1]),"空白",Integer.parseInt(ss[2]));
                       continue;
                   }
                }

               if(md.startsWith("ALLDIE")){
                   if(Game_client_test.how_players==0) {
                       boss.timenumber = -1;
                       JOptionPane.showMessageDialog(null, "boss win");
                       System.exit(0);

                   }
                   else{
                       boss.timenumber=-1;
                       JOptionPane.showMessageDialog(null, "players lose");
                       System.exit(0);


                   }
               }
                System.out.println(md);

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}





