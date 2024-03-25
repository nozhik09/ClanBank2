package org.bankSystem.repository;

import org.bankSystem.model.Operations;
import org.bankSystem.model.TypeOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperationsRepository {
    private Map<Integer, Operations> operationsMap = new HashMap<>();
    private  int currentId = 1;

    // Добавление операции
    public void addOperation(Operations operation) {
        operation.setId(currentId++);

        operationsMap.put(operation.getId(), operation);
    }

    // Получение операции по ID
    public Operations getOperationById(int operationId) {
        return operationsMap.get(operationId);
    }

    // Получение всех операций для конкретного аккаунта
    public List<Operations> getOperationsByAccountId(int bankAccountId) {
        return operationsMap.values().stream()
                .filter(operation -> operation.getIdAccount() == bankAccountId)
                .collect(Collectors.toList());
    }

    // Получение операций по типу
    public List<Operations> getOperationsByType(TypeOperation typeOperation) {
        return operationsMap.values().stream()
                .filter(operation -> operation.getTypeOperation().equals(typeOperation))
                .collect(Collectors.toList());
    }

    // Получение всех операций
    public List<Operations> getAllOperations() {
        return new ArrayList<>(operationsMap.values());
    }
}
