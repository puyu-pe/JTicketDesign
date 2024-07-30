package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.designer.img.SweetImageInfo;
import pe.puyu.jticketdesing.domain.designer.text.SweetCell;
import pe.puyu.jticketdesing.domain.designer.text.SweetStringStyle;
import pe.puyu.jticketdesing.domain.inputs.block.PrinterDesignStyle;
import pe.puyu.jticketdesing.domain.inputs.block.PrinterJustifyAlign;
import pe.puyu.jticketdesing.domain.inputs.block.ScaleType;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class SweetDesignHelper {

    private final @NotNull SweetProperties _properties;
    private final @NotNull PrinterDesignStyle _defaultStyle;

    public SweetDesignHelper(@NotNull SweetProperties properties, @NotNull PrinterDesignStyle defaultStyle) {
        this._properties = properties;
        this._defaultStyle = defaultStyle;
    }

    public @NotNull PainterStyle makePainterStyleFor(
        @NotNull String className,
        @NotNull Integer index,
        @Nullable Map<String, @Nullable PrinterDesignStyle> styles
    ) {
        Optional<Map<String, @Nullable PrinterDesignStyle>> style = Optional.ofNullable(styles);
        int fontWidth = Optional.ofNullable(_defaultStyle.fontWidth()).orElse(1);
        int fontHeight = Optional.ofNullable(_defaultStyle.fontHeight()).orElse(1);
        boolean bold = Optional.ofNullable(_defaultStyle.bold()).orElse(false);
        boolean bgInverted = Optional.ofNullable(_defaultStyle.bgInverted()).orElse(false);
        String charCode = _properties.charCode();
        if (style.isPresent()) {
            String indexStr = String.valueOf(index);
            var styleMap = style.get();
            Optional<PrinterDesignStyle> findByClassName = Optional.ofNullable(styleMap.get(className));
            Optional<PrinterDesignStyle> findByIndex = Optional.ofNullable(styleMap.get(indexStr));
            fontWidth = findByIndex.map(PrinterDesignStyle::fontWidth).orElse(fontWidth);
            fontHeight = findByIndex.map(PrinterDesignStyle::fontHeight).orElse(fontHeight);
            bold = findByIndex.map(PrinterDesignStyle::bold).orElse(bold);
            bgInverted = findByIndex.map(PrinterDesignStyle::bgInverted).orElse(bgInverted);
            fontWidth = findByClassName.map(PrinterDesignStyle::fontWidth).orElse(fontWidth);
            fontHeight = findByClassName.map(PrinterDesignStyle::fontHeight).orElse(fontHeight);
            bold = findByClassName.map(PrinterDesignStyle::bold).orElse(bold);
            bgInverted = findByClassName.map(PrinterDesignStyle::bgInverted).orElse(bgInverted);
        }
        return new PainterStyle(fontWidth, fontHeight, bold, bgInverted, charCode);
    }

    public @NotNull SweetStringStyle makeSweetStringStyleFor(
        @NotNull String className,
        @NotNull Integer index,
        @Nullable Map<String, @Nullable PrinterDesignStyle> styles
    ) {
        Optional<Map<String, @Nullable PrinterDesignStyle>> style = Optional.ofNullable(styles);
        int span = Optional.ofNullable(_defaultStyle.span()).orElse(1);
        char pad = Optional.ofNullable(_defaultStyle.pad()).orElse(' ');
        PrinterJustifyAlign align = Optional.ofNullable(_defaultStyle.align()).orElse(PrinterJustifyAlign.LEFT);
        boolean normalize = _properties.normalize();
        if (style.isPresent()) {
            String indexStr = String.valueOf(index);
            var styleMap = style.get();
            Optional<PrinterDesignStyle> findByClassName = Optional.ofNullable(styleMap.get(className));
            Optional<PrinterDesignStyle> findByIndex = Optional.ofNullable(styleMap.get(indexStr));
            span = findByIndex.map(PrinterDesignStyle::span).orElse(span);
            pad = findByIndex.map(PrinterDesignStyle::pad).orElse(pad);
            align = findByIndex.map(PrinterDesignStyle::align).orElse(align);
            normalize = findByIndex.map(PrinterDesignStyle::normalize).orElse(normalize);
            span = findByClassName.map(PrinterDesignStyle::span).orElse(span);
            pad = findByClassName.map(PrinterDesignStyle::pad).orElse(pad);
            align = findByClassName.map(PrinterDesignStyle::align).orElse(align);
            normalize = findByClassName.map(PrinterDesignStyle::normalize).orElse(normalize);
        }
        return new SweetStringStyle(span, pad, align, normalize);
    }

    public @NotNull SweetImageInfo makeSweetImageInfo(@Nullable Map<String, PrinterDesignStyle> styles) {
        ScaleType scaleType = ScaleType.SMOOTH;
        int width = 290;
        int height = 290;
        PrinterJustifyAlign align = PrinterJustifyAlign.LEFT;
        scaleType = Optional.ofNullable(_defaultStyle.imgScale()).orElse(scaleType);
        width = Optional.ofNullable(_defaultStyle.imgWidth()).orElse(width);
        height = Optional.ofNullable(_defaultStyle.imgHeight()).orElse(height);
        align = Optional.ofNullable(_defaultStyle.align()).orElse(align);
        Optional<Map<String, @Nullable PrinterDesignStyle>> style = Optional.ofNullable(styles);
        if (style.isPresent()) {
            Map<String, PrinterDesignStyle> styleMap = style.get();
            Optional<PrinterDesignStyle> findByClassName = Optional.ofNullable(styleMap.get("$img"));
            scaleType = findByClassName.map(PrinterDesignStyle::imgScale).orElse(scaleType);
            width = findByClassName.map(PrinterDesignStyle::imgWidth).orElse(width);
            height = findByClassName.map(PrinterDesignStyle::imgHeight).orElse(height);
            align = findByClassName.map(PrinterDesignStyle::align).orElse(align);
        }
        width = Math.min(width, calcWidthPaperInPx());
        return new SweetImageInfo(scaleType, width, height, align);
    }

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

    public @NotNull SweetCell justifyCell(SweetCell cell) {
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

    public int calcWidthPaperInPx() {
        int pixelsPerCharacter = 11;
        return _properties.blockWidth() * pixelsPerCharacter + _properties.blockWidth();
    }

}

