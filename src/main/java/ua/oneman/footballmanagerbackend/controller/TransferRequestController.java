package ua.oneman.footballmanagerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.oneman.footballmanagerbackend.dto.req.TransferRequestReqDTO;
import ua.oneman.footballmanagerbackend.dto.resp.TransferRequestRespDTO;
import ua.oneman.footballmanagerbackend.service.TransferRequestService;

@RestController
@RequestMapping("/api/v1/transfer-requests")
@RequiredArgsConstructor
public class TransferRequestController {

    private final TransferRequestService transferRequestService;

    @PostMapping
    public ResponseEntity<TransferRequestRespDTO> createTransferRequest(
            @RequestBody TransferRequestReqDTO transferRequestReqDTO,
            Authentication authentication) {

        TransferRequestRespDTO transferRequest = transferRequestService.createTransferRequest(transferRequestReqDTO, authentication);
        return ResponseEntity.ok(transferRequest);
    }


    @PostMapping("/{transferRequestId}/approve")
    public ResponseEntity<TransferRequestRespDTO> approveTransferRequest(
            @PathVariable Long transferRequestId,
            Authentication authentication) {

        TransferRequestRespDTO approvedRequest = transferRequestService.approveTransfer(transferRequestId, authentication);
        return ResponseEntity.ok(approvedRequest);
    }

    @PostMapping("/{transferRequestId}/reject")
    public ResponseEntity<TransferRequestRespDTO> rejectTransferRequest(
            @PathVariable Long transferRequestId,
            Authentication authentication) {

        TransferRequestRespDTO rejectedRequest = transferRequestService.rejectTransfer(transferRequestId, authentication);
        return ResponseEntity.ok(rejectedRequest);
    }
}