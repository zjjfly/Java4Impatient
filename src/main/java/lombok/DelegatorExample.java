package lombok;

import lombok.experimental.Delegate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * lombok实现代理模式
 *
 * @author zjjfly
 */
@AllArgsConstructor
public class DelegatorExample implements AutoCloseable {
    @Delegate
    private InputStream ip;

    public static void main(String[] args) {

        try {
            @Cleanup DelegatorExample fis = new DelegatorExample(new FileInputStream("build.gradle"));
            byte[] bytes = new byte[2048];
            fis.read(bytes);
            System.out.println(new String(bytes).trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
