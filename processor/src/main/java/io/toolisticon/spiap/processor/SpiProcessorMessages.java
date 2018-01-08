package io.toolisticon.spiap.processor;


import io.toolisticon.spiap.api.OutOfService;

/**
 * SpiProcessorMessages used by annotation processors of Advice annotations.
 */
public enum SpiProcessorMessages {


    ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE("ERROR_001", "Spi annotation must only be used on interfaces"),
    ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR("ERROR_002", "Couldn't create Spi Service Locator : ${0}"),
    ;


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
    private SpiProcessorMessages(String code, String message) {
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
        SpiProcessorMessages.printMessageCodes = printMessageCodes;
    }

}
