package luckyvicky.petharmony.config;

import luckyvicky.petharmony.dto.WordClassificationDTO;
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
                .<WordClassificationDTO, WordClassificationDTO>chunk(100, transactionManager)  // WordClassificationDTO 사용
                .reader(petInfoReader())  // WordClassificationDTO를 읽어옴
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
    public RepositoryItemReader<WordClassificationDTO> petInfoReader() {
        RepositoryItemReader<WordClassificationDTO> reader = new RepositoryItemReader<>();
        reader.setRepository(petInfoRepository);  // PetInfoRepository 사용
        reader.setMethodName("findDesertionNoAndSpecialMarkPaged");  // DTO 반환 메서드
        reader.setPageSize(100);

        Map<String, Sort.Direction> sortMap = new HashMap<>();
        sortMap.put("desertionNo", Sort.Direction.ASC);
        reader.setSort(sortMap);

        return reader;
    }

    @Bean
    public ItemProcessor<WordClassificationDTO, WordClassificationDTO> petInfoProcessor() {
        return dto -> {
            petInfoWordService.processSinglePetInfo(dto);  // PetInfoWordService에서 처리
            return dto;
        };
    }

    @Bean
    public ItemWriter<WordClassificationDTO> petInfoWriter() {
        return items -> {
            // 처리 완료 후 특별히 저장할 필요가 없을 수 있음, 여기서는 그냥 로그 처리 가능
        };
    }
}
