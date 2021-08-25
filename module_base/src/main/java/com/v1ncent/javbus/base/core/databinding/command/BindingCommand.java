package com.v1ncent.javbus.base.core.databinding.command;

/**
 * ================================================
 * 作    者：v1ncent
 * 版    本：1.0.0
 * 创建日期：2021/7/16
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class BindingCommand {
    private final BindingAction execute;

    public BindingCommand(BindingAction execute) {
        this.execute = execute;
    }

    public void execute() {
        if (execute != null) {
            execute.call();
        }
    }
}
