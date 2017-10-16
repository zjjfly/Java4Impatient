package common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zjjfly
 */
@Data
@AllArgsConstructor
public class City {
    private String name;
    private String state;
    private int population;
}
