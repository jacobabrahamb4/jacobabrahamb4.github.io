package livelessons.imagestreamgang.streams;

import android.util.Log;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import livelessons.imagestreamgang.filters.Filter;
import livelessons.imagestreamgang.filters.FilterDecoratorWithImage;
import livelessons.imagestreamgang.utils.StreamsUtils;
import livelessons.imagestreamgang.utils.Image;

import static java.util.stream.Collectors.toList;

/**
 * Customizes ImageStream to use Java 8 CompletableFutures to
 * download, process, and store images concurrently.
 */
public class ImageStreamCompletableFuture2
       extends ImageStreamCompletableFutureBase {
    /**
     * Constructor initializes the superclass and data members.
     */
    public ImageStreamCompletableFuture2(Filter[] filters,
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
        final List<CompletableFuture<List<Image>>> listOfFutures = getInput()
            // Concurrently process each URL in the input List.
            .parallelStream()

            // Only include URLs that have not been already cached.
            .filter(StreamsUtils.not(this::urlCached))

            // Submit non-cached URLs for asynchronous downloading,
            // which returns a stream of unfiltered Image futures.
            .map(this::downloadImageAsync)

            // After each future completes then apply the
            // makeFilterDecoratorWithImage() method, which returns a
            // List of FilterDecoratorWithImage objects stored in a
            // future.
            .map(imageFuture ->
                 imageFuture.thenApply(this::makeFilterDecoratorsWithImage))

            // After each future completes then compose the results
            // with the applyFiltersAsync() method, which returns a
            // list of filtered Image futures.
            .map(listFilterDecoratorsFuture ->
                 listFilterDecoratorsFuture.thenCompose(this::applyFiltersAsync))

            // Terminate the stream, which returns a List of futures
            // to filtered Image futures.
            .collect(toList());

        // Wait for all operations associated with the futures to
        // complete.
        final CompletableFuture<List<List<Image>>> allImagesDone =
                StreamsUtils.joinAll(listOfFutures);
        // The call to join() is needed here to blocks the calling
        // thread until all the futures have been completed.
        int imagesProcessed = allImagesDone.join()
                                           .stream()
                                           .mapToInt(List::size)
                                           .sum();

        Log.d(TAG,
              "processing of "
              + imagesProcessed
              + " image(s) is complete");
    }

    /**
     * Apply the filters in parallel to each @a image.
     */
    private List<FilterDecoratorWithImage> makeFilterDecoratorsWithImage(Image image) {
        return mFilters
            // Iterate through all the configured filters.
            .stream()

            // Create an OutputDecoratedFilter for each image.
            .map(filter -> makeFilterDecoratorWithImage(filter, image))

            // Return a list of FilterDecoratorWithImage objects.
            .collect(toList());
    }

    /**
     * Asynchronously filter the image and store it in an output file.
     */
    private CompletableFuture<List<Image>> applyFiltersAsync
                (List<FilterDecoratorWithImage> decoratedFiltersWithImage) {
        List<CompletableFuture<Image>> listOfFutures = decoratedFiltersWithImage
            // Iterate through all the configured filters.
            .stream()

            // Asynchronously apply a filter to an Image.
            .map(this::filterImageAsync)

            // Collect the list of futures.
            .collect(toList());

        // Return a CompletableFuture to a list of Image objects that
        // will be available when all the CompletableFutures in the
        // listOfFutures have completed.
        return StreamsUtils.joinAll(listOfFutures);
    }
}
