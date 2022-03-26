package service;

import entity.Notice;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class NoticeServiceTest {

    private NoticeService noticeService = new NoticeService();

    @Test
    public void selectByReceiverId() {
        List<Notice> list = noticeService.selectByReceiverId(1l);

        System.out.println(list);
    }
}