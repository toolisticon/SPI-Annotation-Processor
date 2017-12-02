package io.toolisticon.spiap.processor;


/**
 * SpiProcessorMessages used by annotation processors of Advice annotations.
 */
public enum SpiImplProcessorMessages {


    ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_CLASS("ERROR_001", "SpiImpl annotation must only be used on Classes"),
    ERROR_VALUE_ATTRIBUTE_MUST_ONLY_CONTAIN_INTERFACES("ERROR_002", "SpiImpl annotation only accepts interfaces - ${0} is no interface"),
    ERROR_ANNOTATED_CLASS_MUST_IMPLEMENT_CONFIGURED_INTERFACES("ERROR_003", "SpiImpl doesn't implement the ${0} interface"),
    ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR_FILE("ERROR_004", "Cannot open spi service location file for writing : ${0}"),
    ERROR_COULD_NOT_APPEND_TO_SERVICE_LOCATOR_FILE("ERROR_005", "Cannot append to spi service location file : ${0}");


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
    private SpiImplProcessorMessages(String code, String message) {
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
        return (printMessageCodes ? "[" + code + "] : " : "") + message;
    }


    /**
     * Allows toggling if message codes should be printed.
     *
     * @param printMessageCodes defines if message codes should be part of the message text
     */
    public static void setPrintMessageCodes(boolean printMessageCodes) {
        SpiImplProcessorMessages.printMessageCodes = printMessageCodes;
    }

}
