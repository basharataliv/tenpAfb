package com.afba.imageplus;

import com.afba.imageplus.configuration.DB2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
/*
 * Please remove below annotation, once you have successfully setup DB2 on your
 * machine, this will disable auto-configuration of DB2: just to suppress DB2
 * connectivity exception
 */
@ComponentScan(excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = DB2Config.class) })
@EnableRetry
public class AfbaImageplusSystemApplication {

    public static void main(String[] args) {

        SpringApplication.run(AfbaImageplusSystemApplication.class, args);
    }

}
