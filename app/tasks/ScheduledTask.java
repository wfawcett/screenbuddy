package tasks;

import akka.actor.ActorSystem;
import models.Amazon;
import models.Redbox;
import models.Request;
import play.Logger;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class ScheduledTask {

    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public ScheduledTask(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;
        this.initialize();
    }

    private void initialize(){
        // Redbox Crawler: Get all the redbox data.
        this.actorSystem.scheduler().scheduleOnce(
                Duration.create(5, TimeUnit.SECONDS),
                () -> Redbox.crawl(),
                this.executionContext);

        // Amazon Crawler: 2 minutes latter, try to get the requested movies
        // from amazon.
        this.actorSystem.scheduler().scheduleOnce(
                Duration.create(125, TimeUnit.SECONDS),
                () -> Amazon.crawl(),
                this.executionContext);

        // Request Crawler: 30 minutes later run requester service crawl and
        // and send emails.
        this.actorSystem.scheduler().scheduleOnce(
                Duration.create(30, TimeUnit.MINUTES),
                () -> Request.crawl(),
                this.executionContext);




    }


}
