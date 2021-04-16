package util;

import java.util.Random;

/**
 * @author penn
 * @date 2021/4/10 --- 11:10
 * 柠檬班学习
 */
public class PhoneRandomUtil {

    public static String phoneRandom(){
        Random random = new Random() ;
        String prePhoneNumber = "133";
        String phoneNumber = "";
        for (int i=0;i<8;i++){
            int number =random.nextInt(9);
            prePhoneNumber+=number;
            phoneNumber=prePhoneNumber;
        }
        return phoneNumber;
    }

    public static String getUnregisteredPhone(){
        String phone ="";
        while(true){
            phone =phoneRandom();
            Object result =JDBCUtils.querySingleCount("SELECT count(*) FROM member where mobile_phone="+phone);
            if((Long)result==0){
                break;
            }
        }
        return phone;
    }
}
