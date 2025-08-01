package br.com.feedhub.application.services.security.auth;

import br.com.feedhub.application.usecases.security.auth.ResolveToken;
import org.springframework.stereotype.Service;

@Service
public class ResolveTokenImpl implements ResolveToken {

    @Override
    public String execute(String token) {
        final String BEARER_TOKEN = "Bearer ";
        if (token == null || token.isEmpty()) {
            return null;
        }
        return token.startsWith(BEARER_TOKEN) ? token.substring(BEARER_TOKEN.length()) : null;
    }

}
