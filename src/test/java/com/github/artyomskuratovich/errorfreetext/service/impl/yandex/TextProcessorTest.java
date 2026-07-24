package com.github.artyomskuratovich.errorfreetext.service.impl.yandex;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextProcessorTest {
    private final TextProcessor processor = new TextProcessor();

    @Test
    void prepare_ShortTextWithoutDigitsOrUrls_ReturnsSingleBlockWithZeroOptions() {
        TextData data = processor.prepare("Simple text without errors");

        assertEquals(1, data.getBlocks().size());
        assertEquals("Simple text without errors", data.getBlocks().getFirst());
        assertEquals(0, data.getOptions());
    }

    @Test
    void prepare_TextWithDigits_EnablesIgnoreDigitsOption() {
        TextData data = processor.prepare("Text with number 123");

        assertTrue((data.getOptions() & 2) != 0);
    }

    @Test
    void prepare_TextWithUrl_EnablesIgnoreUrlsOption() {
        TextData data = processor.prepare("Check website http://example.com");

        assertTrue((data.getOptions() & 4) != 0);
    }

    @Test
    void prepare_TextExceedsBlockSize_SplitsRealLargeText() {
        String firstPart = "a".repeat(9999) + " ";
        String secondPart = "bbbbb";

        String giantText = firstPart + secondPart; // Итого 10 005 символов

        TextData data = processor.prepare(giantText);

        assertEquals(2, data.getBlocks().size(), "Text must be split into 2 blocks");
        assertEquals(firstPart.trim(), data.getBlocks().get(0), "First block should cut off at the space");
        assertEquals(secondPart, data.getBlocks().get(1), "Second block should contain the rest");
    }

    @Test
    void assemble_MultipleBlocks_AssemblesBackWithSpaces() {
        List<String> blocks = List.of("FirstBlock", "SecondBlock", "ThirdBlock");

        List<List<SpellResult>> corrections = List.of(List.of(), List.of(), List.of());

        String result = processor.assemble(blocks, corrections);

        assertEquals("FirstBlock SecondBlock ThirdBlock", result);
    }

    @Test
    void assemble_ValidCorrections_ReplacesErrorsCorrectlyFromEnd() {
        List<String> blocks = List.of("Helllo wrld");

        SpellResult error1 = new SpellResult(
                0, 0, 0, 0, 6, "Helllo", List.of("Hello")
        );
        SpellResult error2 = new SpellResult(
                0, 7, 0, 0, 4, "wrld", List.of("world")
        );

        List<SpellResult> errors = List.of(error1, error2);
        List<List<SpellResult>> corrections = List.of(errors);

        String result = processor.assemble(blocks, corrections);

        assertEquals("Hello world", result);
    }

    @Test
    void assemble_EmptySuggestions_DoesNotModifyText() {
        List<String> blocks = List.of("UnknownWord");

        SpellResult errorWithNoSuggestions = new SpellResult(
                0, 0, 0, 0, 11, "UnknownWord", List.of()
        );

        List<List<SpellResult>> corrections = List.of(List.of(errorWithNoSuggestions));
        String result = processor.assemble(blocks, corrections);

        assertEquals("UnknownWord", result);
    }
}