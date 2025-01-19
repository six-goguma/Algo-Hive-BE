package com.knu.algo_hive.post.dto;

import java.util.Set;

public record TagResponse(
        Set<String> tags
) {
}
