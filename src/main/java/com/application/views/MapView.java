package com.application.views;

import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;


@PermitAll
@Route(value="map", layout = MainLayout.class)
@PageTitle("Map")
public class MapView extends VerticalLayout {
    /**
     * Build a simple layout holding a map in an iframe
     */
    public MapView(){
        IFrame frame = new IFrame();
        frame.setSrc("//umap.openstreetmap.fr/fr/map/esus_777866?scaleControl=true&miniMap=false&scrollWheelZoom=false&zoomControl=true&allowEdit=false&moreControl=true&searchControl=null&tilelayersControl=null&embedControl=null&datalayersControl=expanded&onLoadPanel=databrowser&captionBar=false");
        frame.setSizeFull();
        add(frame);
        setSizeFull();
    }

}
