package com.aurora.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReadWriteRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        // Utilise le mode transactionnel pour router
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "READER" : "WRITER";
    }
} 