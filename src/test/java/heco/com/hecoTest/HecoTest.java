package heco.com.hecoTest;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.druid.sql.visitor.functions.Hex;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import heco.com.HecoApplication;
import heco.com.heco.entity.TableField;
import heco.com.heco.entity.TreeNode;
import heco.com.heco.listener.NoModelDataListener;
import heco.com.heco.mapper.TableFieldSearchMapper;
import javafx.scene.paint.Stop;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hpsf.Decimal;
import org.java_websocket.WebSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.Utils;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.contracts.token.ERC20Interface;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.ens.Contracts;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import javax.sound.midi.Track;
import java.io.CharConversionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Logger;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = HecoApplication.class)
public class HecoTest {

    private final Log logger = LogFactory.getLog(HecoTest.class);

    @Autowired
    private TableFieldSearchMapper tableFieldSearchMapper;

    //私钥
    String privateKey = "395a4933250026259dae3bd001074e0c64d2dcd168be14e20c872051380c3410";
    //BSC私钥
    String privateKeyBsc = "0xa936e39692be28fd41e248c67ee1fd5167c8b34d54bcb392baf9a0072aa48370";
    //钱包地址(heco)
    String accountAddress = "0x84A0FfBE24B2aDc0BcbBe68b8201d049BE504b34";
    //钱包地址(ETH)
    String ethAccountAddress = "0x52003708EC00325924A568beBB0d2c58c602089F";
    //钱包地址(BSC)
    String bscAccountAddress = "0x8c0DfB251f56EEF01187B5eaC312128629F3Ace6";
    //COS合约地址
    String cosContractAddress = "0xF08A2A19e4766B342b5eD6644550B05acEdeE5A8";
    //USDT合约地址
    String usdtContractAddress = "0xa71edc38d189767582c38a3145b5873052c3e47a";
    //HTMOON合约地址
    String htmoonContractAddress = "0xb62e3b6a3866f5754fdefcf82e733310e2851043";
    //FUNNY合约地址
    String funnyContractAddress = "0xb7A61725Cd8192B6a473d2225B4C436893C2fAd4";
    //HODL合约地址
    String hodlContractAddress = "0x0E3EAF83Ea93Abe756690C62c72284943b96a6Bc";
    //ASNU合约地址
    String asnuContractAddress = "0x539bC8973356cAec25C5730F3B20B15Ca6B8EFC0";


