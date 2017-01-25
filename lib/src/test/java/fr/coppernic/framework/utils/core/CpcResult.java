package fr.coppernic.framework.utils.core;

/**
 * This class contains error codes used by CpcFramework applications
 * 
 * @author bastien.paul
 *
 */
public final class CpcResult {

	public enum RESULT {
		/**
		 * No error
		 */
		OK,
		/**
		 * General error
		 */
		ERROR,
		/**
		 * Method called is not implemented
		 */
		NOT_IMPLEMENTED,
        /**
         * Method called is not supported
         */
        NOT_SUPPORTED,
		/**
		 * Supplied parameters are invalid
		 */
		INVALID_PARAM,
		/**
		 * Current process is busy
		 */
		BUSY,
		/**
		 * Method called in a wrong state
		 */
		WRONG_STATE,
		/**
		 * Method called when not ready
		 */
		NOT_READY,
		/**
		 * Invalid procedure
		 * 
		 * A previous method or procedure should be called before calling this
		 * one
		 */
		INVALID_PROCEDURE,
		/**
		 * Openning of a file has failed
		 */
		OPEN_FAIL,
		/**
		 * File not found
		 */
		FILE_NOT_FOUND,
		/**
		 * Wrong format
		 */
		WRONG_FORMAT,
		/**
		 * Process was interrupted
		 */
		INTERRUPTED,
		/**
		 * Io problems
		 */
		IO,
		/**
		 * Error during reset
		 */
		RESET_FAIL,
		/**
		 * Not authorized to open resource
		 */
		OPEN_SECURITY_EXCEPTION,
		/**
		 * Time out
		 */
		TIMEOUT,
		/**
		 * Port is not opened
		 */
		NOT_OPENED,
		/**
		 * Result should be ignored
		 */
		IGNORE,
		/**
		 * Value already set
		 */
		ALREADY_SET,
		/**
		 * No data available
		 */
		NO_CARD,
		/**
		 * Reader not connected to a card
		 */
		NOT_CONNECTED_TO_A_CARD,
		/**
		 * Operation has been cancelled
		 */
		CANCELLED,
		/**
		 * PC/SC context is invalid
		 */
		INVALID_CONTEXT,
		/**
		 * No PC/SC reader is available
		 */
		NO_READER_AVAILABLE,
		/**
		 * Invalid handle
		 */
		INVALID_HANDLE,
		/**
		 * Invalid command
		 */
		INVALID_COMMAND,
        /**
         * Invalid version
         */
        INVALID_VERSION,
		/**
		 * No reader found
		 */
		NO_READER_FOUND,
		/**
		 * CCID Command status failed
		 */
		CCID_COMMAND_STATUS_FAILED,
		/**
		 * CCID invalid answer
		 */
		CCID_INVALID_ANSWER,
		/**
		 * Host aborted the current activity.
		 */
		CCID_COMMAND_ABORTED,
		/**
		 * CCID timed out while talking to the ICC.
		 */
		CCID_ICC_MUTE,
		/**
		 * Parity error while talking to the ICC.
		 */
		CCID_XFR_PARITY_ERROR,
		/**
		 * Overrun error while talking to the ICC.
		 */
		CCID_XFR_OVERRUN,
		/**
		 * An all inclusive hardware error occurred.
		 */
		CCID_HW_ERROR,
		CCID_BAD_ATR_TS,
		CCID_BAD_ATR_TCK,
		CCID_ICC_PROTOCOL_NOT_SUPPORTED,
		CCID_ICC_WRONG_PARAM,
        CCID_ICC_SM_ERROR,
        CCID_ICC_CLASS_NOT_SUPPORTED,
        CCID_ICC_INVALID_ANSWER,
        CCID_ICC_INVALID_STATUS,
        CCID_PROCEDURE_BYTE_CONFLICT,
		CCID_DEACTIVATED_PROTOCOL,
		/**
		 * Automatic Sequence Ongoing.
		 */
		CCID_BUSY_WITH_AUTO_SEQUENCE,
		CCID_PIN_TIMEOUT,
		CCID_PIN_CANCELLED,
		/**A second command was sent to a slot which
		* was already processing a command.
		*/
		CCID_CMD_SLOT_BUSY,
		CCID_COMMAND_NOT_SUPPORTED,
		/**
		 * The device command sequence and the reader command sequence are
		 * different.
		 */
		CCID_DESYNCHRONYZED,
		/**
		 * Not found that one's was looking for
		 */
		NOT_FOUND,
		/**
		 * No data
		 */
		NO_DATA,
		/**
		 * Error in parsing
		 */
		PARSE_ERROR,
		/**
		 * Security error
		 */
		SECURITY_ERROR,
		/**
		 * Service not found
		 */
		SERVICE_NOT_FOUND,
		/**
		 * Not initialized
		 */
		NOT_INITIALIZED,
		/**
		 * NOt connected
		 */
		NOT_CONNECTED,
		/**
		 * All command has been aborted, reader is ready to receive new command
		 */
		READER_ABORTED,
		/**
		 * No command has been found to abort
		 */
		READER_NOT_ABORTED,
		/**
		 * Fail to abort Reader
		 */
		READER_ABORT_FAIL,
		/**
		 * Fail to set USB Endpoints
		 */
		USB_FAIL_TO_SET_ENDPOINTS,
		/**
		 * Already opened
		 */
		ALREADY_OPENED,
		/**
		 * OUt of range
		 */
		OUT_OF_RANGE,
		/**
		 * Usb not authenticated
		 */
		USB_NOT_AUTHENTICATED,
		/**
		 * Operation is in progress
		 */
		OPP_IN_PROGRESS,
		/**
		 * HTTP Error
		 */
		HTTP_ERROR,
		/**
		 * WEB Service Error
		 */
		WEB_SERVICE_ERROR,
		/**
		 * Pending
		 */
		PENDING,
		/**
		 * Bytes are available
		 */
		BYTES_AVAILABLE,
		/**
		 * Wrong length
		 */
		INVALID_LENGTH,
		/**
		 * Forbidden
		 */
		FORBIDDEN,
		/**
		 * Not authorized
		 */
		NOT_AUTHORIZED,
		/**
		 * No Devices
		 */
		NO_DEVICE,
		/**
		 * BAC Authentication failed
		 */
		BAC_FAILED,
		/**
		 * Read DG1 File Failed
		 */
		DG1_Failed,
        /**
         * Read DG2 File Failed
         */
        DG2_Failed,
        /**
         * Read DG3 File Failed
         */
        DG3_Failed,
        /**
         * Read DG4 File Failed
         */
        DG4_Failed,
		/**
		 * No Connection
		 */
		NO_CONN,
        /**
         * CRC Error
         */
        CRC_ERROR,
        /**
         * MAC Error
         */
        MAC_ERROR,
        /**
         * IMEI Error
         */
        IMEI_ERROR,
        /**
         * Requested algorithm could
         * not be found
         */
        NO_SUCH_ALGORITHM,
        /**
         * Invalid Key
         */
        INVALID_KEY,
        /**
         * Invalid Signature
         */
        INVALID_SIGNATURE,
        /**
         * Invalid certificate
         */
        INVALID_CERTIFICATE,
        /**
         * Certificate chaine validation failed
         */
        CERTIFICATES_VALIDATION_FAILED,
        /**
         * Data hashes comparison failed
         */
        DATA_HASHES_FAILED,
        /**
         * Wrong address
         */
        WRONG_ADDRESS,
        /**
         * Wrong answer
         */
        WRONG_ANSWER,
        /**
         * Fail to parse answer
         */
        ERROR_PARSING_ANSWER,
		/**
		 * Connection error
		 */
		CONNECTION_ERROR,
        /**
         * Hashes Comparison failed
         */
        HASHES_COMPARISON_FAILED,
        /**
         *Encryption failed
         */
        ENCRYPTION_FAILED,
        /**
         *SAM selection failed
         */
        SAM_SELECT_FAILED,
        /**
         *SAM power failed
         */
        SAM_POWER_FAILED,
	}
}

