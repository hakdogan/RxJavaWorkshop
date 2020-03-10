package jugistanbul.backpressure;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.03.2020
 **/

public class Throttling
{
    private static final ExecutorService poolA = Executors.newFixedThreadPool(1);
    private static final ExecutorService poolB = Executors.newFixedThreadPool(1);
    private static final Scheduler schedulerA = Schedulers.from(poolA);
    private static final Scheduler schedulerB = Schedulers.from(poolB);
    private static final Logger logger = LoggerFactory.getLogger(Throttling.class);

    public static void main(String[] args) {

        Flowable.generate(() -> 0L, (state, emitter) -> {
            emitter.onNext(state);
            logger.info("Emitted {}", state);

            if (state > 199) {
                emitter.onComplete();
            }
            return state + 1;
        }).subscribeOn(schedulerA)
                .observeOn(schedulerB, true, 10)
                .subscribe(n -> {
                    TimeUnit.MILLISECONDS.sleep(30);
                    logger.info("Consumed {}", n);
                }, Throwable::printStackTrace,
                        () -> {
                            logger.info("Done!");
                            cleanUp();
                });
    }

    static void cleanUp(){
        poolA.shutdown();
        poolB.shutdown();
    }
}
