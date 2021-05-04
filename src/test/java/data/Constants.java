package data;

/**
 * @author penn
 * @date 2021/4/4 - 20:22
 * 柠檬班学习
 */
public class Constants {
    //输出日志到控制台还是到日志文件中
    public static final boolean LOG_TO_FILE=true;
    //常量的命名都是大写的  //这里为什么是static,因为如果声明为static可以全局使用，不用实例化对象  //final是为了其他人对它修改
    public static final String  Excel_File_PATH ="src//test//resources//futureloan.xls";

    public static final String BASE_URI="http://api.lemonban.com/futureloan";

    //数据库baseuri
    public static final String DB_BASE_URI="api.lemonban.com";
    //数据库名
    public static final String DB_NAME="futureloan";
    public static final String DB_USERNAME="future";
    public static final String DB_PWD="123456";
}
