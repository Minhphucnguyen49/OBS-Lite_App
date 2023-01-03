package com.hciws22.obslite.todo;

import com.hciws22.obslite.db.SqLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class TodoController {

    private final TodoDbService todoDbService;

    public TodoController(SqLiteHelper sqLiteHelper) {
        this.todoDbService = new TodoDbService(sqLiteHelper);
    }

    public List<Todo> getToDo(){
        return todoDbService.selectTodoAppointments();
    }

    public void getExtraInfo(){
        todoDbService.insertExtraInfo(getToDo());
    }
}
