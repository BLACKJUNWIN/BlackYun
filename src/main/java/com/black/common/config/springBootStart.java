package com.black.common.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class springBootStart implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(
                "\t\t\t\t\t◥◤~~\uE822~~◥◤　　\n" +
                "\t\t\t\t\t  ┃  萌萌  ┃\n" +
                "\t\t\t\t\t  ≡━ ﹏ ━≡　　\n" +
                "\t\t\t\t\t  ┗━━┳∞┳━━┛  \n" +
                "\t\t\t\t\t　  ┏┫　┣┓");
    }
}
