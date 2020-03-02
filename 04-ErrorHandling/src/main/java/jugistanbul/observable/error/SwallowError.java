package jugistanbul.observable.error;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 2.03.2020
 **/

public class SwallowError {

    private static final LongAdder ERROR_COUNTER = new LongAdder();
    private static final List<Integer> NUMBERS = Arrays.asList(10, 20, 30, null, 40, 50);

    public static void main(String[] args) {

        doOnErrorOperator();
        onErrorCompleteOperator();

        onErrorResumeNextOperator(NUMBERS)
                .subscribe( n -> System.out.println("\n" + n));

        onErrorReturnOperator(NUMBERS)
                .subscribe( n -> System.out.println("\n" + n),
                        error -> System.err.println("onError should not be printed!"));

        onExceptionResumeNextOperator(NUMBERS)
                .subscribe( n -> System.out.println("\n" + n),
                        error -> System.err.println("onError should not be printed!"));

        onErrorReturnItemOperator()
                .subscribe(v -> System.out.println("\nReturn number is " + v + "\n"),
                        error -> System.err.println("onError should not be printed!"));

        retryOperator()
                .blockingSubscribe(v -> System.out.println("Generated value: " + v), System.out::println);

        retryUntilOperator().retryUntil(() -> ERROR_COUNTER.intValue() >= 3)
                .blockingSubscribe(v -> System.out.println("Generated value: " + v), System.out::println);
    }

    static void doOnErrorOperator(){
        Observable.error(new RuntimeException("Something went wrong"))
                .doOnError(error -> System.err.println("The error message is: " + error.getMessage()))
                .subscribe(e -> System.out.println("onNext should never be printed!"),
                        Throwable::printStackTrace, () -> System.out.println("onComplete should never be printed!"));
    }

    static void onErrorCompleteOperator(){
        Completable.create(subscriber ->
        {
            throw new RuntimeException();
        }).onErrorComplete(e -> e instanceof RuntimeException)
                .subscribe(() -> System.out.println("\nRuntimeException was ignored"),
                        error -> System.err.println("onError should not be printed!"));

        Maybe.create(emitter ->
        {
            throw new IOException();
        }).onErrorComplete(e -> e instanceof IOException)
                .subscribe(e -> System.out.println("\nIOException was ignored"),
                        error -> System.err.println("onError should not be printed!"));

    }

    static Observable onErrorResumeNextOperator(final List<Integer> numbers){
        return Observable.fromIterable(numbers)
                .map(n -> n / 2)
                .onErrorResumeNext(Observable.empty());
    }

    static Observable onErrorReturnOperator(final List<Integer> numbers){
        return Observable.fromIterable(numbers)
                .map(n -> n / 2)
                .onErrorReturn(e -> {
                    if (e instanceof NullPointerException) return 0;
                    else throw new IllegalArgumentException();
                });
    }

    static Observable onExceptionResumeNextOperator(final List<Integer> numbers){
        return Observable.fromIterable(numbers)
                .map(n -> n / 2)
                .onExceptionResumeNext(Observable.just(100));
    }

    static Single onErrorReturnItemOperator(){
        return Single.just("hello")
                .map(v -> Integer.parseInt(v))
                .onErrorReturnItem(0);
    }

    static Observable retryOperator(){
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap(n -> {
                    if (n >= 2) {
                        return Observable.error(new RuntimeException("Something went wrong!"));
                    }  else { return Observable.just(n);}
                }).retry((retryCount, error) -> retryCount < 3);
    }

    static Observable retryUntilOperator(){
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .flatMap(n -> {
                    if (n >= 2) {
                        return Observable.error(new RuntimeException("Something went wrong!"));
                    }  else { return Observable.just(n);}
                }).doOnError(e -> ERROR_COUNTER.increment());
    }
}
