package jugistanbul.backpressure;

import io.reactivex.BackpressureStrategy;
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
 * Created on 9.03.2020
 **/

public class Strategy
{
    private static final ExecutorService poolA = Executors.newFixedThreadPool(1);
    private static final ExecutorService poolB = Executors.newFixedThreadPool(1);
    private static final Scheduler schedulerA = Schedulers.from(poolA);
    private static final Scheduler schedulerB = Schedulers.from(poolB);
    private static final Logger logger = LoggerFactory.getLogger(Strategy.class);

    public static void main(String[] args){

        dropStrategy()
                .subscribeOn(schedulerA)
                .observeOn(schedulerB)
                .subscribe(n -> {
                    TimeUnit.MILLISECONDS.sleep(100);
                    logger.info("Consumed {}", n);
                }, Throwable::printStackTrace, () -> cleanUp());

        latestStrategy()
                .subscribeOn(schedulerA)
                .observeOn(schedulerB, true, 10)
                .subscribe(n -> {
                    TimeUnit.MILLISECONDS.sleep(100);
                    logger.info("Consumed {}", n);
                }, Throwable::printStackTrace, () -> cleanUp());

        errorStrategy()
                .subscribeOn(schedulerA)
                .observeOn(schedulerB)
                .subscribe(n -> {
                    TimeUnit.MILLISECONDS.sleep(100);
                    logger.info("Consumed {}", n);
                }, Throwable::printStackTrace, () -> cleanUp());
    }

    static Flowable<Integer> dropStrategy(){
        return getMyFlowable(BackpressureStrategy.DROP);
    }

    static Flowable<Integer> latestStrategy(){
        return getMyFlowable(BackpressureStrategy.LATEST);
    }

    static Flowable<Integer> errorStrategy(){
        return getMyFlowable(BackpressureStrategy.ERROR);
    }

    static Flowable<Integer> getMyFlowable(final BackpressureStrategy strategy){
        return Flowable.create(emitter -> {
            int state = 0;
            while (state < 200) {
                ++state;
                emitter.onNext(state);
                logger.info("Emitted {}", state);
            }
            emitter.onComplete();
        }, strategy);
    }
    static void cleanUp(){
        poolA.shutdown();
        poolB.shutdown();
    }
}
