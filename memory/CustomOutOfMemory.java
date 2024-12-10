import java.util.List;
import java.util.ArrayList;

class CustomStackOverFlow {
  public static void main(String[] args) {
    List<byte[]> arr = new ArrayList<>();
    
    try {
      while (true) {
        arr.add(new byte[1024]);  
      } 
    } catch (OutOfMemoryError e) {
      System.out.println(arr.size());
    }
  }
}