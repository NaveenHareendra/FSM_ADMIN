package com.example.agrisite;

import com.google.firebase.database.Exclude;
import java.util.List;
public class TaskList {

        @Exclude
        public String month_id;
        public String month, FOName, status;
        public List<Task> tasks;

        public TaskList() {
        }

        public TaskList(String month, String FOName) {
            this.month = month;
            this.FOName = FOName;
        }
}
