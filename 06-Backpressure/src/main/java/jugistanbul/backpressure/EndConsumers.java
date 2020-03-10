package jugistanbul.backpressure;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.03.2020
 **/

public class EndConsumers
{

    private static final Logger logger = LoggerFactory.getLogger(EndConsumers.class);
    private static final ExecutorService poolA = Executors.newFixedThreadPool(1);
    private static final ExecutorService poolB = Executors.newFixedThreadPool(1);
    private static final Scheduler schedulerA = Schedulers.from(poolA);
    private static final Scheduler schedulerB = Schedulers.from(poolB);

    public static void main(String[] args){
        normalEndConsumer();
        slowEndConsumer();
    }

    static void normalEndConsumer(){
        Flowable.range(1, 200)
                .map(n -> {
                    logger.info("Emitted {}", n);
                    return n;
                }).subscribeOn(schedulerA)
                .observeOn(schedulerB)
                .subscribe(new DisposableSubscriber<Integer>() {

                    @Override
                    public void onStart() {
                        request(1);
                    }

                    @Override
                    public void onNext(Integer number) {
                        logger.info("Consumed {}", number);
                        request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        logger.error("An error occurred", t);
                    }

                    @Override
                    public void onComplete() {
                        logger.info("Done!");
                        cleanUp();
                    }
                });
    }

    static void slowEndConsumer(){
        Flowable.range(1, 200)
                .map(n -> {
                    logger.info("Emitted {}", n);
                    return n;
                }).subscribeOn(schedulerA)
                .observeOn(schedulerB)
                .subscribe(new DisposableSubscriber<Integer>() {

                    @Override
                    public void onStart() {
                        request(1);
                    }

                    @Override
                    public void onNext(Integer number) {
                        logger.info("Consumed {}", number);
                        request(1);

                        try {
                            MILLISECONDS.sleep(50);
                        } catch (InterruptedException e) {
                            logger.error("An error occurred in onNext", e);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        logger.error("An error occurred", t);
                    }

                    @Override
                    public void onComplete() {
                        logger.info("Done!");
                        cleanUp();
                    }
                });
    }

    static void cleanUp(){
        poolA.shutdown();
        poolB.shutdown();
    }
}
