package org.demo.graphqldemo.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import org.demo.graphqldemo.service.BookService;
import org.nagarro.graphqldemo.codegen.types.Book;
import org.nagarro.graphqldemo.codegen.types.Inventory;
import org.nagarro.graphqldemo.codegen.types.Review;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@DgsComponent
public class BookMutation {

    private final BookService bookService;

    private final Sinks.Many<Review> reviewSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Inventory> inventorySink = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<Book> bookSink = Sinks.many().multicast().onBackpressureBuffer();

    public BookMutation(BookService bookService) {
        this.bookService = bookService;
    }

    @DgsMutation(field = "addBook")
    public Book addBook(
            @InputArgument String title,
            @InputArgument String author
    ) {
        Book book = bookService.addBook(title, author);
        bookSink.tryEmitNext(book);
        return book;
    }

    @DgsMutation
    public Review addReview(
            @InputArgument String bookUuid,
            @InputArgument Integer stars,
            @InputArgument String txt
    ) {
        Review review = bookService.addReview(bookUuid, stars, txt);
        reviewSink.tryEmitNext(review);
        return review;
    }

    @DgsMutation
            public Inventory updateInventory(
            @InputArgument String bookUuid,
            @InputArgument Integer qty
    ) {
        Inventory inventory = bookService.updateInventory(bookUuid, qty);
        inventorySink.tryEmitNext(inventory);
        return inventory;
    }

    public Flux<Book> bookFlux() {
        return bookSink.asFlux();
    }

    public Flux<Review> reviewFlux() {
        return reviewSink.asFlux();
    }

    public Flux<Inventory> inventoryFlux() {
        return inventorySink.asFlux();
    }
}
