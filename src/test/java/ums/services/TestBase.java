package ums.services;
import java.io.File;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
@ActiveProfiles("test")
public class TestBase { 
	
	
	@BeforeClass
	public static  void before(){
		initializeLogback2();
		logger= LoggerFactory.getLogger(TestBase.class);
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	    // print logback's internal status
	    //StatusPrinter.print(lc); 
		
	}
	
	/*
	private static void initializeLogback() {
		File logbackFile = new File("D:\\eclipse-jee-indigo-SR2-win32-x86_64\\workspace\\ShortMessageServices\\src\\test\\resources\\logback1.xml");
	    System.setProperty("logback.configurationFile", "D:\\eclipse-jee-indigo-SR2-win32-x86_64\\workspace\\ShortMessageServices\\src\\test\\resources\\logback1.xml"  );
	    StaticLoggerBinder loggerBinder = StaticLoggerBinder.getSingleton();
	    LoggerContext loggerContext = (LoggerContext) loggerBinder.getLoggerFactory();
	    loggerContext.reset();
	    JoranConfigurator configurator = new JoranConfigurator();
	    configurator.setContext(loggerContext);
	    try {
	        configurator.doConfigure(logbackFile);
	    } catch( JoranException e ) {
	        throw new RuntimeException(e.getMessage(), e);
	    }
	}
	*/
	
	private static void initializeLogback2(){
		// assume SLF4J is bound to logback in the current environment
	    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
	    File logbackFile = new File("D:\\eclipse-jee-indigo-SR2-win32-x86_64\\workspace\\ShortMessageServices\\src\\test\\resources\\logback2.xml");
	    try {
	      JoranConfigurator configurator = new JoranConfigurator();
	      configurator.setContext(context);
	      // Call context.reset() to clear any previous configuration, e.g. default 
	      // configuration. For multi-step configuration, omit calling context.reset().
	      context.reset(); 
	      configurator.doConfigure(logbackFile);
	    } catch (JoranException je) {
	      // StatusPrinter will handle this
	    }
	    //StatusPrinter.printInCaseOfErrorsOrWarnings(context);
	}
	
	
	
	public static Logger logger;
	
	/*
	static {
		//PropertyConfigurator.configure(".\\src\\test\\resources\\log4j.properties"); 
		
	}
	*/


}
