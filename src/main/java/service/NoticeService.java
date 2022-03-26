package service;

import dao.NoticeDao;
import entity.Notice;
import utils.MybatisUtils;

import java.util.List;

public class NoticeService {

    /**
     * 返回指定编号的通知
     * @param noticeId
     * @return 通知
     */
    public Notice selectById(Long noticeId) {
        return (Notice) MybatisUtils.executeQuery(sqlSession -> sqlSession.getMapper(NoticeDao.class).selectById(noticeId));
    }

    /**
     * 查询指定员工系统消息
     * @param receiverId
     * @return 返回最近十条信息
     */
    public List<Notice> selectByReceiverId(Long receiverId) {
        return (List<Notice>) MybatisUtils.executeQuery(sqlSession -> {
            NoticeDao noticeDao = sqlSession.getMapper(NoticeDao.class);
            List<Notice> noticeList = noticeDao.selectByReceiverId(receiverId);
            return noticeList;
        });
    }
}
