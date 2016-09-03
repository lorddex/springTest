
package org.ldlabs.spring.test.security;

import org.ldlabs.spring.test.rest.AlumniRestController;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configurazione della security.
 * 
 * @author Francesco Apollonio
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

	private static final String	ADMIN_ROLE		= "ADMIN";

	private static final String	ADMIN_PASSWORD	= "test";

	private static final String	ADMIN_USERNAME	= "admin";

	/**
	 * Definizione dell'utente amministratore
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception
	{

		auth.inMemoryAuthentication().withUser(ADMIN_USERNAME)
				.password(ADMIN_PASSWORD).roles(ADMIN_ROLE);
	}

	/**
	 * Abilita il login per il metodo POST di
	 * salvataggio.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{

		http.httpBasic().and().authorizeRequests()
				.antMatchers(HttpMethod.POST, AlumniRestController.REST_BASE)
				.hasRole(ADMIN_ROLE).and().csrf().disable();
	}

}
