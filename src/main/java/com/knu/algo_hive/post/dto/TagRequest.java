package com.knu.algo_hive.post.dto;

import java.util.Set;

public record TagRequest(
        Set<String> tags
) {
}
