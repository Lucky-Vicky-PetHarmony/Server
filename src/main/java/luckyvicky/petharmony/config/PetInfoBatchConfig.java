package luckyvicky.petharmony.config;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.service.PetInfoWordService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PetInfoBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PetInfoRepository petInfoRepository;
    private final PetInfoWordService petInfoWordService;

    public PetInfoBatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                              PetInfoRepository petInfoRepository, PetInfoWordService petInfoWordService) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.petInfoRepository = petInfoRepository;
        this.petInfoWordService = petInfoWordService;
    }

    @Bean
    public Job processPetInfoJob() {
        return new JobBuilder("processPetInfoJob", jobRepository)
                .start(processPetInfoStep())
                .build();
    }

    @Bean
    public Step processPetInfoStep() {
        return new StepBuilder("processPetInfoStep", jobRepository)
                .<PetInfo, PetInfo>chunk(100, transactionManager)
                .reader(petInfoReader())
                .processor(petInfoProcessor())
                .writer(petInfoWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean
    public RepositoryItemReader<PetInfo> petInfoReader() {
        RepositoryItemReader<PetInfo> reader = new RepositoryItemReader<>();
        reader.setRepository(petInfoRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(100);

        Map<String, Sort.Direction> sortMap = new HashMap<>();
        sortMap.put("desertionNo", Sort.Direction.ASC);
        reader.setSort(sortMap);

        return reader;
    }

    @Bean
    public ItemProcessor<PetInfo, PetInfo> petInfoProcessor() {
        return petInfo -> {
            petInfoWordService.processPetInfo(petInfo);
            return petInfo;
        };
    }

    @Bean
    public ItemWriter<PetInfo> petInfoWriter() {
        return items -> petInfoRepository.saveAll(items);
    }
}
