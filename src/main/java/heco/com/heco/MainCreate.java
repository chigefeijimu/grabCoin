package heco.com.heco;

import heco.com.heco.runnable.CreateWallet;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lijunqiao
 */
public class MainCreate {

    public static void main(String[] args) {
        String filePath1 = "/Users/lijunqiao/Desktop/defi/wallet1/";
        String filePath2 = "/Users/lijunqiao/Desktop/defi/wallet2/";
        String filePath3 = "/Users/lijunqiao/Desktop/defi/wallet3/";
        String filePath4 = "/Users/lijunqiao/Desktop/defi/wallet4/";
        String filePath5 = "/Users/lijunqiao/Desktop/defi/wallet5/";
        CreateWallet createWallet1 = new CreateWallet(filePath1);
        CreateWallet createWallet2 = new CreateWallet(filePath2);
        CreateWallet createWallet3 = new CreateWallet(filePath3);
        CreateWallet createWallet4 = new CreateWallet(filePath4);
        CreateWallet createWallet5 = new CreateWallet(filePath5);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        threadPoolExecutor.execute(createWallet1);
        threadPoolExecutor.execute(createWallet2);
        threadPoolExecutor.execute(createWallet3);
        threadPoolExecutor.execute(createWallet4);
        threadPoolExecutor.execute(createWallet5);

    }
}
