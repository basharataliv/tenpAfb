package com.afba.imageplus.configuration;


import com.jcraft.jsch.ChannelSftp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.util.List;

@Profile("!test")
@ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
@Configuration
public class SFTPConfiguration {

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

    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(username);
        factory.setPassword(password);
        factory.setAllowUnknownKeys(true);

        return new CachingSessionFactory<ChannelSftp.LsEntry>(factory);
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
    @ServiceActivator(inputChannel = "sftpGET")
    @Bean
    public SftpOutboundGateway getFiles() {
        SftpOutboundGateway gateway = new SftpOutboundGateway(sftpSessionFactory(), "get", "payload");

        gateway.setAutoCreateDirectory(true);
        gateway.setLocalDirectory(new File(path));
        gateway.setFileExistsMode(FileExistsMode.IGNORE);
        gateway.setOutputChannelName("fileResults");

        return gateway;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
    @ServiceActivator(inputChannel = "sftpLIST")
    @Bean
    public SftpOutboundGateway listFiles() {
        SftpOutboundGateway gateway = new SftpOutboundGateway(sftpSessionFactory(), "ls", "payload");
        gateway.setAutoCreateDirectory(true);
        gateway.setLocalDirectory(new File(path));
        gateway.setOutputChannelName("fileResults");
        return gateway;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
    @Bean
    public MessageChannel fileResults() {
        DirectChannel channel = new DirectChannel();
        channel.addInterceptor(tap());

        return channel;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
    @Bean
    public WireTap tap() {
        return new WireTap("logging");
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
    @ServiceActivator(inputChannel = "logging")
    @Bean
    public LoggingHandler logger() {
        LoggingHandler logger = new LoggingHandler(LoggingHandler.Level.INFO);
        return logger;
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
    @MessagingGateway(defaultRequestChannel = "sftpGET", defaultReplyChannel = "fileResults")
    public interface GetFile {
        File get(String directory);
    }

    @ConditionalOnProperty(name = "ftp.protocol", havingValue = "sftp")
    @MessagingGateway(defaultRequestChannel = "sftpLIST", defaultReplyChannel = "fileResults")
    public interface ListFile {
        List<SftpFileInfo> ls(String directory);
    }
}
