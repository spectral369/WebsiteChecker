package com.sitechecker.sitechecker;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.sitechecker.content.DBCheckExecutor;
import com.sitechecker.db.DBChecker;
import com.sitechecker.updater.DailyUpdater;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@CssImport("./styles/shared-styles.css")
@Push(value = PushMode.AUTOMATIC, transport = Transport.WEBSOCKET_XHR)
public class Application extends SpringBootServletInitializer implements AppShellConfigurator, ServletContextListener {

    /**
     * 
     */
    private static final long serialVersionUID = -3440900717655492031L;
    

    public static void main(String[] args) {
	SpringApplication.run(Application.class, args);

    }

    protected DailyUpdater updater;

    @Override
    public void contextInitialized(ServletContextEvent event) {
	updater = DailyUpdater.getInstance();
	if (updater == null)
	    updater.startExecutionAt(16, 0, 0);

	DBCheckExecutor.getInstance().getSES().scheduleAtFixedRate(DBChecker.getInstance(), 1, 2, TimeUnit.MINUTES);// orig
														    // 10
	
	
	

    }
    


}
