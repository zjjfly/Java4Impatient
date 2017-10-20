package lombok;

import lombok.experimental.Delegate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * lombok实现代理模式
 * @author zjjfly
 */
@AllArgsConstructor
public class Delegator implements AutoCloseable {
    @Delegate
    private InputStream ip;

    public static void main(String[] args) {
        Delegator as;
        try {
            as = new Delegator(new FileInputStream("build.gradle"));
            as.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
