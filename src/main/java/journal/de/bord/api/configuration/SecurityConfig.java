package journal.de.bord.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    CorsFilter corsFilter() {
        CorsFilter filter = new CorsFilter();
        return filter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .authorizeRequests(authorization -> authorization
                        .antMatchers(HttpMethod.GET, "/").permitAll()
                        // This is required for the browser preflight requests.
                        .antMatchers(HttpMethod.OPTIONS, "**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/**").hasAuthority("SCOPE_read")
                        .antMatchers(HttpMethod.POST, "/api/**").hasAuthority("SCOPE_write")
                        .antMatchers(HttpMethod.PUT, "/api/**").hasAuthority("SCOPE_write")
                        .antMatchers(HttpMethod.DELETE, "/api/**").hasAuthority("SCOPE_write")
                        .anyRequest().authenticated())
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt());
    }

}