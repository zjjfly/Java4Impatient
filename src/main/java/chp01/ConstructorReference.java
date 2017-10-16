package chp01;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zjjfly
 */
public class ConstructorReference {
    public static void main(String[] args) {
        List<String> labels = new ArrayList<>();
        labels.add("a");
        labels.add("b");
        List<Button> buttons = labels.stream().map(Button::new).collect(Collectors.toList());
        Object[] btns1 = buttons.stream().toArray();
        Button[] btns2= buttons.stream().toArray(Button[]::new);
    }
}
