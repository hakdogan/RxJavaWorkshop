package jugistanbul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@SpringBootApplication
@EnableScheduling
public class RunApp
{
    public static void main(String[] args){
        SpringApplication.run(RunApp.class, args);
    }
}
