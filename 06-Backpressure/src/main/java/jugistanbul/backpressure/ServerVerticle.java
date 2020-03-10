package jugistanbul.backpressure;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 9.03.2020
 **/

public class ServerVerticle extends AbstractVerticle
{
    private static final int DEFAULT_HTTP_PORT = 8080;
    private static final Logger logger = LoggerFactory.getLogger(ServerVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {

        final Router router = Router.router(vertx);
        router.get("/access").handler(this::access);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(config().getInteger("http.port", DEFAULT_HTTP_PORT), result -> {
                    if(result.failed()){
                        startPromise.fail(result.cause());
                    } else {
                        startPromise.complete();
                    }
        });

    }

    public void access(RoutingContext context){
        context.request().response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .setStatusCode(200)
                .end("{\"status\": 200}");
    }

    public static void main(String[] args){
        final Vertx vertx = Vertx.vertx();
        final DeploymentOptions options = new DeploymentOptions().setWorker(true);
        vertx.deployVerticle(new ServerVerticle(), options, r -> {
            if(r.succeeded()){
                logger.info("Deployment id is {}", r.result());
            } else {
                logger.error("Deployment failed!", r.cause());
            }
        });
    }
}
