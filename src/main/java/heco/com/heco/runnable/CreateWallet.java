package heco.com.heco.runnable;

import heco.com.heco.utils.StrUtil;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;

import java.io.File;

public class CreateWallet implements Runnable{
    private String filePath;
    public CreateWallet(String filePath){
        this.filePath = filePath;
    }

    @Override
    public void run() {
        String pwd = "";
        long l = 1L;

        while(true){
            try {
                ECKeyPair ecKeyPair = Keys.createEcKeyPair();
                String accountAddress = Keys.getAddress(ecKeyPair);
                if(StrUtil.endsWith4(accountAddress)) {
                    String walletFile = WalletUtils.generateWalletFile(pwd, ecKeyPair, new File(filePath), false);
                    Credentials credentials = WalletUtils.loadCredentials(pwd, filePath + walletFile);
                    String address = credentials.getAddress();
                    String privateKey = credentials.getEcKeyPair().getPrivateKey().toString(16);

                    System.out.println("钱包地址：" + address);
                    System.out.println("私钥：" + privateKey);
                    System.out.println("循环打印次数:" + l);
                }else{
                    System.out.println("循环打印次数:" + l);
                }

                l++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
