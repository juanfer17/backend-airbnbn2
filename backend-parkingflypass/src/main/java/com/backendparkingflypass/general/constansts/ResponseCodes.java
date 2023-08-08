
package com.backendparkingflypass.general.constansts;

/**
 * Contiene los codigos y mensajes de respuesta para controladors REST
 * 
 * @author pmunoz@cidenet.com.co
 * @date 23/11/2016
 *
 */
public class ResponseCodes {

	public static final String REQUEST_SUCCESS_CODE = "000";
	public static final String REQUEST_SUCCESS_MESSAGE = "Operación Exitosa";
	public static final String REQUEST_ERROR_CODE = "001";
	public static final String EXCEPTION_CODE = "002";
	public static final String CONNECTION_CODE = "003";
    public static final String SUSPEND_TAG_CODE = "012";
    public static final String TAG_NO_ASIGNED_CODE = "013";
    public static final String LOGIN_CODE = "004";
	public static final String REQUEST_USERNAME_ERROR_CODE = "005";
	public static final String REQUEST_MAIL_ERROR_CODE = "006";
	public static final String REQUEST_USERNAME_MAIL_ERROR_CODE = "007";
	public static final String FORM_VALIDATION_ERROR_CODE = "008";
	public static final String PASSWORD_USER_EQUAL_ERROR_CODE = "009";
	public static final String DOCUMENT_ERROR_CODE = "010";
	public static final String USER_ERROR_CODE = "011";
	public static final String VEHICLE_NO_DATA_ERROR_CODE = "012";

	/**
	 * constructor por defecto
	 */
	private ResponseCodes() {
	}
}
