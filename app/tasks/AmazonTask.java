package tasks;

import akka.actor.ActorSystem;
import models.Amazon;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class AmazonTask {

    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public AmazonTask(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize(){
        this.actorSystem.scheduler().scheduleOnce(
                Duration.create(10, TimeUnit.SECONDS),
                () -> Amazon.crawl(),
                this.executionContext);
    }


}
