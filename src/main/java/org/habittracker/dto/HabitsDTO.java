package org.habittracker.dto;

import lombok.Data;

@Data
public class HabitsDTO {
    public int id;
    public int userId;
    public String habitName;

    public HabitsDTO () {};
    public HabitsDTO (int id, String habitName) {
        this.id = id;
        this.habitName = habitName;
    }
    public HabitsDTO(int id, int userId, String habitName) {
        this.id = id;
        this.userId = userId;
        this.habitName = habitName;
    }
}
