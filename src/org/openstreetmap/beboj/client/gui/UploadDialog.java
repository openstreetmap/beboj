// License: GPL. For details, see LICENSE file.
package org.openstreetmap.beboj.client.gui;

import java.util.Collection;
import java.util.HashSet;

import org.openstreetmap.beboj.client.Beboj;
import org.openstreetmap.beboj.client.io.API;
import org.openstreetmap.beboj.client.io.APIAsync;
import org.openstreetmap.beboj.shared.data.osm.SimpleDataSet;
import org.openstreetmap.beboj.shared.data.osm.UploadRequestData;
import org.openstreetmap.beboj.shared.data.osm.UploadResponseData;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.io.DiffResultEntry;
import org.openstreetmap.josm.tools.Predicate;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UploadDialog extends DialogBox {

    Collection<OsmPrimitive> primitivesForUpload;

    TextBox user;
    PasswordTextBox pw;
    TextBox comment;

    public UploadDialog() {
        setText("Upload");
        setGlassEnabled(true);

        Button cancel = new Button("Cancel");
        Button upload = new Button("Upload");

        cancel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                UploadDialog.this.hide();
            }
        });
        upload.addClickHandler(new UploadClickHandler());

        user = new TextBox();
        user.setText(Beboj.username == null ? "" : Beboj.username);
        pw = new PasswordTextBox();
        pw.setText(Beboj.password == null ? "" : Beboj.password);
        comment = new TextBox();
        comment.setText(Beboj.comment == null ? "" : Beboj.comment);

        Grid g = new Grid(3, 2);

        g.setText(0, 0, "User name:");
        g.setWidget(0, 1, user);
        g.setText(1, 0, "Password:");
        g.setWidget(1, 1, pw);
        g.setText(2, 0, "Changeset Comment:");
        g.setWidget(2, 1, comment);

        primitivesForUpload = new HashSet<OsmPrimitive>();
        for (OsmPrimitive osm : Main.main.getCurrentDataSet().allPrimitives()) {
            if (forUpload.evaluate(osm)) {
                primitivesForUpload.add(osm);
            }
        }

        HorizontalPanel hp = new HorizontalPanel();
        hp.add(cancel);
        hp.add(upload);

        VerticalPanel vp = new VerticalPanel();
        vp.add(new HTML("uploading "+primitivesForUpload.size()+" objects"));
        vp.add(g);
        vp.add(hp);

        setWidget(vp);
        center();
    }

    protected class UploadClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            UploadDialog.this.getElement().getStyle().setCursor(Cursor.WAIT);

            final UploadRequestData request = new UploadRequestData();

            request.data = new SimpleDataSet();
            request.data.addAll(primitivesForUpload);
            request.changeSetComment = Beboj.comment = comment.getText();
            request.username = Beboj.username = user.getText();
            request.password = Beboj.password = pw.getText();

            APIAsync as = GWT.create(API.class);
            as.uploadOsmData(request, new AsyncCallback<UploadResponseData>() {
                @Override
                public void onFailure(Throwable caught) {
                    UploadDialog.this.getElement().getStyle().clearCursor();
                    UploadDialog.this.hide();
                    Window.alert("uploadOsmData/RPC failure");
                }

                @Override
                public void onSuccess(UploadResponseData response) {
                    UploadDialog.this.getElement().getStyle().clearCursor();
                    UploadDialog.this.hide();
                    for (OsmPrimitive p: primitivesForUpload) {
                        DiffResultEntry entry = response.diffResults.get(p.getPrimitiveId());
                        if (entry == null) {
                            continue;
                        }
                        if (!p.isDeleted()) {
                            p.setOsmId(entry.new_id, entry.new_version);
                            p.setVisible(true);
                        } else {
                            p.setVisible(false);
                        }
//                        if (cs != null && !cs.isNew()) {
//                            p.setChangesetId(cs.getId());
//                        }
                    }
                    // ids have changed, so we have to rebuild the HashSet
                    primitivesForUpload = new HashSet<OsmPrimitive>(primitivesForUpload);

                    Main.main.getEditLayer().cleanupAfterUpload(primitivesForUpload);

                    Window.alert("uploadOsmData/RPC success ("+response.diffResults.size()+")");
                }
            });
        }
    }

    Predicate<OsmPrimitive> forUpload = new Predicate<OsmPrimitive>() {
        @Override
        public boolean evaluate(OsmPrimitive osm) {
            return (osm.isDeleted() && !osm.isNew() && osm.isModified() && osm.isVisible())
                        || (!osm.isDeleted() && osm.isModified())
                        || (!osm.isDeleted() && osm.isNewOrUndeleted());
        }
    };

}
