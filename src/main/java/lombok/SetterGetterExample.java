package lombok;

import java.util.List;

/**
 * @author zjjfly
 */
public class SetterGetterExample {
    public static void main(String[] args) {
        Student student = new Student();
        student.setName("James");
        student.setAge(12);
        System.out.println("name:" + student.getName() + ",age:" + student.getAge());
    }

    @Getter
    @Setter
    private static class Student {
        private String name;
        private int age;
        private boolean passExercise;
    }

    @ToString(exclude = {"exerciseQuestions"})
    private static class Teacher {
        @Setter(onParam_={@NonNull})
        @Getter
        private String name;

        @Setter(AccessLevel.PRIVATE)
        @Getter(AccessLevel.PROTECTED)
        private List<String> exerciseQuestions;
    }

    private static class Assistant extends Teacher {

        private void checkExerciseQuestions() {
            getExerciseQuestions().forEach(this::checkQuestion);
        }

        private void checkQuestion(String question) {

        }
    }
}
