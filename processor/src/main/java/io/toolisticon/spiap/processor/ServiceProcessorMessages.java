package io.toolisticon.spiap.processor;


import io.toolisticon.annotationprocessortoolkit.tools.corematcher.ValidationMessage;
import io.toolisticon.spiap.api.OutOfService;

/**
 * SpiProcessorMessages used by annotation processors of Advice annotations.
 */
public enum ServiceProcessorMessages implements ValidationMessage {


    ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS("SERVICE_ERROR_001", "Service annotation must only be used on Classes"),
    ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES("SERVICE_ERROR_002", "Service annotation only accepts interfaces - ${0} is no interface"),
    ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES("SERVICE_ERROR_003", "Service doesn't implement the ${0} interface"),
    ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR_FILE("SERVICE_ERROR_004", "Cannot open spi service location file for writing : ${0}"),
    ERROR_COULD_NOT_APPEND_TO_SERVICE_LOCATOR_FILE("SERVICE_ERROR_005", "Cannot append to spi service location file : ${0}"),
    INFO_SKIP_ELEMENT_ANNOTATED_AS_OUT_OF_SERVICE("SERVICE_INFO_001", "Skipped processing for service(s) class ${0} annotated with " + OutOfService.class.getSimpleName() + " annotation");


    /**
     * Flag that defines if messages codes will be written as part of the message.
     */
    private static boolean printMessageCodes = false;

    /**
     * the message code.
     */
    private final String code;
    /**
     * the message text.
     */
    private final String message;

    /**
     * Constructor.
     *
     * @param code    the message code
     * @param message the message text
     */
    ServiceProcessorMessages(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets the code of the message.
     *
     * @return the message code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Gets the message text.
     *
     * @return the message text
     */
    public String getMessage() {
        return this.message;
    }


}
