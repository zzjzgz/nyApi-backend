package xyz.zzj.project.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.zzj.project.service.NyApiUserInterfaceService;

import javax.annotation.Resource;
import java.util.concurrent.*;

@SpringBootTest
public class NyApiUserInterfaceServiceImplTest {


    @Resource
    private NyApiUserInterfaceService nyApiUserInterfaceService;

    private ExecutorService executorService = new ThreadPoolExecutor(40, 1000, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(2500));
    @Test
    public void invokeCountTest() {
        boolean b = nyApiUserInterfaceService.invokeCount(1L, 5L);
        System.out.println(b);
    }
}