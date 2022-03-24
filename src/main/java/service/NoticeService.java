package service;

import dao.NoticeDao;
import entity.Notice;
import utils.MybatisUtils;

public class NoticeService {

    public Notice selectById(Long noticeId){
        return (Notice) MybatisUtils.executeQuery(sqlSession -> sqlSession.getMapper(NoticeDao.class).selectById(noticeId));
    }
}
