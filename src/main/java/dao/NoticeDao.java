package dao;

import entity.Notice;

public interface NoticeDao {

    public Notice selectById(Long noticeId);

    public void insert(Notice notice);
}
