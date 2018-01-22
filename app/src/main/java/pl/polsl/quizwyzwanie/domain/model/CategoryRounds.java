package pl.polsl.quizwyzwanie.domain.model;

import java.io.Serializable;
import java.util.List;

public class CategoryRounds implements Serializable {

    private String categoryName;
    private List<ChoosenQuestionId> choosenQusetionId; //TODO poprawić nazwę gdy będzie poprawna w bazie literówka w Question

    public CategoryRounds() {
    }

    public CategoryRounds(String categoryName, List<ChoosenQuestionId> choosenQusetionId) {
        this.categoryName = categoryName;
        this.choosenQusetionId = choosenQusetionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<ChoosenQuestionId> getChoosenQusetionId() {
        return choosenQusetionId;
    }

    public void setChoosenQusetionId(List<ChoosenQuestionId> choosenQusetionId) {
        this.choosenQusetionId = choosenQusetionId;
    }
}
