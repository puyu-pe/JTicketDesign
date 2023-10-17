package pe.puyu.jticketdesing.util;

import java.util.LinkedList;
import java.util.List;

public class StringUtils {

  public static String repeat(char character, int length) {
    StringBuilder result = new StringBuilder(length);
    for (int i = 0; i < length; ++i)
      result.append(character);
    return result.toString();
  }

  public static String cutOverflow(String text, int widthCell, int fontsize) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < text.length(); ++i) {
      if ((i + 1) * fontsize > widthCell) {
        break;
      }
      result.append(text.charAt(i));
    }
    return result.toString();
  }

  public static List<String> wrapText(String text, int widthCell, int fontsize) {
    var words = text.split("\\s+");
    List<String> wrapText = new LinkedList<>();
    for (int i = 0; i < words.length; ++i) {
      int accum = 0;
      List<String> row = new LinkedList<>();
      String word = (i == (words.length - 1)) ? words[i] : words[i] + " ";
      while (accum + (word.length() * fontsize) <= widthCell) {
        row.add(word);
        accum += (word.length() * fontsize);
        ++i;
        if(i >= words.length)
         break;
        word = (i == (words.length - 1)) ? words[i] : words[i] + " ";
      }
      if (accum == 0) {
        wrapText.add(cutOverflow(word, widthCell, fontsize));
      } else {
        wrapText.add(String.join("", row));
        --i;
      }
    }
    return wrapText;
  }
}
