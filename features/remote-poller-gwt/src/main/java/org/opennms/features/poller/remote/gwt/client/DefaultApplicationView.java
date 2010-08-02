package org.opennms.features.poller.remote.gwt.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.opennms.features.poller.remote.gwt.client.FilterPanel.StatusSelectionChangedEvent;
import org.opennms.features.poller.remote.gwt.client.location.LocationInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class DefaultApplicationView {
    
    interface Binder extends UiBinder<DockLayoutPanel, DefaultApplicationView> {}
    
    private static final Binder BINDER = GWT.create(Binder.class);
    
    interface LinkStyles extends CssResource {
        String activeLink();
    }
    
    @UiField
    protected LocationPanel locationPanel;
    

    @UiField
    protected DockLayoutPanel mainPanel;
    @UiField
    protected SplitLayoutPanel splitPanel;
    @UiField
    protected Hyperlink locationLink;
    @UiField
    protected Hyperlink applicationLink;
    @UiField
    protected Label updateTimestamp;
    @UiField
    protected LinkStyles linkStyles;
    @UiField
    protected HorizontalPanel statusesPanel;
    @UiField
    protected CheckBox statusDown;
    @UiField
    protected CheckBox statusDisconnected;
    @UiField
    protected CheckBox statusMarginal;
    @UiField
    protected CheckBox statusUp;
    @UiField
    protected CheckBox statusStopped;
    @UiField
    protected CheckBox statusUnknown;
    
    private final MapPanel m_mapPanel;
    
    private final HandlerManager m_eventBus;


    private Application m_presenter;


    static final DateTimeFormat UPDATE_TIMESTAMP_FORMAT = DateTimeFormat.getMediumDateTimeFormat();
    
    
    public DefaultApplicationView(Application presenter, HandlerManager eventBus, MapPanel mapPanel) {
        m_presenter = presenter;
        m_eventBus = eventBus;
        m_mapPanel = mapPanel;
        BINDER.createAndBindUi(this);
        
        locationPanel.setEventBus(eventBus);
        setupWindow();
    }
    
    @UiHandler("statusDown")
    public void onDownClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.DOWN, getStatusDown().getValue()));
    }

    @UiHandler("statusDisconnected")
    public void onDisconnectedClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.DISCONNECTED, getStatusDisconnected().getValue()));
    }

    @UiHandler("statusMarginal")
    public void onMarginalClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.MARGINAL, getStatusMarginal().getValue()));
    }

    @UiHandler("statusUp")
    public void onUpClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.UP, getStatusUp().getValue()));
    }

    @UiHandler("statusStopped")
    public void onStoppedClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.STOPPED, getStatusStopped().getValue()));
    }

    @UiHandler("statusUnknown")
    public void onUnknownClicked(final ClickEvent event) {
        getEventBus().fireEvent(new StatusSelectionChangedEvent(Status.UNKNOWN, getStatusUnknown().getValue()));
    }

    public HandlerManager getEventBus() {
        return m_eventBus;
    }
    
    public DockLayoutPanel getMainPanel() {
        return mainPanel;
    }

    public SplitLayoutPanel getSplitPanel() {
        return splitPanel;
    }

    public HorizontalPanel getStatusesPanel() {
        return statusesPanel;
    }

    public CheckBox getStatusDown() {
        return statusDown;
    }

    public CheckBox getStatusDisconnected() {
        return statusDisconnected;
    }

    public CheckBox getStatusMarginal() {
        return statusMarginal;
    }

    public CheckBox getStatusUp() {
        return statusUp;
    }

    public CheckBox getStatusStopped() {
        return statusStopped;
    }

    public CheckBox getStatusUnknown() {
        return statusUnknown;
    }
    
    public LocationPanel getLocationPanel() {
        return locationPanel;
    }
    
    public Hyperlink getLocationLink() {
        return locationLink;
    }

    public Hyperlink getApplicationLink() {
        return applicationLink;
    }

    public LinkStyles getLinkStyles() {
        return linkStyles;
    }

    public Label getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    Application getPresenter() {
        return m_presenter;
    }

    /**
     * <p>onApplicationClick</p>
     *
     * @param event a {@link com.google.gwt.event.dom.client.ClickEvent} object.
     */
    @UiHandler("applicationLink")
    public void onApplicationClick(ClickEvent event) {
        if (getApplicationLink().getStyleName().contains(getLinkStyles().activeLink())) {
            // This link is already selected, do nothing
        } else {
            getPresenter().onApplicationViewSelected();
            getApplicationLink().addStyleName(getLinkStyles().activeLink());
            getLocationLink().removeStyleName(getLinkStyles().activeLink());
            getLocationPanel().showApplicationList();
            getLocationPanel().showApplicationFilters(true);
            getLocationPanel().resizeDockPanel();
        }
    }

    /**
     * <p>onLocationClick</p>
     *
     * @param event a {@link com.google.gwt.event.dom.client.ClickEvent} object.
     */
    @UiHandler("locationLink")
    public void onLocationClick(ClickEvent event) {
        if (getLocationLink().getStyleName().contains(getLinkStyles().activeLink())) {
            // This link is already selected, do nothing
        } else {
            getPresenter().onLocationViewSelected();
            getLocationLink().addStyleName(getLinkStyles().activeLink());
            getApplicationLink().removeStyleName(getLinkStyles().activeLink());
            getLocationPanel().showLocationList();
            getLocationPanel().showApplicationFilters(false);
            getLocationPanel().resizeDockPanel();
        }
    }

    /**
     * <p>updateTimestamp</p>
     */
    public void updateTimestamp() {
        getUpdateTimestamp().setText("Last update: " + UPDATE_TIMESTAMP_FORMAT.format(new Date()));
    }

    Integer getAppHeight() {
    	final com.google.gwt.user.client.Element e = getMainPanel().getElement();
    	int extraHeight = e.getAbsoluteTop();
    	return Window.getClientHeight() - extraHeight;
    }

    Set<Status> getSelectedStatuses() {
        
        Set<Status> statuses = new HashSet<Status>();
        for (final Widget w : getStatusesPanel()) {
            if (w instanceof CheckBox) {
                final CheckBox cb = (CheckBox)w;
                if(cb.getValue()) {
                    statuses.add(Status.valueOf(cb.getFormValue()));
                }
            }
        }
        return statuses;
    }

    private void setupWindow() {
        Window.setTitle("OpenNMS - Remote Monitor");
        Window.enableScrolling(false);
        Window.setMargin("0px");
        Window.addResizeHandler(new ResizeHandler() {
    		public void onResize(final ResizeEvent event) {
    			getMainPanel().setHeight(getAppHeight().toString());
    		}
        });
    }

    public void initialize() {
        addMapWidget(getMapPanel().getWidget());
        getSplitPanel().setWidgetMinSize(getLocationPanel(), 255);
        getMainPanel().setSize("100%", "100%");
        RootPanel.get("remotePollerMap").add(getMainPanel());
        
        updateTimestamp();
        onLocationClick(null);
    }

    public void addMapWidget(Widget widget) {
        getSplitPanel().add(widget);
    }

    void updateSelectedApplications(Set<ApplicationInfo> applications) {
        getLocationPanel().updateSelectedApplications(applications);
    }

    void updateLocationList( ArrayList<LocationInfo> locationsForLocationPanel) {
        getLocationPanel().updateLocationList(locationsForLocationPanel);
    }

    public void setSelectedTag(String selectedTag, List<String> allTags) {
        getLocationPanel().clearTagPanel();
        getLocationPanel().addAllTags(allTags);
        getLocationPanel().selectTag(selectedTag);
    }

    void updateApplicationList(ArrayList<ApplicationInfo> applications) {
        getLocationPanel().updateApplicationList(applications);
    }

    void updateApplicationNames(TreeSet<String> allApplicationNames) {
        getLocationPanel().updateApplicationNames(allApplicationNames);
    }

    public MapPanel getMapPanel() {
        return m_mapPanel;
    }

    void fitMapToLocations(GWTBounds locationBounds) {
        if (getMapPanel() instanceof SmartMapFit) {
            ((SmartMapFit)getMapPanel()).fitToBounds();
        } else {
            //TODO: Zoom in to visible locations on startup
            
            getMapPanel().setBounds(locationBounds);
        }
    }

    GWTBounds getMapBounds() {
        return getMapPanel().getBounds();
    }

    void showLocationDetails(final String locationName, String htmlTitle, String htmlContent) {
        getMapPanel().showLocationDetails(locationName, htmlTitle, htmlContent);
    }

    void placeMarker(final GWTMarkerState markerState) {
        getMapPanel().placeMarker(markerState);
    }
}
