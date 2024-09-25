package org.demo.graphqldemo.fetcher;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.demo.graphqldemo.service.BookService;
import org.nagarro.graphqldemo.codegen.types.Book;
import org.nagarro.graphqldemo.codegen.types.Review;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
public class BookDataFetcher {
    private final BookService bookService;

    public BookDataFetcher(BookService bookService) {
        this.bookService = bookService;
    }

    // @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.BOOK.TYPE_NAME)
    @DgsQuery(field = "book")
    public Book getBook(@InputArgument String uuid) {
        return bookService.getBookById(uuid).orElse(null);
    }


    // @DgsData(parentType = "Query", field = "books")
    @DgsQuery(field = "books")
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    @DgsData(parentType = "Book", field = "reviews")
    public CompletableFuture<List<Review>> getReviews(DataFetchingEnvironment dfe) {
        Book book = dfe.getSource();
        DataLoader<String, List<Review>> reviewLoader = dfe.getDataLoader("reviewLoader");
        return reviewLoader.load(book.getUuid());
    }

   /* @DgsData(parentType = "Book", field = "reviews")
    public List<Review> getReviewsWithoutDataLoader(DataFetchingEnvironment dfe) {
        Book book = dfe.getSource();
        System.out.println("Fetching reviews for book without DataLoader: " + book.getUuid());
        return bookService.getReviewsForBook(book.getUuid());
    }*/
}
