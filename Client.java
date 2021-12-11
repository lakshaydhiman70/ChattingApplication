import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Component.*;
import java.awt.event.*;
public class Client extends JFrame{
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	//declare components
	private JLabel heading=new JLabel("Client Area");
	private JTextArea messageArea=new JTextArea();
	private JTextField messageInput=new JTextField();
	private Font font=new Font("GROBOLD",Font.PLAIN,20);
	// private JFrame f=new JFrame();
	public Client(){
		System.out.println("This is client");
		try{
			System.out.println("sending request to server");
			socket=new Socket("127.0.0.1",69);//IP,PORT
			System.out.println("Connection done");

			// receiving data
  			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

  			// sending data
  			out=new PrintWriter(socket.getOutputStream());
			
			createGUI();
			handleEvents();
  			startReading();
  			// startWriting();


		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void handleEvents(){
		messageInput.addKeyListener(new KeyListener(){
			public void keyTyped(KeyEvent e){

			}
			public void keyPressed(KeyEvent e){

			}
			public void keyReleased(KeyEvent e){
				// System.out.println("key released"+e.getKeyCode());
				if(e.getKeyCode()==10){
					String contentToSend=messageInput.getText();
					messageArea.append("Me :"+contentToSend+"\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
		});
	}
	
	private void createGUI(){
		this.setTitle("Client Messenger");
		this.setSize(300,500);
		this.setLocationRelativeTo(null);//window in the centre
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// f.getContentPane().add(new JPanelWithBackground("1.jpeg"));
		//components
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));//spacing
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);//put inputtext in center

		heading.setIcon(new ImageIcon("name.png"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);//image

		//frame set layout
		this.setLayout(new BorderLayout());

		this.add(heading,BorderLayout.NORTH);
		JScrollPane jScrollPane=new JScrollPane(messageArea);
		this.add(jScrollPane,BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);

		this.setVisible(true);
	}
	public void startReading(){
    		Runnable r1=()->{
    			System.out.println("Thread started");
    			
    			try{
    				while(true || !socket.isClosed()){
    					String msg=br.readLine();
    					if(msg.equals("exit")){
	    					System.out.println("Server terminated the chat");
	    					JOptionPane.showMessageDialog(this,"Server Terminated the chat");
	    					messageInput.setEnabled(false);
	    					socket.close();
	    					break;
    					}
    				//System.out.println("Server: "+msg);
    					messageArea.append("Server: "+msg+"\n");
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    		};
    		new Thread(r1).start();
    	}
    	public void startWriting(){
    		Runnable r2=()->{
    			System.out.println("Writer Started");
    			try{
    		
    				while(true){
    					BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
    					String content=br1.readLine();

    					out.println(content);
    					out.flush();
    					if(content.equals("exit")){
    						socket.close();
    						break;
    					}

    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    		};
    		new Thread(r2).start();
    	} 
	public static void main(String[] args) {
		new Client();
	}
}