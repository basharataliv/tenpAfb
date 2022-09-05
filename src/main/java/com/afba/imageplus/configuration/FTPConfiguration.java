package com.afba.imageplus.configuration;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.gateway.FtpOutboundGateway;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpFileInfo;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.util.List;

@Profile("!test")
@ConditionalOnProperty(name = "ftp.protocol", havingValue = "ftp")
@Configuration
public class FTPConfiguration {

    @Value("${ftp.local.path}")
    String path;
    @Value("${ftp.host}")
    String host;
    @Value("${ftp.port}")
    Integer port;
    @Value("${ftp.username}")
    String username;
    @Value("${ftp.password}")
    String password;

    public DefaultFtpSessionFactory sessionFactory() {
        DefaultFtpSessionFactory sessionFactory = new DefaultFtpSessionFactory();

        sessionFactory.setHost(host);
        sessionFactory.setPort(port);
        sessionFactory.setUsername(username);
        sessionFactory.setPassword(password);
        sessionFactory.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE);
        return sessionFactory;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "ftp")
    @ServiceActivator(inputChannel = "ftpGET")
    @Bean
    public FtpOutboundGateway getFiles() {
        FtpOutboundGateway gateway = new FtpOutboundGateway(sessionFactory(), "get", "payload");

        gateway.setAutoCreateDirectory(true);
        gateway.setLocalDirectory(new File(path));
        gateway.setFileExistsMode(FileExistsMode.IGNORE);
        gateway.setOutputChannelName("fileResults");

        return gateway;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "ftp")
    @ServiceActivator(inputChannel = "ftpLIST")
    @Bean
    public FtpOutboundGateway listFiles() {
        FtpOutboundGateway gateway = new FtpOutboundGateway(sessionFactory(), "ls", "payload");
        gateway.setAutoCreateDirectory(true);
        gateway.setLocalDirectory(new File(path));
        gateway.setOutputChannelName("fileResults");
        return gateway;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "ftp")
    @Bean
    public MessageChannel fileResults() {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(tap());

        return channel;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "ftp")
    @Bean
    public WireTap tap() {
        return new WireTap("logging");
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "ftp")
    @ServiceActivator(inputChannel = "logging")
    @Bean
    public LoggingHandler logger() {
        LoggingHandler logger = new LoggingHandler(LoggingHandler.Level.INFO);
        return logger;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "ftp")
    @MessagingGateway(defaultRequestChannel = "ftpGET", defaultReplyChannel = "fileResults")
    public interface GetFile {
        File get(String directory);
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "ftp")
    @MessagingGateway(defaultRequestChannel = "ftpLIST", defaultReplyChannel = "fileResults")
    public interface ListFile {
        List<FtpFileInfo> ls(String directory);
    }

}
