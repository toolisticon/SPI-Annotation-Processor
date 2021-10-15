package io.toolisticon.spiap.processor;


import io.toolisticon.aptk.tools.corematcher.ValidationMessage;

/**
 * SpiProcessorMessages used by annotation processors of Advice annotations.
 */
public enum SpiProcessorMessages implements ValidationMessage {


    ERROR_SPI_ANNOTATION_MUST_BE_PLACED_ON_INTERFACE("SPI_ERROR_001", "Spi annotation must only be used on interfaces"),
    ERROR_COULD_NOT_CREATE_SERVICE_LOCATOR("SPI_ERROR_002", "Couldn't create Spi Service Locator : ${0}"),
    ERROR_SERVICE_LOCATOR_PASSED_SPI_CLASS_MUST_BE_AN_INTERFACE("SPI_ERROR_003", "Passed spi class in service locator annotation isn't an interface"),
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
    SpiProcessorMessages(String code, String message) {
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
        return message;
    }


}
