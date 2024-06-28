package com.ps.database;

import com.ps.models.SalesContract;

public interface SalesDao {
    void addSalesContract(SalesContract salesContract);
    SalesContract getSalesContract(int contractId);
    void updateSalesContract(SalesContract salesContract);
    void deleteSalesContract(int contractId);
}
