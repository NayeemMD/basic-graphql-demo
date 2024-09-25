package org.demo.graphqldemo.service;

import com.netflix.graphql.dgs.DgsDataLoader;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.BatchLoaderWithContext;
import org.nagarro.graphqldemo.codegen.types.Review;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@DgsDataLoader(name = "reviewLoader")
public class ReviewDataLoader implements BatchLoaderWithContext<String, List<Review>> {

    private final ReviewService reviewService;

    public ReviewDataLoader(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    public CompletionStage<List<List<Review>>> load(List<String> keys, BatchLoaderEnvironment batchLoaderEnvironment) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("ReviewDataLoader load called with keys: " + keys);
            Map<String, List<Review>> reviewsMap = reviewService.getReviewsForBooks(keys);

            return keys.stream()
                    .map(key -> reviewsMap.getOrDefault(key, List.of()))
                    .collect(Collectors.toList());
        });
    }
}
