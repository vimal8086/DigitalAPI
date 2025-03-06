package com.one.digitalapi.exception;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DigitalAPIExceptionHelper {
	
	private static PropertiesConfig propertiesConfig;
	private static final long DEFAULT_ERROR_CODE = -1L;

    @Autowired
    public DigitalAPIExceptionHelper(PropertiesConfig propertiesConfig) {
    	DigitalAPIExceptionHelper.propertiesConfig = propertiesConfig;
    }

    /**
     * Returns new RuntimeException based on template and args
     *
     * @param messageTemplate
     * @param args
     * @return
     */
    public static RuntimeException throwException(String messageTemplate, String... args) {
        return new RuntimeException(format(messageTemplate, args));
    }

    /**
     * Returns new RuntimeException based on EntityType, ExceptionType and args
     *
     * @param entityType
     * @param exceptionType
     * @param args
     * @return
     */
    public static RuntimeException throwException(Class<? extends RuntimeException> customException, EntityType entityType, ExceptionType exceptionType, String... args) {
        Long errorCode = getErrorCode(entityType, exceptionType);
    	String messageTemplate = getMessageTemplate(entityType, exceptionType);
        return throwException(customException, exceptionType.getErrorCategory() ,errorCode, messageTemplate, args);
    }

    /**
     * Returns new RuntimeException based on EntityType, ExceptionType, messageTemplate and args
     *
     * @param entityType
     * @param exceptionType
     * @param messageTemplate
     * @param args
     * @return
     */
    public static RuntimeException throwExceptionWithTemplate(Class<? extends RuntimeException> customException, String errorCategory, String messageTemplate, String... args) {
        return throwException(customException, errorCategory, DEFAULT_ERROR_CODE, messageTemplate, args);
    }

    /**
     * Returns new RuntimeException based on template and args
     *
     * @param messageTemplate
     * @param args
     * @return
     */
    private static RuntimeException throwException(Class<? extends RuntimeException> customException, String errorCategory,long errorCode, String messageTemplate, String... args) {
        try {
			return customException.getDeclaredConstructor(Long.class, String.class, String.class).newInstance(errorCode, format(messageTemplate, args), errorCategory);
		} 
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			return new RuntimeException(e.getMessage(),e);
		}
    }

    private static String getMessageTemplate(EntityType entityType, ExceptionType exceptionType) {
    	if(Objects.nonNull(entityType)) {
    		return entityType.name().concat(".").concat(exceptionType.getMessageMsgId()).toLowerCase();
    	}
    	return exceptionType.getMessageMsgId().toLowerCase();
    }
    
    private static Long getErrorCode(EntityType entityType, ExceptionType exceptionType) {
    	if(Objects.nonNull(entityType)) {
    		return Long.parseLong("-".concat(String.valueOf(entityType.getEntityCode())).concat(String.valueOf(exceptionType.getErrorCode())));
    	}
    	return Long.parseLong("-".concat(String.valueOf(exceptionType.getErrorCode())));
    }

    public static String format(String template, String... args) {
    	if(Objects.nonNull(propertiesConfig)) {
	        Optional<String> templateContent = Optional.ofNullable(propertiesConfig.getConfigValue(template));
	        if (templateContent.isPresent()) {
	            return MessageFormat.format(templateContent.get(), args);
	        }
    	}
        return String.format(template, args);
    }


}
