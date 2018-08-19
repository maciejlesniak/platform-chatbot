package pl.sparkidea.chatbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.Executors;

/**
 * @author Maciej Lesniak
 */
@Configuration
public class TaskExecutorConfig {
    private final static Logger LOG = LoggerFactory.getLogger(TaskExecutorConfig.class);

    @Value("${async.threads}")
    private int nThreads;

    @Bean
    public TaskExecutor taskExecutor() {
        LOG.info("Configuring thread pool for message handlers: {}", nThreads);
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(nThreads));
    }

}