    /**
     * 简单的获取当前钱包链上基准货币(gas)的余额
     * @throws URISyntaxException
     */
    @Test
    public void test1() throws URISyntaxException {
        //Gwei单位转换标准
        double pow = Math.pow(10, 18);
        long value = new Double(pow).longValue();

        String url = "https://http-mainnet.hecochain.com";
        String url2 = "https://web3.mytokenpocket.vip";
        String url3 = "https://bsc-dataseed.binance.org";

        Web3j web3j = Web3j.build(new HttpService(url3));
        try {

            //获取钱包gas代币余额
            EthGetBalance balance = web3j.ethGetBalance(accountAddress, DefaultBlockParameterName.LATEST).send();
            String balance1 = balance.getBalance().toString();
//            double doubleValue = balance1.doubleValue();
//            DecimalFormat format = new DecimalFormat("0.000000");
            //单位转换
//            String balance2 = format.format(doubleValue/value);
//            System.out.println(balance1);
//            System.out.println(balance2);
            BigDecimal result = Convert.fromWei(balance1, Convert.Unit.ETHER);
            System.out.println("BNB余额:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            web3j.shutdown();
        }
    }

    /**
     * 测试功能,获取钱包代币余额
    @Test
    public BigDecimal testGetTokenBalance(){
        //Gwei单位转换标准
        double pow = Math.pow(10, 18);
        long value = new Double(pow).longValue();
        //智能链节点地址
//        String url = "https://http-mainnet.hecochain.com";
        String url = "https://bsc-dataseed.binance.org";
        BigDecimal result = null;

        //获取web3j对象
        Web3j web3j = Web3j.build(new HttpService(url));
        Function balanceOf = new Function("balanceOf"
                , Arrays.asList(new Address(bscAccountAddress))
                , Arrays.asList(new TypeReference<Address>() {
        }));
        String encode = FunctionEncoder.encode(balanceOf);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(accountAddress, asnuContractAddress, encode);
        try{
            EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value1 = ethCall.getValue();
            String bigInteger = Numeric.decodeQuantity(value1).toString();
            result = Convert.fromWei(bigInteger, Convert.Unit.ETHER);
//            BigDecimal result = Convert.fromWei(value1, Convert.Unit.ETHER);
//            boolean hexNumber = HexUtil.isHexNumber(value1);
//            BigInteger result = null;
//            if(hexNumber){
//                result = HexUtil.toBigInteger(value1);
//            }
//            System.out.println("isHexNum" + hexNumber);
            System.out.println(bigInteger);
            System.out.println(result);
//            System.out.println("result" + result);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            web3j.shutdown();
        }

        return result;
    }
    */

    /**
     * 测试功能,获取钱包代币余额
     */
    public BigDecimal getTokenBalance(Web3j web3j, String accountAddress, String tokenAddress){
        BigDecimal result = null;

        Function balanceOf = new Function("balanceOf"
                , Arrays.asList(new Address(bscAccountAddress))
                , Arrays.asList(new TypeReference<Address>() {
        }));
        String encode = FunctionEncoder.encode(balanceOf);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(accountAddress, tokenAddress, encode);
        try{
            EthCall ethCall = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).sendAsync().get();
            String value1 = ethCall.getValue();
            String bigInteger = Numeric.decodeQuantity(value1).toString();
            result = Convert.fromWei(bigInteger, Convert.Unit.GWEI);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 测试转账功能
     */
    @Test
    public void testTradeOnPancake(){
        String url = "https://bsc-dataseed.binance.org";

        Web3j web3j = Web3j.build(new HttpService(url));
        try {
            BigDecimal tokenBalance = getTokenBalance(web3j, bscAccountAddress, asnuContractAddress);
            if(tokenBalance.compareTo(new BigDecimal(0)) == 1){
                Credentials credentials = Credentials.create(privateKeyBsc);
                BigInteger nonce;
                BigInteger gasPrice;

                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(bscAccountAddress, DefaultBlockParameterName.PENDING).send();
                if (ethGetTransactionCount == null) {
                    System.out.println("ethGetTransactionCount is null!");
                    Thread.currentThread().interrupt();
                }
                nonce = ethGetTransactionCount.getTransactionCount();

                EthGasPrice ethGasPrice = web3j.ethGasPrice().sendAsync().get();
                if(ethGasPrice == null){
                    System.out.println("ethGasPrice is null!");
                    Thread.currentThread().interrupt();
                }
                gasPrice = ethGasPrice.getGasPrice();
                BigInteger gasLimit = BigInteger.valueOf(60000L);

                // 单位换算
                String value = "38635";
                int decimal = 9;
                String toAddress = "0xb8626Dd3758C1572395Cd7fb4aF2BC6ECeeE6682";
                BigInteger val = new BigDecimal(value).multiply(new BigDecimal("10").pow(decimal)).toBigInteger();
                Function transfer = new Function(
                        "transfer",
                        Arrays.asList(new Address(toAddress), new Uint256(val)),
                        Collections.singletonList(new TypeReference<Type>() {

                        }));
                //创建交易对象
                String encode = FunctionEncoder.encode(transfer);
                RawTransaction transaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, asnuContractAddress, encode);

                //签名
                byte[] signMessage = TransactionEncoder.signMessage(transaction, credentials);
                String hexString = Numeric.toHexString(signMessage);
                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexString).sendAsync().get();
                String transactionHash = ethSendTransaction.getTransactionHash();
                if(transactionHash != null){
                    System.out.println("交易已执行!交易hash为:" + transactionHash);
                }
            }else{
                logger.error("该token余额不正确");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.trace(e.getMessage());
        }finally {
            web3j.shutdown();
            logger.info("web3j已关闭!");
        }

    }

    @Test
    public void testGetMdexList(){
        String url = "https://ht.mdex.com/tokenlist.json";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("t", "1622101699777");
        String result = HttpUtil.get(url, paramMap);
        System.out.println(result);
    }

    @Test
    public void test2() {
        //根节点
        TreeNode root = new TreeNode(1);

        TreeNode l1 = new TreeNode(2);
        TreeNode r1 = new TreeNode(3);

        TreeNode ll1 = new TreeNode(4);
        TreeNode lr1 = new TreeNode(5);

        TreeNode rl1 = new TreeNode(6);
        TreeNode rr1 = new TreeNode(7);

        root.setLeft(l1);
        root.setRight(r1);

        l1.setLeft(ll1);
        l1.setRight(lr1);

        r1.setLeft(rl1);
        r1.setRight(rr1);

        System.out.println(" " + root.getVal());
        System.out.println("/" + "\\");
        System.out.println(root.getLeft().getVal() + " " + root.getRight().getVal());

    }

    @Test
    public void test3() throws FileNotFoundException {
        String userId = "a,b,c";
        int key = 1;
        char[] use = userId.toCharArray();
        char result = (char) (use[0] - key);
        System.out.println(result);
    }

    @Test
    public void test4(){
        List<String> tableNames = tableFieldSearchMapper.selectAllTableName();
        if(!tableNames.isEmpty()){
            for(int i = 0;i < tableNames.size();i++){
                String tableName = tableNames.get(i);
                String fileName = "D:/工作/database-colunm/" + tableName + ".xlsx";
                List<TableField> result = tableFieldSearchMapper.selectTableFielByTableName(tableName);
                ExcelWriter excelWriter = null;
                if(CollectionUtil.isNotEmpty(result)){
                    System.out.println(result);
                    try {
                        excelWriter = EasyExcel.write(fileName, TableField.class).build();
                        WriteSheet writeSheet = EasyExcel.writerSheet("1").build();
                        excelWriter.write(result,writeSheet);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if(excelWriter != null){
                            excelWriter.finish();
                        }
                    }
                }else{
                    System.out.println("获取到的结果为空!");
                }
            }
        }else{
            System.out.println("获取到的数据库表名列表为空!");
        }


    }

    @Test
    public void test5() throws Exception {
        String driverName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://49.232.195.109:3306/macrowing_erms_db_v2?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true";
        String username = "root";
        String password = "123456";

        Class.forName(driverName);
        Connection connection = DriverManager.getConnection(url, username, password);
        String sql = "SELECT COLUMN_NAME ,COLUMN_TYPE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, IS_NULLABLE, COLUMN_DEFAULT COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'macrowing_erms_db_v2' AND TABLE_NAME = 'cfg_sys';";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        connection.close();
        if(resultSet.toString().isEmpty()){
            System.out.println("结果为空!");
        }else{
            System.out.println(resultSet.toString());
        }
    }

    @Test
    public void test6(){
        int d1 = 16;
        long l = System.currentTimeMillis();
        double d2 = Math.pow(d1, 4);
        long l1 = System.currentTimeMillis();
        System.out.println("耗时为:" + (l1-l));
        System.out.println(d2);
    }

    @Test
    public void excelWritterTest(){
        excelReader();

    }

    private void excelReader(){
        String fileName = "D:\\lijq0521\\卷内目录-五冶竣工资料(1).xlsx";
        EasyExcel.read(fileName, new NoModelDataListener()).sheet().doRead();
    }

    private List<List<String>> head(){
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("卷内顺序号");
        List<String> head1 = new ArrayList<String>();
        head1.add("责任者(必填)");
        List<String> head2 = new ArrayList<String>();
        head2.add("日期");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<List<Object>> dataList() {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<Object>();
            data.add("字符串" + i);
            data.add(new Date());
            data.add(0.56);
            list.add(data);
        }
        return list;
    }

}