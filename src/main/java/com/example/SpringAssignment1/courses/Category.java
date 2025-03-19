package com.example.SpringAssignment1.courses;

public enum Category {

        FOUNDATION("FOUNDATION"),
        HONORS("HONORS"),
        UNDERGRADUATE("UNDERGRADUATE");

        private final String category;
        Category(String category){
            this.category =category;
        }
        public String getCategory() {
            return this.category;
        }

}
