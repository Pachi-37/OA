package dao;

import entity.ProcessFlow;

import java.util.List;

public interface ProcessFlowDao {

    public List<ProcessFlow> selectByFormId(Long formId);

    public void insert(ProcessFlow processFlow);

    public void update(ProcessFlow processFlow);
}
