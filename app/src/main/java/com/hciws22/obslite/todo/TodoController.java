package com.hciws22.obslite.todo;

import com.hciws22.obslite.db.SqLiteHelper;

import java.util.ArrayList;

public class TodoController {

    private final TodoDbService todoDbService;

    public TodoController(SqLiteHelper sqLiteHelper) {
        this.todoDbService = new TodoDbService(sqLiteHelper);
    }

    public ArrayList<Todo> getToDo(){
        return todoDbService.getToDo();
    }
}
