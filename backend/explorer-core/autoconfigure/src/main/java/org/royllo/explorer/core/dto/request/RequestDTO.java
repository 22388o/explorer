package org.royllo.explorer.core.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.royllo.explorer.core.dto.user.UserDTO;
import org.royllo.explorer.core.util.enums.RequestStatus;

import static org.royllo.explorer.core.util.enums.RequestStatus.FAILURE;
import static org.royllo.explorer.core.util.enums.RequestStatus.SUCCESS;

/**
 * User request to update royllo database.
 */
@Getter
@SuperBuilder
@ToString
@SuppressWarnings("checkstyle:VisibilityModifier")
public abstract class RequestDTO {

    /** Unique identifier. */
    Long id;

    /** Request UUID. */
    @NotNull(message = "Request ID is required")
    String requestId;

    /** Request creator. */
    @NotNull(message = "Request creator is required")
    UserDTO creator;

    /** Request status. */
    @NotNull(message = "Request status is mandatory")
    RequestStatus status;

    /** Error message - Not empty if status is equals to ERROR. */
    String errorMessage;

    /**
     * Set the request as success.
     */
    public void success() {
        status = SUCCESS;
    }

    /**
     * Set the request as failure.
     *
     * @param newErrorMessage new error message
     */
    public void failure(final String newErrorMessage) {
        errorMessage = newErrorMessage;
        status = FAILURE;
    }

    /**
     * Returns true is the request is successful.
     *
     * @return true if successful
     */
    public boolean isSuccessful() {
        return status == SUCCESS;
    }

}
