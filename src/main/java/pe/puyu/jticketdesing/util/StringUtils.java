package pe.puyu.jticketdesing.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

  public static String repeat(char character, int length) {
    StringBuilder result = new StringBuilder(length);
    for (int i = 0; i < length; ++i)
      result.append(character);
    return result.toString();
  }

  public static String padRight(String str, int length, char pad) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < length; ++i) {
      if (i < str.length()) {
        result.append(str.charAt(i));
      } else {
        result.append(pad);
      }
    }
    return result.toString();
  }

  public static String padLeft(String text, int length, char pad, int marginRight) {
    String str = text + " ".repeat(marginRight);
    if (length > str.length()) {
      StringBuilder result = new StringBuilder();
      int padLength = length - str.length();
      for (int i = 0; i < length; ++i) {
        if (i < padLength) {
          result.append(pad);
        } else {
          result.append(str.charAt(i - padLength));
        }
      }
      return result.toString();
    }
    return padRight(str, length, pad);
  }

  public static String padBoth(String text, int length, char pad) {
    int start = (length / 2) - (text.length() / 2);
    int end = start + text.length();
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < length; ++i) {
      if (i >= start && i < end) {
        result.append(text.charAt(i - start));
      } else {
        result.append(pad);
      }
    }
    return result.toString();
  }

  public static List<String> split(String text, int characterPerLine) {
    List<String> paragraph = new ArrayList<>();
    var pattern = Pattern.compile(String.format(".{1,%d}", characterPerLine));
    var matcher = pattern.matcher(text);
    while (matcher.find()) {
      paragraph.add(matcher.group());
    }
    return paragraph;
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
