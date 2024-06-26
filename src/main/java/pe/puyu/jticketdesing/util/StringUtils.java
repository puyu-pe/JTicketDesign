package pe.puyu.jticketdesing.util;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

  public static String repeat(char character, int length) {
    length = Math.max(length, 0);
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
      String word = (i == (words.length - 1)) ? words[i] : words[i] + " "; // agregar espacios intermedios
      while (accum + (word.length() * fontsize) <= widthCell) { // mientras la palabra no cubra el ancho de la celda
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

  public static String normalize(String text){
    String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
    return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized)
      .replaceAll("")
      .replaceAll("[^\\p{ASCII}]", "");
  }
}
