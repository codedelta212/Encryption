import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class Codex
{
  System.out.println("test1");
  static DateFormat dateFormat;
  
  public static void fileEncrypt(String sourcePath, String destPath, String key)
  {
    try{
    File srcFile = new File(sourcePath);
    Scanner fileScanner = new Scanner(srcFile);
    ArrayList<String> textList = new ArrayList<String>();
    while(fileScanner.hasNext())
    {
     String line = fileScanner.nextLine();
     textList.add(encrypt(line, key));
    }
    File destFile = new File(destPath);
    PrintWriter writer = new PrintWriter(destFile);
    for(int i=0; i<textList.size(); i++)
    {
     writer.println(textList.get(i)); 
    }
    writer.close();
    fileScanner.close();
    
    }catch(IOException ioe){}
  }
  public static void fileDecrypt(String sourcePath, String destPath, String key)
  {
    try{
    File srcFile = new File(sourcePath);
    Scanner fileScanner = new Scanner(srcFile);
    ArrayList<String> textList = new ArrayList<String>();
    while(fileScanner.hasNext())
    {
     String line = fileScanner.nextLine();
     textList.add(decrypt(line, key));
    }
    File destFile = new File(destPath);
    PrintWriter writer = new PrintWriter(destFile);
    for(int i=0; i<textList.size(); i++)
    {
     writer.println(textList.get(i)); 
    }
    writer.close();
    fileScanner.close();
    
    }catch(IOException ioe){}
  }
  public static String encrypt(String plainText, String plainKey)//String message, String key)
  {

    plainText = addTimeStamp(plainText);
    plainText = getNumRep(plainText);
    plainKey = getNumRep(plainKey);
    String key = plainKey;
    String message = plainText;
    String nKey = matchKeyToMessage(key, message);
    String cipherText = createCipherText(message, nKey);
    ArrayList<Character> alphabet = new ArrayList<Character>(); 
    alphabet = createAlphabet(alphabet,nKey);
    
    return translateToCipher(alphabet, cipherText); 
  }
  private static String addTimeStamp(String plainText)
  {
//  {
//    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
// Date date = new Date();
// String currDateNTime = "[XDATESTAMPX"+dateFormat.format(date)+"XDATESTAMPX]:"+plainText;
// 
//    return currDateNTime;
//    
    return plainText;
    
  }
  private static String removeTimeStamp(String plainText)
  {
    return plainText;
    
//   String [] text = plainText.split("XDATESTAMPX]:");
//   return text[1];
    
  }
  private static String getNumRep(String s)
  {
    
    String output = "";
    for(int i=0; i<s.length(); i++)
    {
      int numVal = s.charAt(i) - 28;
      if(numVal < 10) { output += 0; }
      output += numVal;
    }
    return output;
    
  }
  private static String getStringRep(String s)
  {
    
    String output = "";
    //System.out.println("size of string :"+s.length());
    for(int i=0; i<(s.length()); i+=2)
    {
      try{
      String pairVal = ""+s.charAt(i);

      pairVal += s.charAt(i+1);

      int numVal = Integer.parseInt(pairVal);
      numVal += 28;
      char cVal = (char)numVal;
      output += cVal;
      }catch(Exception e){continue;}
    }
    return output;
    
  }
  private static ArrayList<Character> createAlphabet(ArrayList<Character> alphabet, String nKey)
  {
    String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    for(int i=0; i<letters.length(); i++)
    {
      alphabet.add(letters.charAt(i)); 
    }
    String cipherBet = "";
    String alphaKey = nKey;
    if(nKey.length() < 36)
    {
      int diff = 36 - nKey.length();
      for(int i=0; i<diff; i++)
      {
        int index = i % nKey.length();
        alphaKey += nKey.charAt(index);  
      } 
    }
    for(int i=0; i<alphaKey.length(); i++)
    {
      int index = (int) (alphaKey.charAt(i) - 48);
      char currChar = alphabet.remove(index);
      alphabet.add(currChar);
    } 
    return alphabet;
  }
  
  private static String matchKeyToMessage(String key, String message)
  {
    String nKey = key;
    if(key.length() < message.length())
    {
      int addAmnt = message.length() - key.length();
      
      for(int i = 0; i < addAmnt; i++)
      {
        int index = i % key.length();
        nKey += key.charAt(index);
      }
    }
  
    
    return nKey; 
  }
  private static String createCipherText(String message, String nKey)
  {
    String cipherText = "";
    for(int i = 0; i< message.length(); i++)
    {
      int messVal = (int) (message.charAt(i) -48);  
      int keyVal = (int)( nKey.charAt(i) -48);
      int cipherVal = messVal + keyVal;
      cipherVal = cipherVal % 10;
      cipherText += cipherVal;
    }
    return cipherText;
  }
  private static String translateToCipher(ArrayList<Character> alphabet, String cipherText)
  {
    String finalCipher = "";
    for(int i=0; i<cipherText.length(); i++)
    {
      int index = (int)(cipherText.charAt(i) - 48);
      finalCipher += alphabet.get(index);
      for(int j=0; j<9; j++)
      {
        char currChar = alphabet.remove(0);
        alphabet.add(currChar);
      }
    }
    return finalCipher;
  }
  public static String decrypt(String cipher, String key)
  {
    key = getNumRep(key);
    String nKey = key;
    nKey = matchKeyToMessage(nKey, cipher);
    ArrayList<Character> alphabet = new ArrayList<Character> ();
    alphabet = createAlphabet(alphabet, nKey);
    String localcipher = cipher;
    cipher = finalCipherToCipher(alphabet, localcipher); 
    String numRep = getPlainText(cipher, nKey);
    String plainText = getStringRep(numRep);
    plainText = removeTimeStamp(plainText);
    return plainText;
  }
  private static String getPlainText(String cipher, String nKey)
  {
    
    String plainText = "";
    for(int i=0; i<cipher.length(); i++)
    {
      try{
      int ciVal = (int) (cipher.charAt(i)-48);
      int keyVal = (int)( nKey.charAt(i)-48);
      int nVal = ciVal += 10;
      nVal -= keyVal;
      nVal = nVal % 10;
      plainText += nVal;
      }catch(Exception e){continue;}
    }
    
    return plainText;
  }
  private static String finalCipherToCipher(ArrayList<Character> alphabet, String cipherText)
  {
    String finalCipher = ""; 
    int cycles = cipherText.length();
    cycles *= 9;
    int marker = cycles % 36;
    for(int i=0; i<marker; i++)
    {
      char currChar = alphabet.remove(0);
      alphabet.add(currChar);
    }
    String rev = "";
    for(int i=cipherText.length()-1; i>=0; i--)
    { 
      rev += cipherText.charAt(i); 
    }
    String finRev = "";
    for(int i=0; i<rev.length(); i++)
    {
      for(int j=0; j<9; j++)
      {
        char letter = alphabet.remove(alphabet.size()-1);
        alphabet.add(0,letter);
      }
      char currChar =  (char)(rev.charAt(i));
      int alphaindex =  alphabet.indexOf(currChar);
      finRev = alphaindex + finRev;
    }
    return finRev; 
  }
  private static boolean checkSize(String message, String key)
  {
    if(message.length() != key.length())
    {
      return false;
    }
    return true;
  }
  
}