package com.knu.algo_hive.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiApiResponse {

    private List<Candidate> candidates;

    @Getter
    public static class Candidate {
        private Content content;
        private String finishReason;
        private CitationMetadata citationMetadata;
        private double avgLogprobs;

        @Setter
        @Getter
        public static class Content {
            private List<Part> parts;
            private String role;
        }

        @Setter
        @Getter
        public static class Part {
            private String text;

        }

        @Setter
        @Getter
        public static class CitationMetadata {
            @JsonProperty("citationSources")
            private List<CitationSource> citationSources;

        }

        @Setter
        @Getter
        public static class CitationSource {
            private int startIndex;
            private int endIndex;
            private String uri;

        }
    }
}
