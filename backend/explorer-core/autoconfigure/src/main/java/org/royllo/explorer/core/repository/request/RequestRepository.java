package org.royllo.explorer.core.repository.request;

import org.royllo.explorer.core.domain.request.Request;
import org.royllo.explorer.core.util.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * {@link Request} repository.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    /**
     * Find a request by its request id.
     *
     * @param requestId request id
     * @return request
     */
    Optional<Request> findByRequestId(String requestId);

    /**
     * Find all requests with the corresponding status.
     *
     * @param status status
     * @return Requests with the corresponding status
     */
    List<Request> findByStatusOrderById(RequestStatus status);

    /**
     * Find all requests with the corresponding status.
     *
     * @param status   status
     * @param pageable page parameters
     * @return Requests with the corresponding status
     */
    List<Request> findByStatusOrderById(RequestStatus status, Pageable pageable);

    /**
     * Find all requests with the corresponding status.
     *
     * @param status status list
     * @return Requests with the corresponding status
     */
    List<Request> findByStatusInOrderById(List<RequestStatus> status);

    /**
     * Find all requests with the corresponding status (ordered by id and with pagination).
     *
     * @param status   status filter
     * @param pageable page parameters
     * @return Requests with the corresponding status
     */
    Page<Request> findByStatusInOrderById(List<RequestStatus> status, Pageable pageable);

}
