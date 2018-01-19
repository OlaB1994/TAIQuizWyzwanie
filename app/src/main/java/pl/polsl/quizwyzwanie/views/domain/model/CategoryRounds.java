package pl.polsl.quizwyzwanie.views.domain.model;

import java.util.List;

/**
 * Created by Mateusz on 19.01.2018.
 */

public class CategoryRounds {

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
}
