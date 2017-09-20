package com.chenjishi.slidedemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.Stack;

/**
 * Created by jishichen on 2017/6/16.
 */
public class Slide {

    private static final Slide INSTANCE = new Slide();

    private Stack<Activity> stack;

    private Slide() {
        stack = new Stack<>();
    }

    public void clear() {
        stack.clear();
    }

    public static Slide getInstance() {
        return INSTANCE;
    }

    public Activity peek() {
        if (stack.empty()) return null;
        return stack.peek();
    }

    public void pop() {
        if (stack.empty()) return;

        stack.pop();
    }

    public void startActivity(final Context context, Intent intent) {
        stack.push((Activity) context);
        context.startActivity(intent);
    }
}
