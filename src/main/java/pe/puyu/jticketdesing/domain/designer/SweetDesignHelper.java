package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.designer.img.SweetImageInfo;
import pe.puyu.jticketdesing.domain.designer.qr.SweetQrInfo;
import pe.puyu.jticketdesing.domain.designer.qr.SweetQrStyle;
import pe.puyu.jticketdesing.domain.designer.text.SweetCell;
import pe.puyu.jticketdesing.domain.designer.text.SweetStringStyle;
import pe.puyu.jticketdesing.domain.components.block.*;
import pe.puyu.jticketdesing.domain.printer.SweetPrinterStyle;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class SweetDesignHelper {

    private final @NotNull SweetProperties _properties;
    private final @NotNull SweetStyleComponent _defaultStyle;

    public SweetDesignHelper(@NotNull SweetProperties properties, @NotNull SweetStyleComponent defaultStyle) {
        this._properties = properties;
        this._defaultStyle = defaultStyle;
    }

    public @NotNull SweetPrinterStyle makePrinterStyleFor(
        @NotNull String className,
        @NotNull Integer index,
        @Nullable Map<String, @Nullable SweetStyleComponent> styles
    ) {
        Optional<Map<String, @Nullable SweetStyleComponent>> style = Optional.ofNullable(styles);
        int fontWidth = Optional.ofNullable(_defaultStyle.fontWidth()).orElse(1);
        int fontHeight = Optional.ofNullable(_defaultStyle.fontHeight()).orElse(1);
        boolean bold = Optional.ofNullable(_defaultStyle.bold()).orElse(false);
        boolean bgInverted = Optional.ofNullable(_defaultStyle.bgInverted()).orElse(false);
        String charCode = _properties.charCode();
        if (style.isPresent()) {
            String indexStr = String.valueOf(index);
            var styleMap = style.get();
            Optional<SweetStyleComponent> findByClassName = Optional.ofNullable(styleMap.get(className));
            Optional<SweetStyleComponent> findByIndex = Optional.ofNullable(styleMap.get(indexStr));
            fontWidth = findByIndex.map(SweetStyleComponent::fontWidth).orElse(fontWidth);
            fontHeight = findByIndex.map(SweetStyleComponent::fontHeight).orElse(fontHeight);
            bold = findByIndex.map(SweetStyleComponent::bold).orElse(bold);
            bgInverted = findByIndex.map(SweetStyleComponent::bgInverted).orElse(bgInverted);
            fontWidth = findByClassName.map(SweetStyleComponent::fontWidth).orElse(fontWidth);
            fontHeight = findByClassName.map(SweetStyleComponent::fontHeight).orElse(fontHeight);
            bold = findByClassName.map(SweetStyleComponent::bold).orElse(bold);
            bgInverted = findByClassName.map(SweetStyleComponent::bgInverted).orElse(bgInverted);
        }
        return new SweetPrinterStyle(fontWidth, fontHeight, bold, bgInverted, charCode);
    }

    public @NotNull SweetStringStyle makeSweetStringStyleFor(
        @NotNull String className,
        @NotNull Integer index,
        @Nullable Map<String, @Nullable SweetStyleComponent> styles
    ) {
        Optional<Map<String, @Nullable SweetStyleComponent>> style = Optional.ofNullable(styles);
        int span = Optional.ofNullable(_defaultStyle.span()).orElse(1);
        char pad = Optional.ofNullable(_defaultStyle.pad()).orElse(' ');
        SweetJustify align = Optional.ofNullable(_defaultStyle.align()).orElse(SweetJustify.LEFT);
        boolean normalize = _properties.normalize();
        if (style.isPresent()) {
            String indexStr = String.valueOf(index);
            var styleMap = style.get();
            Optional<SweetStyleComponent> findByClassName = Optional.ofNullable(styleMap.get(className));
            Optional<SweetStyleComponent> findByIndex = Optional.ofNullable(styleMap.get(indexStr));
            span = findByIndex.map(SweetStyleComponent::span).orElse(span);
            pad = findByIndex.map(SweetStyleComponent::pad).orElse(pad);
            align = findByIndex.map(SweetStyleComponent::align).orElse(align);
            normalize = findByIndex.map(SweetStyleComponent::normalize).orElse(normalize);
            span = findByClassName.map(SweetStyleComponent::span).orElse(span);
            pad = findByClassName.map(SweetStyleComponent::pad).orElse(pad);
            align = findByClassName.map(SweetStyleComponent::align).orElse(align);
            normalize = findByClassName.map(SweetStyleComponent::normalize).orElse(normalize);
        }
        return new SweetStringStyle(span, pad, align, normalize);
    }

    public @NotNull SweetImageInfo makeImageInfo(@Nullable Map<String, SweetStyleComponent> styles) {
        SweetScale sweetScale = SweetScale.SMOOTH;
        int width = 290;
        int height = 290;
        SweetJustify align = SweetJustify.LEFT;
        sweetScale = Optional.ofNullable(_defaultStyle.scale()).orElse(sweetScale);
        width = Optional.ofNullable(_defaultStyle.width()).orElse(width);
        height = Optional.ofNullable(_defaultStyle.height()).orElse(height);
        align = Optional.ofNullable(_defaultStyle.align()).orElse(align);
        Optional<Map<String, @Nullable SweetStyleComponent>> style = Optional.ofNullable(styles);
        if (style.isPresent()) {
            Map<String, SweetStyleComponent> styleMap = style.get();
            Optional<SweetStyleComponent> findByClassName = Optional.ofNullable(styleMap.get("$img"));
            sweetScale = findByClassName.map(SweetStyleComponent::scale).orElse(sweetScale);
            width = findByClassName.map(SweetStyleComponent::width).orElse(width);
            height = findByClassName.map(SweetStyleComponent::height).orElse(height);
            align = findByClassName.map(SweetStyleComponent::align).orElse(align);
        }
        width = Math.max(Math.min(width, calcWidthPaperInPx()), 0);
        height = Math.max(0, height);
        return new SweetImageInfo(sweetScale, width, height, align);
    }

    public @NotNull SweetQrStyle makeQrStyles(@Nullable Map<String, SweetStyleComponent> styles) {
        SweetJustify align = SweetJustify.CENTER;
        int size = 250;
        SweetScale scale = SweetScale.SMOOTH;
        align = Optional.ofNullable(_defaultStyle.align()).orElse(align);
        size = Optional.ofNullable(_defaultStyle.width()).orElse(size);
        scale = Optional.ofNullable(_defaultStyle.scale()).orElse(scale);
        Optional<Map<String, @Nullable SweetStyleComponent>> style = Optional.ofNullable(styles);
        if (style.isPresent()) {
            Map<String, SweetStyleComponent> styleMap = style.get();
            Optional<SweetStyleComponent> findByClassName = Optional.ofNullable(styleMap.get("$qr"));
            align = findByClassName.map(SweetStyleComponent::align).orElse(align);
            size = findByClassName.map(SweetStyleComponent::height).orElse(size); // first height
            size = findByClassName.map(SweetStyleComponent::width).orElse(size); // priority width
            scale = findByClassName.map(SweetStyleComponent::scale).orElse(scale);
        }
        size = Math.max(0, Math.min(size, calcWidthPaperInPx()));
        return new SweetQrStyle(align, size, scale);
    }

    public @NotNull SweetQrInfo makeQrInfo(
        @NotNull SweetQrComponent designQr,
        @NotNull SweetQrComponent defaultQr
    ) {
        String data = "";
        SweetQrType type = SweetQrType.IMG;
        SweetQrCorrectionLevel correctionLevel = SweetQrCorrectionLevel.Q;
        data = Optional.ofNullable(defaultQr.data()).orElse(data);
        type = Optional.ofNullable(defaultQr.qrType()).orElse(type);
        correctionLevel = Optional.ofNullable(defaultQr.correctionLevel()).orElse(correctionLevel);
        data = Optional.ofNullable(designQr.data()).orElse(data);
        type = Optional.ofNullable(designQr.qrType()).orElse(type);
        correctionLevel = Optional.ofNullable(designQr.correctionLevel()).orElse(correctionLevel);
        return new SweetQrInfo(data, type, correctionLevel);
    }

    public @NotNull List<String> wrapText(String text, int numberOfCharactersAvailable, int fontWidth) {
        List<String> wrappedText = new LinkedList<>();
        if (text.length() <= numberOfCharactersAvailable) {
            wrappedText.add(text);
            return wrappedText;
        }
        numberOfCharactersAvailable = Math.max(0, numberOfCharactersAvailable);
        fontWidth = Math.max(0, fontWidth);
        String[] splitWords = text.split("\\s+"); // divide in words
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
                new SweetPrinterStyle(cell.printerStyle()),
                new SweetStringStyle(cell.stringStyle())
            );
        }
        return new SweetCell(cell);
    }

    public @NotNull SweetCell justifyCell(SweetCell cell) {
        int spacesAvailable = Math.max(cell.width() - (cell.text().length() * cell.printerStyle().fontWidth()), 0);
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
            new SweetPrinterStyle(cell.printerStyle()),
            new SweetStringStyle(cell.stringStyle())
        );
    }

    public int calcWidthPaperInPx() {
        int pixelsPerCharacter = 11;
        return _properties.blockWidth() * pixelsPerCharacter + _properties.blockWidth();
    }

}

