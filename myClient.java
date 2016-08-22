
import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class myClient {


    public static void main(String[] args) throws Exception{

        File f1=new File("input.txt");
        FileReader fr=new FileReader(f1);
        BufferedReader br=new BufferedReader(fr);
        Socket s=new Socket("127.0.0.1",7860);
	System.out.println("connection Established at 7860 port:");

        DataInputStream sc1;
        sc1 = new DataInputStream(s.getInputStream());
        String no=br.readLine();
        System.out.println("\nplain Text \t: "+no);
        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
        DesEncrypter  encrypter = new DesEncrypter (key);
        String encrypted = encrypter.encrypt(no);
	System.out.println("\nDes key generated:");

        PrintStream p=new PrintStream(s.getOutputStream());
        p.println(encrypted);
	System.out.println("\nEncrypted text is "+encrypted);
        String temp;
        temp = sc1.readLine();
        String s3=encrypter.decrypt(temp);

        File file2=new File("output.txt");
        FileWriter fw=new FileWriter(file2);
        BufferedWriter bw=new BufferedWriter(fw);
        bw.write(s3);
	System.out.println("\nDecrypted Text Is: \n"+s3);
        bw.close();


    }

}

class DesEncrypter {
  Cipher ecipher;

  Cipher dcipher;

  DesEncrypter(SecretKey key) throws Exception {
    ecipher = Cipher.getInstance("DES");
    dcipher = Cipher.getInstance("DES");
    ecipher.init(Cipher.ENCRYPT_MODE, key);
    dcipher.init(Cipher.DECRYPT_MODE, key);
  }

  public String encrypt(String str) throws Exception {

    byte[] utf8 = str.getBytes("UTF8");

    byte[] enc = ecipher.doFinal(utf8);

    // Encode bytes to base64 to get a string
    return new sun.misc.BASE64Encoder().encode(enc);
  }

  public String decrypt(String str) throws Exception {
    // Decode base64 to get bytes
    byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

    byte[] utf8 = dcipher.doFinal(dec);

    // Decode using utf-8
    return new String(utf8, "UTF8");
  }
}
