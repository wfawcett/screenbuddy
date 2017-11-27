package tasks;

import akka.actor.ActorSystem;
import models.Amazon;
import models.Redbox;
import models.Request;
import play.Logger;
import play.libs.mailer.MailerClient;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class ScheduledTask {

    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;
    private final MailerClient mailerClient;
    private final WSClient ws;

    @Inject
    public ScheduledTask(ActorSystem actorSystem, ExecutionContext executionContext, MailerClient mailerClient, WSClient ws) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;
        this.mailerClient = mailerClient;
        this.ws = ws;
        this.initialize();
    }

    private void initialize(){
//        this.actorSystem.scheduler().scheduleOnce(Duration.create(1, TimeUnit.SECONDS),() -> Amazon.setup(),this.executionContext);
//        this.actorSystem.scheduler().scheduleOnce(Duration.create(1, TimeUnit.SECONDS),() -> Redbox.setup(),this.executionContext);
//
        // Redbox Crawler: Get all the redbox data. once every 24 hours
        this.actorSystem.scheduler().schedule(
                Duration.create(1, TimeUnit.SECONDS), // initialDelay
                Duration.create(24, TimeUnit.HOURS), // interval
                () -> Redbox.crawl(this.ws),
                this.executionContext
        );
//
//        // link redbox data to title, limit how many at a time so we don't kill our api limits. run every 10 seconds
        this.actorSystem.scheduler().schedule(
                Duration.create(1, TimeUnit.SECONDS), // initialDelay
                Duration.create(5, TimeUnit.SECONDS), // interval
                () -> Redbox.titleLinker(this.ws),
                this.executionContext
        );
//
//        // Amazon Crawler: starts after one second and runs every 10 seconds until it runs out of titles to crawl.
//        this.actorSystem.scheduler().schedule(
//                Duration.create(1, TimeUnit.SECONDS), // initialDelay
//                Duration.create(10, TimeUnit.SECONDS), // interval
//                () -> Amazon.crawl(),
//                this.executionContext
//        );
//
//        // Request Crawler: 30 minutes later run requester service crawl and
//        // and send emails.
//        this.actorSystem.scheduler().schedule(
//                Duration.create(1, TimeUnit.SECONDS), // initialDelay
//                Duration.create(30, TimeUnit.SECONDS), // interval
//                () -> Request.crawl(mailerClient),
//                this.executionContext
//        );
    }


}
