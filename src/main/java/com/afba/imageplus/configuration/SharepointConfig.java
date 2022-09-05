package com.afba.imageplus.configuration;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public class SharepointConfig {

	@Value("${sharepoint.tenant.id}")
	private String tenantId;

	@Value("${sharepoint.client.id}")
	private String clientId;

	@Value("${sharepoint.client.secret}")
	private String clientSecret;

	@Bean
	public ClientSecretCredential getClientSecretCredential() {
		return new ClientSecretCredentialBuilder().clientId(clientId).clientSecret(clientSecret).tenantId(tenantId)
				.build();
	}

	@Bean
	public TokenCredentialAuthProvider getTokenCredAuthProvider(ClientSecretCredential clientSecretCredential) {
		return new TokenCredentialAuthProvider(clientSecretCredential);
	}

	@Bean
	public GraphServiceClient getGraphClient(TokenCredentialAuthProvider tokenCredAuthProvider) {
		return GraphServiceClient.builder().authenticationProvider(tokenCredAuthProvider).buildClient();
	}

}
