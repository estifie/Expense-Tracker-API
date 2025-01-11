package com.estifie.expensetracker.dto.expense;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data transfer object for adding a tag to an expense")
public class ExpenseAddTagDTO {
    @NotBlank
    @Schema(description = "The name of the tag to add", example = "groceries")
    private String tag;

    public ExpenseAddTagDTO(String tag) {
        this.tag = tag;
    }

    public ExpenseAddTagDTO() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
