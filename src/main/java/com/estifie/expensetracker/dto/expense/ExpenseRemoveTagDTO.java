package com.estifie.expensetracker.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data transfer object for removing a tag from an expense")
public class ExpenseRemoveTagDTO {
    @NotBlank
    @Schema(description = "The name of the tag to remove", example = "groceries")
    private String tag;

    public ExpenseRemoveTagDTO(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
