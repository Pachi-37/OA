package dao;

import entity.ProcessFlow;

public interface ProcessFlowDao {

    public ProcessFlow selectById(Long processId);

    public void insert(ProcessFlow processFlow);
}
