package org.demo.graphqldemo.service;


import org.nagarro.graphqldemo.codegen.types.Book;
import org.nagarro.graphqldemo.codegen.types.Inventory;
import org.nagarro.graphqldemo.codegen.types.Review;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();

    Optional<Book> getBookById(String id);

    Book addBook(String title, String author);

    Review addReview(String bookUuid, int stars, String comment);

    Inventory updateInventory(String bookUuid, int quantity);

    List<Review> getAllReviews();

    List<Review> getReviewsForBook(String bookUuid);

}
