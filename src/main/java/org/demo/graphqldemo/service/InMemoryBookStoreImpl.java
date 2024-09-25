package org.demo.graphqldemo.service;

import org.jetbrains.annotations.NotNull;
import org.nagarro.graphqldemo.codegen.types.Book;
import org.nagarro.graphqldemo.codegen.types.Inventory;
import org.nagarro.graphqldemo.codegen.types.Review;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static java.lang.System.out;

@Service
public class InMemoryBookStoreImpl implements BookService {
    private final List<Book> books;
    private final List<Review> reviews;
    private final List<Inventory> inventories;

    public InMemoryBookStoreImpl() {
        inventories = new ArrayList<>();
        reviews = new ArrayList<>();
        books = new ArrayList<>();
    }

    @Override
    public List<Book> getAllBooks() {
        out.println("Getting all the books!");
        return books.stream().map(this::appendInventory).toList();
    }

    @Override
    public Optional<Book> getBookById(String id) {
        out.println("Getting A book by Id: " + id);
        return books.stream().filter(it -> Objects.equals(it.getUuid(), id)).findFirst()
                .map(book -> {
                    appendedAdditionalDetails(book);
                    return book;
                });
    }

    @Override
    public Book addBook(String title, String author) {
        Book book = createABook(title, author);
        books.add(book);
        return book;
    }

    private static @NotNull Book createABook(String title, String author) {
        var book = new Book();
        book.setUuid(UUID.randomUUID().toString());
        book.setTitle(title);
        book.setAuthor(author);
        return book;
    }

    @Override
    public Review addReview(String bookUuid, int stars, String comment) {
        books.stream()
                .filter(book -> book.getUuid().equals(bookUuid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Review review = createReview(bookUuid, stars, comment);
        reviews.add(review);
        return review;
    }

    private static @NotNull Review createReview(String bookUuid, int stars, String comment) {
        Review review = new Review();
        review.setBookId(bookUuid);
        review.setId(UUID.randomUUID().toString());
        review.setStars(stars);
        review.setTxt(comment);

        return review;
    }

    @Override
    public Inventory updateInventory(String bookUuid, int quantity) {
        books.stream()
                .filter(book -> book.getUuid().equals(bookUuid))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Inventory inventory = createInventory(bookUuid, quantity);
        inventories.removeIf(inv -> inv.getUuid().equals(bookUuid));
        inventories.add(inventory);
        return inventory;
    }

    private static @NotNull Inventory createInventory(String bookUuid, int quantity) {
        Inventory inventory = new Inventory();
        inventory.setUuid(bookUuid);
        inventory.setQty(quantity);
        return inventory;
    }

    @Override
    public List<Review> getAllReviews() {
        return new ArrayList<>(reviews);
    }

    @Override
    public List<Review> getReviewsForBook(String bookUuid) {
        System.out.println("Fetching reviews for book: " + bookUuid);
        return reviews.stream()
                .filter(review -> review.getBookId().equals(bookUuid))
                .toList();
    }

    private Book appendInventory(Book book) {
        var inventory = inventories.stream().filter(it -> Objects.equals(it.getUuid(), book.getUuid())).findFirst().orElse(null);
        book.setInventory(inventory);
        return book;
    }

    private void appendReviews(Book book) {
        var bookReviews = reviews.stream().filter(it -> Objects.equals(it.getBookId(), book.getUuid())).toList();
        book.setReviews(bookReviews);
    }

    private void appendedAdditionalDetails(Book book) {
        appendInventory(book);
        appendReviews(book);
    }

}
