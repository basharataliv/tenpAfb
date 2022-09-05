package com.afba.imageplus.dto.res;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DDProcessResponse {

    private List<TransactionResponse> transactionResponses;
    private Integer transactionsProcessed;
    private Integer transactionsSucceed;
    private Integer transactionsFailed;

    public DDProcessResponse() {
        this.transactionResponses = new ArrayList<>();
    }

    public void addSuccess(String transactionId) {
        this.transactionResponses.add(new TransactionResponse(transactionId, TransactionStatus.SUCCEED));
    }

    public void addFailure(String transactionId, String message) {
        this.transactionResponses.add(new TransactionResponse(transactionId, TransactionStatus.FAILED, message));
    }

    public DDProcessResponse summarize() {
        transactionsProcessed = transactionResponses.size();
        transactionsSucceed = ((Long) transactionResponses
                .stream()
                .filter(r -> TransactionStatus.SUCCEED.equals(r.getStatus()))
                .count()).intValue();
        transactionsFailed = transactionsProcessed - transactionsSucceed;
        return this;
    }

    @Data
    private static class TransactionResponse {
        private String transactionId;
        private TransactionStatus status;
        private String message;

        public TransactionResponse(String transactionId, TransactionStatus status) {
            setTransactionId(transactionId);
            setStatus(status);
        }

        public TransactionResponse(String transactionId, TransactionStatus status, String message) {
            setTransactionId(transactionId);
            setStatus(status);
            setMessage(message);
        }

        public void setStatus(TransactionStatus status) {
            this.status = status;
            if (TransactionStatus.SUCCEED.equals(status)) {
                this.message = "Transaction processed successfully.";
            }
        }
    }

    private enum TransactionStatus {
        SUCCEED,
        FAILED
    }
}
