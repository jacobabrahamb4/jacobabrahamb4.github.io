package livelessons.imagestreamgang.streams;

import android.util.Log;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import livelessons.imagestreamgang.filters.Filter;
import livelessons.imagestreamgang.filters.FilterDecoratorWithImage;
import livelessons.imagestreamgang.utils.StreamsUtils;
import livelessons.imagestreamgang.utils.Image;

import static java.util.stream.Collectors.toList;

/**
 * Base class that factors out common code and customizes ImageStream
 * to use Java 8 CompletableFutures to download, process, and store
 * images concurrently.
 */
public abstract class ImageStreamCompletableFutureBase
       extends ImageStreamGang {
    /**
     * Constructor initializes the superclass and data members.
     */
    public ImageStreamCompletableFutureBase(Filter[] filters,
                                        Iterator<List<URL>> urlListIterator,
                                        Runnable completionHook) {
        super(filters, urlListIterator, completionHook);
    }

    /**
     * Asynchronously download an Image from the @a url parameter.
     */
    protected CompletableFuture<Image> downloadImageAsync(URL url) {
        // Asynchronously download an Image from the url parameter.
        return CompletableFuture.supplyAsync(() -> downloadImage(url),
                                             getExecutor());
    }

    /**
     * Asynchronously filter the image and store it in an output file.
     */
    protected CompletableFuture<Image> filterImageAsync
        (FilterDecoratorWithImage filterDecoratorWithImage) {
        // Asynchronously filter the image and store it in an output
        // file.
        return CompletableFuture.supplyAsync(filterDecoratorWithImage::run,
                                             getExecutor());
    }
}
