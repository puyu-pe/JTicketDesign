package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignStyle;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

    public @NotNull List<String> wrapText(String text, int numberOfCharactersAvailable, int fontWidth) {
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

    public @NotNull List<String> sliceWordInEqualParts(String word, int sliceWidth) {
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

    public @NotNull SweetCell normalize(@NotNull SweetCell cell) {
        if (cell.stringStyle().normalize()) {
            String normalized = Normalizer.normalize(cell.text(), Normalizer.Form.NFD);
            String textNormalized = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized)
                .replaceAll("")
                .replaceAll("[^\\p{ASCII}]", "");
            return new SweetCell(
                textNormalized,
                cell.width(),
                new PainterStyle(cell.painterStyle()),
                new SweetStringStyle(cell.stringStyle())
            );
        }
        return new SweetCell(cell);
    }

    public @NotNull SweetCell justify(SweetCell cell) {
        int spacesAvailable = Math.max(cell.width() - (cell.text().length() * cell.painterStyle().fontWidth()), 0);
        int startSpaces = spacesAvailable / 2;
        int endSpaces = spacesAvailable - startSpaces;
        String pad = cell.stringStyle().pad().toString();
        String justifiedText = switch (cell.stringStyle().align()) {
            case RIGHT -> pad.repeat(spacesAvailable) + cell.text();
            case CENTER -> pad.repeat(startSpaces) + cell.text() + pad.repeat(endSpaces);
            case LEFT -> cell.text() + pad.repeat(spacesAvailable);
        };
        return new SweetCell(
            justifiedText,
            cell.width(),
            new PainterStyle(cell.painterStyle()),
            new SweetStringStyle(cell.stringStyle())
        );
    }

}

