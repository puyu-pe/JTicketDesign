package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignStyle;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SweetDesignHelper {

    private final @NotNull SweetProperties _properties;
    private final @NotNull PrinterDesignStyle _defaultStyle;

    public SweetDesignHelper(@NotNull SweetProperties properties, @NotNull PrinterDesignStyle defaultStyle) {
        this._properties = properties;
        this._defaultStyle = defaultStyle;
    }
//
//    public @NotNull PainterStyle makePainterStyleFor(@NotNull String className, @NotNull Integer index, @Nullable Map<String, PrinterDesignStyle> styles) {
//
//    }
//
//    public @NotNull SweetStringStyle makeSweetStringStyleFor(@NotNull String className, @NotNull Integer index, @Nullable Map<String, PrinterDesignStyle> styles) {
//
//    }

    public static @NotNull List<String> wrapText(String text, int numberOfCharactersAvailable, int fontWidth) {
        numberOfCharactersAvailable = Math.max(0, numberOfCharactersAvailable);
        fontWidth = Math.max(0, fontWidth);
        String[] splitWords = text.split("\\s+"); // divide in words
        List<String> wrappedText = new LinkedList<>();
        List<String> words = new LinkedList<>();
        String midCharacter = "-";
        for (String word : splitWords) {
            int sliceWidth = fontWidth == 0 ? 0 : numberOfCharactersAvailable / fontWidth;
            words.addAll(sliceWordInEqualParts(word, sliceWidth));
        }
        for (int i = 0; i < words.size(); ++i) {
            StringBuilder newString = new StringBuilder();
            String currentWord = words.get(i);
            int numberCharactersCoveredByCurrentWord = currentWord.length() * fontWidth;
            int midCharacterCovered = 0;
            while ((newString.length() * fontWidth) + numberCharactersCoveredByCurrentWord + midCharacterCovered <= numberOfCharactersAvailable) {
                newString.append(newString.isEmpty() ? currentWord : midCharacter + currentWord);
                midCharacterCovered = 1;
                ++i;
                if (i >= words.size())
                    break;
                currentWord = words.get(i);
                numberCharactersCoveredByCurrentWord = currentWord.length() * fontWidth;
            }
            wrappedText.add(newString.toString());
            --i;
        }
        return wrappedText;
    }

    public static @NotNull List<String> sliceWordInEqualParts(String word, int sliceWidth) {
        int lastPart = sliceWidth <= 0 ? 0 : word.length() % sliceWidth;
        lastPart = lastPart > 0 ? 1 : 0;
        int numberOfParts = sliceWidth <= 0 ? 0 : word.length() / sliceWidth + lastPart;
        List<String> slicedWords = new LinkedList<>();
        for (int currentPart = 0, startIndex = 0; currentPart < numberOfParts; ++currentPart, startIndex += sliceWidth) {
            int endIndex = Math.min(startIndex + sliceWidth, word.length());
            slicedWords.add(word.substring(startIndex, endIndex));
        }
        return slicedWords;
    }

    public @NotNull SweetProperties getProperties() {
        return _properties;
    }

}

