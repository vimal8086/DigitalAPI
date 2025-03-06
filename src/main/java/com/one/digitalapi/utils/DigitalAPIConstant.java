package com.one.digitalapi.utils;

public class DigitalAPIConstant{
	
	public static final String SUCCESS = "SUCCESS";
	public static final long SUCCESS_RESPONSE_CODE = 0;
	public static final String SUCCESS_RESPONSE_MESSAGE = "Operation is performed successfully.";
	
	public static final String METHOD_START_LOG = "Started ";
	public static final String METHOD_END_LOG = "Ended ";
	public static final String METHOD_EXCEPTION_LOG = "exception occurred : ";
	
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	public static final String ENCRYPT_KEY_VALUE = "ENCRYPT_KEY_VALUE";
	
	
	public static final String AUTHENTICATION_ERROR = "Authentication Error";
	public static final String[] KEYCLOAK_SKIP_URI = {"/","login.zul","logout.zul","/servlet/externallogin","//servlet/externallogin"};
	public static final String ENCRYPTION_TYPE_VALUE = "BCRYPT";
	public static final String ENCRYPTION_TYPE_PROPERTY = "login.encryption.type";
	
	public static final long INVALID_REQUEST_ERROR_CODE = -500L;
	public static final String INVALID_REQUEST_ERROR_ATTRIBUTE = "ERROR_REASON";
	public static final String INVALID_REQUEST_ERROR_CODE_ATTRIBUTE = "ERROR_CODE";
	public static final String INVALID_REQUEST_AUTH_TOKEN_MISSING = "Mandatory parameter Authorization is missing ";
	public static final String INVALID_REQUEST_AUTH_TOKEN_INVALID = "Either your token is invalid or your session has expired. Kindly re-login and try again.";
	public static final String AUTHORIZATION = "Authorization";
	public static final String AUTH_TOKEN = "Authorization Token";
	public static final Long ACCESS_TOKEN_EXPIRED_CODE = -400L;
	public static final String ACCESS_ROLE_LIST = "ACCESS_ROLE_LIST";
	public static final String MACHINE_MACHINE_CALL = "MACHINE_MACHINE_CALL";

	public static final String SSO_URL = "sso.url";
	public static final String SSO_CLIENT_ID= "sso.clientId";
	public static final String SSO_SECRET = "sso.secret";
	public static final String SSO_AVAILABILITY_IN_SYSTEM = "sso.available";
	public static final String SSO_REALM = "sso.realm";
	public static final String SSO_REDIRECT_URL = "sso.redirectUrl";
	public static final String SSO_ADMIN_CLIENT = "sso.admin.client";
	public static final String SSO_TOKEN_ALGORITHM = "sso.token_algorithm";
	public static final String SSO_TOKEN_PUBLIC_KEY = "sso.token_public_key";
	public static final String SSO_KEYCLOAK_API_VALIDATION = "sso.keycloak_api_validation";
	public static final String GENERATE_TOKEN_API = "/generateToken";
	public static final String  USERNAME = "USERNAME";
	
	
	public static final String DATE_FORMAT_DDMMYYYY_HH_MM_SS="yyyy-MM-dd'T'HH:mm:ss";


}