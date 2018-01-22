package pl.polsl.quizwyzwanie.domain.model;

import java.util.List;

/**
 * Created by Mateusz on 19.01.2018.
 */

class CategoryRounds {

    private String categoryName;
    private List<ChoosenQuestionId> choosenQusetionId; //TODO poprawić nazwę gdy będzie poprawna w bazie literówka w Question

    @SuppressWarnings("unused")
    public CategoryRounds() {
    }

    @SuppressWarnings("unused")
    public CategoryRounds(String categoryName, List<ChoosenQuestionId> choosenQusetionId) {
        this.categoryName = categoryName;
        this.choosenQusetionId = choosenQusetionId;
    }

    @SuppressWarnings("unused")
    public String getCategoryName() {
        return categoryName;
    }

    @SuppressWarnings("unused")
    public List<ChoosenQuestionId> getChoosenQusetionId() {
        return choosenQusetionId;
    }
}
