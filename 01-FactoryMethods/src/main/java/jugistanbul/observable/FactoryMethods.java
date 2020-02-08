package jugistanbul.observable;

import io.reactivex.Observable;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

public class FactoryMethods {

    public static void main(String[] args){


        createFactoryMethod().subscribe(System.out::println);

        justFactoryMethod().subscribe(System.out::println, System.out::println, System.out::println);

        callableFactoryMethod().subscribe(System.out::println);

        rangeFactoryMethod(0, 10)
                .subscribe(System.out::print, System.out::println, System.out::println);

        intervalFactoryMethod().take(3).subscribe(v -> System.out.print("\r" + v + " sn "));

        emptyFactoryMethod().subscribe(
                v -> System.out.println("This should never be printed!"),
                error -> System.out.println("Or this!"),
                () -> System.out.println("Done will be printed."));

        fromArrayFactoryMethod().subscribe(System.out::println);
    }

    static Observable<String> createFactoryMethod(){
        return Observable.create(subscribe -> {
            subscribe.onNext("Hello World!");
            subscribe.onComplete();
        });
    }

    static Observable<String> justFactoryMethod(){
        return Observable.just("A", "B", "C", "D");
    }

    static Observable callableFactoryMethod(){
        return Observable.fromCallable(() -> "Hello from Callable...");
    }

    static Observable<Integer> rangeFactoryMethod(final int start, final int count){
        return Observable.range(start, count);
    }

    static Observable<Long> intervalFactoryMethod(){
        return Observable.interval(1, TimeUnit.SECONDS);
    }

    static Observable emptyFactoryMethod(){
        return Observable.empty();
    }

    static Observable<String> fromArrayFactoryMethod(){
        System.out.println("*** Please write a statement ***");
        final Scanner scanner = new Scanner(System.in);
        final String line = scanner.nextLine();
        final Observable<String> fromObservable = Observable.fromArray(line);
        scanner.close();
        return fromObservable;
    }
}
