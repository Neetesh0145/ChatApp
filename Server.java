import java.net.*;
import java.io.*;

class Server{

    ServerSocket serverSocket;
    Socket socket;

    BufferedReader br;
    PrintWriter pw;

    //--Constructor--
    public Server(){
        try{
            serverSocket = new ServerSocket(8887);
            System.out.println("Server is ready to connection...");
            System.out.println("Waiting...");
            socket=serverSocket.accept();
            System.out.println("Connection Established...");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw=new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void startReading(){
        //--Thread 1: Took Data from CLient(Incoming Messages)--
        Runnable r1=()->{
            System.out.println("reader Start...");
            try{
            while(true){
                String incomingMessage = br.readLine();
                if(incomingMessage.equals("quit")){
                    System.out.println("Client Terminated the Chat...");
                    socket.close();
                    break;
                }
                System.out.println("Client: " +incomingMessage);
            } 
            }catch(Exception e){
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        //--Thread 2: Took Data from CLient(Incoming Messages)--
        Runnable r2=()->{
            System.out.println("Writer Started...");
            try{
            while(true){
                BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                String content = br1.readLine();
                pw.println(content);
                pw.flush();

                pw.println(br);
                String incomingMessage=br.readLine();
            }
            }catch(Exception e){
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }

    public static void main (String[] args) {
        System.out.println("Server Started...");
        new Server();
    }
}