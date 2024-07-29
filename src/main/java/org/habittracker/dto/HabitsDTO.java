package org.habittracker.dto;

import lombok.Data;

@Data
public class HabitsDTO {
    public int id;
    public int userId;
    public String habitName;
    public String habitDescription;
}
