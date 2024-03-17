package livelessons.imagestreamgang.streams;

import android.util.Log;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import livelessons.imagestreamgang.filters.Filter;
import livelessons.imagestreamgang.filters.FilterDecoratorWithImage;
import livelessons.imagestreamgang.utils.Image;
import livelessons.imagestreamgang.utils.StreamsUtils;

import static java.util.stream.Collectors.toList;

/**
 * Customizes ImageStream to use Java 8 CompletableFutures to download, process,
 * and store images concurrently.
 */
public class ImageStreamCompletableFuture1
        extends ImageStreamCompletableFutureBase {
    /**
     * Constructor initializes the superclass and data members.
     */
    public ImageStreamCompletableFuture1(
            Filter[] filters,
            Iterator<List<URL>> urlListIterator,
            Runnable completionHook) {
        super(filters, urlListIterator, completionHook);
    }

    /**
     * Perform the ImageStream processing, which uses Java 8 CompletableFutures
     * to download, process, and store images concurrently.
     */
    @Override
    protected void processStream() {
        // Create a list of futures.
        List<CompletableFuture<Image>> listOfFutures = getInput()
            // Concurrently process each URL in the input List.
            .stream()

            // Only include URLs that have not been already cached.
            .filter(StreamsUtils.not(this::urlCached))

            // Submit the URLs for asynchronous downloading.
            .map(this::downloadImageAsync)

            // Map each image to a stream containing the filtered
            // versions of the image.
            .flatMap(this::applyFilters)

            // Terminate the stream.
            .collect(toList());

        // Convert the list of futures to a list of images.
        List<Image> listOfImages = listOfFutures
            .stream()

            // Wait for all async operations to finish.
            .map(CompletableFuture::join)
                
            // Terminate the stream.
            .collect(toList());

        Log.d(TAG, "processing of "
              + listOfImages.size()
              + " image(s) is complete");
    }

    /**
     * Apply the filters in parallel to each @a image.
     */
    private Stream<CompletableFuture<Image>> applyFilters(CompletableFuture<Image> imageFuture) {
        return mFilters.stream()
                // Create a FilterDecoratorWithImage for each
                // filter/image combo.
                .map(filter ->
                     imageFuture.thenApply(image ->
                                           makeFilterDecoratorWithImage(filter,
                                                                        image)))

                // Asynchronously filter the image and store it in an
                // output file.
                .map(this::filterFutureImageAsync);
    }

    /**
     * Asynchronously filter the image and store it in an output file.
     */
    protected CompletableFuture<Image> filterFutureImageAsync
        (CompletableFuture<FilterDecoratorWithImage> filterDecoratorWithImageFuture) {
        // Asynchronously filter image and store it in an output file.
        return filterDecoratorWithImageFuture
            .thenCompose(filter ->
                         CompletableFuture.supplyAsync(filter::run,
                                                       getExecutor()));
    }
}
