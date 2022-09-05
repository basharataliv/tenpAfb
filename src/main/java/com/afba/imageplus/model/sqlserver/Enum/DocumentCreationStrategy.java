package com.afba.imageplus.model.sqlserver.Enum;

public enum DocumentCreationStrategy {
    CREATE_NEW_CASE("1"), ADD_TO_EXISTING_CASE("2"), CREATE_NEW_OR_ADD_TO_EXISTING_CASE("3");

    private String id;

    DocumentCreationStrategy(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
