package org.demo.graphqldemo.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsSubscription;
import graphql.schema.DataFetchingEnvironment;
import org.nagarro.graphqldemo.codegen.types.Book;
import org.nagarro.graphqldemo.codegen.types.Inventory;
import org.nagarro.graphqldemo.codegen.types.Review;
import org.reactivestreams.Publisher;

@DgsComponent
public class BookSubscription {
    private final BookMutation bookMutation;

    public BookSubscription(BookMutation mutation) {
        this.bookMutation = mutation;
    }

    @DgsSubscription
    public Publisher<Book> bookAdded(DataFetchingEnvironment dfe) {
        return bookMutation.bookFlux()
                .doOnSubscribe(subscription -> System.out.println("Client subscribed to bookAdded"))
                .doOnNext(book -> System.out.println("Emitting book: " + book.getTitle()));
    }

    @DgsSubscription
    public Publisher<Review> reviewAdded(DataFetchingEnvironment dfe) {
        return bookMutation.reviewFlux();
    }

    @DgsSubscription
    public Publisher<Inventory> inventoryUpdated(DataFetchingEnvironment dfe) {
        return bookMutation.inventoryFlux();
    }
}
