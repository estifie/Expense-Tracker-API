package com.estifie.expensetracker.enums;

public enum Permission {
    // Permissions that are related to permissions.

    /**
     * Permission to manage permissions.
     */
    MANAGE_PERMISSIONS,

    /**
     * Permission to grant a permission to a user.
     */
    GRANT_PERMISSION,

    /**
     * Permission to revoke a permission from a user.
     */
    REVOKE_PERMISSION,

    /**
     * Permission to view permissions.
     */
    VIEW_PERMISSIONS,

    // Permissions that are related to users.

    /**
     * Permission to manage users.
     */
    MANAGE_USERS,

    /**
     * Permission to view users.
     */
    VIEW_USERS,

    /**
     * Permission to hard delete a user.
     */
    HARD_DELETE_USER,

    /**
     * Permission to batch delete users.
     */
    BATCH_DELETE_USERS,

    // Permissions that are related to categories

    /**
     * Permission to manage categories.
     */
    MANAGE_CATEGORIES,

    /**
     * Permission to hard delete a category.
     */
    HARD_DELETE_CATEGORY,

    /**
     * Permission to batch delete categories.
     */
    BATCH_DELETE_CATEGORIES,

    // Permissions that are related to expenses

    /**
     * Permission to manage expenses.
     */
    MANAGE_EXPENSES,

    /**
     * Permission to view expenses.
     */
    VIEW_EXPENSES,

    /**
     * Permission to hard delete an expense.
     */
    HARD_DELETE_EXPENSE,

    /**
     * Permission to batch delete expenses.
     */
    BATCH_DELETE_EXPENSES,
}
