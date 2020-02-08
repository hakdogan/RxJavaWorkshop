package jugistanbul.observable.type;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 5.01.2020
 **/

public class MaybeObservable {

    public static void main(String[] args){

        Maybe<String> nonEmptyMaybe = getMaybeObservable("Hello");

        nonEmptyMaybe.subscribe(m -> System.out.println("Received message: " + m));
        Single<Boolean> isEmpty = nonEmptyMaybe.isEmpty();
        isEmpty.subscribe(e -> System.out.println("Is nonEmptyMaybe Observable Empty: " + e));

        Maybe<Integer> emptyMaybe = Maybe.empty();
        isEmpty = emptyMaybe.isEmpty();
        isEmpty.subscribe(e -> System.out.println("Is emptyMaybe Observable Empty: " + e));

    }

    static Maybe<String> getMaybeObservable(final String message){
        return Maybe.create(emitter -> {
            emitter.onSuccess(message);
            emitter.onComplete();
        });
    }
}
