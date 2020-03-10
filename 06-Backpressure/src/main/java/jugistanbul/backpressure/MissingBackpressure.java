package jugistanbul.backpressure;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.03.2020
 **/

public class MissingBackpressure
{

    private static final Logger logger = LoggerFactory.getLogger(MissingBackpressure.class);

    public static void main(String[] args) throws InterruptedException {

        Flowable.interval(1, MILLISECONDS)
                .map(n -> {
                    logger.info("Emitted {}", n);
                    return n;
                }).observeOn(Schedulers.computation())
                .subscribe(x -> {
                            MILLISECONDS.sleep(100);
                            logger.info("Consumed {}", x);
                        }, Throwable::printStackTrace, () -> logger.info("Done!")
                );

        TimeUnit.SECONDS.sleep(1);
    }
}
