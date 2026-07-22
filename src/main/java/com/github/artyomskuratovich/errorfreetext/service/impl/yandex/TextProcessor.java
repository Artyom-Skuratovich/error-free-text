package com.github.artyomskuratovich.errorfreetext.service.impl.yandex;

import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Component
class TextProcessor {
    private static final int MAX_BLOCK_SIZE = 10_000;

    private static final int IGNORE_DIGITS = 2;
    private static final int IGNORE_URLS = 4;

    private static final LinkExtractor LINK_EXTRACTOR = LinkExtractor.builder()
            .linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW))
            .build();


    public TextData prepare(String text) {
        int estimatedBlocks = (text.length() + MAX_BLOCK_SIZE - 1) / MAX_BLOCK_SIZE;
        List<String> blocks = new ArrayList<>(estimatedBlocks);

        int options = 0;
        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + MAX_BLOCK_SIZE, text.length());

            if (end < text.length()) {
                int lastSpace = text.lastIndexOf(' ', end - 1);

                if (lastSpace > start) {
                    end = lastSpace;
                }
            }

            String block = text.substring(start, end);
            blocks.add(block);

            if (options != (IGNORE_DIGITS | IGNORE_URLS)) {
                options = analyzeBlockContent(block, options);
            }

            start = (end < text.length() && text.charAt(end) == ' ') ? end + 1 : end;
        }

        return new TextData(blocks, options);
    }

    public String assemble(List<String> blocks, List<List<SpellResult>> corrections) {
        StringBuilder correctedText = new StringBuilder();

        for (int i = 0; i < blocks.size(); i++) {
            String originalBlock = blocks.get(i);
            List<SpellResult> errors = corrections.get(i);

            String correctedBlock = applyCorrections(originalBlock, errors);
            correctedText.append(correctedBlock);

            if (i < blocks.size() - 1) {
                correctedText.append(' ');
            }
        }

        return correctedText.toString();
    }

    private int analyzeBlockContent(String block, int currentOptions) {
        boolean hasDigit = (currentOptions & IGNORE_DIGITS) != 0;
        boolean hasUrl = (currentOptions & IGNORE_URLS) != 0;

        for (int i = 0; i < block.length(); i++) {
            char c = block.charAt(i);

            if (!hasDigit && Character.isDigit(c)) {
                hasDigit = true;
                currentOptions |= IGNORE_DIGITS;
            }

            if (!hasUrl) {
                Iterable<LinkSpan> links = LINK_EXTRACTOR.extractLinks(block);

                if (links.iterator().hasNext()) {
                    currentOptions |= IGNORE_URLS;
                }
            }

            if (hasDigit && hasUrl) {
                break;
            }
        }

        return currentOptions;
    }

    private String applyCorrections(String block, List<SpellResult> errors) {
        StringBuilder sb = new StringBuilder(block);

        for (int i = errors.size() - 1; i >= 0; i--) {
            SpellResult error = errors.get(i);

            if (error.getS() != null && !error.getS().isEmpty()) {
                String suggestion = error.getS().getFirst();
                int start = error.getPos();
                int end = start + error.getLen();

                sb.replace(start, end, suggestion);
            }
        }

        return sb.toString();
    }
}