package org.demo.graphqldemo.service;

import org.nagarro.graphqldemo.codegen.types.Review;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final BookService bookService;

    public ReviewService(BookService bookService) {
        this.bookService = bookService;
    }

    public Map<String, List<Review>> getReviewsForBooks(List<String> keys) {
        System.out.println("Getting reviews for " + keys);
        Map<String, List<Review>> reviewMap = keys.stream()
                .collect(Collectors.toMap(uuid -> uuid, uuid -> new ArrayList<>()));

        bookService.getAllReviews().stream()
                .filter(review -> keys.contains(review.getBookId()))
                .forEach(review -> reviewMap.get(review.getBookId()).add(review));

        return reviewMap;
    }
}
