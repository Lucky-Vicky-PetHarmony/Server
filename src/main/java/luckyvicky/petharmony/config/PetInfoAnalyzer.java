package luckyvicky.petharmony.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * PetInfoAnalyzer 클래스는 CommandLineRunner를 구현하여 애플리케이션 시작 시 특정 배치 작업을 실행
 */
@Component
public class PetInfoAnalyzer implements CommandLineRunner{

    private final JobLauncher jobLauncher; // 배치 작업을 실행하기 위한 JobLauncher
    private final Job processPetInfoJob; // 실행할 배치 Job

    @Autowired
    public PetInfoAnalyzer(JobLauncher jobLauncher, Job processPetInfoJob) {
        this.jobLauncher = jobLauncher;
        this.processPetInfoJob = processPetInfoJob;
    }

    public void run(String... args) {
        try {
            // JobParameters 생성: 각 배치 실행 시 유니크한 값을 부여하여 여러 번 실행 가능하게 함
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            // JobLauncher를 통해 배치 Job을 실행
            jobLauncher.run(processPetInfoJob, jobParameters);
        } catch (Exception e) {
            // 배치 작업 실행 중 예외가 발생한 경우 로그 출력 또는 에러 핸들링
            System.err.println("배치 작업 실행 중 오류 발생: " + e.getMessage());
        }
    }
}
