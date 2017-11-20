package tasks;

import com.google.inject.AbstractModule;

public class Initializer extends AbstractModule {
    @Override
    protected void configure() {
        bind(AmazonTask.class).asEagerSingleton();
    }
}
