package common;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import com.lemon.encryption.RSAManager;

import data.Constants;
import data.EnvironmentConstant;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.config.LogConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import pojo.ExcelPojo;
import util.JDBCUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

/**
 * @author penn
 * @date 2021/4/4 - 20:17
 * 柠檬班学习
 */
public class BaseTest {
    @BeforeTest
    public void GlobalSetup() {
        RestAssured.config = RestAssured.config().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_DECIMAL));
        RestAssured.baseURI = Constants.BASE_URI;
    }

    public Response request(ExcelPojo excelData, String interfaceModuleName) {
        String logFilePath;
        if(Constants.LOG_TO_FILE ){
            File dirPath =new File(System.getProperty("user.dir")+"\\log\\"+interfaceModuleName );
            if (!dirPath.exists()){
                dirPath.mkdirs() ;
            }
            logFilePath =dirPath +"\\test"+excelData.getCaseId() +".log";
            PrintStream fileOutPutStream = null;
            try{
                //PrintStream 是打印流的意思
                 fileOutPutStream= new PrintStream(new File(logFilePath) );
            }catch (FileNotFoundException fileNOtFound){
                fileNOtFound.printStackTrace() ;
            }
            RestAssured.config =RestAssured.config.logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream) ) ;
        }

        String requestHeader = excelData.getRequestHeader();
        Map<String, Object> requestHeaderMap = JSONObject.parseObject(requestHeader, Map.class);
        String url = excelData.getUrl();
        String params = excelData.getInputParams();
        String method = excelData.getMethod();
        Response response = null;
        if (method.equalsIgnoreCase("get")) {
            response = given().log().all().headers(requestHeaderMap).when().get(url).then().log().all().extract().response();
        } else if (method.equalsIgnoreCase("post")) {
            response = given().log().all().headers(requestHeaderMap).body(params).when().post(url).then().log().all().extract().response();
        } else if (method.equalsIgnoreCase("patch")) {
            response = given().log().all().headers(requestHeaderMap).body(params).when().patch(url).then().log().all().extract().response();
        }
//        这里面可以更好一些当报错的时候写log到allure报表当中
        if(Constants.LOG_TO_FILE )
            try{
                Allure.addAttachment("API Request Message",new FileInputStream(logFilePath));
            }catch (FileNotFoundException e){
                e.printStackTrace() ;
            }
        return response;
    }


    public void assertResponse(ExcelPojo excelPojo,Response res){
        //断言
        if(excelPojo.getExpected()!=null) {
            Map<String, Object> expectedMap = JSONObject.parseObject(excelPojo.getExpected(), Map.class);
            for (String key : expectedMap.keySet()) {
                //获取map里面的value
                //获取期望结果
                Object expectedValue = expectedMap.get(key);
                //获取接口返回的实际结果（jsonPath表达式）
                Object actualValue = res.jsonPath().get(key);
                System.out.println("Debug message is "+key);
                Assert.assertEquals(actualValue, expectedValue);
            }
        }
    }
    public void assertSQL(ExcelPojo excelPojo){
        String dbAssert = excelPojo.getDbAssert();
        //数据库断言
        if(dbAssert != null) {
            Map<String, Object> map = JSONObject.parseObject(dbAssert, Map.class);
            Set<String> keys = map.keySet();
            for (String key : keys) {
                //key其实就是我们执行的sql语句
                //value就是数据库断言的期望值
                Object expectedValue = map.get(key);
                //System.out.println("expectedValue类型::" + expectedValue.getClass());
                //获得的是count，只有两种数据类型
                if(expectedValue instanceof BigDecimal){
                    Object actualValue = JDBCUtils.querySingleCount(key) ;
                    //System.out.println("actualValue类型:" + actualValue.getClass());
                    Assert.assertEquals(actualValue,expectedValue);
                }else if(expectedValue instanceof Integer){
                    //此时从excel里面读取到的是integer类型
                    //从数据库里面拿到的是Long类型
                    Long expectedValue2 = ((Integer) expectedValue).longValue();
                    Object actualValue = JDBCUtils.querySingleCount(key);
                    Assert.assertEquals(actualValue,expectedValue2);
                }
            }
        }
    }

    public void extractToEnvironment(ExcelPojo excelPojo, Response response) {
        Map<String, Object> extractMap = JSONObject.parseObject(excelPojo.getExtractResult(), Map.class);
        for (String key : extractMap.keySet()) {
            Object path = extractMap.get(key);
            Object value = response.jsonPath().get(path + "");
            EnvironmentConstant.environmentData.put(key, value);
        }
    }

    public String regexReplace(String originalStr) {
        if (originalStr != null) {
            Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
            Matcher matcher = pattern.matcher(originalStr);
            //这里替换的是整个json的键值对
            String result = originalStr;
            while (matcher.find()) {
                String outerStr = matcher.group(0);
                String innerStr = matcher.group(1);
                Object replaceStr = EnvironmentConstant.environmentData.get(innerStr);
                result = result.replace(outerStr, replaceStr.toString());
            }
            return result;
        }
        return null;
    }

    //casesReplace和regexReplace共同组成，在需要替换的特定表格的特定字段，才去替换，除非一个单元格之内有两个{}的情况,但是如果这样，内部的键值对，通过innerStr也会做相应的匹配
    public ExcelPojo casesReplace(ExcelPojo excelpojo) {
        String inputParams = regexReplace(excelpojo.getInputParams());
        excelpojo.setInputParams(inputParams);

        String requestHeader = regexReplace(excelpojo.getRequestHeader() );
        excelpojo.setRequestHeader(requestHeader);

        String url = regexReplace(excelpojo.getUrl());
        excelpojo.setUrl(url);

        String expected = regexReplace(excelpojo.getExpected());
        excelpojo.setExpected(expected);
        return excelpojo;
    }


    public List<ExcelPojo> readAllData(int sheetNumber) {
        File file = new File(Constants.Excel_File_PATH);
        ImportParams importParams = new ImportParams();

        //所以sheetNumber是从0开始的
        importParams.setStartSheetIndex(sheetNumber - 1);
        //将这一个sheet的所有数据都读出来
        List<ExcelPojo> excelListData = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return excelListData;
    }

    public List<ExcelPojo> readDataFromSpecificRow(int sheetNumber, int startRowNumber) {
        File file = new File(Constants.Excel_File_PATH);
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNumber - 1);
        importParams.setStartRows(startRowNumber);
        List<ExcelPojo> excelListData = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return excelListData;
    }

    public List<ExcelPojo> readDataToSpecificRow(int sheetNumber, int startRowNumber, int readRowCount) {
        File file = new File(Constants.Excel_File_PATH);
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(sheetNumber - 1);
        importParams.setStartRows(startRowNumber);
        importParams.setReadRows(readRowCount);
        List<ExcelPojo> excelListData = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return excelListData;
    }


}
