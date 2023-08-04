package com.hxl.plugin.springboot.invoke.view;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {
    private static final Color SELECTED_COLOR=Color.getColor("#03a9f4");
    private JTable table;
    private Action action;
    private int mnemonic;
    private Border originalBorder;
    private Border focusBorder;

    private JButton renderButton;
    private JButton editButton;
    private Object editorValue;
    private boolean isButtonColumnEditor;

    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;

        renderButton = new JButton();
        editButton = new JButton();
        editButton.setFocusPainted(false);
        editButton.addActionListener(this);
        originalBorder = editButton.getBorder();
        setFocusBorder(new LineBorder(Color.BLUE));

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
        table.addMouseListener(this);
    }

    public Border getFocusBorder() {
        return focusBorder;
    }

    public void setFocusBorder(Border focusBorder) {
        this.focusBorder = focusBorder;
        editButton.setBorder(focusBorder);
    }

    public int getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(int mnemonic) {
        this.mnemonic = mnemonic;
        renderButton.setMnemonic(mnemonic);
        editButton.setMnemonic(mnemonic);
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null) {
            editButton.setText("");
            editButton.setIcon(null);
        } else if (value instanceof Icon) {
            editButton.setText("");
            editButton.setIcon((Icon) value);
        } else {
            editButton.setText(value.toString());
            editButton.setIcon(null);
        }
        if (isSelected) {
            editButton.setBorder(  new ZoneBorder(SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR));
        } else {
        }
        this.editorValue = value;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout ());
        renderButton.setSize(20,20);
        jPanel.add(editButton,BorderLayout.CENTER);
        jPanel.setBorder(   new ZoneBorder(SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR));
        return jPanel;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    //
//  Implement TableCellRenderer interface
//
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            renderButton.setForeground(table.getSelectionForeground());
            renderButton.setBackground(table.getSelectionBackground());
            renderButton.setBorder(  new ZoneBorder(SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR));
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background"));
        }

        if (hasFocus) {
            renderButton.setBorder(  new ZoneBorder(SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR));
        } else {
            renderButton.setBorder(originalBorder);
        }

//		renderButton.setText( (value == null) ? "" : value.toString() );
        if (value == null) {
            renderButton.setText("");
            renderButton.setIcon(null);
        } else if (value instanceof Icon) {
            renderButton.setText("");
            renderButton.setIcon((Icon) value);
        } else {
            renderButton.setText(value.toString());
            renderButton.setIcon(null);
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout ());
        renderButton.setSize(20,20);
        jPanel.add(renderButton,BorderLayout.CENTER);
        jPanel.setBorder(   new ZoneBorder(SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR,SELECTED_COLOR));
        return jPanel;
    }

    //
//  Implement ActionListener interface
//
    /*
     *	The button has been pressed. Stop editing and invoke the custom Action
     */
    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped();

        //  Invoke the Action

        ActionEvent event = new ActionEvent(
                table,
                ActionEvent.ACTION_PERFORMED,
                "" + row);
        action.actionPerformed(event);
    }

    //
//  Implement MouseListener interface
//
    /*
     *  When the mouse is pressed the editor is invoked. If you then then drag
     *  the mouse to another cell before releasing it, the editor is still
     *  active. Make sure editing is stopped when the mouse is released.
     */
    public void mousePressed(MouseEvent e) {
        if (table.isEditing()
                && table.getCellEditor() == this)
            isButtonColumnEditor = true;
    }

    public void mouseReleased(MouseEvent e) {
        if (isButtonColumnEditor
                && table.isEditing())
            table.getCellEditor().stopCellEditing();

        isButtonColumnEditor = false;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}