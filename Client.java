import java.net.*;
import java.io.*;

import java.awt.event.*;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout;

class Client extends JFrame{
    //at the time of object creation window also create( bcoz of extend)

    ServerSocket serverSocket;
    Socket socket;

    BufferedReader br;
    PrintWriter pw;


    //---Declare Component---
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto", Font.PLAIN,20);

    //--Constructor--
    public Client(){
        try{
            System.out.println("Sending Req to Server...");
            socket = new Socket("127.0.0.1",8887);
            System.out.println("Connection Established...");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

            createGUI();
            handleEvents();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private  void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e){

            }

            @Override
            public void keyPressed(KeyEvent e){
                
            }

            @Override
            public void keyReleased(KeyEvent e){
                System.out.println("Key  Released: " +e.getKeyCode());
                if(e.getKeyCode() ==10){
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me: "+contentToSend +"\n");
                    pw.println(contentToSend);
                    pw.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    } 

    private  void createGUI(){
        this.setTitle("Client Messager");
        this.setSize(700,700);
        this.setLocationRelativeTo(null);  //window open in center
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        //heading.setIcon(new ImageIcon("chatIcon.jpg").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        heading.setIcon(new ImageIcon("Icon.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        messageArea.setEditable(false);
        //--Frame ka Layout BorderLayout( divided into North, Center, South)--
        this.setLayout(new BorderLayout());
        //--Adding component to Frame--
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        //this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);
    }


    public void startReading(){
        //--Thread 1: Took Data from CLient(Incoming Messages)--
        Runnable r1=()->{
            System.out.println("reader Start...");
            try{
                while(true){
                    String incomingMessage = br.readLine();
                    if(incomingMessage.equals("quit")){
                        System.out.println("Server Terminated the Chat...");
                        JOptionPane.showMessageDialog(this, "Server Terminated");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Server: " +incomingMessage);
                    messageArea.append("Server: " +incomingMessage +"\n");
                } 
            }catch(Exception e){
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        //--Thread 2:Took User Input and Send to Server(Outgoing Messages)--
        Runnable r2=()->{
            System.out.println("Writer Started...");
        try{
            while(!socket.isClosed()){
                BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                String content = br1.readLine();
                pw.println(content);
                pw.flush();

                if(content.equals("exit")){
                    socket.close();
                    break;  
                }
            }
            System.out.println("Connection Closed...");
        }catch(Exception e){
            e.printStackTrace();
        }
        };
        new Thread(r2).start();
    }

    public static void main (String[] args) {
        System.out.println("Client Started...");
        new Client();

    }
}