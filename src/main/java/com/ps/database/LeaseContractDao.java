package com.ps.database;

import com.ps.models.LeaseContract;

public interface LeaseContractDao {
    void addLeaseContract(LeaseContract leaseContract);
    LeaseContract getLeaseContract(int contractId);
    void updateLeaseContract(LeaseContract leaseContract);
    void deleteLeaseContract(int contractId);
}
