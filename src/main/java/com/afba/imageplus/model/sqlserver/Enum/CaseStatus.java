package com.afba.imageplus.model.sqlserver.Enum;

public enum CaseStatus {

    A, N, P, U, W, C;

    public static boolean isValidStatusForDeqFLR(CaseStatus status) {
        return A.equals(status) || N.equals(status);
    }

}
