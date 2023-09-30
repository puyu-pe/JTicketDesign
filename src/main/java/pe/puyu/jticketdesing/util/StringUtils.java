package pe.puyu.jticketdesing.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

  public static String repeat(char character, int length) {
    StringBuilder result = new StringBuilder(length);
    for (int i = 0; i < length; ++i)
      result.append(character);
    return result.toString();
  }

  public static String padRight(String str, int length, char character) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < length; ++i) {
      if (i < str.length()) {
        result.append(str.charAt(i));
      } else {
        result.append(character);
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

  public static String padLeft(String text, int length, char character, int marginRight) {
    String str = text + " ".repeat(marginRight);
    if (length > str.length()) {
      StringBuilder result = new StringBuilder();
      int padLength = length - str.length();
      for (int i = 0; i < length; ++i) {
        if (i < padLength) {
          result.append(character);
        } else {
          result.append(str.charAt(i - padLength));
        }
      }
      return result.toString();
    }
    return padRight(str, length, character);
  }

}
