package com.co2nsensus.co2mission.logging;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AspectLogger {

	static Logger log = LoggerFactory.getLogger(AspectLogger.class);

	@Pointcut("within(@com.co2nsensus.co2mission *)")
	public void loggable() {
	}

	@Around("@annotation(loggable)")
	public Object AllFunctions(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
		return logFunctions(joinPoint, loggable);
	}

	@Around("@within(loggable)")
	public Object AllClasses(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
		return logFunctions(joinPoint, loggable);
	}

	public Object logFunctions(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
		long start = 0;
		String className = null;
		String methodName = null;
		StringBuilder apiName = null;
		String logId = null;
		StringBuilder logString = null;
		StringBuilder startString = null;

		if (loggable.logThis()) {
			start = System.currentTimeMillis();
			className = joinPoint.getSignature().getDeclaringTypeName();
			className = className.length() >= 39 ? className.substring(39) : className;
			methodName = joinPoint.getSignature().getName();
			apiName = new StringBuilder(className).append(".").append(methodName);

			HttpServletRequest request = null;
			try {
				request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			} catch (Exception e) {

			}

			logId = UUID.randomUUID().toString();
			logString = new StringBuilder().append("; uri:").append(request == null ? "" : request.getRequestURI())
					.append("; logid:").append(logId);
		}
		Object result = joinPoint.proceed();

		if (loggable.logThis()) {
			long elapsedTime = System.currentTimeMillis() - start;
			StringBuilder endString = new StringBuilder("E:").append(apiName).append("; duration:").append(elapsedTime)
					.append(logString);
			log.info(endString.toString());
		}
		return result;
	}

}