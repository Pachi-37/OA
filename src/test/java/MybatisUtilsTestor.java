import org.junit.Test;
import utils.MybatisUtils;

public class MybatisUtilsTestor {

    @Test
    public void test() {
        String str = (String) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("test.test"));
        System.out.println(str);
    }

}
