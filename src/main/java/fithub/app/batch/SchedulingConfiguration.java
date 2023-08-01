    package fithub.app.batch;

    import lombok.RequiredArgsConstructor;
    import org.springframework.batch.core.Job;
    import org.springframework.batch.core.JobExecutionException;
    import org.springframework.batch.core.JobParameters;
    import org.springframework.batch.core.JobParametersBuilder;
    import org.springframework.batch.core.launch.JobLauncher;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.scheduling.annotation.EnableScheduling;
    import org.springframework.scheduling.annotation.Scheduled;

    @Configuration
    @EnableScheduling
    @RequiredArgsConstructor
    public class SchedulingConfiguration {

        private final JobLauncher jobLauncher;

        private final Job setBestRecorder;

        private final Job returnMonthlyRecord;

        // 매달 첫 날 0 0 0 1 * *
        @Scheduled(cron = "0 0 0 1 * *")
        public void monthlySetRecordCounts() throws JobExecutionException {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(returnMonthlyRecord, jobParameters);
        }

        // 매일 수행 0 0 0 * * ?
        @Scheduled(cron = "40 44 16 * * ?")
        public void dailySetBestUser() throws JobExecutionException{
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(setBestRecorder, jobParameters);
        }
    }
