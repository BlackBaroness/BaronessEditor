package ru.baronessdev.editor.expressions;

import ru.baronessdev.editor.Core;

public abstract class ExampleExpression {

    public abstract void execute(Object o, Object obj);

    public String getMessage(String path) {
        return Core.getMessage(path);
    }
}
