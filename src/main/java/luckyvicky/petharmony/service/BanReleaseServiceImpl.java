package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BanReleaseServiceImpl implements BanReleaseService {
    private final UserService userService;

    /**
     * 매일 오후 11시59분 해당 날짜에 벤이 풀려야하는 유저들 벤 풀어줌
     */
    @Override
    @Scheduled(cron = "0 59 23 * * *")
    public void releaseUserBans() {
        userService.releaseBans();
    }
}
