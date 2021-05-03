package teststeps;

import common.BaseTest;
import data.EnvironmentConstant;
//import io.restassured.response.Response;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pojo.ExcelPojo;
import util.PhoneRandomUtil;

import java.util.List;

/**
 * @author penn
 * @date 2021/4/10 --- 23:41
 * 柠檬班学习
 */
public class NewLoanTest extends BaseTest {


    @BeforeClass
    public void setup() {
        //生成三个角色的随机手机号码（借款人+管理员）
        String borrowserPhone = PhoneRandomUtil.getUnregisteredPhone();
        String adminPhone = PhoneRandomUtil.getUnregisteredPhone();
        EnvironmentConstant.environmentData .put("borrower_phone",borrowserPhone);
        EnvironmentConstant.environmentData.put("admin_phone",adminPhone);
        //读取用例数据-前面4条
        List<ExcelPojo> list = readDataToSpecificRow(3,0,4) ;
        for (int i=0 ;i<list.size();i++){
            //发送请求
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            Response res = request(excelPojo,"NewLoan");
            //判断是否要提取响应数据
            if(excelPojo.getExtractResult() != null){
                extractToEnvironment(excelPojo,res);
            }
        }

    }

    @DataProvider
    public Object[] getNewLoanData() {
        List<ExcelPojo> listExcelData = readDataFromSpecificRow(3, 4);
        return listExcelData.toArray();

    }

    @Test(dataProvider = "getNewLoanData")
    public void testNewLoan(ExcelPojo excelPojo) {
        excelPojo = casesReplace(excelPojo);
        Response res = request(excelPojo,"NewLoan");
        //断言
        assertResponse(excelPojo, res);
    }
    @AfterTest
    public void teardown(){

    }
}