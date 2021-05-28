import java.util.*;

public class LabVariants<T>{
    Map<String, List<T>> variants = new HashMap<>();
    int max;


    LabVariants(List<T> variants, int max){
        this.variants.put("available", variants);
        this.variants.put("all", variants);
        this.max = max;
    }

    int getVariant(String student, T variant){
        if (variants.get("all").contains(variant)){
            // Если вариант существует
            if (variants.get("available").size() == 0){
                // Если нету доступных вариантов
                return 2;
            }
            if (variants.containsKey("chosen")) {
                if (variants.get("chosen").contains(variant)) {
                    // Если вариант уже выбран
                    return 3;
                }
                if (variants.containsKey(student)){
                    // Если у студента уже есть варианты
                    if (variants.get(student).size() == max){
                        // Если количество вариантов студента достигло максимума
                        return 4;
                    }
                    // Если все ок
                    variants.get(student).add(variant);
                    variants.get("chosen").add(variant);
                    variants.get("available").remove(variant);
                    return 0;
                }
                // Если все ок
                ArrayList<T> studentVariants = new ArrayList<>();
                studentVariants.add(variant);
                variants.put(student, studentVariants);
                variants.get("chosen").add(variant);
                variants.get("available").remove(variant);
                return 0;
            }
            // Если все ок
            List<T> studentVariants = new ArrayList<>();
            studentVariants.add(variant);
            variants.put(student, studentVariants);
            variants.put("chosen", studentVariants);
            ArrayList<T> s = new ArrayList<>(variants.get("available"));
            for (int i = 0; i < s.size(); i++){
                T v = s.get(i);
                // Удаляем вариант из available
                if (v.equals(variant)){
                    s.remove(i);
                    break;
                }
            }
            variants.put("available", s);
            return 0;
        }
        return 1;
    }

    List<T> getAvailableVariants(){
        return variants.get("available");
    }

    List<T> getChosenVariants(){
        return variants.get("chosen");
    }

    T getStudentVariant(String student){
        return variants.containsKey(student) ? (T) variants.get(student) : null;
    }


}
