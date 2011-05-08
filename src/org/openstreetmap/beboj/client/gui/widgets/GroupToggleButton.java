// License: Apache 2.0. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui.widgets;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * Simple implementation of a group of toggle buttons where only
 * one can be pressed at a time.
 * Depressing a button is not possible, but initially all buttons can be up.
 * Add all buttons to a ToggleGroup.
 */
public class GroupToggleButton extends ToggleButton {

    protected ToggleGroup group;

    public GroupToggleButton() {
        super();
    }

    public GroupToggleButton(Image upImage, ClickHandler handler) {
        super(upImage, handler);
    }

    public GroupToggleButton(Image upImage, Image downImage,
            ClickHandler handler) {
        super(upImage, downImage, handler);
    }

    public GroupToggleButton(Image upImage, Image downImage) {
        super(upImage, downImage);
    }

    public GroupToggleButton(Image upImage) {
        super(upImage);
    }

    public GroupToggleButton(String upText, ClickHandler handler) {
        super(upText, handler);
    }

    public GroupToggleButton(String upText, String downText,
            ClickHandler handler) {
        super(upText, downText, handler);
    }

    public GroupToggleButton(String upText, String downText) {
        super(upText, downText);
    }

    public GroupToggleButton(String upText) {
        super(upText);
    }

    void setGroup(ToggleGroup group) {
        this.group = group;
    }

    @Override
    public void onClick() {
        if (isDown())
            return;
        super.onClick();
        group.clicked(this);
    }
}
