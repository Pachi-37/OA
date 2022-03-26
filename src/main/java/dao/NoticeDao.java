package dao;

import entity.Notice;

import java.util.List;

public interface NoticeDao {

    public Notice selectById(Long noticeId);

    public void insert(Notice notice);

    public List<Notice> selectByReceiverId(Long receiverId);
}
