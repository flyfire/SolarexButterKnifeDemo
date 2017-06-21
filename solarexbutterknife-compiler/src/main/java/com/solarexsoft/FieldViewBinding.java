package com.solarexsoft;

import javax.lang.model.type.TypeMirror;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 21/06/2017
 *    Desc:
 * </pre>
 */

public class FieldViewBinding {
    private String name;
    private TypeMirror type;
    private int resId;

    public FieldViewBinding(String name, TypeMirror type, int resId) {
        this.name = name;
        this.type = type;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public TypeMirror getType() {
        return type;
    }

    public int getResId() {
        return resId;
    }
}
