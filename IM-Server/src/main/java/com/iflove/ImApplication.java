package com.iflove;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@MapperScan("com.iflove.**.mapper")
@Slf4j
public class ImApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ImApplication.class);
        Environment env = app.run(args).getEnvironment();
        app.setBannerMode(Banner.Mode.CONSOLE);
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/doc.html";
        } else {
            contextPath = contextPath + "/doc.html";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("""
                        \t
                        ----------------------------------------------------------
                        \t应用程序“{}”正在运行中......
                        \t接口文档访问 URL:
                        \t本地: \t\t{}://localhost:{}{}
                        \t外部: \t{}://{}:{}{}
                        \t配置文件: \t{}
                        ----------------------------------------------------------""",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }
}
